package gui.mainPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import helpers.ThemeListItem;
import persistence.serialization.QuizDataManager;
import quizlogic.Answer;
import quizlogic.Question;
import quizlogic.Theme;

/**
 * The main quiz 'play' panel for users to practice questions interactively.
 * <p>
 * Displays questions, answers, and allows selecting questions by theme. Also
 * enables users to reveal the correct solution, check their answer, or load a
 * new question. All theme/question lookup is performed by ID via
 * {@link ThemeListItem} and {@link QuestionListItem}.
 * </p>
 *
 * <h2>Key Features</h2>
 * <ul>
 * <li>Theme selection (supports "All Themes" and per-theme).</li>
 * <li>Shows the current question, possible answers, and tracks the question
 * ID.</li>
 * <li>Allows users to check their answer and see the correct one.</li>
 * <li>Switches to a new random question in the selected theme.</li>
 * <li>Listens for changes to the theme and question pool for live
 * refreshes.</li>
 * </ul>
 *
 * <h2>Event Handling</h2>
 * <p>
 * Implements {@link QuestionsChangeListener} and {@link ThemeChangeListener} so
 * that, when the quiz data changes in other panels, this view immediately
 * reloads themes and questions.
 * </p>
 * 
 * @author
 */
public class MainPlayPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	/** Provides access to persistent quiz data and all themes/questions. */
	private final QuizDataManager dataManager = QuizDataManager.getInstance();

	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;
	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;

	/**
	 * GUI control for picking themes/questions. Holds
	 * ThemeListItem/QuestionListItem.
	 */
	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> comboPanel;

	/** Cached list of all loaded Theme objects. */
	private List<Theme> allThemes = new ArrayList<>();
	/** Parallel display objects for theme combo/list—includes "All Themes". */
	private List<ThemeListItem> themeItems = new ArrayList<>();
	/** Currently selected theme business object in play panel. */
	private Theme selectedTheme;
	/** Currently displayed quiz question (for state/answer checking). */
	private Question randomQuestion;
	/** ID of the current quiz question (or -1 if none). */
	private int currentQuestionId = -1;

	/**
	 * Constructs and fully initializes the main play panel, loading the default
	 * random question for user interaction.
	 */
	public MainPlayPanel() {
		super();
		init();
		initButtonActions();
		fillWithData(dataManager.getRandomQuestion());
	}

	/**
	 * Loads quiz question data into all UI fields. If the provided question is
	 * {@code null}, clears the panels.
	 *
	 * @param question The {@link Question} to populate the UI, or {@code null} to
	 *                 clear all fields.
	 */
	private void fillWithData(final Question question) {
		if (question == null) {
			themePanel.setText(EMPTY_STRING);
			titlePanel.setText(EMPTY_STRING);
			questionPanel.setTextInfo(EMPTY_STRING);
			for (int i = 0; i < MAX_ANSWERS; i++) {
				answerPanel.getAnswerFields(i).setText(EMPTY_STRING);
				answerPanel.getAnswerCheckBoxes(i).setSelected(false);
			}
			currentQuestionId = -1;
			return;
		}

		themePanel.setText(question.getTheme().getTitle());
		titlePanel.setText(question.getTitle());
		questionPanel.setTextInfo(question.getText());
		currentQuestionId = question.getId();

		final List<Answer> answers = question.getAnswers();
		final int answerCount = Math.min(answers.size(), MAX_ANSWERS);

		for (int i = 0; i < answerCount; i++) {
			answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
		}
		for (int i = answerCount; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerFields(i).setText("");
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
		}
	}

	/**
	 * Initializes the panel layout, subpanels, themes, and listeners.
	 */
	private void init() {
		initLayout();
		initComponents();
		refreshThemesAndItems();
		initComboBoxListener();
	}

	/**
	 * Sets the main layout manager for this panel.
	 */
	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Loads (or reloads) all themes from the backend, and maintains the parallel
	 * ThemeListItem objects, with "All Themes" as a special entry.
	 */
	private void refreshThemesAndItems() {
		allThemes = dataManager.getThemes();
		themeItems = new ArrayList<>();
		themeItems.add(new ThemeListItem(-1, ALL_THEMES));
		themeItems.addAll(
				allThemes.stream().map(t -> new ThemeListItem(t.getId(), t.getTitle())).collect(Collectors.toList()));
	}

	/**
	 * Reloads the question list in the combo panel, based on the currently selected
	 * theme. If no theme is selected, all questions are shown.
	 */
	private void refreshQuestionsList() {
		if (selectedTheme == null || selectedTheme.getTitle().equals(ALL_THEMES)) {
			comboPanel.updateQuestions(dataManager.getAllThemeQuestions().stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList()));
		} else {
			List<QuestionListItem> themeQuestions = dataManager.getQuestionsForTheme(selectedTheme).stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			comboPanel.updateQuestions(themeQuestions);
		}
	}

	/**
	 * Initializes and arranges all major subpanels visually.
	 */
	private void initComponents() {
		westPanel = createWestPanel();
		centerPanel = createCenterPanel();
		bottomPanel = new SouthPanel(SHOW_ANSWER, SAVE_ANSWER, NEXT_QUESTION);

		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private SubPanel createWestPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(WEST_PANEL_DIMENSIONS);
		panel.setMaximumSize(WEST_PANEL_DIMENSIONS);

		panel.add(createThemePanel());
		panel.add(createTitlePanel());
		panel.add(createQuestionPanel());
		panel.add(createPossibleAnswerPanel());
		panel.add(createAnswerPanel());
		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

		return panel;
	}

	/** @return Theme field editor/subpanel */
	private LabelFieldPanel createThemePanel() {
		themePanel = new LabelFieldPanel(LABEL_THEME, EMPTY_STRING);
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return themePanel;
	}

	/** @return Title field editor/subpanel */
	private LabelFieldPanel createTitlePanel() {
		titlePanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return titlePanel;
	}

	/** @return Question text area editor/subpanel */
	private LabelTextAreaPanel createQuestionPanel() {
		questionPanel = new LabelTextAreaPanel(LABEL_QUESTION);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return questionPanel;
	}

	/** @return Panel showing answer labels/headers */
	private SubPanel createPossibleAnswerPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(DISTANCE_BETWEEN_ELEMENTS);

		MyLabel possibleAnswerLabel = new MyLabel(LABEL_POSSIBLE_ANSWERS);
		MyLabel correctAnswerLabel = new MyLabel(CORRECTNESS);

		panel.add(possibleAnswerLabel);
		panel.add(Box.createHorizontalGlue());
		panel.add(correctAnswerLabel);
		return panel;
	}

	/** @return Read-only answer entry panel for answer buttons/checkboxes */
	private SubPanel createAnswerPanel() {
		answerPanel = new AnswerPanel();
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerFields(i).setEditable(false);
		}
		return answerPanel;
	}

	/**
	 * Initializes and returns the central subpanel with the theme/question
	 * selector.
	 * 
	 * @return The center panel with theme and question list.
	 */
	private SubPanel createCenterPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		panel.add(createComboPanel());
		return panel;
	}

	/**
	 * Creates the combobox/list control for selecting themes and questions.
	 * 
	 * @return The populated ComboBoxJListPanel for this quiz view.
	 */
	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> createComboPanel() {
		refreshThemesAndItems();
		final List<QuestionListItem> questionItems = dataManager.getAllThemeQuestions().stream()
				.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());

		comboPanel = new ComboBoxJListPanel<>(themeItems, questionItems);
		return comboPanel;
	}

	/**
	 * Called to reload all themes and questions from persistent storage and update
	 * the theme and question combo/list with new data. This should be called when
	 * themes or questions in the app have changed.
	 */
	public void refreshThemes() {
		refreshThemesAndItems();
		comboPanel.updateThemes(themeItems);
		refreshQuestionsList();
	}

	/**
	 * Sets up the combo box to load and display questions for the selected theme.
	 * If the user selects "All Themes", all questions are shown at random.
	 */
	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			ThemeListItem selectedThemeItem = comboPanel.getSelectedThemeItem();
			if (selectedThemeItem == null) {
				selectedTheme = null;
				themePanel.setText(EMPTY_STRING);
				comboPanel.updateQuestions(new ArrayList<>());
				return;
			}
			themePanel.setText(selectedThemeItem.getTitle());
			selectedTheme = getThemeById(selectedThemeItem.getId());
			refreshQuestionsList();
			nextQuestion(selectedTheme);
		});
	}

	/**
	 * Loads a new random question for the current theme (or across all themes), and
	 * fills the content fields. If the theme has no questions, displays an error.
	 * 
	 * @param theme The selected theme, or {@code null} for "all themes".
	 */
	private void nextQuestion(Theme theme) {
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
			answerPanel.getAnswerCheckBoxes(i).setEnabled(true);
		}

		Random r = new Random();
		randomQuestion = null;

		if (theme == null || theme.getTitle().equals(ALL_THEMES)) {
			randomQuestion = dataManager.getRandomQuestion();
			comboPanel.updateQuestions(dataManager.getAllThemeQuestions().stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList()));
			showMessage(EMPTY_STRING);
		} else {
			List<Question> themeQuestions = dataManager.getQuestionsForTheme(theme);
			if (themeQuestions == null || themeQuestions.isEmpty()) {
				showMessage(ERROR_NO_QUESTIONS_FOR_THEME);
				fillWithData(null);
				return;
			}
			showMessage(EMPTY_STRING);
			int randomIndex = r.nextInt(themeQuestions.size());
			randomQuestion = themeQuestions.get(randomIndex);
			List<QuestionListItem> themeQuestionsAsList = themeQuestions.stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			comboPanel.updateQuestions(themeQuestionsAsList);
		}
		fillWithData(randomQuestion);
	}

	/**
	 * Adds listeners to quiz control buttons: "Show Answer", "Save Answer", "Next
	 * Question". Handles answer checking, solution display, and cycling to new
	 * questions.
	 */
	private void initButtonActions() {
		final MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> showAnswer());
		buttons[1].addActionListener(e -> saveAnswer());
		buttons[2].addActionListener(e -> nextQuestion(selectedTheme));
	}

	/**
	 * Shows which answers are correct for the current question. Displays this
	 * information in the questions list panel and disables answer input.
	 */
	private void showAnswer() {
		String displayedTitle = titlePanel.getText();
		if (displayedTitle == null || displayedTitle.isEmpty()) {
			showMessage(QUESTION_NOT_SELECTED);
			return;
		}
		Question currentQuestion = findQuestionById(currentQuestionId);
		if (currentQuestion == null) {
			showMessage(QUESTION_NOT_FOUND);
			return;
		}
		List<Integer> correctIndices = new ArrayList<>();
		List<Answer> answers = currentQuestion.getAnswers();
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).isCorrect()) {
				correctIndices.add(i + 1);
			}
		}
		String correctText;
		if (correctIndices.isEmpty()) {
			correctText = NO_RIGHT_ANSWER_FOUND;
		} else {
			correctText = THE_RIGHT_ANSWER_IS
					+ correctIndices.stream().map(String::valueOf).collect(Collectors.joining(", "));
		}
		List<QuestionListItem> specialList = new ArrayList<>();
		specialList.add(new QuestionListItem(-1, correctText));
		comboPanel.updateQuestions(specialList);

		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(false);
		}
	}

	/**
	 * Checks if the user's answer matches the actual correct answer(s) for the
	 * current question, and displays immediate feedback with message and disables
	 * answer input.
	 */
	private void saveAnswer() {
		boolean[] userSelections = new boolean[MAX_ANSWERS];
		boolean isAnySelected = false;
		for (int i = 0; i < MAX_ANSWERS; i++) {
			userSelections[i] = answerPanel.getAnswerCheckBoxes(i).isSelected();
			if (userSelections[i]) {
				isAnySelected = true;
			}
		}
		if (currentQuestionId == -1) {
			showMessage(QUESTION_NOT_SELECTED);
			return;
		}
		Question currentQuestion = findQuestionById(currentQuestionId);
		if (currentQuestion == null) {
			showMessage(QUESTION_NOT_FOUND);
			return;
		}

		List<Answer> answers = currentQuestion.getAnswers();
		boolean allCorrect = true;
		boolean hasCorrectSelection = false;
		for (int i = 0; i < Math.min(answers.size(), MAX_ANSWERS); i++) {
			boolean isCorrect = answers.get(i).isCorrect();
			boolean userChecked = userSelections[i];
			if (userChecked && isCorrect) {
				hasCorrectSelection = true;
			} else if (userChecked && !isCorrect) {
				allCorrect = false;
			} else if (!userChecked && isCorrect) {
				allCorrect = false;
			}
		}

		if (!isAnySelected) {
			showMessage(CHOOSE_AN_ANSWER);
		} else if (allCorrect && hasCorrectSelection) {
			showMessage(CORRECT_ANSWER);
		}
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(false);
		}
	}

	/**
	 * Returns a question object for the requested id, searching all loaded
	 * questions.
	 *
	 * @param questionId The unique question id to find.
	 * @return The {@link Question} matching the id, or {@code null} if not found.
	 */
	private Question findQuestionById(int questionId) {
		return dataManager.getAllThemeQuestions().stream().filter(q -> q.getId() == questionId).findFirst()
				.orElse(null);
	}

	/**
	 * Returns a theme object for the requested id, searching all loaded themes.
	 *
	 * @param id The unique theme id to find.
	 * @return The {@link Theme} matching the id, or {@code null} if not found.
	 */
	private Theme getThemeById(int id) {
		return allThemes.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}

	/**
	 * Called whenever question data has changed (from another part of the app).
	 * Triggers a full reload of themes/questions to reflect the latest state.
	 */
	@Override
	public void onQuestionsChanged() {
		refreshThemes();
	}

	/**
	 * Called whenever theme data has changed (from another part of the app).
	 * Triggers a full reload of themes/questions to reflect the latest state.
	 */
	@Override
	public void onThemesChanged() {
		refreshThemes();
	}

	/**
	 * Displays a message to the user in the play panel's message area.
	 * 
	 * @param message Message to display (never {@code null})
	 */
	private void showMessage(String message) {
		bottomPanel.getMessagePanel().setMessageAreaText(message);
	}
}
