package gui.swing;

import javax.swing.JCheckBox;

import static gui.GuiConstants.CHECKBOX_SIZE;

/**
 * A customized {@link JCheckBox} with consistent default styling.
 * <p>
 * This class ensures visual consistency across the application by applying
 * standard UI settings (non-opaque and a fixed preferred size).
 * It can be used in forms and panels where uniform checkbox styling is needed.
 * </p>
 */
public class MyCheckBox extends JCheckBox {

    /**
     * Constructs a {@code MyCheckBox} with no label
     * and applies the default styling.
     */
    public MyCheckBox() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyCheckBox} with the specified label text
     * and applies the default styling.
     *
     * @param text the label text associated with the checkbox
     */
    public MyCheckBox(String text) {
        super(text);
        applyDefaultStyling();
    }

    /**
     * Applies the default look and feel for this checkbox.
     * <p>
     * The default settings include:
     * <ul>
     *   <li>Non-opaque background (transparent)</li>
     *   <li>Fixed preferred size defined in {@link gui.GuiConstants#CHECKBOX_SIZE}</li>
     * </ul>
     * Subclasses may override this method to apply custom styles.
     * </p>
     */
    protected void applyDefaultStyling() {
        setOpaque(false);
        setPreferredSize(CHECKBOX_SIZE);
    }
}
