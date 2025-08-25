package gui.panels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.swing.MyCheckBox;
import gui.swing.MyLabel;
import gui.swing.MyTextField;

import static gui.GuiConstants.DISTANCE_BETWEEN_ELEMENTS;
import static gui.GuiConstants.H_GAP_SMALL;
import static gui.GuiConstants.LABEL_SIZE;

/**
 * A panel that displays a fixed number of rows for quiz answers.
 * Each row contains:
 * - a label
 * - a text field for the answer
 * - a checkbox indicating whether the answer is correct
 *
 * This component is used in question creation and editing workflows to
 * capture multiple-choice answers with a single correct option.
 * 
 * @author DejanKrstovski
 */
public class AnswerPanel extends SubPanel {

    private static final int ANSWER_ROWS = 4;

    private List<MyTextField> answerFields;
    private List<MyCheckBox> answerCheckBoxes;

    /**
     * Constructs an {@code AnswerPanel} and initializes {@value #ANSWER_ROWS} rows
     * for entering answers and marking correctness.
     */
    public AnswerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.answerFields = new ArrayList<>(ANSWER_ROWS);
        this.answerCheckBoxes = new ArrayList<>(ANSWER_ROWS);

        for (int index = 0; index < ANSWER_ROWS; index++) {
            add(createAnswerRow(index));
        }
    }

    /**
     * Creates a single row containing a label, a text field, and a checkbox
     * representing one possible answer.
     *
     * @param index the zero-based row index
     * @return a subpanel representing one answer row
     */
    private JPanel createAnswerRow(int index) {
        SubPanel row = new SubPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        MyLabel label = new MyLabel("Antwort " + (index + 1));
        label.setPreferredSize(LABEL_SIZE);
        label.setMaximumSize(LABEL_SIZE);
        MyTextField field = new MyTextField();
        MyCheckBox checkBox = new MyCheckBox();

        answerFields.add(field);
        answerCheckBoxes.add(checkBox);

        row.add(label);
        row.add(field);
        row.add(Box.createHorizontalStrut(H_GAP_SMALL));
        row.add(checkBox);

        if (index != ANSWER_ROWS - 1) {
            row.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        }

        return row;
    }

    /**
     * Returns the text field for the answer at the given index.
     *
     * Expected range: 0 to {@value #ANSWER_ROWS} - 1.
     *
     * @param index the zero-based index of the answer field
     * @return the text field for the specified answer
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public MyTextField getAnswerField(int index) {
        return answerFields.get(index);
    }

    /**
     * Returns the checkbox for the answer at the given index.
     *
     * Expected range: 0 to {@value #ANSWER_ROWS} - 1.
     *
     * @param index the zero-based index of the checkbox
     * @return the checkbox indicating if the answer is correct
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public MyCheckBox getAnswerCheckBox(int index) {
        return answerCheckBoxes.get(index);
    }

    /**
     * Returns the fixed number of answer rows managed by this panel.
     *
     * @return the number of rows
     */
    public int getRowCount() {
        return ANSWER_ROWS;
    }
}
