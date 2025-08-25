package gui.mainPanels;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import bussinesLogic.AnswerDTO;
import bussinesLogic.ErrorHandler;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import bussinesLogic.Validator;
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
import gui.swing.MyLabel;
import helpers.QuestionListItem;
import helpers.ThemeListItem;

/**
 * Panel for managing quiz questions, supporting creation, editing, and
 * deletion.
 * <p>
 * Displays a list of questions filtered by the selected theme
 * ({@link ThemeListItem}), and allows viewing/editing their title, theme,
 * question text, and possible answers.
 * </p>
 * 
 * <h2>Core Functions:</h2>
 * <ul>
 * <li>Select theme from combo box to load questions for that theme.</li>
 * <li>Create new questions for a theme.</li>
 * <li>Edit existing questions.</li>
 * <li>Delete questions.</li>
 * </ul>
 * 
 * <p>
 * Uses ID-based lookups (via {@link ThemeListItem} and
 * {@link QuestionListItem}) to avoid issues with duplicate or renamed titles.
 * Implements {@link ThemeChangeListener} so it refreshes when themes change in
 * other panels.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class MainQuestionPanel extends SubPanel implements ThemeChangeListener, GuiConstants {
	
	/** Access point for quiz themes and questions. */
	private final QuizDBDataManager dataManager = QuizDBDataManager.getInstance();
	private final QuizSManager sManager = QuizSManager.getInstance();
	private final ErrorHandler errorHandler = ErrorHandler.getInstance();
	private MyButton buttonShow;
	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;
	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;

	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> comboPanel;
	private String selectedThemeInfo;
	private int currentQuestionId = NO_SELECTION;
	private int selectedThemeId = NO_SELECTION;

	private ThemeDTO selectedTheme;
	private List<ThemeDTO> allThemes = new ArrayList<>();
	private List<ThemeListItem> themeItems = new ArrayList<>();
	private List<QuestionsChangeListener> questionChangeListeners = new ArrayList<>();
	private List<QuestionDTO> allQuestions;
	private List<QuestionListItem> questionItems;

	/**
	 * Constructs the panel, building its layout, components, and initial listeners.
	 */
	public MainQuestionPanel() {
		super();
		init();
		initQuestionListListener();
		initButtonActions();
	}

	/**
	 * Populates UI fields with the given question's data, or clears fields if null.
	 * 
	 * @param question Question to display, or null to clear.
	 */
	public void fillWithData(final QuestionDTO question) {
		clearAllFields();
		if (question != null) {
			currentQuestionId = question.getId();
			String themeTitel = getThemeById(question.getThemeId()).getTitle();
			themePanel.setText(themeTitel);
			titlePanel.setText(question.getTitle());
			questionPanel.setQuestionText(question.getText());
			final List<AnswerDTO> answers = dataManager.getAnswersFor(question);
			for (int i = 0; i < Math.min(answers.size(), MAX_ANSWERS); i++) {
				answerPanel.getAnswerField(i).setText(answers.get(i).getText());
				answerPanel.getAnswerCheckBox(i).setSelected(answers.get(i).isCorrect());
			}
		}
	}

	/** Initializes layout, components, and combo box listener. */
	private void init() {
		initLayout();
		initComponents();
		initComboBoxListener();
	}

	/** Sets main layout manager. */
	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/** Creates subcomponents and adds them to the main layout. */
	private void initComponents() {
		westPanel = initWestPanel();
		centerPanel = initCenterPanel();
		bottomPanel = new SouthPanel(SAVE_QUESTION, NEW_QUESTION, DELETE_QUESTION);

		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		refreshThemesFromData();
	}

	/** Builds the left-hand panel with form fields and answer editor. */
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

	/** @return Theme label/display field. */
	private LabelFieldPanel initThemePanel() {
		themePanel = new LabelFieldPanel(LABEL_THEME, EMPTY_STRING);
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return themePanel;
	}

	/** @return Question title text field. */
	private LabelFieldPanel initTitlePanel() {
		titlePanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
		titlePanel.getTextField().setEditable(true);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return titlePanel;
	}

	/** @return Question text area. */
	private LabelTextAreaPanel initQuestionPanel() {
		questionPanel = new LabelTextAreaPanel(LABEL_QUESTION);
		questionPanel.getQuestionTextArea().setEditable(true);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return questionPanel;
	}

	/** @return Header row for possible/correct answers. */
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

	/** @return Answers entry panel. */
	private AnswerPanel initAnswerPanel() {
		answerPanel = new AnswerPanel();
		return answerPanel;
	}

	/** @return Center panel holding theme label and combo/question list. */
	private SubPanel initCenterPanel() {
		final SubPanel panel = new SubPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
		panel.add(initThemeLabelPanel());
		panel.add(initComboPanel());
		return panel;
	}

	/** @return Panel with theme label and "show theme info" button. */
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
		buttonShow.setFocusable(false);
		return panel;
	}

	/** @return ComboBoxJListPanel initialized with themes and all questions. */
	private ComboBoxJListPanel<ThemeListItem, QuestionListItem> initComboPanel() {
		refreshThemesFromData();
		allQuestions = dataManager.getAllQuestions();
		questionItems = allQuestions.stream().map(q -> new QuestionListItem(q.getId(), q.getTitle()))
				.collect(Collectors.toList());
		comboPanel = new ComboBoxJListPanel<>(themeItems, questionItems);
		return comboPanel;
	}

	/** Loads themes and builds ThemeListItem objects (including ALL_THEMES). */
	private void refreshThemesFromData() {
		allThemes = dataManager.getAllThemes();
		themeItems = new ArrayList<>();
		themeItems.add(new ThemeListItem(NO_SELECTION, ALL_THEMES));
		themeItems.addAll(
				allThemes.stream().map(t -> new ThemeListItem(t.getId(), t.getTitle())).collect(Collectors.toList()));
	}

	/** Handles theme selection changes in the combo box. */
	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			ThemeListItem selectedItem = comboPanel.getSelectedThemeItem();
			if (selectedItem == null)
				return;
			selectedThemeId = selectedItem.getId();
			if (selectedThemeId == NO_SELECTION) {
				selectedTheme = null;
				buttonShow.setVisible(false);
				updateQuestionsList();
				return;
			}
			selectedTheme = getThemeById(selectedThemeId);
			if (selectedTheme != null) {
				selectedThemeInfo = selectedTheme.getText();
				buttonShow.setVisible(true);
				updateQuestionsList();
				themePanel.setText(selectedTheme.getTitle());
			} else {
				errorHandler.setError(ERROR_THEME_NOT_FOUND);
				showMessage(ErrorHandler.getInstance().getError());
			}
		});
	}

	/**
	 * Sets up button handlers for saving, clearing, deleting, and toggling theme
	 * info.
	 */
	private void initButtonActions() {
		final MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> saveQuestionFromUI());
		buttons[0].setMnemonic(KeyEvent.VK_S); // Alt+S activates the button
		buttons[1].addActionListener(e -> clearAllFields());
		buttons[2].addActionListener(e -> deleteQuestion());
		buttonShow.addActionListener(e -> toggleShowListOrInfo());
	}

	/** Adds a listener to respond to question selection in the list. */
	public void initQuestionListListener() {
		comboPanel.addQuestionSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				final QuestionListItem selectedItem = comboPanel.getSelectedQuestionItem();

				if (selectedItem == null) {
					return;
				}
				if (selectedItem != null && selectedItem.getId() != -1) {
					QuestionDTO question = new QuestionDTO();
					question = getQuestionById(selectedItem.getId());
					question.setAnswers(dataManager.getAnswersFor(question));
					fillWithData(question);
				} else {
					clearAllFields();
				}
			}
		});
	}

	/** Gathers data from UI and saves it as a new or updated question. */
	public void saveQuestionFromUI() {
		if (selectedThemeId == NO_SELECTION) {
			showMessage(CHOOSE_A_THEME_MSG);
			return;
		}
		final ThemeDTO theme = getThemeById(selectedThemeId);
		if (theme == null) {
			showMessage(ERROR_THEME_NOT_FOUND);
			return;
		}
		final QuestionDTO question = collectQuestionFromUI(theme);
		if (!Validator.validateQuestion(question) 
				|| !Validator.validateAnswers(question.getAnswers())
				|| !Validator.validateTheme(theme)) {
			showMessage(ErrorHandler.getInstance().getError());
			return;
		}
//		for(AnswerDTO answer : question.getAnswers()) {
//			sManager.saveAnswer(answer);
//		}
		final String result = dataManager.saveQuestion(question);
		sManager.saveQuestion(question);
		allQuestions = dataManager.getAllQuestions();
		questionItems = dataManager.getQuestionsFor(theme).stream()
		        .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
		        .collect(Collectors.toList());
		comboPanel.updateQuestions(questionItems);
		notifyQuestionsChanged();
		if (QUESTION_SAVED.equals(result)) {
			showMessage(QUESTION_SAVED);
			updateQuestionsList();
		} else {
			showMessage(result);
		}
	}

	/** Builds a Question from current UI field values for the specified theme. */
	private QuestionDTO collectQuestionFromUI(final ThemeDTO theme) {
		final QuestionDTO question = new QuestionDTO();
		question.setId(currentQuestionId);
		question.setTitle(titlePanel.getText());
		question.setText(questionPanel.getText());
		question.setThemeId(theme.getId());
		List<AnswerDTO> answers = new ArrayList<>();
		for (int i = 0; i < MAX_ANSWERS; i++) {
			final AnswerDTO answer = new AnswerDTO();
			String text = answerPanel.getAnswerField(i).getText();
			boolean isCorrect = answerPanel.getAnswerCheckBox(i).isSelected();
			if(!text.isEmpty() || !text.isBlank()) {
				answer.setText(text);
				answer.setCorrect(isCorrect);
				answer.setQuestionId(currentQuestionId);
				answers.add(answer);
			}
		}
		question.setAnswers(answers);
		return question;
	}

	/** Clears all input fields and resets selection state. */
	private void clearAllFields() {
		currentQuestionId = NO_SELECTION;
		titlePanel.setText(EMPTY_STRING);
		questionPanel.setQuestionText(EMPTY_STRING);
		for (int i = 0; i < MAX_ANSWERS; i++) {
			answerPanel.getAnswerField(i).setText(EMPTY_STRING);
			answerPanel.getAnswerCheckBox(i).setSelected(false);
		}
		showMessage("");
	}

	/** Deletes the currently selected question after user confirmation. */
	public void deleteQuestion() {
		final QuestionListItem selectedItem = comboPanel.getSelectedQuestionItem();
		if (selectedItem == null || selectedItem.getId() == -1) {
			bottomPanel.getMessagePanel().setMessage(CHOOSE_A_QUESTION_MSG);
			return;
		}
		final int confirm = JOptionPane.showConfirmDialog(this, QUESTION_DELETE_INFORMATION, DELETE_CONFIRMATION,
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			final QuestionDTO selectedQuestion = getQuestionById(selectedItem.getId());
			if (selectedQuestion != null) {
				String msg = dataManager.deleteQuestion(selectedQuestion);
				notifyQuestionsChanged();
				updateQuestionsList();
				clearAllFields();
				showMessage(msg);
			} else {
				showMessage(QUESTION_DELETING_NOT_POSSIBLE);
			}
		}
	}

	/** Refreshes the question list based on the current theme selection. */
	private void updateQuestionsList() {
	    allQuestions = dataManager.getAllQuestions();

	    if (selectedThemeId == NO_SELECTION) {
	        questionItems = allQuestions.stream()
	            .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
	            .collect(Collectors.toList());
	        comboPanel.updateQuestions(questionItems);
	        return;
	    }

	    ThemeDTO theme = getThemeById(selectedThemeId);
	    if (theme == null) {
	        comboPanel.updateQuestions(new ArrayList<>());
	        showMessage(ERROR_THEME_NOT_FOUND);
	        return;
	    }

	    List<QuestionListItem> filteredItems = dataManager.getQuestionsFor(theme).stream()
	        .map(q -> new QuestionListItem(q.getId(), q.getTitle()))
	        .collect(Collectors.toList());
	    comboPanel.updateQuestions(filteredItems);
	}


	/** Toggles between showing theme info text and the question list. */
	private void toggleShowListOrInfo() {
		if (buttonShow.getText().contains(LABEL_THEME)) {
			comboPanel.showInfo(selectedThemeInfo);
			buttonShow.setText(SHOW_LIST);
		} else {
			buttonShow.setText(SHOW_THEME);
			updateQuestionsList();
		}
	}

	/** Reloads the question list for the current theme. */
	public void refreshQuestionList() {
		updateQuestionsList();
	}

	/** Reloads the theme list from the data manager and updates the combo box. */
	public void refreshThemes() {
		refreshThemesFromData();
		comboPanel.updateThemes(themeItems);
	}

	/** Retrieves a Theme by its numeric ID. */
	private ThemeDTO getThemeById(int id) {
		return allThemes.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}

	/** Displays a message in the bottom panel's message area. */
	private void showMessage(final String message) {
		bottomPanel.getMessagePanel().setMessage(message);
	}

	/** Registers a listener to be invoked when questions change. */
	public void addOnQuestionsChangeListener(QuestionsChangeListener listener) {
		if(listener != null) {
			questionChangeListeners.add(listener);
		}
	}

	/** Invokes the registered QuestionsChangeListener, if present. */
	private void notifyQuestionsChanged() {
		for (QuestionsChangeListener listener : questionChangeListeners) {
			listener.onQuestionsChanged();
		}
	}

	/**
	 * Called when themes change anywhere in the app; reloads this panel's themes.
	 */
	@Override
	public void onThemesChanged() {
		refreshThemes();
	}

	/** @return Question object by ID, or null if not found. */
	private QuestionDTO getQuestionById(int id) {
		return allQuestions.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}
}
