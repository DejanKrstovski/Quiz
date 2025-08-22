package gui.mainPanels;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import bussinesLogic.datenBank.QuizDBDataManager;
import bussinesLogic.statistics.StatisticsService;
import bussinesLogic.statistics.StatisticsService.Period;
import gui.GuiConstants;
import gui.panels.RadioButtonsPanel;
import gui.panels.SouthPanel;
import gui.panels.SubPanel;
import gui.swing.*;
import helpers.ThemeListItem;

/**
 * Main panel for displaying quiz statistics.
 *
 * Shows:
 *   - Table of statistics per theme
 *   - Table of statistics per question (for a selected theme)
 *   - Summary statistics
 *
 * Filters:
 *   - By theme (combo box)
 *   - By time period (all time, last month, today)
 *
 * Also allows refreshing or deleting statistics.
 */
public class MainStatisticPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

    // --- Services and Data ---
    private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();
    private final StatisticsService statisticsService = new StatisticsService();

    private List<ThemeDTO> allThemes = new ArrayList<>();
    private List<QuestionDTO> allQuestions = new ArrayList<>();
    private List<PlayerAnswerDTO> allPlayerAnswers = new ArrayList<>();

    private Map<Integer, List<PlayerAnswerDTO>> answersByQuestion = new HashMap<>();
    private Map<Integer, List<AnswerDTO>> correctAnswersByQuestion = new HashMap<>();

    private ThemeDTO selectedTheme;
    private List<QuestionDTO> questionsForTheme;
    private Period selectedPeriod = Period.ALL_TIME;

    // --- UI Components ---
    private SubPanel filterPanel;
    private SubPanel contentPanel;
    private SouthPanel bottomPanel;
    private RadioButtonsPanel periodPanel;
    private MyComboBox<ThemeListItem> themeFilter;
    private MyTable tableByTheme;
    private MyTable tableByQuestions;
    private MyTable tableSummary;

    // --- Constructor ---
    public MainStatisticPanel() {
        loadData();
        initUI();
        updateStatisticsUI();
        initListeners();
        initButtonActions();
    }

    /**
     * Initializes the main layout with filter, content, and bottom panels.
     */
    private void initUI() {
        setLayout(new BorderLayout());
        filterPanel = createFilterPanel();
        contentPanel = createContentPanel();
        bottomPanel = new SouthPanel(REFRESH, EMPTY_STRING, ALL_DELETE);

        add(filterPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Builds the top filter panel with theme selection and period radio buttons.
     */
    private SubPanel createFilterPanel() {
        SubPanel panel = new SubPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

        themeFilter = new MyComboBox<>();
        updateThemesFilter();
        themeFilter.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        themeFilter.setPreferredSize(COMBO_BOX);

        periodPanel = new RadioButtonsPanel(ALL_TIME, LAST_MONTH, TODAY);

        panel.add(themeFilter);
        panel.add(Box.createVerticalStrut(10));
        panel.add(periodPanel);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    /**
     * Builds the central content panel with the statistics tables.
     */
    private SubPanel createContentPanel() {
        SubPanel panel = new SubPanel();
        SubPanel panel1 = new SubPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        tableByTheme = new MyTable();
        tableByQuestions = new MyTable();
        tableSummary = new MyTable();
        int width = UIManager.getInt("ScrollBar.width");
        panel.add(new MyScrollPane(tableByQuestions));
        panel.add(Box.createVerticalStrut(10));
        panel.add(new MyScrollPane(tableByTheme));
        panel1.add(tableSummary);
        panel1.add(Box.createHorizontalStrut(width));
        panel.add(panel1);
        return panel;
    }

    /**
     * Loads themes, questions, and player answers from the database.
     */
    private void loadData() {
        allThemes = dataManager.getAllThemes();
        allQuestions = dataManager.getAllQuestions();
        allPlayerAnswers = dataManager.getAllPlayerAnswers();

        correctAnswersByQuestion.clear();
        for (QuestionDTO question : allQuestions) {
            List<AnswerDTO> answers = dataManager.getAnswersFor(question);
            List<AnswerDTO> correct = answers.stream().filter(AnswerDTO::isCorrect).collect(Collectors.toList());
            correctAnswersByQuestion.put(question.getId(), correct);
        }
        rebuildIndexesForFilteredAnswers();
    }

    /**
     * Rebuilds indexes for answers grouped by question, using selected period.
     */
    private void rebuildIndexesForFilteredAnswers() {
        List<PlayerAnswerDTO> filtered = statisticsService.filterAnswersByPeriod(allPlayerAnswers, selectedPeriod);
        answersByQuestion = statisticsService.buildAnswersByQuestion(filtered);
    }

    /**
     * Updates all UI tables based on loaded data.
     */
    private void updateStatisticsUI() {
        updateThemeTable();
    }

    /**
     * Updates the "by theme" and summary tables.
     */
    private void updateThemeTable() {
        var rows = new ArrayList<Object[]>();

        for (ThemeDTO theme : allThemes) {
            List<QuestionDTO> themeQuestions = allQuestions.stream()
                    .filter(q -> q.getThemeId() == theme.getId())
                    .collect(Collectors.toList());

            int questionCount = themeQuestions.size();
            int answeredCount = statisticsService.computeAnsweredCountForTheme(themeQuestions, answersByQuestion);
            double accuracy = statisticsService.computeAccuracyForQuestions(themeQuestions, answersByQuestion, correctAnswersByQuestion);

            rows.add(new Object[]{
                    theme.getTitle(),
                    questionCount,
                    answeredCount,
                    String.format("%.1f%%", accuracy * 100)
            });
        }

        String[] cols = {"Theme", "Fragenanzahl pro Thema", "Beantwortet", "Genauigkeit"};

        tableByTheme.setModel(new javax.swing.table.DefaultTableModel(rows.toArray(new Object[0][]), cols));
        tableByTheme.centerColumns(1, 2, 3);

        long totalAnswersFiltered = statisticsService.filterAnswersByPeriod(allPlayerAnswers, selectedPeriod).size();
        Object[] summaryRow = new Object[]{
                "Gesamt Themen: " + allThemes.size(),
                "Gesamt Fragen: " + allQuestions.size(),
                totalAnswersFiltered,
                String.format("%.1f%%", statisticsService.computeGlobalAccuracy(allQuestions, answersByQuestion, correctAnswersByQuestion) * 100)
        };

        tableSummary.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{summaryRow}, cols));
        tableSummary.hideHeader();
        tableSummary.centerColumns(1, 2, 3);

        tableByTheme.getColumnModel().addColumnModelListener(new javax.swing.event.TableColumnModelListener() {
            public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
                syncColumnWidths(tableByTheme, tableSummary);
            }
            public void columnAdded(javax.swing.event.TableColumnModelEvent e) {}
            public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {}
            public void columnMoved(javax.swing.event.TableColumnModelEvent e) {}
            public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {}
        });
    }

    /**
     * Updates the "by question" table for the currently selected theme.
     */
    private void updateQuestionsTable() {
        String[] cols = {"Frage", "Beantwortet", "Genauigkeit"};

        if (selectedTheme == null || questionsForTheme == null || questionsForTheme.isEmpty()) {
            tableByQuestions.setModel(new javax.swing.table.DefaultTableModel(new Object[0][], cols));
            return;
        }

        var rows = new ArrayList<Object[]>();
        for (QuestionDTO question : questionsForTheme) {
            double acc = statisticsService.computeAccuracyForQuestion(question, answersByQuestion, correctAnswersByQuestion);
            int count = statisticsService.computeAnsweredCount(question, answersByQuestion);
            rows.add(new Object[]{question.getTitle(), count, String.format("%.1f%%", acc * 100)});
        }

        tableByQuestions.setModel(new javax.swing.table.DefaultTableModel(rows.toArray(new Object[0][]), cols));
        tableByQuestions.setRowHeight(ROW_HEIGHT);
        centerColumns(tableByQuestions, 1, 2);
    }

    /**
     * Initializes listeners for theme and period filter components.
     */
    private void initListeners() {
        // Theme selection listener
        themeFilter.addActionListener(e -> {
            ThemeListItem selectedItem = (ThemeListItem) themeFilter.getSelectedItem();
            if (selectedItem == null || selectedItem.getId() == NO_SELECTION) {
                selectedTheme = null;
                questionsForTheme = new ArrayList<>();
                updateQuestionsTable();
                return;
            }
            selectedTheme = getThemeById(selectedItem.getId());
            questionsForTheme = dataManager.getQuestionsFor(selectedTheme);
            updateQuestionsTable();
        });

        // Period selection listener
        periodPanel.addSelectionListener(selText -> {
            if (ALL_TIME.equals(selText)) selectedPeriod = Period.ALL_TIME;
            else if (LAST_MONTH.equals(selText)) selectedPeriod = Period.LAST_MONTH;
            else if (TODAY.equals(selText)) selectedPeriod = Period.TODAY;

            rebuildIndexesForFilteredAnswers();
            updateStatisticsUI();
            if (selectedTheme != null) {
                questionsForTheme = dataManager.getQuestionsFor(selectedTheme);
                updateQuestionsTable();
            }
        });
    }

    /**
     * Initializes bottom panel buttons (reload + delete).
     */
    private void initButtonActions() {
        final MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
        buttons[0].addActionListener(e -> reload());
        buttons[0].setMnemonic(KeyEvent.VK_R);

        buttons[1].setVisible(false); // hidden button

        buttons[2].addActionListener(e -> deleteStatistics());
        buttons[2].setMnemonic(KeyEvent.VK_L);
    }

    private void reload() {
        loadData();
        updateStatisticsUI();
        if (selectedTheme != null) {
            questionsForTheme = dataManager.getQuestionsFor(selectedTheme);
            updateQuestionsTable();
        }
    }

    private void deleteStatistics() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Möchtest du wirklich alle Statistiken (Antworten) dauerhaft löschen?",
                "Bestätigen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (option != JOptionPane.YES_OPTION) return;

        dataManager.deleteAllPlayerAnswers();
        allPlayerAnswers = new ArrayList<>();
        answersByQuestion.clear();
        updateStatisticsUI();
        if (selectedTheme != null) {
            questionsForTheme = dataManager.getQuestionsFor(selectedTheme);
        }
        updateQuestionsTable();
        showMessage(STATISTICS_DELETED);
    }

    private void refreshThemesFromData() {
        allThemes = dataManager.getAllThemes();
    }

    private ThemeDTO getThemeById(int id) {
        return allThemes.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    private void updateThemesFilter() {
        themeFilter.removeAllItems();
        for (ThemeListItem item : buildThemeItems()) {
            themeFilter.addItem(item);
        }
    }

    private List<ThemeListItem> buildThemeItems() {
        List<ThemeListItem> items = new ArrayList<>();
        items.add(new ThemeListItem(NO_SELECTION, CHOOSE_A_THEME_MSG));
        items.addAll(allThemes.stream().map(t -> new ThemeListItem(t.getId(), t.getTitle())).collect(Collectors.toList()));
        return items;
    }

    // --- Helper methods ---

    private void syncColumnWidths(JTable source, JTable target) {
        var src = source.getColumnModel();
        var tgt = target.getColumnModel();
        int n = Math.min(src.getColumnCount(), tgt.getColumnCount());
        for (int i = 0; i < n; i++) {
            int w = src.getColumn(i).getWidth();
            tgt.getColumn(i).setPreferredWidth(w);
            tgt.getColumn(i).setWidth(w);
        }
        target.doLayout();
    }

    public static void centerColumns(JTable table, int... columnIndexes) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int colIndex : columnIndexes) {
            if (colIndex >= 0 && colIndex < table.getColumnModel().getColumnCount()) {
                TableColumn col = table.getColumnModel().getColumn(colIndex);
                col.setCellRenderer(centerRenderer);
            }
        }
    }

    private void showMessage(final String message) {
        bottomPanel.getMessagePanel().setMessageAreaText(message);
    }

    // --- Interface Implementations ---
    @Override
    public void onQuestionsChanged() {
        loadData();
        updateStatisticsUI();
    }

    @Override
    public void onThemesChanged() {
        refreshThemesFromData();
        updateThemesFilter();
        loadData();
        updateStatisticsUI();
    }
}
