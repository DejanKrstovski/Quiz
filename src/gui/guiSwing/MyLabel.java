package gui.guiSwing;

import javax.swing.Icon;
import javax.swing.JLabel;

import gui.GuiConstants;

public class MyLabel extends JLabel {

	public MyLabel() {
	}

	public MyLabel(String text) {
		super(text);
		setFont(GuiConstants.FONT_LABEL);
	}

	public MyLabel(Icon image) {
		super(image);
	}
}
