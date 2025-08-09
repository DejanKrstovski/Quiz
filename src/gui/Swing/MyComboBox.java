package gui.Swing;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * A customized {@link JComboBox} implementation with application-specific appearance
 * and behavior settings.
 * <p>
 * This component allows the use of generic type parameters for type-safe item handling
 * (e.g., {@code MyComboBox<ThemeListItem>}) while ensuring consistent GUI styling.
 * </p>
 * <p>
 * Default customizations:
 * <ul>
 *   <li>Component is set to non-opaque for better integration with custom look & feel.</li>
 *   <li>The dropdown list is limited to a maximum of 10 visible rows.</li>
 * </ul>
 * </p>
 *
 * @param <T> The type of elements contained in the combo box model.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create with no initial items
 * MyComboBox<String> box = new MyComboBox<>();
 * box.addItem("Item 1");
 * box.addItem("Item 2");
 *
 * // Create with predefined items
 * MyComboBox<ThemeListItem> themeBox =
 *     new MyComboBox<>(new ThemeListItem[] {
 *         new ThemeListItem(1, "Math"),
 *         new ThemeListItem(2, "Science")
 *     });
 *
 * // Retrieve selected item with correct type
 * ThemeListItem selected = themeBox.getItemAt(themeBox.getSelectedIndex());
 * }</pre>
 *
 * @see JComboBox
 */
public class MyComboBox<T> extends JComboBox<T> {

    /**
     * Constructs an empty {@code MyComboBox} with default styling.
     * <p>Use {@link #addItem(Object)} to populate items later.</p>
     */
    public MyComboBox() {
        super();
        applyCustomStyling();
    }

    /**
     * Constructs a {@code MyComboBox} populated with the specified items.
     *
     * @param items an array of items to add to the combo box
     */
    public MyComboBox(T[] items) {
        super(items);
        applyCustomStyling();
    }

    /**
     * Constructs a {@code MyComboBox} with the specified data model.
     *
     * @param model the {@link ComboBoxModel} to use
     */
    public MyComboBox(ComboBoxModel<T> model) {
        super(model);
        applyCustomStyling();
    }

    /**
     * Applies predefined appearance settings to ensure consistent styling for
     * all combo box instances in the application.
     * <ul>
     *   <li>Sets the combo box to be non-opaque (let background of parent show).</li>
     *   <li>Limits the dropdown list to 10 visible items.</li>
     * </ul>
     */
    private void applyCustomStyling() {
        setOpaque(false);
        setMaximumRowCount(10);
    }
}
