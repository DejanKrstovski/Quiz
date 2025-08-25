package gui.panels;

import javax.swing.BoxLayout;

import gui.swing.MyTextField;

import static gui.GuiConstants.DISTANCE_BETWEEN_ELEMENTS;

/**
 * A simple panel for displaying messages or status updates to the user.
 * <p>
 * This component contains a single, non-editable text field intended to show
 * short, single-line messages such as errors, confirmations, or system feedback.
 * It is typically placed at the bottom of forms or dialogs.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class MessagePanel extends SubPanel {

    private final MyTextField messageField;

    /**
     * Constructs a {@code MessagePanel} with a single-line, non-editable text field
     * for displaying messages.
     */
    public MessagePanel() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(DISTANCE_BETWEEN_ELEMENTS);

        this.messageField = new MyTextField();
        this.messageField.setEditable(false);
        // Optionally disable focus if not desired for accessibility/UX:
        // this.messageField.setFocusable(false);

        add(this.messageField);
    }

    /**
     * Returns the text field used for displaying messages.
     *
     * @return the {@link MyTextField} used for message display
     */
    public MyTextField getMessageField() {
        return messageField;
    }

    /**
     * Sets the message text to be displayed in this panel.
     *
     * @param message the message to display (null will be treated as empty)
     */
    public void setMessage(String message) {
        this.messageField.setText(message != null ? message : "");
    }
}
