package gui.swing;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import static gui.GuiConstants.MAX_ROW_COUNT;

/**
 * A customized {@link JComboBox} with consistent default styling.
 * <p>
 * This component provides type-safe item handling (via generics) while ensuring
 * a unified look and feel across the application.
 * </p>
 * <p>
 * Default settings:
 * <ul>
 *   <li>Non-opaque background (inherits parent background).</li>
 *   <li>Dropdown list limited to a maximum of {@link gui.GuiConstants#MAX_ROW_COUNT} visible rows.</li>
 * </ul>
 * </p>
 *
 * @param <T> the type of elements contained in the combo box
 */
public class MyComboBox<T> extends JComboBox<T> {

    /**
     * Constructs an empty {@code MyComboBox} with default styling.
     * <p>Items can be added later via {@link #addItem(Object)}.</p>
     */
    public MyComboBox() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyComboBox} initialized with the specified items.
     *
     * @param items the array of items to add to the combo box
     */
    public MyComboBox(T[] items) {
        super(items);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyComboBox} with the specified data model.
     *
     * @param model the {@link ComboBoxModel} to use
     */
    public MyComboBox(ComboBoxModel<T> model) {
        super(model);
        applyDefaultStyling();
    }

    /**
     * Applies the default UI settings for this combo box.
     * <ul>
     *   <li>Sets the combo box to be non-opaque (translucent background).</li>
     *   <li>Restricts the dropdown list to {@code MAX_ROW_COUNT} visible items.</li>
     * </ul>
     * <p>
     * Subclasses may override this method to modify or extend the default styling.
     * </p>
     */
    protected void applyDefaultStyling() {
        setOpaque(false);
        setMaximumRowCount(MAX_ROW_COUNT);
    }
}
