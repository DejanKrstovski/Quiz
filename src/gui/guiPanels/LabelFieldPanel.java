package gui.guiPanels;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyTextField;

/**
 * A reusable panel that contains a label and a single text field, arranged
 * horizontally using a BoxLayout.
 * <p>
 * Typically used in forms to display and optionally edit textual values with
 * labels. The text field is non-editable by default.
 * 
 * @author DejanKrstovski
 */
public class LabelFieldPanel extends SubPanel implements GuiConstants {
	private MyLabel label;
	private MyTextField textField;

	/**
     * Constructs a LabelFieldPanel with a given label and initial field text.
     *
     * @param labelText the text to display in the label
     * @param fieldText the initial text to set in the text field
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

	/**
     * Sets the label's text.
     *
     * @param text the new text for the label
     */
	public void setLabelText(String text) {
		label.setText(text);
	}

	/**
     * Sets the text in the text field.
     *
     * @param text the text to display in the field
     */
	public void setText(String text) {
		textField.setText(text);
	}

	/**
     * Returns the current text from the text field.
     *
     * @return the current field text
     */
	public String getText() {
		return textField.getText();
	}

	/**
     * Returns the text field component.
     * Can be used for adding custom properties or listeners.
     *
     * @return the text field
     */
	public MyTextField getTextField() {
		return textField;
	}

}