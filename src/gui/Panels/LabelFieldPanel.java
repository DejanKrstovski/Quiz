package gui.panels;

import static gui.GuiConstants.DISTANCE_BETWEEN_ELEMENTS;
import static gui.GuiConstants.LABEL_SIZE;

import javax.swing.BoxLayout;

import gui.swing.MyLabel;
import gui.swing.MyTextField;

/**
 * A reusable panel that displays a label and a single text field
 * arranged horizontally using a {@link BoxLayout}.
 * <p>
 * Default behavior:
 * <ul>
 *   <li>The label uses a standardized preferred size ({@link gui.GuiConstants#LABEL_SIZE}).</li>
 *   <li>The text field is non-editable by default (read-only display).</li>
 *   <li>Panel padding uses {@link gui.GuiConstants#DISTANCE_BETWEEN_ELEMENTS}.</li>
 * </ul>
 * This component is typically used in forms to show and optionally update
 * textual values alongside labels.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class LabelFieldPanel extends SubPanel {

    private final MyLabel label;
    private final MyTextField textField;

    /**
     * Constructs a {@code LabelFieldPanel} with a given label and initial field text.
     *
     * @param labelText the text to display in the label
     * @param fieldText the initial text to set in the text field
     */
    public LabelFieldPanel(String labelText, String fieldText) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.label = new MyLabel(labelText);
        label.setPreferredSize(LABEL_SIZE);
        label.setMaximumSize(LABEL_SIZE);
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
     * Useful for adding custom properties (e.g., document filters) or listeners.
     *
     * @return the text field
     */
    public MyTextField getTextField() {
        return textField;
    }
}
