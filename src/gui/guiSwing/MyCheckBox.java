package gui.guiSwing;

import javax.swing.JCheckBox;

import gui.GuiConstants;

public class MyCheckBox extends JCheckBox {

	public MyCheckBox() {
		super();
		setOpaque(false);
		setPreferredSize(GuiConstants.CHECKBOX_SIZE);
	}
}
