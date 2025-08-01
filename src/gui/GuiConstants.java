package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This interface contains all the constant values used throughout the GUI of the Quiz application.
 * 
 * <p>
 * It centralizes configuration such as frame dimensions, button labels, fonts, component sizes,
 * and layout borders to ensure consistency and easy maintenance across the UI code.
 * </p>
 * 
 * <p>
 * Each constant is intended to be self-explanatory; modify here to adjust the look and behavior 
 * of GUI components globally.
 * </p>
 * 
 * @author DejanKrstovski
 */
public interface GuiConstants {
	public static final int FRAME_X = 200;
	public static final int FRAME_Y = 50;
	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 500;
	
	public static final String ALL_THEMES = "Alle Themen";
	public static final String SAVE_THEME = "Speichern";
	public static final String DELETE_THEME = "Thema löschen";
	public static final String NEW_THEME = "Neues Thema";
	public static final String SAVE_QUESTION = "Frage speichern";
	public static final String DELETE_QUESTION = "Frage löschen";
	public static final String NEW_QUESTION = "Neue Frage";
	public static final String SHOW_ANSWER = "Antwort zeigen";
	public static final String SAVE_ANSWER = "Antwort speichern";
	public static final String NEXT_QUESTION = "Nächste Frage";
	public static final String SHOW_THEME = "Thema anzeigen";
	public static final String SHOW_LIST = "Liste anzeigen";
	
	public static final String TAB_THEMES = "QuizThemen";
	public static final String TAB_QUESTIONS = "Quizfragen";
	public static final String TAB_PLAY = "Quiz";
	public static final String THEME_INFORMATION = "Informationen zum Thema";
	public static final String EMPTY_STRING = "";
	public static final String TITLE = "Titel";
	public static final String THEMES = "Themen";
	public static final String THEME = "Thema";
	public static final String QUESTION = "Frage";
	public static final String QUESTION_FOR_THEME = "Frage zum Thema";
	public static final String QUESTION_SAVED = "Frage erfolgreich gespeichert.";
	public static final String QUESTION_UPDATED = "Frage aktualisiert.";
	public static final String QUESTION_DELETED = "Frage gelöscht.";

	public static final String CORRECTNESS = "Richtig";
	public static final String POSSIBLE_ANSWERS = "Mögliche Antwortwahl";
	public static final String NEW_THEME_LABEL = "Neues Thema";
	public static final String NEW_THEME_SUCCESFULLY_SAVED = "Neues Thema erfolgreich gespeichert.";
	public static final String THEME_SUCCESFULLY_UPDATED = "Thema erfolgreich aktualisiert.";
	public static final String SUCCESFULLY_DELETED_THEME_AND_QUESTIONS = "Das Thema und alle Themensfragen sind erfolgreich gelöscht.";
	public static final String CHOOSE_A_THEME_MSG = "Bitte eine Thema auswählen!";
	public static final String CHOOSE_A_QUESTION_MSG = "Bitte eine Frage auswählen!";
	public static final String QUESTION_DELETE_INFORMATION = "Möchten Sie diese Frage wirklich löschen?";
	public static final String DELETE_CONFIRMATION = "Bestätigung für löschen";
	public static final String THEME_DELETE_INFORMATION = "Mit löschen den gewählten Thema werden alle verbundenen Fragen gelöscht. Sind sie sicher?";
	public static final String WARNING_EMPTY_TITLE_INFO = "Titel und Informationen dürfen nicht leer sein!";
	public static final String WARNING_THEME_EXISTS = "Das Thema ist schon vorhanden";
	public static final String WARNING_ALL_FIELDS = "Bitte alle Felder ausfüllen!";
	public static final Font FONT_LABEL = (new Font("Arial", Font.PLAIN, 20));
	public static final Font FONT_TITLE = (new Font("Helvetica", Font.BOLD, 24));
	
	public static final Dimension CHECKBOX_SIZE = new Dimension(40, 40);
	public static final Dimension LABEL_SIZE = new Dimension(100, 20);
	public static final Dimension WEST_PANEL_DIMENSIONS= new Dimension(430, 800);

	public static final Border DISTANCE_BETWEEN_ELEMENTS = BorderFactory.createEmptyBorder(0, 0, 10, 0);
	public static final Border OUTSIDE_BORDERS = BorderFactory.createEmptyBorder(15, 20, 0, 20);

}
