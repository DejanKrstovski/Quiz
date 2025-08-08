package gui.Panels;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.Swing.MyTextField;

/**
 * A simple panel for displaying messages or status updates to the user.
 * <p>
 * It uses a non-editable text field to show messages such as errors,
 * confirmations, or system feedback.
 * </p>
 * 
 * Typically used in the bottom area of forms or dialogs.
 * 
 * @author DejanKrstovski
 */
public class MessagePanel extends SubPanel implements GuiConstants {

    private MyTextField messageField;

    /**
     * Constructs a MessagePanel with a single-line text field for displaying messages.
     */
    public MessagePanel() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(DISTANCE_BETWEEN_ELEMENTS);
        messageField = new MyTextField();
        messageField.setEditable(false);
        add(messageField);
    }

    /**
     * Returns the message text field component.
     *
     * @return the {@link MyTextField} used for displaying messages
     */
    public MyTextField getMessageArea() {
        return messageField;
    }

    /**
     * Sets the message text to be displayed in the message panel.
     *
     * @param message the message to display
     */
    public void setMessageAreaText(String message) {
        this.messageField.setText(message);
    }
}
