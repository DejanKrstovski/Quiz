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
import persistence.serialization.QuizDataManager;
import quizlogic.Answer;
import quizlogic.Question;
import quizlogic.Theme;

/**
 * Main quiz panel where users can view questions, answers, and select themes.
 * <p>
 * This panel displays the quiz question, theme, title, answer options, and
 * allows interaction such as showing answers, saving answers, and loading the
 * next question.
 * </p>
 * <p>
 * Implements {@link QuestionsChangeListener} to refresh theme and question data
 * when changes occur elsewhere.
 * </p>
 * 
 * @author
 */
public class MainPlayPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	private final QuizDataManager dataManager = QuizDataManager.getInstance();

	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;
	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;
	private ComboBoxJListPanel comboPanel;
	private Theme selectedTheme;
	private Question randomQuestion;
	private int currentQuestionId;

	/**
	 * Constructs the MainPlayPanel and initializes its components.
	 */
	public MainPlayPanel() {
		super();
		init();
		initButtonActions();
		fillWithData(dataManager.getRandomQuestion());
	}

	/**
	 * Fills UI fields with data from the provided question. If question is null, no
	 * changes are made.
	 * 
	 * @param question the {@link Question} to display
	 */
	private void fillWithData(final Question question) {
		if (question == null) {
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

	private void init() {
		initLayout();
		initComponents();
		initComboBoxListener();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Initializes and adds the main subpanels to this panel.
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

	private LabelFieldPanel createThemePanel() {
		themePanel = new LabelFieldPanel(LABEL_THEME, EMPTY_STRING);
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return themePanel;
	}

	private LabelFieldPanel createTitlePanel() {
		titlePanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return titlePanel;
	}

	private LabelTextAreaPanel createQuestionPanel() {
		questionPanel = new LabelTextAreaPanel(LABEL_QUESTION);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return questionPanel;
	}

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

	private SubPanel createAnswerPanel() {
		answerPanel = new AnswerPanel();
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerFields(i).setEditable(false);
		}
		return answerPanel;
	}

	private SubPanel createCenterPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		panel.add(createComboPanel());
		return panel;
	}

	/**
	 * Creates combo box and list panel displaying themes and questions.
	 * 
	 * @return the initialized ComboBoxJListPanel
	 */
	private ComboBoxJListPanel createComboPanel() {
		final List<String> themeTitles = dataManager.getThemes().stream().map(Theme::getTitle)
				.collect(Collectors.toList());

		final List<String> comboItems = new ArrayList<>();
		comboItems.add(ALL_THEMES);
		comboItems.addAll(themeTitles);

		final List<QuestionListItem> questionItems = dataManager.getAllThemeQuestions().stream()
				.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
		comboPanel = new ComboBoxJListPanel(comboItems, questionItems);
		return comboPanel;
	}

	/**
	 * Refreshes the themes and questions shown in combo panel. To be called when
	 * question or theme data changes.
	 */
	public void refreshThemes() {
		final List<String> themes = dataManager.getThemes().stream().map(Theme::getTitle)
				.collect(Collectors.toList());

		comboPanel.updateThemes(themes);

		final List<QuestionListItem> questionItems = dataManager.getAllThemeQuestions().stream()
				.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());

		comboPanel.updateQuestions(questionItems);
	}

	/**
	 * Initializes combo box theme selection listener to update question list
	 * accordingly.
	 */
	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			final String selectedThemeTitle = (String) comboPanel.getSelectedTheme();
			if (selectedThemeTitle == null) {
				themePanel.setText("");
				comboPanel.updateQuestions(new ArrayList<>());
				return;
			}
			themePanel.setText(selectedThemeTitle);
			selectedTheme = dataManager.getThemes().stream()
					.filter(t -> t.getTitle().trim().equalsIgnoreCase(selectedThemeTitle.trim())).findFirst()
					.orElse(null);

			updateQuestionsList(selectedThemeTitle);
			nextQuestion(selectedTheme);
		});

	}

	/**
	 * Updates the combo panel's question list depending on selected theme.
	 * 
	 * @param themeTitle the theme title selected in the combo box
	 */
	private void updateQuestionsList(String themeTitle) {
		if (ALL_THEMES.equalsIgnoreCase(themeTitle)) {
			final List<QuestionListItem> allQuestions = dataManager.getAllThemeQuestions().stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			comboPanel.updateQuestions(allQuestions);
		} else {
			selectedTheme = dataManager.getThemes().stream()
					.filter(t -> t.getTitle().trim().equalsIgnoreCase(themeTitle.trim())).findFirst().orElse(null);
			if (selectedTheme == null) {
				comboPanel.updateQuestions(new ArrayList<>());
				themePanel.setText(EMPTY_STRING);
			} else {
				List<QuestionListItem> themeQuestions = dataManager.getQuestionsForTheme(selectedTheme).stream()
						.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
				comboPanel.updateQuestions(themeQuestions);
			}
		}
	}

	/**
	 * Initializes button action listeners for Show Answer, Save Answer, and Next
	 * Question.
	 */
	private void initButtonActions() {
		final MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> showAnswer());
		buttons[1].addActionListener(e -> saveAnswer());
		buttons[2].addActionListener(e -> nextQuestion(selectedTheme));
	}

	/**
	 * 
	 */
	private void showAnswer() {
		String displayedTitle = titlePanel.getText();
		if (displayedTitle == null || displayedTitle.isEmpty()) {
			showMessage(QUESTION_NOT_SELECTED);
			return;
		}
		Question currentQuestion = dataManager.getAllThemeQuestions().stream()
				.filter(q -> q.getTitle().equals(displayedTitle)).findFirst().orElse(null);
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
	 * Checks the user's selected answers, gives immediate feedback by highlighting
	 * correct and incorrect answers, and optionally updates a score or shows a
	 * message.
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
		String displayedQuestionTitle = titlePanel.getText();
		if (displayedQuestionTitle == null || displayedQuestionTitle.isEmpty()) {
			showMessage(QUESTION_NOT_SELECTED);
			return;
		}
		Question currentQuestion = dataManager.getAllThemeQuestions().stream().filter(q -> q.getId() == currentQuestionId)
				.findFirst().orElse(null);
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
			showMessage("Bitte mindestens eine Antwort auswählen.");
		} else if (allCorrect && hasCorrectSelection) {
			showMessage("Richtig! Alle richtigen Antworten ausgewählt.");
		}
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(false);
		}
	}

	/**
	 * Loads next random question and fills the UI fields. Handles possible empty
	 * question list gracefully.
	 */
	private void nextQuestion(Theme theme) {
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
			answerPanel.getAnswerCheckBoxes(i).setEnabled(true);
		}

		Random r = new Random();
		randomQuestion = new Question();

		if (theme == null || theme.getTitle().equals(ALL_THEMES)) {
			randomQuestion = dataManager.getRandomQuestion();
			List<QuestionListItem> allQuestions = dataManager.getAllThemeQuestions().stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			comboPanel.updateQuestions(allQuestions);
		} else {

			List<Question> themeQuestions = dataManager.getQuestionsForTheme(selectedTheme);
			
			if (themeQuestions == null || themeQuestions.isEmpty()) {
				showMessage(ERROR_NO_QUESTIONS_FOR_THEME);
				return;
			}
			showMessage(EMPTY_STRING);
			int randomId = r.nextInt(themeQuestions.size());
			randomQuestion = themeQuestions.get(randomId);
			List<QuestionListItem> themeQuestionsAsList = themeQuestions.stream()
					.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			comboPanel.updateQuestions(themeQuestionsAsList);
		}
		if (randomQuestion != null) {
			fillWithData(randomQuestion);
		} else {
			themePanel.setText(EMPTY_STRING);
			titlePanel.setText(EMPTY_STRING);
			questionPanel.setTextInfo(EMPTY_STRING);
			for (int i = 0; i < MAX_ANSWERS; i++) {
				answerPanel.getAnswerFields(i).setText(EMPTY_STRING);
				answerPanel.getAnswerCheckBoxes(i).setSelected(false);
			}
		}
	}

	/**
	 * Called when questions data has changed; refreshes theme and question lists.
	 */
	@Override
	public void onQuestionsChanged() {
		refreshThemes();
	}

	private void showMessage(String message) {
		bottomPanel.getMessagePanel().setMessageAreaText(message);
	}

	@Override
	public void onThemesChanged() {
		refreshThemes();		
	}
}
