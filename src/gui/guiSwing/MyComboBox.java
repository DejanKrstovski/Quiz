package gui.guiSwing;

import javax.swing.JComboBox;

/**
 * A customized {@link JComboBox} with GUI-specific appearance settings.
 * <p>
 * This combo box is non-opaque and limits the dropdown list to a maximum of 10
 * visible items. Designed for consistent rendering in custom-themed panels.
 * </p>
 */
public class MyComboBox extends JComboBox<Object> {

	/**
	 * Constructs an empty {@code MyComboBox} with default styling.
	 */
	public MyComboBox() {
		super();
		setOpaque(false);
		setMaximumRowCount(10);
	}

	/**
	 * Constructs a {@code MyComboBox} with the specified items and default styling.
	 *
	 * @param items the items to populate the combo box
	 */
	public MyComboBox(Object[] items) {
		super(items);
		setOpaque(false);
		setMaximumRowCount(10);
	}
}
