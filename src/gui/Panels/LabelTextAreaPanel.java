package gui.panels;

import static gui.GuiConstants.LABEL_SIZE;
import static gui.GuiConstants.TEXT_AREA_SIZE;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import gui.swing.MyLabel;
import gui.swing.MyScrollPane;
import gui.swing.MyTextArea;

/**
 * A reusable panel that combines a label with a scrollable text area.
 * <p>
 * This component supports two layout variants:
 * <ul>
 *   <li>Horizontal (LINE_AXIS): label to the left of the text area</li>
 *   <li>Vertical (BorderLayout): label on top of the text area</li>
 * </ul>
 * It is commonly used to display or edit larger text content (e.g., question text).
 * </p>
 *
 * <p>
 * Defaults:
 * <ul>
 *   <li>The label uses a standardized preferred size ({@link gui.GuiConstants#LABEL_SIZE}).</li>
 *   <li>The text area supports line wrapping and word wrapping (as configured in {@link MyTextArea}).</li>
 *   <li>The text area is embedded in a scroll pane with vertical scrollbars always visible and horizontal scrollbars disabled.</li>
 *   <li>The scroll pane uses a standardized preferred size ({@link gui.GuiConstants#TEXT_AREA_SIZE}).</li>
 * </ul>
 * </p>
 * 
 * @author DejanKrstovski
 */
public class LabelTextAreaPanel extends SubPanel {

    private final MyLabel label;
    private final MyTextArea questionTextArea;
    private final MyScrollPane scrollPane;

    /**
     * Constructs a horizontally arranged panel with a label on the left and an empty text area.
     *
     * @param labelText the text to display in the label (non-null)
     */
    public LabelTextAreaPanel(String labelText) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.label = new MyLabel(labelText);
        this.questionTextArea = new MyTextArea();
        this.scrollPane = configureScrollPane(questionTextArea);

        label.setPreferredSize(LABEL_SIZE);

        add(label);
        add(scrollPane);
    }

    /**
     * Constructs a vertically arranged panel with a label on top and a pre-filled text area.
     *
     * @param labelText the label text (non-null)
     * @param areaText  the initial text content for the text area (may be null/empty)
     */
    public LabelTextAreaPanel(String labelText, String areaText) {
        setLayout(new BorderLayout());
        this.label = new MyLabel(labelText);
        this.questionTextArea = new MyTextArea(areaText != null ? areaText : "");
        this.scrollPane = configureScrollPane(questionTextArea);

        label.setPreferredSize(LABEL_SIZE);

        add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Configures the scroll pane that wraps the provided text area.
     * Vertical scrollbars are always shown; horizontal scrollbars are disabled
     * to favor line wrapping.
     */
    private MyScrollPane configureScrollPane(MyTextArea area) {
        MyScrollPane sp = new MyScrollPane(area);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(TEXT_AREA_SIZE);
        return sp;
    }

    /**
     * Returns the embedded text area component.
     *
     * @return the {@link MyTextArea} instance
     */
    public MyTextArea getQuestionTextArea() {
        return questionTextArea;
    }

    /**
     * Returns the current text content of the text area.
     *
     * @return the text content, never null
     */
    public String getText() {
        return questionTextArea.getText();
    }

    /**
     * Sets the text displayed in the text area.
     *
     * @param text the new text content (null is treated as empty)
     */
    public void setQuestionText(String text) {
        questionTextArea.setText(text != null ? text : "");
    }

    /**
     * Returns the label component shown alongside the text area.
     *
     * @return the {@link MyLabel} instance
     */
    public MyLabel getLabel() {
        return label;
    }

    /**
     * Returns the scroll pane that wraps the text area.
     *
     * @return the {@link MyScrollPane} instance
     */
    public MyScrollPane getScrollPane() {
        return scrollPane;
    }
}
