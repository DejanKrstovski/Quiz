package gui.mainPanels;

/**
 * @author DejanKrstovski
 * 
 * This panel will be used for the statistics to be saved
 * 
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import bussinesLogic.datenBank.QuizDBDataManager;
import gui.GuiConstants;
import gui.Panels.ComboBoxJListPanel;
import gui.Panels.SubPanel;
import helpers.ThemeListItem;

public class MainStatisticPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();

	// UI
	private SubPanel topFilterPanel; // Filter + Aktionen
	private SubPanel contentPanel; // Tabellen
	private ComboBoxJListPanel<ThemeListItem, Object> themeFilter; // nutze generisch

	// Tabellen
	private JTable tableByTheme;
	private JTable tableHardestQuestions;

	// Daten
	private List<ThemeDTO> allThemes = new ArrayList<>();
	private List<QuestionDTO> allQuestions = new ArrayList<>();
	private List<AnswerDTO> allAnswers = new ArrayList<>();
	private List<PlayerAnswerDTO> allPlayerAnswers = new ArrayList<>();

	public MainStatisticPanel() {
		super();
		init();
		loadData();
		updateStatisticsUI();
	}

	private void init() {
		setLayout(new BorderLayout());

		topFilterPanel = initFilterPanel();
		contentPanel = initContentPanel();

		add(topFilterPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
	}

	private SubPanel initFilterPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

		// Theme Filter
		refreshThemesFromData();
		themeFilter = new ComboBoxJListPanel<>(buildThemeItems(), new ArrayList<>());
		themeFilter.setBorder(DISTANCE_BETWEEN_ELEMENTS);

		p.add(themeFilter);

		return p;
	}


	private SubPanel initContentPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new GridLayout(2, 1, 8, 8));
		p.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

		// Tabellen erstellen
		tableByTheme = new JTable();
		tableHardestQuestions = new JTable();

		p.add(new JScrollPane(tableByTheme));
		p.add(new JScrollPane(tableHardestQuestions));
		return p;
	}

	private void loadData() {
		allThemes = dataManager.getAllThemes();
		allQuestions = dataManager.getAllQuestions();
		allAnswers = dataManager.getAllAnswers();
		allPlayerAnswers = dataManager.getAllPlayerAnswers();
	}

	private void updateStatisticsUI() {
		updateThemeTable();
	}

	private void updateThemeTable() {
		// Aggregation pro Theme: Fragen, beantwortete Fragen, Accuracy
		var rows = new ArrayList<Object[]>();
		for (ThemeDTO t : allThemes) {
			List<QuestionDTO> qs = allQuestions.stream().filter(q -> q.getThemeId() == t.getId())
					.collect(Collectors.toList());
			int qCount = qs.size();
			int answeredDistinct = countAnsweredQuestionsDistinctForTheme(t.getId(), qs);
			double acc = computeAccuracyForQuestions(qs);
			rows.add(new Object[] { t.getTitle(), qCount, answeredDistinct, String.format("%.1f%%", acc * 100) });
		}
		String[] cols = { "Theme", "Fragen", "Beantwortet", "Genauigkeit" };
		tableByTheme.setModel(new javax.swing.table.DefaultTableModel(rows.toArray(new Object[0][]), cols));
	}

	private double computeGlobalAccuracy() {
		// Idee:
		// Sammle alle PlayerAnswers, mappen auf Answer.isCorrect für die ausgewählten
		// Antworten.
		// Definiere: Accuracy = korrekte Auswahlen / alle Auswahlen (oder pro
		// Frage-Logik, je nach Modell)
		// Hier nur Platzhalter: implementiere, wenn getAllPlayerAnswers verfügbar ist.
		return 0.0;
	}

	private int countAnsweredQuestionsDistinct() {
		allPlayerAnswers = dataManager.getAllPlayerAnswers();
		return allPlayerAnswers.size();
	}

	private int countAnsweredQuestionsDistinctForTheme(int themeId, List<QuestionDTO> questionsOfTheme) {
		// Anzahl unterschiedlicher questionId in allPlayerAnswers, gefiltert auf Fragen
		// dieses Themes
		return 0;
	}

	private double computeAccuracyForQuestions(List<QuestionDTO> questions) {
		// Mittelwert der Frage-Accuracies über eine Menge Fragen
		return 0.0;
	}

	// ---- Listener ----

	@Override
	public void onQuestionsChanged() {
		loadData();
		updateStatisticsUI();
	}

	@Override
	public void onThemesChanged() {
		refreshThemesFromData();
		themeFilter.updateThemes(buildThemeItems());
		loadData();
		updateStatisticsUI();
	}

	private void refreshThemesFromData() {
		allThemes = dataManager.getAllThemes();
	}

	private List<ThemeListItem> buildThemeItems() {
		List<ThemeListItem> items = new ArrayList<>();
		items.add(new ThemeListItem(NO_SELECTION, ALL_THEMES));
		items.addAll(
				allThemes.stream().map(t -> new ThemeListItem(t.getId(), t.getTitle())).collect(Collectors.toList()));
		return items;
	}
}
