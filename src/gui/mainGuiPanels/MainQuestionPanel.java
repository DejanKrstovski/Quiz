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
import gui.guiSwing.MyButton;
import gui.guiSwing.MyLabel;
import gui.guiSwing.SubPanel;
import quizlogic.Answer;
import quizlogic.FakeDataDeliverer;
import quizlogic.Question;
import quizlogic.Theme;

/**
 * @author DejanKrstovski
 * 
 * This is the Question Panel. Here can be the questions for the <br>
 * specified  theme made or changed
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
	 * Constructs the question panel and initialize the sub panels
	 */
	public MainQuestionPanel(FakeDataDeliverer fdd) {
		super();
		this.fdd = fdd;
		init();
		initQuestionListListener();
		initButtonActions();
	}

	/**
	 * This method is used to simulate a chosen question and <br>
	 * on the left side are filled all TextFields and possible answers <br>
	 * and the paired true or false
	 * 
	 * @param q Question
	 */
	private void fillWithData(Question q) {
		if (q != null) {
			titlePanel.setText(q.getTitle());
			questionPanel.setTextInfo(q.getText());
			ArrayList<Answer> answers = q.getAnswers();
			for (int i = 0; i < answers.size(); i++) {
				answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
				answerPanel.getAnswerCheckBoxes(i).setSelected(answers.get(i).isCorrect());
			}
		}
	}

	/**
	 * This method sets the layout and initialize the Components
	 */
	private void init() {
		initLayout();
		initComponents();
		initComboBoxListener();
	}

	/**
	 * Sets the layout for the question panel
	 */
	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Here are initialized the 3 sub panels and placed in the border layout
	 * 
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
	 * This panel has the labels the TextFields and the possible answers
	 * 
	 * @return Sub panel
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
		p.add(initAnwserPanel());
		p.setBorder(OUTSIDE_BORDERS);
		return p;
	}

	/**
	 * This is a sub panel for the theme label and the theme TextField
	 * 
	 * @return themePanel
	 */
	private SubPanel initThemePanel() {
		themePanel = new LabelFieldPanel("Thema", "");
		themePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return themePanel;
	}

	/**
	 * This is a sub panel for the title label and the title TextField for the
	 * question
	 * 
	 * @return titlePanel
	 */
	private SubPanel initTitlePanel() {
		titlePanel = new LabelFieldPanel("Titel", "");
		titlePanel.getTextField().setEditable(true);
		titlePanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return titlePanel;
	}

	/**
	 * This is a sub panel for the label and the TextArea for the question
	 * 
	 * @return QuestionPanel
	 */
	private SubPanel initQuestionPanel() {
		questionPanel = new LabelTextAreaPanel("Frage");
		questionPanel.getQuestionTextArea().setEditable(true);
		questionPanel.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		return questionPanel;
	}

	/**
	 * This is a sub panel for the two labels
	 * 
	 * @return Sub panel
	 */
	private SubPanel initPossibleAnswerPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.LINE_AXIS));
		p.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		MyLabel possibleAnwserLabel = new MyLabel("Mögliche Antwortwahl");
		MyLabel rightAnswerLabel = new MyLabel("Richtig");
		p.add(possibleAnwserLabel);
		p.add(Box.createHorizontalGlue());
		p.add(rightAnswerLabel);
		return p;
	}

	/**
	 * This is a sub panel for the possible Answers
	 * 
	 * @return AnswerPanel
	 */
	private SubPanel initAnwserPanel() {
		answerPanel = new AnswerPanel();
		return answerPanel;
	}

	/**
	 * This is a right sub panel from the question Main panel
	 * 
	 * @return Sub panel
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
	 * This is a sub panel that has a label and a button that is set <br>
	 * invisible. When the user chooses a theme than the button will <br>
	 * be shown
	 * 
	 * @return Sub panel
	 */
	private SubPanel initThemaLabelPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.LINE_AXIS));
		p.setBorder(DISTANCE_BETWEEN_ELEMENTS);
		MyLabel questionLabel = new MyLabel("Frage zum Thema");
		p.add(questionLabel);
		buttonShow = new MyButton("Thema anzeigen");
		p.add(Box.createHorizontalGlue());
		p.add(buttonShow);
		buttonShow.setVisible(false);
		return p;
	}

	/**
	 * This is a panel for the ComboBox and the JList In the ComboBox are all themes
	 * and in the JList can be seen all questions or the questions for the selected
	 * theme
	 * 
	 * @return ComboBoxJListPanel
	 */
	private SubPanel initComboPanel() {
		List<String> themeTitles = fdd.getThemes().stream().map(Theme::getTitle).collect(Collectors.toList());

		List<String> comboItems = new ArrayList<>();
		comboItems.add("Alle Themen");
		comboItems.addAll(themeTitles);

		List<String> listQuestions = new ArrayList<>(fdd.getAllQuestionTitle().values());
		comboPanel = new ComboBoxJListPanel(comboItems, listQuestions);
		return comboPanel;
	}

	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			String selectedTheme = (String) comboPanel.getSelectedTheme();
			Theme t = (fdd.getThemeByTitle(selectedTheme));
			if (!(t == null))
				info = t.getInfoText();
			if (selectedTheme != null && !selectedTheme.equals("Alle Themen")) {
			    buttonShow.setVisible(true);
			} else {
			    buttonShow.setVisible(false);
			}
			themePanel.setText(selectedTheme);
			updateQuestionsList(selectedTheme);
		});
	}

	private void initButtonActions() {
		MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> saveQuestion());
		buttons[1].addActionListener(e -> newQuestion());
		buttons[2].addActionListener(e -> deleteQuestion());
		buttonShow.addActionListener(e -> showListOrInfo());
	}
	
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

	private void saveQuestion() {
		String themeTitle = themePanel.getText();
		String title = titlePanel.getText();
		String questionText = questionPanel.getTextInfo();

		if (themeTitle.isEmpty() || title.isEmpty() || questionText.isEmpty()) {
			bottomPanel.getMessagePanel().setMessageAreaText("Bitte alle Felder ausfüllen!");
			return;
		}

		Theme theme = fdd.getThemeByTitle(themeTitle);
		if (theme == null) {
			bottomPanel.getMessagePanel().setMessageAreaText("Thema nicht gefunden.");
			return;
		}

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
			bottomPanel.getMessagePanel().setMessageAreaText("Frage erfolgreich gespeichert.");
		} else {
			fdd.updateQuestion(q, theme);
			updateQuestionsList(themeTitle);
			bottomPanel.getMessagePanel().setMessageAreaText("Frage aktualisiert.");
		}
	}
	
	private void newQuestion() {
		themePanel.setText("");
		titlePanel.setText("");
		questionPanel.setTextInfo("");

		for (int i = 0; i < 4; i++) {
			answerPanel.getAnswerFields(i).setText("");
			answerPanel.getAnswerCheckBoxes(i).setSelected(false);
		}
		comboPanel.getList().clearSelection();
	}

	private void deleteQuestion() {
		String selectedTitle = comboPanel.getList().getSelectedValue();
		if (selectedTitle == null) {
			bottomPanel.getMessagePanel().setMessageAreaText("Bitte eine Frage auswählen.");
			return;
		}
		int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Möchten Sie diese Frage wirklich löschen?",
				"Frage löschen", javax.swing.JOptionPane.YES_NO_OPTION);
		if (confirm == javax.swing.JOptionPane.YES_OPTION) {
			fdd.deleteQuestionByTitle(selectedTitle);
			refreshComboThemes();
			newQuestion();
			bottomPanel.getMessagePanel().setMessageAreaText("Frage gelöscht.");
		}
	}

	private void updateQuestionsList(String themeTitle) {
		List<Question> filteredQuestions = fdd.getQuestionsByTheme(themeTitle);
		List<String> questionTitles = filteredQuestions.stream().map(Question::getTitle).collect(Collectors.toList());
		comboPanel.updateQuestions(questionTitles);
	}
	
	private void showListOrInfo() {
		if (buttonShow.getText().contains("Thema")) {
			comboPanel.showInfo(info);
			buttonShow.setText("Liste anzeigen");
		} else {
			buttonShow.setText("Thema anzeigen");
			refreshQuestionList(themePanel.getText());
		}
	}
	
	public void refreshQuestionList(String selectedThemeTitle) {
		List<String> questionsForTheme = fdd.getQuestionsByTheme(selectedThemeTitle).stream().map(Question::getTitle)
				.collect(Collectors.toList());
		comboPanel.updateQuestions(questionsForTheme);
	}

	public void refreshComboThemes() {
		List<String> themeTitles = fdd.getThemes().stream().map(Theme::getTitle).collect(Collectors.toList());
		comboPanel.updateThemes(themeTitles);
		List<String> questionTitles = new ArrayList<>(fdd.getAllQuestionTitle().values());
		comboPanel.updateQuestions(questionTitles);
	}
}
