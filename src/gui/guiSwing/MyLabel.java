package gui.guiSwing;

import javax.swing.JLabel;
import gui.GuiConstants;

/**
 * A customized {@link JLabel} that applies a consistent font style defined in {@link GuiConstants}.
 * <p>
 * Intended to be used across the GUI for visual consistency.
 * </p>
 */
public class MyLabel extends JLabel {

    /**
     * Constructs an empty {@code MyLabel} with default styling.
     */
    public MyLabel() {
    }

    /**
     * Constructs a {@code MyLabel} with the given text and applies the global label font.
     *
     * @param text the text to display in the label
     */
    public MyLabel(String text) {
        super(text);
        setFont(GuiConstants.FONT_LABEL);
    }
}
