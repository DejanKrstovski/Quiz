package gui.guiPanels;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiSwing.MyButton;
/**
 * This panel is used for the three buttons
 *
 */
public class ButtonsPanel extends JPanel implements GuiConstants{
	private MyButton[] buttons = new MyButton[3];

	/**
	 * This constructor takes 3 string for the text of the buttons
	 * 
	 * @param s The text for the first button
	 * @param l The text for the second button
	 * @param t The text for the third button
	 */
	public ButtonsPanel(String s, String l, String t) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(DISTANCE_BETWEEN_ELEMENTS);
        buttons[0]=new MyButton(s);
        buttons[0].setActionCommand(s);
        add(buttons[0]);
        add(Box.createHorizontalGlue());
        buttons[1]=new MyButton(l);
        buttons[1].setActionCommand(l);
        add(buttons[1]);
        add(Box.createHorizontalGlue());
        buttons[2]=new MyButton(t);
        buttons[2].setActionCommand(t);
        add(buttons[2]);
    }	
	
	public MyButton[] getButtons() {
		return buttons;
	}
	
	public void setButtons(MyButton[] buttons) {
		this.buttons = buttons;
	}
}
