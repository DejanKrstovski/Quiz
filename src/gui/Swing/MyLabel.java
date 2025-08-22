package gui.swing;

import javax.swing.JLabel;

import static gui.GuiConstants.FONT_LABEL;

/**
 * A customized {@link JLabel} with consistent default styling.
 * <p>
 * This label applies a globally defined font (via {@link gui.GuiConstants#FONT_LABEL}),
 * ensuring a unified look across the application UI.
 * </p>
 */
public class MyLabel extends JLabel {

    /**
     * Constructs an empty {@code MyLabel} with default styling.
     */
    public MyLabel() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyLabel} with the given text
     * and applies the default styling.
     *
     * @param text the text to display in the label
     */
    public MyLabel(String text) {
        super(text);
        applyDefaultStyling();
    }

    /**
     * Applies the default UI settings for this label.
     * <ul>
     *   <li>Sets the font to {@link gui.GuiConstants#FONT_LABEL}</li>
     * </ul>
     * <p>
     * Subclasses may override this method to apply different styling rules.
     * </p>
     */
    protected void applyDefaultStyling() {
        setFont(FONT_LABEL);
    }
}
