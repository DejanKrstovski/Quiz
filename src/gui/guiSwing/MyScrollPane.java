package gui.guiSwing;

import java.awt.Component;

import javax.swing.JScrollPane;

public class MyScrollPane extends JScrollPane {

	public MyScrollPane() {
	}

	public MyScrollPane(Component view) {
		super(view);
	}

	public MyScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	public MyScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}

}
