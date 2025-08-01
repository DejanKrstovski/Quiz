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
 * This is the Main quiz Panel. Here can the user questions answer
 */
public class MainPlayPanel extends JPanel implements GuiConstants {

	private SubPanel centerPanel;
	private SubPanel westPanel;
	private SouthPanel bottomPanel;
	private LabelFieldPanel themePanel;
	private LabelFieldPanel titlePanel;
	FakeDataDeliverer fdd;

	private LabelTextAreaPanel questionPanel;
	private AnswerPanel answerPanel;
	private ComboBoxJListPanel comboPanel;

	/**
	 * Constructs the quiz panel and initialize the sub panels
	 */
	public MainPlayPanel(FakeDataDeliverer fdd) {
		super();
		this.fdd = fdd;
		init();
		initButtonActions();
		fillWithData(fdd.getRandomQuestion());
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
			themePanel.setText(q.getTheme().getInfoText());
			titlePanel.setText(q.getTitle());
			questionPanel.setTextInfo(q.getText());
			ArrayList<Answer> answers = q.getAnswers();
			for (int i = 0; i < answers.size(); i++) {
				answerPanel.getAnswerFields(i).setText(answers.get(i).getText());
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
	 * Sets the layout for the quiz panel
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
		bottomPanel = new SouthPanel(SHOW_ANSWER, SAVE_ANSWER, NEXT_QUESTION);
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
		for (int i = 0; i < 4; i++) {
			answerPanel.getAnswerFields(i).setEditable(false);
		}
		return answerPanel;
	}

	/**
	 * This is a right sub panel from the quiz Main panel
	 * 
	 * @return Sub panel
	 */
	private SubPanel initCenterPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(OUTSIDE_BORDERS);
		p.add(initComboPanel());
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
	
	public void refreshThemes() {
        List<String> themes = fdd.getThemes().stream()
            .map(Theme::getTitle)
            .collect(Collectors.toList());
        comboPanel.updateThemes(themes); 
        List<String> questionTitles = new ArrayList<>(fdd.getAllQuestionTitle().values());
        comboPanel.updateQuestions(questionTitles);
    }
	
	private void initComboBoxListener() {
		comboPanel.addThemeSelectionListener(e -> {
			String selectedTheme = (String) comboPanel.getSelectedTheme();
			themePanel.setText(selectedTheme);
			updateQuestionsList(selectedTheme);
		});
	}
	
	private void updateQuestionsList(String themeTitle) {
		List<Question> filteredQuestions = fdd.getQuestionsByTheme(themeTitle);
		List<String> questionTitles = filteredQuestions.stream().map(Question::getTitle).collect(Collectors.toList());
		comboPanel.updateQuestions(questionTitles);
	}

	private void initButtonActions() {
		MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> showAnswer());
		buttons[1].addActionListener(e -> saveAnswer());
		buttons[2].addActionListener(e -> nextQuestion());
	}

	private Object showAnswer() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object saveAnswer() {
		// TODO Auto-generated method stub
		return null;
	}

	private void nextQuestion() {
		fillWithData(fdd.getRandomQuestion());
	}
}
