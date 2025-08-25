package gui.panels;

import java.awt.GridLayout;

import static gui.GuiConstants.OUTSIDE_BORDERS_FOR_SUBPANELS;

/**
 * A reusable bottom-area panel composed of:
 * <ul>
 *   <li>A {@link MessagePanel} for status or error messages (top row).</li>
 *   <li>A {@link ButtonsPanel} with three action buttons (bottom row).</li>
 * </ul>
 *
 * <p>
 * Defaults:
 * <ul>
 *   <li>Uses a 2x1 {@link GridLayout} to stack message and buttons vertically.</li>
 *   <li>Applies {@link gui.GuiConstants#OUTSIDE_BORDERS_FOR_SUBPANELS} as padding.</li>
 *   <li>Designed to be placed at the bottom of a container (e.g., {@code BorderLayout.SOUTH}).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Typical usage: usually placed in forms or main panels to show contextual messages
 * and provide Save/New/Delete actions.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class SouthPanel extends SubPanel {

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

        this.messagePanel = new MessagePanel();
        add(this.messagePanel);

        this.buttonsPanel = new ButtonsPanel(firstButtonText, secondButtonText, thirdButtonText);
        add(this.buttonsPanel);
    }

    /**
     * Returns the {@link ButtonsPanel} containing the three action buttons.
     *
     * @return the buttons panel
     */
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    /**
     * Returns the {@link MessagePanel} used for displaying short status messages.
     *
     * @return the message panel
     */
    public MessagePanel getMessagePanel() {
        return messagePanel;
    }
}
