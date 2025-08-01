package gui.guiSwing;

import javax.swing.JCheckBox;

import gui.GuiConstants;

/**
 * A customized {@link JCheckBox} used in the GUI for consistent appearance.
 * <p>
 * This checkBox is non-opaque and uses a predefined size from
 * {@link GuiConstants}. Useful for ensuring layout consistency across panels.
 * </p>
 */
public class MyCheckBox extends JCheckBox {

	/**
	 * Constructs a {@code MyCheckBox} with default settings: non-opaque and a fixed
	 * preferred size.
	 */
	public MyCheckBox() {
		super();
		setOpaque(false);
		setPreferredSize(GuiConstants.CHECKBOX_SIZE);
	}
}
