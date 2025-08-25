package gui.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import gui.swing.MyRadioButton;

import static gui.GuiConstants.DISTANCE_BETWEEN_RADIO_BUTTONS;

/**
 * A reusable panel that displays three mutually exclusive radio buttons
 * arranged horizontally.
 * <p>
 * Typical usage is to present a small set of options (e.g., modes or filters),
 * with exactly one option selected at all times. The first option is selected
 * by default.
 * </p>
 * <p>
 * Defaults:
 * <ul>
 * <li>Buttons are grouped in a {@link ButtonGroup} to enforce single
 * selection.</li>
 * <li>Equal horizontal spacing between adjacent buttons using
 * {@link gui.GuiConstants#DISTANCE_BETWEEN_RADIO_BUTTONS}.</li>
 * <li>Each button's action command is set to its label text.</li>
 * </ul>
 * </p>
 * 
 * @author DejanKrstovski
 */
public class RadioButtonsPanel extends SubPanel {

	private final List<MyRadioButton> radioButtons = new ArrayList<>(3);
	private final ButtonGroup group = new ButtonGroup();

	/**
	 * Constructs a {@code RadioButtonsPanel} with three labeled options. The first
	 * option is selected by default.
	 *
	 * @param first  the label for the first (initially selected) button
	 * @param second the label for the second button
	 * @param third  the label for the third button
	 */
	public RadioButtonsPanel(String first, String second, String third) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		MyRadioButton rb1 = createAndRegisterButton(first, true);
		MyRadioButton rb2 = createAndRegisterButton(second, false);
		MyRadioButton rb3 = createAndRegisterButton(third, false);

		add(rb1);
		add(Box.createHorizontalStrut(DISTANCE_BETWEEN_RADIO_BUTTONS));
		add(rb2);
		add(Box.createHorizontalStrut(DISTANCE_BETWEEN_RADIO_BUTTONS));
		add(rb3);
	}

	/**
	 * Creates a radio button, configures its selection state and action command,
	 * adds it to the internal group and list, and returns it.
	 */
	private MyRadioButton createAndRegisterButton(String label, boolean selected) {
		MyRadioButton rb = new MyRadioButton(label, selected);
		rb.setActionCommand(label);
		rb.setFocusable(false);

		group.add(rb);
		radioButtons.add(rb);
		return rb;
	}

	/**
	 * Registers a listener that is invoked when any radio button is selected. The
	 * consumer receives the selected button's action command (which by default
	 * equals its label text).
	 *
	 * @param onChange a consumer invoked with the selected action command
	 */
	public void addSelectionListener(Consumer<String> onChange) {
		for (MyRadioButton rb : radioButtons) {
			rb.addActionListener(e -> onChange.accept(rb.getActionCommand()));
		}
	}

	/**
	 * Returns the label text of the currently selected radio button.
	 *
	 * @return the selected label text, or {@code null} if none is selected
	 */
	public String getSelectedText() {
		return radioButtons.stream().filter(MyRadioButton::isSelected).map(MyRadioButton::getText).findFirst()
				.orElse(null);
	}

	/**
	 * Selects a radio button by matching its label text. If the provided text is
	 * {@code null} or no match is found, the selection remains unchanged.
	 *
	 * @param text the label text of the radio button to select
	 */
	public void selectByText(String text) {
		for (MyRadioButton rb : radioButtons) {
			if (Objects.equals(rb.getText(), text)) {
				rb.setSelected(true);
				break;
			}
		}
	}

	/**
	 * Returns the radio buttons as an unmodifiable list. This prevents external
	 * code from adding or removing buttons, while still allowing property updates
	 * on the contained instances.
	 *
	 * @return an unmodifiable view of the buttons in this panel
	 */
	public List<MyRadioButton> getButtons() {
		return Collections.unmodifiableList(radioButtons);
	}
}
