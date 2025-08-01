package gui.guiSwing;

import javax.swing.JButton;

/**
 * A customized JButton used throughout the application.
 * <p>
 * This class extends the standard Swing {@link JButton} to apply common styling
 * and behavior across all buttons. Currently, it does not add new functionality
 * but serves as a base for future enhancements.
 * </p>
 */
public class MyButton extends JButton {

	/**
	 * Creates a new button with no text.
	 */
	public MyButton() {
	}

	/**
	 * Creates a new button with the specified text.
	 *
	 * @param text the text of the button
	 */
	public MyButton(String text) {
		super(text);
	}
}
