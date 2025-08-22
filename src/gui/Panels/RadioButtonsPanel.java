package gui.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import gui.GuiConstants;
import gui.swing.MyRadioButton;

public class RadioButtonsPanel extends SubPanel implements GuiConstants{
    private final List<MyRadioButton> radioButtons = new ArrayList<>();
    private final ButtonGroup group = new ButtonGroup();

    public RadioButtonsPanel(String first, String second, String third) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        MyRadioButton rb1 = new MyRadioButton(first, true);
        MyRadioButton rb2 = new MyRadioButton(second, false);
        MyRadioButton rb3 = new MyRadioButton(third, false);

        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);

        rb1.setActionCommand(first);
        rb1.setFocusable(false);
        rb2.setActionCommand(second);
        rb2.setFocusable(false);
        rb3.setActionCommand(third);
        rb3.setFocusable(false);

        group.add(rb1);
        group.add(rb2);
        group.add(rb3);

        add(rb1);
        add(Box.createHorizontalStrut(DISTANCE_BETWEEN_RADIO_BUTTONS));
        add(rb2);
        add(Box.createHorizontalStrut(DISTANCE_BETWEEN_RADIO_BUTTONS));
        add(rb3);
    }

    public void addSelectionListener(Consumer<String> onChange) {
        for (MyRadioButton rb : radioButtons) {
            rb.addActionListener(e -> onChange.accept(rb.getActionCommand()));
        }
    }

    public String getSelectedText() {
        return radioButtons.stream()
            .filter(MyRadioButton::isSelected)
            .map(MyRadioButton::getText)
            .findFirst()
            .orElse(null);
    }

    public void selectByText(String text) {
        for (MyRadioButton rb : radioButtons) {
            if (rb.getText().equals(text)) {
                rb.setSelected(true);
                break;
            }
        }
    }

    public List<MyRadioButton> getButtons() {
        return radioButtons;
    }
}
