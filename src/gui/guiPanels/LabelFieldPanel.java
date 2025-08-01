package gui.guiPanels;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyTextField;
import gui.guiSwing.SubPanel;

/**
 * @author DejanKrstovski
 * 
 * This panel makes the label and the textField 
 */
public class LabelFieldPanel extends SubPanel implements GuiConstants{
    private MyLabel label;
    private MyTextField textField;

    /**
     * It takes the names of the label and the text for the textField
     * 
     * @param labelText the text for the label
     * @param fieldText the text for the fieldText
     */
    public LabelFieldPanel(String labelText, String fieldText) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.label = new MyLabel(labelText);
        label.setPreferredSize(LABEL_SIZE);
        this.textField = new MyTextField(fieldText);
        textField.setEditable(false);
        add(label);
        add(textField);
        setBorder(DISTANCE_BETWEEN_ELEMENTS);
    }

    public void setLabelText(String text) {
    		label.setText(text);
    }
    public void setText(String text) {
        textField.setText(text);
    }

    public String getText() {
        return textField.getText();
    }

    public MyTextField getTextField() {
        return textField;
    }
    
}