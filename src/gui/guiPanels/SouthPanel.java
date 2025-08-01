package gui.guiPanels;

import java.awt.GridLayout;

import gui.GuiConstants;
import gui.guiSwing.SubPanel;

/**
 * @author DejanKrstovski
 * 
 * This panel contains the message(error) panel and the Buttons panel 
 */
public class SouthPanel extends SubPanel implements GuiConstants{
	private ButtonsPanel buttonsPanel;
	private MessagePanel messagePanel;

	/**
	 * This Constructor take the names of the buttons as parameter
	 * 
	 * @param s Name for the first button
	 * @param m Name for the second button
	 * @param l Name for the third button
	 */
	public SouthPanel(String s, String m, String l) {
		setLayout(new GridLayout(2, 1));
		setBorder(OUTSIDE_BORDERS);
		messagePanel = new MessagePanel();
		add(messagePanel);
		buttonsPanel = new ButtonsPanel(s, m, l);
        add(buttonsPanel);
	}
	
	public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }
	
	public MessagePanel getMessagePanel() {
        return messagePanel;
    }
}
