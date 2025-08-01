package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.guiSwing.MyTabPane;
import gui.mainGuiPanels.MainPlayPanel;
import gui.mainGuiPanels.MainQuestionPanel;
import gui.mainGuiPanels.MainThemePanel;
import quizlogic.FakeDataDeliverer;

/**
 * @author DejanKrstovski
 * 
 *         Here Class contains the main Frame for the quiz
 */
public class QuizApp extends JFrame implements GuiConstants {

	FakeDataDeliverer fdd = new FakeDataDeliverer();
	MainQuestionPanel questionPanel = new MainQuestionPanel(fdd);
	MainThemePanel themePanel = new MainThemePanel(fdd);
	MainPlayPanel playPanel = new MainPlayPanel(fdd);
	private MyTabPane tabPane;

	public QuizApp() {
		super("Quiz Application");
		init();
	}

	/**
	 * Here are initialized the 4 tabs of the Program
	 */
	public void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		tabPane = new MyTabPane();
		themePanel.setOnThemeChangeListener(() -> {
			questionPanel.refreshComboThemes();
			playPanel.refreshThemes();
		});
		tabPane.addTab("QuizThemen", themePanel);
		tabPane.addTab("Quizfragen", questionPanel);
		tabPane.addTab("Quiz", playPanel);
//      tabPane.addTab("Statistik", new QuizThemePanel() );

		for (int i = 0; i < tabPane.getTabCount(); i++) {
			JLabel tabLabel = new JLabel(tabPane.getTitleAt(i));
			tabLabel.setPreferredSize(new Dimension(123, 25));
			tabLabel.setHorizontalAlignment(JLabel.CENTER);
			tabLabel.setVerticalAlignment(JLabel.CENTER);
			tabLabel.setOpaque(true);
			tabLabel.setBackground(Color.ORANGE);
			tabLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
			tabPane.setTabComponentAt(i, tabLabel);
		}

		add(tabPane);
		setResizable(true);
		setVisible(true);
	}

	/**
	 * Here starts the quiz
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new QuizApp();
	}
}
