package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
/**@author DejanKrstovski
 * This interface contains all the Constants in the GUI
 */
public interface GuiConstants {
	public static final int FRAME_X = 200;
	public static final int FRAME_Y = 50;
	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 500;
	
	public static final String SAVE_THEME = "Speichern";
	public static final String DELETE_THEME = "Thema löschen";
	public static final String NEW_THEME = "Neues Thema";
	public static final String SAVE_QUESTION = "Frage speichern";
	public static final String DELETE_QUESTION = "Frage löschen";
	public static final String NEW_QUESTION = "Neue Frage";
	public static final String SHOW_ANSWER = "Antwort zeigen";
	public static final String SAVE_ANSWER = "Antwort speichern";
	public static final String NEXT_QUESTION = "Nächste Frage";
	public static final String SHOW = "SHOW";
	public static final Font FONT_LABEL = (new Font("Arial", Font.PLAIN, 20));
	public static final Font FONT_TITLE = (new Font("Helvetica", Font.BOLD, 24));
	public static final Dimension CHECKBOX_SIZE = new Dimension(40, 40);
	public static final Dimension LABEL_SIZE = new Dimension(100, 20);
	public static final Border DISTANCE_BETWEEN_ELEMENTS = BorderFactory.createEmptyBorder(0, 0, 10, 0);
	public static final Border OUTSIDE_BORDERS = BorderFactory.createEmptyBorder(15, 20, 0, 20);
	
	public static final Dimension WEST_PANEL_DIMENSIONS= new Dimension(430, 800);
}
