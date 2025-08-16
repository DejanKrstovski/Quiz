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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import bussinesLogic.datenBank.PlayerAnswerDTO;
import bussinesLogic.datenBank.QuestionDTO;
import bussinesLogic.datenBank.QuizDBDataManager;
import bussinesLogic.datenBank.ThemeDTO;
import gui.GuiConstants;
import gui.Panels.ComboBoxJListPanel;
import gui.Panels.SubPanel;
import gui.Swing.MyButton;
import gui.Swing.MyLabel;
import helpers.ThemeListItem;

public class MainStatisticPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();

	// UI
	private SubPanel topFilterPanel; // Filter + Aktionen
	private SubPanel kpiPanel; // KPI-Kacheln
	private SubPanel contentPanel; // Tabellen
	private ComboBoxJListPanel<ThemeListItem, Object> themeFilter; // nutze generisch
	private MyButton btnRefresh;
	private MyButton btnExportCsv;

	// Tabellen
	private JTable tableByTheme;
	private JTable tableHardestQuestions;

	// Daten
	private List<ThemeDTO> allThemes = new ArrayList<>();
	private List<QuestionDTO> allQuestions = new ArrayList<>();
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
		kpiPanel = initKpiPanel();
		contentPanel = initContentPanel();

		add(topFilterPanel, BorderLayout.NORTH);
		add(kpiPanel, BorderLayout.WEST);
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

		// Buttons
		btnRefresh = new MyButton("Aktualisieren");
		btnExportCsv = new MyButton("Export CSV");

		btnRefresh.addActionListener(e -> {
			loadData();
			updateStatisticsUI();
		});

		btnExportCsv.addActionListener(e -> exportStatsToCsv());

		p.add(themeFilter);
		p.add(btnRefresh);
		p.add(btnExportCsv);
		return p;
	}

	private SubPanel initKpiPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new GridLayout(4, 1, 8, 8));
		p.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		// Platzhalter; Labels werden in updateKpis() gesetzt
		p.add(new MyLabel("Themes: -"));
		p.add(new MyLabel("Fragen: -"));
		p.add(new MyLabel("Beantwortet: -"));
		p.add(new MyLabel("Genauigkeit: -"));
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
		// Diese Methode musst du im DataManager ergänzen:
		// allPlayerAnswers = dataManager.getAllPlayerAnswers();
		// Fallback: falls nicht verfügbar, über Theme/Question iterieren und eigene
		// Aggregation bauen.
	}

	private void updateStatisticsUI() {
		updateKpis();
		updateThemeTable();
//        updateHardestQuestionsTable();
	}

	private void updateKpis() {
		int themeCount = allThemes.size();
		int questionCount = allQuestions.size();

		// Korrektheit berechnen:
		// Annahme: PlayerAnswerDTO.selected==true speichert die Auswahl;
		// für Korrektheit brauchst du AnswerDTO.isCorrect() zu den gespeicherten
		// answerId.
		// Baue dir hier eine Hilfsmethode computeGlobalAccuracy()
		double accuracy = computeGlobalAccuracy();

		int answeredCount = countAnsweredQuestionsDistinct();

		// kpiPanel neu befüllen
		kpiPanel.removeAll();
		kpiPanel.setLayout(new GridLayout(4, 1, 8, 8));
		kpiPanel.add(new MyLabel("Themes: " + themeCount));
		kpiPanel.add(new MyLabel("Fragen: " + questionCount));
		kpiPanel.add(new MyLabel("Beantwortet: " + answeredCount));
		kpiPanel.add(new MyLabel(String.format("Genauigkeit: %.1f%%", accuracy * 100)));
		kpiPanel.revalidate();
		kpiPanel.repaint();
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

	private void updateHardestQuestionsTable() {
		// Für alle Fragen Accuracy berechnen, die mit Antworten in allPlayerAnswers
		// vorkommen
		var rows = allQuestions.stream()
				.map(q -> new Object[] { q.getTitle(), q.getId(), computeAccuracyForQuestion(q) })
				.sorted(Comparator.comparingDouble(o -> (double) o[2])) // aufsteigend: schwerste zuerst
				.limit(5).map(o -> new Object[] { o, String.format("%.1f%%", ((double) o[2]) * 100) })
				.collect(Collectors.toList());

		String[] cols2 = { "Frage", "Genauigkeit" };
		Object[][] data2 = (Object[][]) rows.toArray(new Object[0]);
		tableHardestQuestions.setModel(new javax.swing.table.DefaultTableModel(data2, cols2));
	}

	// ---- Hilfsfunktionen: Accuracy und Counts ----

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
		// Anzahl unterschiedlicher questionId in allPlayerAnswers
		return 0;
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

	private double computeAccuracyForQuestion(QuestionDTO q) {
		// Hole alle PlayerAnswers zu q, vergleiche mit Answer.isCorrect
		// Accuracy-Definition klar festlegen:
		// - Single-Choice: 1 wenn ausgewählte Antwort korrekt, sonst 0
		// - Multiple-Choice: Anzahl korrekt markierter / Anzahl markierter? Oder
		// strenger „alles richtig oder 0“?
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
		// Theme-Filter neu setzen
		themeFilter.updateThemes(buildThemeItems());
		loadData();
		updateStatisticsUI();
	}

	// ---- Utilities ----

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

	private void exportStatsToCsv() {
		// Implementiere Export der aktuellen Tabellen in CSV (z.B. über StringBuilder
		// und JFileChooser)
		// Optional: DataManager-Methode zum Schreiben auf Platte.
	}
}
