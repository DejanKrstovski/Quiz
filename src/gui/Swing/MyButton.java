package gui.swing;

import static gui.GuiConstants.COLOR_RANDOM;
import static gui.GuiConstants.DEFAULT_CURSOR;

import javax.swing.JButton;

/**
 * A customized {@link JButton} with consistent default styling.
 * <p>
 * This class standardizes the appearance and behavior of buttons across
 * the application by applying a shared background color and cursor style.
 * It serves as a base component for maintaining UI consistency
 * and can be extended for additional customizations.
 * </p>
 */
public class MyButton extends JButton {

    /**
     * Creates a new button with no text and applies the default styling.
     */
    public MyButton() {
        applyDefaultStyling();
    }

    /**
     * Creates a new button with the specified text
     * and applies the default styling.
     *
     * @param text the button label
     */
    public MyButton(String text) {
        super(text);
        applyDefaultStyling();
    }

    /**
     * Applies the default look and feel for this button.
     * <p>
     * Subclasses can override this method to customize styles
     * (e.g., background color, cursor, font).
     * </p>
     */
    protected void applyDefaultStyling() {
        setBackground(COLOR_RANDOM);
        setCursor(DEFAULT_CURSOR);
    }
}
