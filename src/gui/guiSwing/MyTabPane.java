package gui.guiSwing;

import javax.swing.JTabbedPane;

public class MyTabPane extends JTabbedPane {

	public MyTabPane() {
		super(JTabbedPane.TOP);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public MyTabPane(int tabPlacement) {
		super(tabPlacement);
		
	}

}
