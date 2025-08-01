package gui.mainGuiPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiPanels.AnswerPanel;
import gui.guiPanels.ComboBoxJListPanel;
import gui.guiPanels.LabelFieldPanel;
import gui.guiPanels.LabelTextAreaPanel;
import gui.guiPanels.SouthPanel;
import gui.guiPanels.SubPanel;
import gui.guiSwing.MyButton;
import gui.guiSwing.MyLabel;
import quizlogic.Answer;
import quizlogic.FakeDataDeliverer;
import quizlogic.Question;
import quizlogic.Theme;

/**
 * The main panel responsible for creating, displaying, and managing questions
 * related to a selected quiz theme. Users can create new questions, edit
 * existing ones, or delete them.
 * 
 * This panel contains input fields for question title, text, possible answers,
 * and a list for theme-specific or global question selection.
 *
 * @author DejanKrstovski
 */
public class MainQuestionPanel extends JPanel implements GuiConstants {

	private MyButton buttonShow;
	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;
	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	private FakeDataDeliverer fdd;

	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;
	private ComboBoxJListPanel comboPanel;
	private String info;

	/**
	 * Constructs the question panel and initializes all subcomponents.
	 *
	 * @param fdd the data deliverer providing themes and questions
	 */
	public MainQuestionPanel(FakeDataDeliverer fdd) {
		super();
		this.fdd = fdd;
		init();
		initQuestionListListener();
		initButtonActions();
	}

	/**
	 * Populates the input fields with data from the selected question, including
	 * title, text, and answers with their correctness.
	 *
	 * @param q the question to load into the UI
	 */
	private void fillWithData(Question q) {
		if (q != null) {
			titlePanel.setText(q.getTitle());
			questionPanel.setTextInfo(q.getText());
			ArrayList<Answer> answers = q.getAnswers();
			for (int i = 0; i < Math.min(answers.size(), 4); i++) {
				answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
				answerPanel.getAnswerCheckBoxes(i).setSelected(answers.get(i).isCorrect());
			}
		}
	}

	/**
	 * Initializes layout and components of the main panel.
	 */
	private void init() {
		initLayout();
		initComponents();
		initComboBoxListener();
	}

	/**
	 * Sets the layout manager of the main panel to BorderLayout.
	 */
	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Initializes and adds all subpanels (west, center, and bottom) to the main
	 * layout.
	 */
	private void initComponents() {
		westPanel = initWestPanel();
		centerPanel = initCenterPanel();
		bottomPanel = new SouthPanel(SAVE_QUESTION, NEW_QUESTION, DELETE_QUESTION);
		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Creates the left subpanel that includes input fields for theme, title,
	 * question text, and answers.
	 *
	 * @return the initialized west subpanel
	 */
	private SubPanel initWestPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.PAGE_AXIS));
		p.setPreferredSize(WEST_PANEL_DIMENSIONS);
		p.setMaximumSize(WEST_PANEL_DIMENSIONS);
		p.add(initThemePanel());
		p.add(initTitlePanel());
		p.add(initQuestionPanel());
		p.add(initPossibleAnswerPanel());
		p.add(initAnswerPanel());
		p.setBorder(OUTSIDE_BORDERS);
		return p;
	}

	/**
	 * Creates the subpanel for the theme label and text field.
	 *
	 * @return the initialized theme panel
	 */
	private SubPanel initThemePanel() {
		themePanel = new LabelFieldPanel(THEME, EMPTY_STRING);
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return themePanel;
	}

	/**
	 * Creates the subpanel for entering the question title.
	 *
	 * @return the initialized title panel
	 */
	private SubPanel initTitlePanel() {
		titlePanel = new LabelFieldPanel(TITLE, EMPTY_STRING);
		titlePanel.getTextField().setEditable(true);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return titlePanel;
	}

	/**
	 * Creates the subpanel for entering the main question text.
	 *
	 * @return the initialized question panel
	 */
	private SubPanel initQuestionPanel() {
		questionPanel = new LabelTextAreaPanel(QUESTION);
		questionPanel.getQuestionTextArea().setEditable(true);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return questionPanel;
	}

	/**
	 * Creates a panel with labels for possible answers and their correctness.
	 *
	 * @return the initialized label panel
	 */
	private SubPanel initPossibleAnswerPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.LINE_AXIS));
		p.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		MyLabel possibleAnwserLabel = new MyLabel(POSSIBLE_ANSWERS);
		MyLabel rightAnswerLabel = new MyLabel(CORRECTNESS);
		p.add(possibleAnwserLabel);
		p.add(Box.createHorizontalGlue());
		p.add(rightAnswerLabel);
		return p;
	}

	/**
	 * Creates the panel used for entering multiple possible answers.
	 *
	 * @return the initialized answer panel
	 */
	private SubPanel initAnswerPanel() {
		answerPanel = new AnswerPanel();
		return answerPanel;
	}

	/**
	 * Creates the right subpanel with the theme label and a combo box list of
	 * questions.
	 *
	 * @return the initialized center panel
	 */
	private SubPanel initCenterPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(OUTSIDE_BORDERS);
		p.add(initThemaLabelPanel());
		p.add(initComboPanel());
		return p;
	}

	/**
	 * Creates a panel with the theme label and a button to toggle theme info or
	 * question list.
	 *
	 * @return the initialized theme label panel
	 */
	private SubPanel initThemaLabelPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.LINE_AXIS));
		p.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		MyLabel questionLabel = new MyLabel(QUESTION_FOR_THEME);
		p.add(questionLabel);
		buttonShow = new MyButton(SHOW_THEME);
		p.add(Box.createHorizontalGlue());
		p.add(buttonShow);
		buttonShow.setVisible(false);
		return p;
	}

	/**
	 * Creates and initializes the combo box (for themes) and JList (for questions).
	 *
	 * @return the initialized combo box/JList panel
	 */
	private SubPanel initComboPanel() {
		List<String> themeTitles = fdd.getThemes().stream().map(Theme::getTitle).collect(Collectors.toList());

		List<String> comboItems = new ArrayList<>();
		comboItems.add(ALL_THEMES);
		comboItems.addAll(themeTitles);

		List<String> listQuestions = new ArrayList<>(fdd.getAllQuestionTitle().values());
		comboPanel = new ComboBoxJListPanel(comboItems, listQuestions);
		return comboPanel;
	}

	/**
	 * Adds a listener to the combo box. Updates UI and visibility based on theme
	 * selection.
	 */
	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			String selectedTheme = (String) comboPanel.getSelectedTheme();
			Theme t = (fdd.getThemeByTitle(selectedTheme));
			if (!(t == null))
				info = t.getInfoText();
			if (selectedTheme != null && !selectedTheme.equals(ALL_THEMES)) {
				buttonShow.setVisible(true);
			} else {
				buttonShow.setVisible(false);
			}
			themePanel.setText(selectedTheme);
			updateQuestionsList(selectedTheme);
		});
	}

	/**
	 * Initializes all button listeners for saving, creating, deleting, and
	 * displaying questions.
	 */
	private void initButtonActions() {
		MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> saveQuestion());
		buttons[1].addActionListener(e -> newQuestion());
		buttons[2].addActionListener(e -> deleteQuestion());
		buttonShow.addActionListener(e -> showListOrInfo());
	}

	/**
	 * Initializes the question list listener to update the UI when a question is
	 * selected from the list.
	 */
	public void initQuestionListListener() {
		comboPanel.addQuestionSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				String selectedQuestionTitle = comboPanel.getList().getSelectedValue();
				Question selectedQuestion = fdd.getQuestionByTitle(selectedQuestionTitle);
				if (selectedQuestion != null) {
					fillWithData(selectedQuestion);
				}
			}
		});
	}

	/**
	 * Validates the input fields required to create or update a quiz question.
	 * <p>
	 * This method checks that the theme title, question title, and question text
	 * are not empty, and that the provided theme exists in the data source. If a
	 * validation check fails, an appropriate warning message is displayed in the
	 * message panel.
	 *
	 * @param themeTitle the title of the theme to which the question belongs
	 * @param title      the title of the question
	 * @param text       the main text/content of the question
	 * @return {@code true} if all inputs are valid and the theme exists;
	 *         {@code false} otherwise
	 */

	private boolean validateQuestionInput(String themeTitle, String title, String text) {
		if (themeTitle.isEmpty() || title.isEmpty() || text.isEmpty()) {
			bottomPanel.getMessagePanel().setMessageAreaText(WARNING_ALL_FIELDS);
			return false;
		}
		if (fdd.getThemeByTitle(themeTitle) == null) {
			bottomPanel.getMessagePanel().setMessageAreaText(CHOOSE_A_THEME_MSG);
			return false;
		}
		return true;
	}

	/**
	 * Saves a new question or updates an existing one, depending on whether the
	 * title already exists. Validates that all fields are filled in before saving.
	 */
	private void saveQuestion() {
		String themeTitle = themePanel.getText();
		String title = titlePanel.getText();
		String questionText = questionPanel.getTextInfo();
		Theme theme = fdd.getThemeByTitle(themeTitle);
		if (!validateQuestionInput(themeTitle, title, questionText))
			return;

		ArrayList<Answer> answers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			String answerText = answerPanel.getAnswerFields(i).getText();
			boolean isCorrect = answerPanel.getAnswerCheckBoxes(i).isSelected();
			answers.add(new Answer(answerText, isCorrect));
		}

		Question q = new Question(theme);
		q.setText(questionText);
		q.setTitle(title);
		q.setAnswers(answers);

		if (fdd.getQuestionByTitle(title) == null) {
			fdd.addQuestion(q);
			updateQuestionsList(themeTitle);
			bottomPanel.getMessagePanel().setMessageAreaText(QUESTION_SAVED);
		} else {
			fdd.updateQuestion(q, theme);
			updateQuestionsList(themeTitle);
			bottomPanel.getMessagePanel().setMessageAreaText(QUESTION_UPDATED);
		}
	}

	/**
	 * Clears all input fields to allow the user to enter a new question.
	 */
	private void newQuestion() {
		themePanel.setText(EMPTY_STRING);
		titlePanel.setText(EMPTY_STRING);
		questionPanel.setTextInfo(EMPTY_STRING);

		for (int i = 0; i < 4; i++) {
			answerPanel.getAnswerFields(i).setText(EMPTY_STRING);
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
		}
		comboPanel.getList().clearSelection();
	}

	/**
	 * Deletes the selected question after user confirmation.
	 */
	private void deleteQuestion() {
		String selectedTitle = comboPanel.getList().getSelectedValue();
		if (selectedTitle == null) {
			bottomPanel.getMessagePanel().setMessageAreaText(CHOOSE_A_QUESTION_MSG);
			return;
		}
		int confirm = javax.swing.JOptionPane.showConfirmDialog(this, QUESTION_DELETE_INFORMATION, DELETE_CONFIRMATION,
				javax.swing.JOptionPane.YES_NO_OPTION);
		if (confirm == javax.swing.JOptionPane.YES_OPTION) {
			fdd.deleteQuestionByTitle(selectedTitle);
			refreshComboThemes();
			newQuestion();
			bottomPanel.getMessagePanel().setMessageAreaText(QUESTION_DELETED);
		}
	}

	/**
	 * Updates the displayed list of questions based on the selected theme. If no
	 * theme is selected or the theme has no questions, the list will be cleared.
	 *
	 * @param themeTitle the title of the theme to filter questions for
	 */
	private void updateQuestionsList(String themeTitle) {
		List<Question> filteredQuestions = fdd.getQuestionsByTheme(themeTitle);
		List<String> questionTitles = filteredQuestions.stream().map(Question::getTitle).collect(Collectors.toList());
		comboPanel.updateQuestions(questionTitles);
	}

	/**
	 * Toggles between displaying theme information and the list of questions for
	 * the currently selected theme.
	 */
	private void showListOrInfo() {
		if (buttonShow.getText().contains(THEME)) {
			comboPanel.showInfo(info);
			buttonShow.setText(SHOW_LIST);
		} else {
			buttonShow.setText(SHOW_THEME);
			refreshQuestionList(themePanel.getText());
		}
	}

	/**
	 * Refreshes the question list for the selected theme.
	 *
	 * @param selectedThemeTitle the title of the selected theme
	 */
	public void refreshQuestionList(String selectedThemeTitle) {
		List<String> questionsForTheme = fdd.getQuestionsByTheme(selectedThemeTitle).stream().map(Question::getTitle)
				.collect(Collectors.toList());
		comboPanel.updateQuestions(questionsForTheme);
	}

	/**
	 * Refreshes both the theme combo box and the list of all questions.
	 */
	public void refreshComboThemes() {
		List<String> themeTitles = fdd.getThemes().stream().map(Theme::getTitle).collect(Collectors.toList());
		comboPanel.updateThemes(themeTitles);
		List<String> questionTitles = new ArrayList<>(fdd.getAllQuestionTitle().values());
		comboPanel.updateQuestions(questionTitles);
	}
}
