package gui.guiPanels;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.guiSwing.MyTextField;
import gui.guiSwing.SubPanel;

/**
 * @author DejanKrstovski
 * This panel is the panel for the errors
 */
public class MessagePanel extends SubPanel implements GuiConstants{
	MyTextField messageArea;

	public MessagePanel() {
		super();
		setLayout(new BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
		setBorder(DISTANCE_BETWEEN_ELEMENTS);
		messageArea = new MyTextField();
		messageArea.setEditable(false);
		add(messageArea);
	}	
	
	public MyTextField getMessageArea() {
		return messageArea;
	}

	public void setMessageAreaText(String message) {
		this.messageArea.setText(message);;
	}

}
