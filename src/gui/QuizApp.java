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
 * The {@code QuizApp} class initializes and displays the main GUI for the quiz
 * application. It sets up the main frame and adds tabs for managing themes,
 * questions, and playing the quiz.
 * <p>
 * Each tab is visually customized using a colored label.
 * 
 * @author DejanKrstovski
 */
public class QuizApp extends JFrame implements GuiConstants {

	private final FakeDataDeliverer fdd = new FakeDataDeliverer();
	private final MainQuestionPanel questionPanel = new MainQuestionPanel(fdd);
	private final MainThemePanel themePanel = new MainThemePanel(fdd);
	private final MainPlayPanel playPanel = new MainPlayPanel(fdd);
	private MyTabPane tabPane;

	/**
	 * Constructs a {@code QuizApp} and initializes the GUI components.
	 */
	public QuizApp() {
		super("Quiz Application");
		init();
	}

	/**
	 * Initializes the GUI layout, tab pane, and sets visibility.
	 */
	private void init() {
		setupFrame();
		setupTabs();
		customizeTabLabels();
		setVisible(true);
	}

	/**
	 * Sets up the main window frame size, closing operation and layout properties.
	 */
	private void setupFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(true);
	}

	/**
	 * Creates and adds tabs to the application, and registers a listener to sync
	 * changes between panels when themes are updated.
	 */
	private void setupTabs() {
		tabPane = new MyTabPane();

		// Synchronize updates across panels when themes are changed
		themePanel.setOnThemeChangeListener(() -> {
			questionPanel.refreshComboThemes();
			playPanel.refreshThemes();
		});

		tabPane.addTab(TAB_THEMES, themePanel);
		tabPane.addTab(TAB_QUESTIONS, questionPanel);
		tabPane.addTab(TAB_PLAY, playPanel);

		add(tabPane);
	}

	/**
	 * Customizes each tab with a centered orange label.
	 */
	private void customizeTabLabels() {
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
	}

	/**
	 * Main entry point to launch the Quiz Application.
	 *
	 * @param args Command-line arguments (not used)
	 */
	public static void main(String[] args) {
		new QuizApp();
	}
}
