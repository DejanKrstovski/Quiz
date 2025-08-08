package gui.Swing;

import javax.swing.JTextArea;

/**
 * A custom {@link JTextArea} used as a base text area throughout the
 * application.
 * <p>
 * Currently, it behaves like a standard {@code JTextArea}, but it provides a
 * centralized place for future styling or behavior enhancements.
 * </p>
 */
public class MyTextArea extends JTextArea {

	/**
	 * Constructs an empty {@code MyTextArea}.
	 */
	public MyTextArea() {
		super();
	}

	/**
	 * Constructs a {@code MyTextArea} with the given initial text.
	 *
	 * @param text the text to be displayed initially
	 */
	public MyTextArea(String text) {
		super(text);
	}
}
