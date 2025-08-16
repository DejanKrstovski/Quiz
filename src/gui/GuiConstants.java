package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This interface contains all constant values used throughout the GUI of the Quiz application.
 * 
 * <p>
 * It serves as a centralized configuration point to define, for example, the main window dimensions,
 * button labels, fonts, component sizes, and layout borders to ensure uniformity and maintainability.
 * </p>
 * 
 * <p>
 * By using constants here, consistent styling and easy global adjustments of GUI components
 * across the entire source code are ensured.
 * </p>
 * 
 * <p>
 * All constants are self-explanatory by their names. To change the appearance or behavior 
 * of GUI components globally, you only need to modify this interface.
 * </p>
 * 
 * @author DejanKrstovski
 */
public interface GuiConstants {
	
	/**
	 * This is 
	 */
	public static final int NO_SELECTION = -1;
    /**
     * X-coordinate for the main window's initial position.
     */
    public static final int FRAME_X = 200;
    
    /**
     * Y-coordinate for the main window's initial position.
     */
    public static final int FRAME_Y = 50;
    
    /**
     * Width of the main window.
     */
    public static final int FRAME_WIDTH = 1100;
    
    /**
     * Height of the main window.
     */
    public static final int FRAME_HEIGHT = 500;

    /**
     * The maximum number of answers and correctness
     */
    public static final int MAX_ANSWERS = 4;
    
    // Button labels and menu texts
    public static final String ALL_THEMES = "Alle Themen";
    public static final String SAVE_THEME = "Speichern";
    public static final String DELETE_THEME = "Thema löschen";
    public static final String NEW_THEME = "Neues Thema";
    public static final String SAVE_QUESTION = "Frage speichern";
    public static final String DELETE_QUESTION = "Frage löschen";
    public static final String NEW_QUESTION = "Neue Frage";
    public static final String SHOW_ANSWER = "Antwort anzeigen";
    public static final String SAVE_ANSWER = "Antwort speichern";
    public static final String NEXT_QUESTION = "Nächste Frage";
    public static final String SHOW_THEME = "Thema anzeigen";
    public static final String SHOW_LIST = "Liste anzeigen";

    // Tab names and headers
    public static final String TAB_THEMES = "Quiz-Themen";
    public static final String TAB_QUESTIONS = "Quiz-Fragen";
    public static final String TAB_PLAY = "Quiz";
	public static final String TAB_STATISTIC = "Statistics";
    public static final String LABEL_THEME_INFORMATION = "Informationen zum Thema";

    // General strings
    public static final String EMPTY_STRING = "";
    public static final String LABEL_TITLE = "Titel";
    public static final String LABEL_THEMES = "Themen";
    public static final String LABEL_THEME = "Thema";
    public static final String LABEL_QUESTION = "Frage";
    public static final String LABEL_QUESTION_FOR_THEME = "Frage zum Thema";

    // Confirmation and status messages
    public static final String QUESTION_UPDATED = "Frage aktualisiert.";
    public static final String QUESTION_NOT_FOUND = "Frage nicht gefunden.";
    public static final String QUESTION_NOT_SELECTED = "Keine Frage ausgewählt.";
    
    // Messages related to answer actions
    public static final String NO_RIGHT_ANSWER_FOUND = "Keine richtige Antwort vorhanden.";
    public static final String THE_RIGHT_ANSWER_IS = "Die richtige Antwort ist: ";
    
    // Additional GUI labels
    public static final String CORRECTNESS = "Richtig";
    public static final String LABEL_POSSIBLE_ANSWERS = "Mögliche Antwortmöglichkeiten";
    public static final String LABEL_NEW_THEME = "Neues Thema";

    // Messages related to theme actions
    public static final String THEME_SUCCESFULLY_UPDATED = "Thema erfolgreich aktualisiert.";
    public static final String CHOOSE_A_THEME_MSG = "Bitte ein Thema auswählen!";
    public static final String CHOOSE_A_QUESTION_MSG = "Bitte eine Frage auswählen!";

    // Confirmation dialogs and warnings
    public static final String DELETE_CONFIRMATION = "Löschbestätigung";
    public static final String THEME_DELETE_INFORMATION = "Beim Löschen des ausgewählten Themas werden alle zugehörigen Fragen gelöscht. Sind Sie sicher?";
    public static final String WARNING_EMPTY_TITLE_INFO = "Titel oder Informationen dürfen nicht leer sein!";
    public static final String WARNING_THEME_EXISTS = "Das Thema ist bereits vorhanden.";
    public static final String WARNING_ALL_FIELDS = "Bitte alle Felder ausfüllen!";
    public static final String ERROR_THEME_LOAD = "Das Thema konnte nicht geladen werden.";
    public static final String ERROR_THEME_NOT_FOUND = "Das Thema konnte nicht gefunden werden.";
    public static final String QUESTION_DELETING_NOT_POSSIBLE = "Die Frage konnte nicht gelöscht werden.";
    public static final String QUESTION_DELETE_INFORMATION = "Möchten Sie diese Frage wirklich löschen?";
    public static final String QUESTION_SAVED = "Frage erfolgreich gespeichert.";
    public static final String QUESTION_DELETED = "Frage gelöscht";
    public static final String ERROR_LOAD_QUESTION = "Ausgewählte Frage konnte nicht geladen werden.";
    public static final String ERROR_NO_QUESTIONS_FOR_THEME = "Keine Fragen für das Thema vorhanden.";
    public static final String CHOOSE_AN_ANSWER = "Bitte eine Antwort auswählen!";
    public static final String CORRECT_ANSWER = "Korrekt! Alle Antworten sind selektiert.";
    
    // Fonts for different components
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 20);
    public static final Font FONT_TITLE = new Font("Helvetica", Font.BOLD, 24);
    public static final Font FONT_TABS = new Font("Arial", Font.PLAIN, 14);
    
    // Dimensions for UI components
    public static final Dimension TABS_LABEL_SIZE = new Dimension(150, 25);
    public static final Dimension CHECKBOX_SIZE = new Dimension(40, 40);
    public static final Dimension LABEL_SIZE = new Dimension(100, 20);
    public static final Dimension WEST_PANEL_DIMENSIONS = new Dimension(730, 800);

    // Borders and spacing for layout purposes
    public static final Border DISTANCE_BETWEEN_LABEL_TABS = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    public static final Border DISTANCE_BETWEEN_ELEMENTS = BorderFactory.createEmptyBorder(0, 0, 10, 0);
    public static final Border OUTSIDE_BORDERS_FOR_SUBPANELS = BorderFactory.createEmptyBorder(15, 20, 0, 20);
}
