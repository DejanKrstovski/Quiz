package gui.panels;

import java.awt.GridLayout;

import gui.GuiConstants;

/**
 * The {@code SouthPanel} is a reusable panel that combines:
 * <ul>
 *   <li>A {@link MessagePanel} for displaying status or error messages.</li>
 *   <li>A {@link ButtonsPanel} containing three action buttons.</li>
 * </ul>
 * <p>
 * This panel is typically placed at the bottom of a main content panel (e.g. in {@code BorderLayout.SOUTH}).
 * </p>
 * 
 * @author DejanKrstovski
 */
public class SouthPanel extends SubPanel implements GuiConstants {

    private final ButtonsPanel buttonsPanel;
    private final MessagePanel messagePanel;

    /**
     * Constructs a {@code SouthPanel} with the given button labels.
     *
     * @param firstButtonText  the label for the first button
     * @param secondButtonText the label for the second button
     * @param thirdButtonText  the label for the third button
     */
    public SouthPanel(String firstButtonText, String secondButtonText, String thirdButtonText) {
        setLayout(new GridLayout(2, 1));
        setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);

        messagePanel = new MessagePanel();
        add(messagePanel);

        buttonsPanel = new ButtonsPanel(firstButtonText, secondButtonText, thirdButtonText);
        add(buttonsPanel);
    }

    /**
     * Returns the {@link ButtonsPanel} containing the three buttons.
     * 
     * @return the buttons panel
     */
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    /**
     * Returns the {@link MessagePanel} used for displaying messages.
     * 
     * @return the message panel
     */
    public MessagePanel getMessagePanel() {
        return messagePanel;
    }
}
