package gui.swing;

import javax.swing.Action;
import javax.swing.JRadioButton;

import static gui.GuiConstants.FONT_LABEL;

/**
 * A customized {@link JRadioButton} with consistent default styling.
 * <p>
 * This component is used for creating radio buttons that visually match
 * the application's design language. It ensures consistent appearance
 * across all radio buttons by applying a unified font and making the
 * background non-opaque.
 * </p>
 */
public class MyRadioButton extends JRadioButton {
    
    /**
     * Constructs an empty {@code MyRadioButton} with default styling.
     */
    public MyRadioButton() {
        super();
        applyDefaultStyling();
    }
    
    /**
     * Constructs a {@code MyRadioButton} that is associated with a given {@link Action}.
     *
     * @param action the {@link Action} used for the radio button
     */
    public MyRadioButton(Action action) {
        super(action);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyRadioButton} with the given text and initial selection state.
     *
     * @param text the text to display
     * @param selected whether the radio button is initially selected
     */
    public MyRadioButton(String text, boolean selected) {
        super(text, selected);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyRadioButton} with the specified text.
     *
     * @param text the text to display
     */
    public MyRadioButton(String text) {
        super(text);
        applyDefaultStyling();
    }

    /**
     * Applies the default UI styling for this radio button.
     * <ul>
     *   <li>Non-opaque background (lets parent container background show).</li>
     *   <li>Font set to {@link gui.GuiConstants#FONT_LABEL} for consistency with labels.</li>
     * </ul>
     * <p>
     * Subclasses may override this method to provide custom styling rules.
     * </p>
     */
    protected void applyDefaultStyling() {
        setOpaque(false);
        setFont(FONT_LABEL);
    }
}
