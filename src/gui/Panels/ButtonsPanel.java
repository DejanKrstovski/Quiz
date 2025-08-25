package gui.panels;

import javax.swing.Box;
import javax.swing.BoxLayout;

import gui.swing.MyButton;

import static gui.GuiConstants.DISTANCE_BETWEEN_ELEMENTS;

/**
 * A panel that displays three horizontally aligned action buttons.
 * <p>
 * This component is commonly used for form actions such as Save, New, and Delete.
 * Buttons are created with the provided labels and use those labels as their
 * action commands.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class ButtonsPanel extends SubPanel {

    private static final int BUTTON_COUNT = 3;

    private final MyButton[] buttons = new MyButton[BUTTON_COUNT];

    /**
     * Constructs a {@code ButtonsPanel} with three buttons, each initialized
     * with its own label and action command.
     *
     * @param firstLabel  the label (and action command) for the first button
     * @param secondLabel the label (and action command) for the second button
     * @param thirdLabel  the label (and action command) for the third button
     */
    public ButtonsPanel(String firstLabel, String secondLabel, String thirdLabel) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(DISTANCE_BETWEEN_ELEMENTS);

        buttons[0] = createButton(firstLabel);
        add(buttons[0]);
        add(Box.createHorizontalGlue());

        buttons[1] = createButton(secondLabel);
        add(buttons[1]);
        add(Box.createHorizontalGlue());

        buttons[2] = createButton(thirdLabel);
        add(buttons[2]);
    }

    /**
     * Creates a button with the specified label and assigns the same value
     * as its action command.
     *
     * @param label the text to display on the button and use as the action command
     * @return the configured {@link MyButton}
     */
    private MyButton createButton(String label) {
        MyButton btn = new MyButton(label);
        btn.setActionCommand(label);
        // Consider whether disabling focus is appropriate for your UX/accessibility.
        // If this is a global rule, move it into MyButton.applyDefaultStyling().
        btn.setFocusable(false);
        return btn;
    }

    /**
     * Returns an array containing the three buttons.
     * <p>
     * Note: The returned array is the internal representation; modifying it will
     * affect this panel. Prefer using {@link #getButton(int)} for direct access.
     * </p>
     *
     * @return the internal array of buttons (length is always {@value #BUTTON_COUNT})
     */
    public MyButton[] getButtons() {
        return buttons;
    }

    /**
     * Returns the button at the specified index.
     *
     * @param index the zero-based index (0 to {@value #BUTTON_COUNT} - 1)
     * @return the button at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public MyButton getButton(int index) {
        return buttons[index];
    }

    /**
     * Convenience accessor for the first button.
     *
     * @return the first button
     */
    public MyButton getFirstButton() {
        return buttons[0];
    }

    /**
     * Convenience accessor for the second button.
     *
     * @return the second button
     */
    public MyButton getSecondButton() {
        return buttons[1];
    }

    /**
     * Convenience accessor for the third button.
     *
     * @return the third button
     */
    public MyButton getThirdButton() {
        return buttons[2];
    }
}
