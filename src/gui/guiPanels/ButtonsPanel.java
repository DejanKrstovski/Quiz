package gui.guiPanels;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiSwing.MyButton;

/**
 * A panel that contains three horizontally aligned buttons.
 * Each button is initialized with a custom label and action command.
 * <p>
 * Commonly used for Save, New, and Delete actions in forms.
 * 
 * @author DejanKrstovski
 */
public class ButtonsPanel extends JPanel implements GuiConstants{
	private MyButton[] buttons = new MyButton[3];

	/**
	 * Constructs the panel with three buttons, each with its own label and action command.
	 *
	 * @param firstLabel  the label for the first button
	 * @param secondLabel the label for the second button
	 * @param thirdLabel  the label for the third button
	 */
	public ButtonsPanel(String firstLabel, String secondLabel, String thirdLabel) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(DISTANCE_BETWEEN_ELEMENTS);
        buttons[0]=new MyButton(firstLabel);
        buttons[0].setActionCommand(firstLabel);
        add(buttons[0]);
        add(Box.createHorizontalGlue());
        buttons[1]=new MyButton(secondLabel);
        buttons[1].setActionCommand(secondLabel);
        add(buttons[1]);
        add(Box.createHorizontalGlue());
        buttons[2]=new MyButton(thirdLabel);
        buttons[2].setActionCommand(thirdLabel);
        add(buttons[2]);
    }	
	
	/**
	 * Returns the array of buttons in this panel.
	 *
	 * @return an array containing the three buttons
	 */
	public MyButton[] getButtons() {
		return buttons;
	}
	
	/**
	 * Sets the array of buttons for this panel.
	 *
	 * @param buttons an array of three buttons to replace the current ones
	 */
	public void setButtons(MyButton[] buttons) {
		this.buttons = buttons;
	}
}
