package gui.swing;

import static gui.GuiConstants.FONT_TEXT;

import javax.swing.JTextField;

/**
 * A customized {@link JTextField} with consistent default styling.
 * <p>
 * This component centralizes common input field settings to ensure a unified
 * look and behavior across the application. It also serves as a base for future
 * enhancements (e.g., validation hooks, theming).
 * </p>
 * <p>
 * Default settings:
 * <ul>
 * <li>Global input font applied (if defined in {@link gui.GuiConstants})</li>
 * <li>Default column width set to improve layout consistency</li>
 * </ul>
 * </p>
 * 
 * @author DejanKrstovski
 */
public class MyTextField extends JTextField {

	private static final int DEFAULT_COLUMNS = 20;

	/**
	 * Constructs an empty {@code MyTextField} with default styling.
	 */
	public MyTextField() {
		super();
		applyDefaultStyling();
	}

	/**
	 * Constructs a {@code MyTextField} with the specified initial text and applies
	 * the default styling.
	 *
	 * @param text the initial text to display in the field
	 */
	public MyTextField(String text) {
		super(text);
		applyDefaultStyling();
	}

	/**
	 * Applies the default UI settings for this text field.
	 * <p>
	 * Subclasses may override this method to customize styles (e.g., font, margins,
	 * document filters).
	 * </p>
	 */
	protected void applyDefaultStyling() {
		setFont(FONT_TEXT);
		if (getColumns() <= 0) {
			setColumns(DEFAULT_COLUMNS);
		}
	}
}
