package gui.mainPanels;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;

import bussinesLogic.datenBank.AnswerDTO;
import bussinesLogic.datenBank.PlayerAnswerDTO;
import bussinesLogic.datenBank.QuestionDTO;
import bussinesLogic.datenBank.QuizDBDataManager;
import bussinesLogic.datenBank.ThemeDTO;
import gui.GuiConstants;
import gui.Panels.AnswerPanel;
import gui.Panels.ComboBoxJListPanel;
import gui.Panels.LabelFieldPanel;
import gui.Panels.LabelTextAreaPanel;
import gui.Panels.SouthPanel;
import gui.Panels.SubPanel;
import gui.Swing.MyButton;
import helpers.QuestionListItem;
import helpers.ThemeListItem;

public class MainPlayPanel extends SubPanel implements QuestionsChangeListener, ThemeChangeListener, GuiConstants {

	private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();

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

	private int currentQuestionId;

	public MainPlayPanel() {
		super();
		init();
		initListeners();
		loadRandomQuestion();
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
			System.out.println(selectedItem);
			if (selectedItem == null)
				return;

			if (selectedItem.getId() == NO_SELECTION) {
				currentQuestion = null;
				updateQuestionList();
				return;
			}
			updateQuestionList();
		});

		MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		MyButton btnShowSolution = buttons[0];
		MyButton btnCheckAnswer = buttons[1];
		MyButton btnNextQuestion = buttons[2];

		btnShowSolution.setMnemonic(KeyEvent.VK_S);
		btnCheckAnswer.setMnemonic(KeyEvent.VK_C);
		btnNextQuestion.setMnemonic(KeyEvent.VK_N);

		btnShowSolution.addActionListener(e -> showAnswer());
		btnCheckAnswer.addActionListener(e -> savePlayerAnswers());
		btnNextQuestion.addActionListener(e -> loadRandomQuestion());
	}

	private void fillWithData(QuestionDTO question) {
		clearAllFields();
		if (question != null) {
			currentQuestionId = question.getId();
			System.out.println("Current is : "+ currentQuestionId + " And actuell is: " + question.getId());
			ThemeDTO theme = getThemeById(question.getThemeId());
			themePanel.setText(theme != null ? theme.getTitle() : "");
			titlePanel.setText(question.getTitle());
			questionPanel.setQuestionText(question.getText());
			questionPanel.getQuestionTextArea().setEditable(false);
			List<AnswerDTO> answers = dataManager.getAnswersFor(question);
			for (int i = 0; i < Math.min(answers.size(), MAX_ANSWERS); i++) {
				answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
			}
			for (int i = 0; i <  MAX_ANSWERS; i++)
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
	    List<QuestionListItem> newQuestions;

	    if (selectedTheme == null || selectedTheme.getId() == NO_SELECTION) {
	        newQuestions = dataManager.getAllQuestions().stream()
	                .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
	                .collect(Collectors.toList());
	    } else {
	        theme = getThemeById(selectedTheme.getId());
	        if (theme != null) {
	            newQuestions = dataManager.getQuestionsFor(theme).stream()
	                    .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
	                    .collect(Collectors.toList());
	        } else {
	            newQuestions = new ArrayList<>();
	        }
	    }

	    if (currentQuestion == null || 
	    	    newQuestions.stream().noneMatch(q -> q.getId() == currentQuestion.getId())) {
	    	    fillWithData(null); // no match found, reset
	    	}
	    comboPanel.updateQuestions(newQuestions);
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
		QuestionDTO currentQuestion = findQuestionById(currentQuestionId);
		if (currentQuestion == null) {
			showMessage(QUESTION_NOT_FOUND);
			return;
		}
		currentQuestion.setAnswers(dataManager.getAnswersFor(currentQuestion));
		List<Integer> correctIndices = new ArrayList<>();
		List<AnswerDTO> answers = currentQuestion.getAnswers();
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

	private void savePlayerAnswers() {
	    if (currentQuestion == null) {
	        showMessage("Es ist keine Frage geladen.");
	        return;
	    }
	    List<AnswerDTO> possibleAnswers = dataManager.getAnswersFor(currentQuestion);
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
	        if (answerPanel.getAnswerCheckBoxes(i).isSelected()) {
	            AnswerDTO answer = possibleAnswers.get(i);

	            PlayerAnswerDTO playerAnswer = new PlayerAnswerDTO();
	            playerAnswer.setQuestionId(currentQuestion.getId());
	            playerAnswer.setAnswerId(answer.getId());
	            playerAnswer.setSelected(true);

	            String result = dataManager.savePlayerAnswer(playerAnswer);
	            if (result != null) {
	                showMessage("Fehler: " + result);
	                return;
	            }
	        }
	    }
	    for (int i = 0; i < MAX_ANSWERS; i++) {
	        answerPanel.getAnswerCheckBoxes(i).setEnabled(false);
	    }
	    showMessage("Antwort gespeichert");
	}

	private void loadRandomQuestion() {
		updateQuestionList();
		for(int i=0; i<MAX_ANSWERS; i++) {
			answerPanel.getAnswerCheckBoxes(i).setEnabled(true);
		}
		if (!allQuestions.isEmpty()) {
			if (theme == null || comboPanel.getSelectedThemeItem().getId() == NO_SELECTION) {
				currentQuestion = dataManager.getRandomQuestion();
			} else {
				currentQuestion = dataManager.getRandomQuestionFor(theme);
				System.out.println(theme);
			}
			fillWithData(currentQuestion);
		}
	}

	private QuestionDTO findQuestionById(int questionId) {
		return dataManager.getAllQuestions().stream().filter(q -> q.getId() == questionId).findFirst()
				.orElse(null);
	}
	
	@Override
	public void onQuestionsChanged() {
		allQuestions = dataManager.getAllQuestions();
		updateQuestionList();
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
