package gui.mainPanels;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import bussinesLogic.datenBank.QuizDBDataManager;
import bussinesLogic.serialization.QuizSManager;
import gui.GuiConstants;
import gui.panels.AnswerPanel;
import gui.panels.ComboBoxJListPanel;
import gui.panels.LabelFieldPanel;
import gui.panels.LabelTextAreaPanel;
import gui.panels.SouthPanel;
import gui.panels.SubPanel;
import gui.swing.MyButton;
import helpers.QuestionListItem;
import helpers.ThemeListItem;

public class MainPlayPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();
    private final QuizSManager sManager = QuizSManager.getInstance();

	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;

	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;

	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> comboPanel;

	private List<ThemeDTO> allThemes = new ArrayList<>();
	private List<QuestionDTO> allQuestions = new ArrayList<>();

	private QuestionDTO currentQuestion;
	private ThemeDTO theme;
	private MyButton[] buttons;

	private List<AnswerDTO> visibleAnswers;

	public MainPlayPanel() {
		super();
		init();
		initListeners();
		loadRandomQuestion();
		setForeground(COLOR_RANDOM);
	}

	private void init() {
		setLayout(new BorderLayout());
		westPanel = initWestPanel();
		centerPanel = initCenterPanel();
		bottomPanel = new SouthPanel(SHOW_ANSWER, SAVE_ANSWER, NEXT_QUESTION);

		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		refreshThemesFromData();
		updateQuestionList();
	}

	private SubPanel initWestPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(WEST_PANEL_DIMENSIONS);
		panel.setMaximumSize(WEST_PANEL_DIMENSIONS);

		themePanel = new LabelFieldPanel(LABEL_THEME, EMPTY_STRING);
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		titlePanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);

		questionPanel = new LabelTextAreaPanel(LABEL_QUESTION);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);

		answerPanel = new AnswerPanel();

		panel.add(themePanel);
		panel.add(titlePanel);
		panel.add(questionPanel);
		panel.add(answerPanel);

		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		return panel;
	}

	private SubPanel initCenterPanel() {
		SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		panel.add(initComboPanel());
		return panel;
	}

	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> initComboPanel() {
		refreshThemesFromData();
		allQuestions = dataManager.getAllQuestions();
		List<QuestionListItem> questionItems = allQuestions.stream()
				.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
		comboPanel = new ComboBoxJListPanel<>(buildThemeItems(), questionItems);
		return comboPanel;
	}

	private void initListeners() {
		comboPanel.addThemeSelectionListener(e -> {
			ThemeListItem selectedItem = comboPanel.getSelectedThemeItem();
			if (selectedItem == null)
				return;

			if (selectedItem.getId() == NO_SELECTION) {
				currentQuestion = null;
				updateQuestionList();
				return;
			}
			updateQuestionList();
			loadRandomQuestion();
		});

		buttons = bottomPanel.getButtonsPanel().getButtons();
		MyButton btnShowSolution = buttons[0];
		MyButton btnSaveAnswer = buttons[1];
		MyButton btnNextQuestion = buttons[2];

		btnShowSolution.setMnemonic(KeyEvent.VK_A);
		btnSaveAnswer.setMnemonic(KeyEvent.VK_S);
		btnNextQuestion.setMnemonic(KeyEvent.VK_N);

		btnShowSolution.addActionListener(e -> showAnswer());
		btnSaveAnswer.addActionListener(e -> savePlayerAnswers());
		btnNextQuestion.addActionListener(e -> loadRandomQuestion());
	}

	private void fillWithData(QuestionDTO question) {
		clearAllFields();
		showMessage(EMPTY_STRING);
		if (question != null) {
			currentQuestion = question;

			ThemeDTO theme = getThemeById(question.getThemeId());
			themePanel.setText(theme != null ? theme.getTitle() : "");
			titlePanel.setText(question.getTitle());
			questionPanel.setQuestionText(question.getText());
			questionPanel.getQuestionTextArea().setEditable(false);
			visibleAnswers = new ArrayList<>(dataManager.getAnswersFor(question));
			currentQuestion.setAnswers(visibleAnswers);
			for (int i = 0; i < Math.min(visibleAnswers.size(), MAX_ANSWERS); i++) {
				answerPanel.getAnswerFields(i).setText(visibleAnswers.get(i).getText());
				answerPanel.getAnswerCheckBoxes(i).putClientProperty(ANSWER_ID, visibleAnswers.get(i).getId());
			}
			for (int i = 0; i < MAX_ANSWERS; i++)
				answerPanel.getAnswerFields(i).setEditable(false);
		}
	}

	private void clearAllFields() {
		themePanel.setText("");
		titlePanel.setText("");
		questionPanel.setQuestionText("");
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerFields(i).setText("");
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
		}
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

	private void updateQuestionList() {
		ThemeListItem selectedTheme = comboPanel.getSelectedThemeItem();
		List<QuestionListItem> newQuestions = new ArrayList<>();

		if (selectedTheme == null || selectedTheme.getId() == NO_SELECTION) {
			newQuestions = allQuestions.stream().map(q -> new QuestionListItem(q.getId(), q.getTitle()))
					.collect(Collectors.toList());
			comboPanel.updateQuestions(newQuestions);
			return;
		} else {
			theme = getThemeById(selectedTheme.getId());
			if (theme != null) {
				newQuestions = dataManager.getQuestionsFor(theme).stream()
						.map(q -> new QuestionListItem(q.getId(), q.getTitle())).collect(Collectors.toList());
			} else {
				newQuestions = new ArrayList<>();
			}
		}
		comboPanel.updateQuestions(newQuestions);
		if (currentQuestion == null || newQuestions.stream().noneMatch(q -> q.getId() == currentQuestion.getId())) {
			fillWithData(null);
		}

	}

	private ThemeDTO getThemeById(int id) {
		return allThemes.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}

	private void showAnswer() {
		String displayedTitle = titlePanel.getText();
		if (displayedTitle == null || displayedTitle.isEmpty()) {
			showMessage(QUESTION_NOT_SELECTED);
			return;
		}
		if (currentQuestion == null) {
			showMessage(QUESTION_NOT_FOUND);
			return;
		}
		List<Integer> correctIndices = new ArrayList<>();
		List<AnswerDTO> answers = currentQuestion.getAnswers();
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).isCorrect()) {
				correctIndices.add(i + 1);
			}
		}
		String correctText = "";
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
		buttons[1].setEnabled(false);
	}

	private void savePlayerAnswers() {
		if (currentQuestion == null) {
			showMessage(NO_QUESTION_LOADED);
			return;
		}
		List<AnswerDTO> possibleAnswers = visibleAnswers;
		boolean anySelected = false;
		for (int i = 0; i < possibleAnswers.size() && i < MAX_ANSWERS; i++) {
			if (answerPanel.getAnswerCheckBoxes(i).isSelected()) {
				anySelected = true;
				break;
			}
		}
		if (!anySelected) {
			showMessage("Bitte mindestens eine Antwort auswählen.");
			return;
		}
		for (int i = 0; i < possibleAnswers.size() && i < MAX_ANSWERS; i++) {
			var cb = answerPanel.getAnswerCheckBoxes(i);
			if (answerPanel.getAnswerCheckBoxes(i).isSelected()) {
				Object idObj = cb.getClientProperty(ANSWER_ID);
				if (idObj == null) {
		            showMessage(ERROR_MISSING_ID);
		            return;
		        }
		        int answerId = (int) idObj;
				PlayerAnswerDTO playerAnswer = new PlayerAnswerDTO();
				playerAnswer.setQuestionId(currentQuestion.getId());
				playerAnswer.setAnswerId(answerId);

				String result = dataManager.savePlayerAnswer(playerAnswer);
				sManager.savePlayerAnswer(playerAnswer);
				if (result != null) {
					showMessage(result);
					return;
				}
			}
		}
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(false);
		}
		showMessage(ANSWER_SAVED);
		buttons[1].setEnabled(false);
	}

	private void loadRandomQuestion() {
		updateQuestionList();
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(true);
		}
		if (!allQuestions.isEmpty()) {
			if (theme == null || comboPanel.getSelectedThemeItem().getId() == NO_SELECTION) {
				currentQuestion = dataManager.getRandomQuestion();
			} else {
				currentQuestion = dataManager.getRandomQuestionFor(theme);
			}
			if (currentQuestion != null)
				fillWithData(currentQuestion);
			else
				showMessage(ERROR_NO_QUESTIONS_FOR_THEME);
		}
		buttons[1].setEnabled(true);
	}

	@Override
	public void onQuestionsChanged() {
		refreshQuestions();
		currentQuestion = null;
		updateQuestionList();
	}

	private void refreshQuestions() {
		allQuestions = dataManager.getAllQuestions();
	}

	@Override
	public void onThemesChanged() {
		refreshThemesFromData();
		comboPanel.updateThemes(buildThemeItems());
		updateQuestionList();
	}

	/** Displays a message in the bottom panel's message area. */
	private void showMessage(final String message) {
		bottomPanel.getMessagePanel().setMessageAreaText(message);
	}
}
