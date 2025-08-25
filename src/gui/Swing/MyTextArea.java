package gui.swing;

import javax.swing.JTextArea;

import static gui.GuiConstants.FONT_TEXT;

import java.awt.Color;

/**
 * A customized {@link JTextArea} with consistent default styling.
 * <p>
 * This component centralizes common text area settings to ensure a unified
 * look and behavior across the application. It serves as a base for future
 * enhancements (e.g., theming, input validation hooks).
 * </p>
 * <p>
 * Default settings:
 * <ul>
 *   <li>Line wrapping enabled</li>
 *   <li>Word wrapping enabled</li>
 *   <li>Global text font applied (if defined in {@link gui.GuiConstants})</li>
 * </ul>
 * </p>
 * 
 * @author DejanKrstovski
 */
public class MyTextArea extends JTextArea {

    /**
     * Constructs an empty {@code MyTextArea} with default styling.
     */
    public MyTextArea() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTextArea} with the given initial text
     * and applies the default styling.
     *
     * @param text the initial text to display
     */
    public MyTextArea(String text) {
        super(text);
        applyDefaultStyling();
    }

    /**
     * Applies the default UI settings for this text area.
     * <p>
     * Subclasses may override this method to customize styles (e.g.,
     * font, margins, colors).
     * </p>
     */
    protected void applyDefaultStyling() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setSelectedTextColor(Color.BLUE);
        if (FONT_TEXT != null) {
            setFont(FONT_TEXT);
        }
    }
}
