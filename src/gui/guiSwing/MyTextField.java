package gui.guiSwing;

import javax.swing.JTextField;

/**
 * A custom {@link JTextField} used throughout the application for input fields.
 * <p>
 * This class provides a centralized place for styling or extending behavior in
 * a consistent way. Currently, it acts as a basic wrapper for
 * {@code JTextField}.
 * </p>
 */
public class MyTextField extends JTextField {

	/**
	 * Constructs an empty {@code MyTextField}.
	 */
	public MyTextField() {
		super();
	}

	/**
	 * Constructs a {@code MyTextField} with the specified initial text.
	 *
	 * @param text the text to be displayed initially in the text field
	 */
	public MyTextField(String text) {
		super(text);
	}
}
