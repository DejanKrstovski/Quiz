package gui.mainPanels;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.Panels.AnswerPanel;
import gui.Panels.ComboBoxJListPanel;
import gui.Panels.LabelFieldPanel;
import gui.Panels.LabelTextAreaPanel;
import gui.Panels.SouthPanel;
import gui.Panels.SubPanel;
import gui.Swing.MyButton;
import gui.Swing.MyLabel;
import helpers.QuestionListItem;
import persistence.serialization.QuizDataManager;
import quizlogic.Answer;
import quizlogic.ErrorHandler;
import quizlogic.Question;
import quizlogic.Theme;
import quizlogic.Validator;
/**
 * Panel for managing quiz questions, including creation, editing, deleting, and displaying.
 * <p>
 * Provides UI components to select themes and questions, input question text and answers,
 * and interact with the quiz data backend via {@link QuizDataManager}.
 * </p>
 * <p>
 * Supports registering {@link QuestionsChangeListener} to notify external components
 * about changes in the question data for synchronization purposes.
 * </p>
 * <p>
 * Internally, the panel contains multiple subpanels and input fields:
 * <ul>
 *   <li>Theme selector and display panel</li>
 *   <li>Question title and text area</li>
 *   <li>Answer input fields with correctness checkboxes</li>
 *   <li>Control buttons for saving, creating new, and deleting questions</li>
 * </ul>
 * </p>
 * 
 * The UI updates the list of questions dynamically based on selected themes,
 * and handles user interaction events for question management.
 * 
 * @author DejanKrstovski
 */
public class MainQuestionPanel extends SubPanel implements ThemeChangeListener, GuiConstants {

    /** Backend manager for quiz data operations */
    private final QuizDataManager dataManager = QuizDataManager.getInstance();

    private MyButton buttonShow;
    private SubPanel centerPanel;
    private SubPanel westPanel;
    private SouthPanel bottomPanel;
    private LabelFieldPanel themePanel;
    private LabelFieldPanel titlePanel;
    private LabelTextAreaPanel questionPanel;
    private AnswerPanel answerPanel;
    private ComboBoxJListPanel comboPanel;

    /** Stores current selected theme info (description to display) */
    private String selectedThemeInfo;

    /** ID of the current selected question; -1 if no selection */
    private int currentQuestionId = -1;

    /** Currently selected theme */
    private Theme selectedTheme;

    /** Listener notified on question list changes */
    private QuestionsChangeListener questionsChangeListener;

    /** Flag to disable combo box listener during programmatic updates */
    private boolean isUpdatingCombo = false;

    /**
     * Constructs a new {@code MainQuestionPanel}, initializes the user interface,
     * and sets up event listeners.
     */
    public MainQuestionPanel() {
        super();
        init();
        initQuestionListListener();
        initButtonActions();
    }

    /**
     * Populates the UI fields with data from the specified question.
     * If the question is null, clears all fields.
     * 
     * @param question The question whose data will be displayed, or null to clear inputs.
     */
    private void fillWithData(final Question question) {
        if (question != null) {
            currentQuestionId = question.getId();
            themePanel.setText(question.getTheme().getTitle());
            titlePanel.setText(question.getTitle());
            questionPanel.setTextInfo(question.getText());

            final List<Answer> answers = question.getAnswers();
            for (int i = 0; i < Math.min(answers.size(), MAX_ANSWERS); i++) {
                answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
                answerPanel.getAnswerCheckBoxes(i).setSelected(answers.get(i).isCorrect());
            }
        } else {
            currentQuestionId = -1;
            clearAllFields();
        }
    }

    /**
     * Initializes the layout, components, and event listeners.
     */
    private void init() {
        initLayout();
        initComponents();
        initComboBoxListener();
    }

    /** Initializes layout of the panel. */
    private void initLayout() {
        setLayout(new BorderLayout());
    }

    /** Creates and adds subpanels to the main panel. */
    private void initComponents() {
        westPanel = initWestPanel();
        centerPanel = initCenterPanel();
        bottomPanel = new SouthPanel(SAVE_QUESTION, NEW_QUESTION, DELETE_QUESTION);

        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /** Initializes the panel on the west side containing question details and answers. */
    private SubPanel initWestPanel() {
        final SubPanel panel = new SubPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setPreferredSize(WEST_PANEL_DIMENSIONS);
        panel.setMaximumSize(WEST_PANEL_DIMENSIONS);
        panel.add(initThemePanel());
        panel.add(initTitlePanel());
        panel.add(initQuestionPanel());
        panel.add(initPossibleAnswerPanel());
        panel.add(initAnswerPanel());
        panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
        return panel;
    }

    /** Initializes the theme display/input panel. */
    private LabelFieldPanel initThemePanel() {
        themePanel = new LabelFieldPanel(LABEL_THEME, EMPTY_STRING);
        themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        return themePanel;
    }

    /** Initializes the title input panel. */
    private LabelFieldPanel initTitlePanel() {
        titlePanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
        titlePanel.getTextField().setEditable(true);
        titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        return titlePanel;
    }

    /** Initializes the question text area panel. */
    private LabelTextAreaPanel initQuestionPanel() {
        questionPanel = new LabelTextAreaPanel(LABEL_QUESTION);
        questionPanel.getQuestionTextArea().setEditable(true);
        questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        return questionPanel;
    }

    /** Creates container panel with labels for possible and correct answers. */
    private SubPanel initPossibleAnswerPanel() {
        final SubPanel panel = new SubPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBorder(DISTANCE_BETWEEN_ELEMENTS);

        MyLabel possibleAnswerLabel = new MyLabel(LABEL_POSSIBLE_ANSWERS);
        MyLabel rightAnswerLabel = new MyLabel(CORRECTNESS);

        panel.add(possibleAnswerLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(rightAnswerLabel);
        return panel;
    }

    /** Initializes the answers input panel. */
    private AnswerPanel initAnswerPanel() {
        answerPanel = new AnswerPanel();
        return answerPanel;
    }

    /** Initializes the center panel containing theme label and selection list. */
    private SubPanel initCenterPanel() {
        final SubPanel panel = new SubPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
        panel.add(initThemeLabelPanel());
        panel.add(initComboPanel());
        return panel;
    }

    /** Creates a horizontal panel with theme question label and show theme button. */
    private SubPanel initThemeLabelPanel() {
        final SubPanel panel = new SubPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBorder(DISTANCE_BETWEEN_ELEMENTS);

        MyLabel questionLabel = new MyLabel(LABEL_QUESTION_FOR_THEME);
        buttonShow = new MyButton(SHOW_THEME);

        panel.add(questionLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(buttonShow);

        buttonShow.setVisible(false);
        return panel;
    }

    /** Initializes the combo box and question list panel for theme/question selection. */
    private ComboBoxJListPanel initComboPanel() {
        final List<String> themeTitles = dataManager.getThemes().stream()
                .map(Theme::getTitle)
                .collect(Collectors.toList());

        final List<String> comboItems = new ArrayList<>();
        comboItems.add(ALL_THEMES);
        comboItems.addAll(themeTitles);

        final List<QuestionListItem> questionItems = dataManager.getAllThemeQuestions().stream()
                .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
                .collect(Collectors.toList());
        comboPanel = new ComboBoxJListPanel(comboItems, questionItems);
        return comboPanel;
    }

    /**
     * Adds listener to theme selection combo box to update the question list and related UI.
     */
    private void initComboBoxListener() {
        comboPanel.addThemeSelectionListener(e -> {
            if (isUpdatingCombo) {
                return;
            }

            final String selectedThemeTitle = (String) comboPanel.getSelectedTheme();
            if (selectedThemeTitle == null || selectedThemeTitle.trim().isEmpty()) {
                return;
            }

            if (ALL_THEMES.equalsIgnoreCase(selectedThemeTitle.trim())) {
                buttonShow.setVisible(false);
                updateQuestionsList(ALL_THEMES);
                return;
            }

            buttonShow.setVisible(true);
            selectedTheme = dataManager.getThemes().stream()
                    .filter(t -> t.getTitle().trim().equalsIgnoreCase(selectedThemeTitle.trim()))
                    .findFirst()
                    .orElse(null);

            if (selectedTheme != null) {
                selectedThemeInfo = selectedTheme.getThemeInfo();
                updateQuestionsList(selectedTheme.getTitle());
                themePanel.setText(selectedTheme.getTitle());
            } else {
                ErrorHandler.getInstance().setError("Failed to load theme.");
                showMessage(ErrorHandler.getInstance().getError());
            }
        });
    }

    /**
     * Adds action listeners to buttons for saving, deleting, and creating new questions,
     * and shows/hides theme information.
     */
    private void initButtonActions() {
        final MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
        buttons[0].addActionListener(e -> saveQuestionFromUI());
        buttons[1].addActionListener(e -> clearAllFields());
        buttons[2].addActionListener(e -> deleteQuestion());
        buttonShow.addActionListener(e -> toggleShowListOrInfo());
    }

    /**
     * Adds listener to question list selection to display data of selected question.
     */
    public void initQuestionListListener() {
        comboPanel.addQuestionSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final QuestionListItem selectedItem = comboPanel.getSelectedQuestionItem();
                if (selectedItem != null && selectedItem.getId() != -1) {
                    final Question selectedQuestion = dataManager.getQuestionById(selectedItem.getId());
                    if (selectedQuestion != null) {
                        fillWithData(selectedQuestion);
                    } else {
                        showMessage(ERROR_LOAD_QUESTION);
                    }
                } else {
                    clearAllFields();
                }
            }
        });
    }

    /**
     * Saves the current question from UI fields after validation.
     * Notifies registered listeners after successful save.
     */
    public void saveQuestionFromUI() {
        final String selectedThemeTitle = comboPanel.getSelectedTheme();
        if (selectedThemeTitle == null || ALL_THEMES.equals(selectedThemeTitle)) {
            showMessage(CHOOSE_A_THEME_MSG);
            return;
        }

        final Theme selectedTheme = dataManager.getThemes().stream()
                .filter(t -> t.getTitle().trim().equalsIgnoreCase(selectedThemeTitle.trim()))
                .findFirst()
                .orElse(null);

        if (selectedTheme == null) {
            showMessage(ERROR_THEME_FIND);
            return;
        }

        final Question question = collectQuestionFromUI(selectedTheme);

        if (!Validator.validateQuestion(question)) {
            showMessage(ErrorHandler.getInstance().getError());
            return;
        }

        final List<Question> questions = selectedTheme.getQuestions();
        if (!questions.contains(question)) {
            questions.add(question);
        }

        if (!Validator.validateTheme(selectedTheme)) {
            showMessage(ErrorHandler.getInstance().getError());
            return;
        }

        final String result = dataManager.saveQuestion(question);
        notifyQuestionsChanged();
        dataManager.getAllThemes();
        if (QUESTION_SAVED.equals(result)) {
            showMessage(QUESTION_SAVED);
            updateQuestionsList(selectedTheme.getTitle());
        } else {
            showMessage(result);
        }
    }

    /**
     * Constructs a {@link Question} object from the current UI inputs linked to the specified theme.
     * 
     * @param theme The theme associated with the question
     * @return A populated {@link Question} object representing current UI input
     */
    private Question collectQuestionFromUI(final Theme theme) {
        if (currentQuestionId == -1) {
            currentQuestionId = dataManager.createNewQuestionId();
        }

        final Question question = new Question();
        question.setId(currentQuestionId);
        question.setTitle(titlePanel.getText());
        question.setText(questionPanel.getTextInfo());
        question.setTheme(theme);

        List<Answer> answers = new ArrayList<>(MAX_ANSWERS);
        for (int i = 0; i < MAX_ANSWERS; i++) {
            final Answer answer = new Answer();
            answer.setText(answerPanel.getAnswerFields(i).getText());
            answer.setCorrect(answerPanel.getAnswerCheckBoxes(i).isSelected());
            answers.add(answer);
        }
        question.setAnswers(answers);

        return question;
    }

    /**
     * Clears all input fields and resets the current question selection.
     */
    private void clearAllFields() {
        currentQuestionId = -1;
        titlePanel.setText(EMPTY_STRING);
        questionPanel.setTextInfo(EMPTY_STRING);

        for (int i = 0; i < MAX_ANSWERS; i++) {
            answerPanel.getAnswerFields(i).setText(EMPTY_STRING);
            answerPanel.getAnswerCheckBoxes(i).setSelected(false);
        }
        comboPanel.getList().clearSelection();
        showMessage("");
    }

    /**
     * Deletes the currently selected question after user confirmation and updates the UI.
     */
    private void deleteQuestion() {
        final QuestionListItem selectedItem = comboPanel.getSelectedQuestionItem();
        if (selectedItem == null || selectedItem.getId() == -1) {
            bottomPanel.getMessagePanel().setMessageAreaText(CHOOSE_A_QUESTION_MSG);
            return;
        }

        final int confirm = javax.swing.JOptionPane.showConfirmDialog(this, QUESTION_DELETE_INFORMATION,
                DELETE_CONFIRMATION, javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            final Question selectedQuestion = dataManager.getQuestionById(selectedItem.getId());
            if (selectedQuestion != null) {
                String msg = dataManager.deleteQuestion(selectedQuestion.getId());
                notifyQuestionsChanged();
                dataManager.getAllThemes();
                updateQuestionsList(selectedTheme != null ? selectedTheme.getTitle() : ALL_THEMES);
                clearAllFields();
                showMessage(msg);
            } else {
                showMessage(QUESTION_DELETING_NOT_POSSIBLE);
            }
        }
    }

    /**
     * Updates the question list in the comboPanel based on the given theme title.
     * 
     * @param themeTitle The title of the selected theme or ALL_THEMES.
     */
    private void updateQuestionsList(final String themeTitle) {
        if (ALL_THEMES.equalsIgnoreCase(themeTitle)) {
        		dataManager.reloadAllThemeQuestions();
            comboPanel.updateQuestions(dataManager.getAllThemeQuestions().stream()
                    .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
                    .collect(Collectors.toList()));
            return;
        }

        selectedTheme = dataManager.getThemes().stream().filter(t -> t.getTitle().trim().
        			equalsIgnoreCase(themeTitle.trim())).findFirst().orElse(null);

        if (selectedTheme == null) {
            comboPanel.updateQuestions(new ArrayList<>());
            showMessage(ERROR_THEME_FIND);
            return;
        }
        final List<QuestionListItem> questionItems = dataManager.getQuestionsForTheme(selectedTheme).stream()
                .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
                .collect(Collectors.toList());

        comboPanel.updateQuestions(questionItems);
    }

    /**
     * Toggles between showing the theme information and the question list in the UI.
     */
    private void toggleShowListOrInfo() {
        if (buttonShow.getText().contains(LABEL_THEME)) {
            comboPanel.showInfo(selectedThemeInfo);
            buttonShow.setText(SHOW_LIST);
        } else {
            buttonShow.setText(SHOW_THEME);
            updateQuestionsList(themePanel.getText());
        }
    }

    /**
     * Refreshes the question list filtered by the specified theme title.
     * 
     * @param selectedThemeTitle The title of the selected theme.
     */
    public void refreshQuestionList(final String selectedThemeTitle) {
        final Theme theme = dataManager.readThemeById(comboPanel.getSelectedIndex());
        if (theme == null) {
            showMessage(ERROR_THEME_FIND);
            return;
        }
        final List<QuestionListItem> questionItems = dataManager.getQuestionsForTheme(theme).stream()
                .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
                .collect(Collectors.toList());
        comboPanel.updateQuestions(questionItems);
    }

    /**
     * Refreshes combo box themes and question list, temporarily disables listeners during refresh.
     */
    public void refreshThemes() {
        isUpdatingCombo = true;
        final ActionListener[] listeners = comboPanel.getComboBox().getActionListeners();

        for (final ActionListener l : listeners) {
            comboPanel.getComboBox().removeActionListener(l);
        }

        final List<String> themeTitles = dataManager.getThemes().stream()
                .map(Theme::getTitle)
                .collect(Collectors.toList());

        comboPanel.updateThemes(themeTitles);
//
//        final List<QuestionListItem> questionItems = dataManager.getAllQuestions().stream()
//                .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
//                .collect(Collectors.toList());
//
//        comboPanel.updateQuestions(questionItems);
//
//        for (final ActionListener l : listeners) {
//            comboPanel.getComboBox().addActionListener(l);
//        }
        isUpdatingCombo = false;
    }

    /**
     * Displays a message in the bottom panel's message area.
     * 
     * @param message The message to display.
     */
    private void showMessage(final String message) {
        bottomPanel.getMessagePanel().setMessageAreaText(message);
    }

    /**
     * Registers a {@link QuestionsChangeListener} to be notified on question list changes.
     * 
     * @param listener The listener to register.
     */
    public void setOnQuestionsChangeListener(final QuestionsChangeListener listener) {
        this.questionsChangeListener = listener;
    }

    /**
     * Notifies the registered {@link QuestionsChangeListener} about question data changes.
     */
    private void notifyQuestionsChanged() {
        if (questionsChangeListener != null) {
            questionsChangeListener.onQuestionsChanged();
        }
    }

	@Override
	public void onThemesChanged() {
		refreshThemes();
	}
}
