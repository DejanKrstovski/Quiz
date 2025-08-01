package gui.guiSwing;

import javax.swing.JComboBox;

public class MyComboBox extends JComboBox<Object> {
	public MyComboBox() {
		super();
		setOpaque(false);
		setMaximumRowCount(10);
	}

	public MyComboBox(Object[] items) {
		super(items);
		setOpaque(false);
		setMaximumRowCount(10);
	}

}
