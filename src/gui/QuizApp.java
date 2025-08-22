package gui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.mainPanels.MainPlayPanel;
import gui.mainPanels.MainQuestionPanel;
import gui.mainPanels.MainStatisticPanel;
import gui.mainPanels.MainThemePanel;
import gui.swing.MyTabPane;
import persistence.mariaDB.createDB.DBCreator;

/**
 * The {@code QuizApp} class initializes and displays the main GUI window for
 * the quiz application. It organizes the main frame and manages the tabs for
 * themes, questions, and playing the quiz.
 * <p>
 * Each tab is customized visually by using a centered orange label to enhance
 * the UI appearance.
 * </p>
 * <p>
 * The class also handles synchronization between panels, e.g., updating
 * question themes when the theme list changes.
 * </p>
 * 
 * <p>
 * This class implements {@link GuiConstants} to use centralized GUI
 * configuration constants.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class QuizApp extends JFrame implements GuiConstants {

	/** Panel managing the quiz themes. */
	private final MainThemePanel themePanel = new MainThemePanel();
	
	/** Panel managing the quiz questions. */
	private final MainQuestionPanel questionPanel = new MainQuestionPanel();

	/** Panel for playing the quiz. */
	private final MainPlayPanel playPanel = new MainPlayPanel();
	
	/** Panel for statistic for the quiz. */
	private final MainStatisticPanel statisticPanel = new MainStatisticPanel();

	/** The tabbed pane containing the main sections of the application. */
	private MyTabPane tabPane;

	/**
	 * Constructs a new {@code QuizApp} frame and initializes the GUI.
	 */
	private QuizApp() {
		super("Quiz Application");
		init();
	}

	/**
	 * Initializes the GUI components, including the frame setup, tabbed pane, and
	 * tab labels. Sets the frame visible at the end of initialization.
	 */
	private void init() {		
		setupFrame();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
		    e.printStackTrace();
		}
		setupTabs();
		setVisible(true);
	}

	/**
	 * Sets up the main application frame size, location, close operation, and
	 * resize behavior.
	 */
	private void setupFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(true);
	}

	/**
	 * Creates and configures the tab pane with tabs for themes, questions, and quiz
	 * play. Also registers a listener on the theme panel to propagate theme changes
	 * to other panels.
	 */
	private void setupTabs() {
		tabPane = new MyTabPane();

		tabPane.addTab(TAB_THEMES, themePanel);
		tabPane.addTab(TAB_QUESTIONS, questionPanel);
		tabPane.addTab(TAB_PLAY, playPanel);
		tabPane.addTab(TAB_STATISTIC, statisticPanel);
		tabPane.applyCustomLabels();
		
		themePanel.addOnThemeChangeListener(questionPanel);
		themePanel.addOnThemeChangeListener(playPanel);
		themePanel.addOnThemeChangeListener(statisticPanel);
		questionPanel.addOnQuestionsChangeListener(playPanel);
		questionPanel.addOnQuestionsChangeListener(statisticPanel);
		
		add(tabPane);
	}

	/**
	 * The main method to launch the Quiz Application.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		DBCreator.createDB();
		new QuizApp();
		
	}
}
