package gui.guiSwing;

import javax.swing.JPanel;

/**
 * @author DejanKrstovski
 * 
 * This is a class for sub panels
 */
public class SubPanel extends JPanel {
	
	public SubPanel() {
		super();
	}
	
	public SubPanel(int rows, int cols) {
		setLayout(new java.awt.GridLayout(rows, cols));
	}

}
