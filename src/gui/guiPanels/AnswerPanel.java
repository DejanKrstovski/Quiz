package gui.guiPanels;

import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiSwing.MyCheckBox;
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyTextField;

/**
 * A panel that displays four rows of input fields for quiz answers.
 * Each row contains a label, a text field for the answer, and a checkbox
 * to indicate whether the answer is correct.
 * 
 * Used in question creation/editing interfaces.
 * 
 * @author DejanKrstovski
 */
public class AnswerPanel extends SubPanel implements GuiConstants{
    private ArrayList<MyTextField> answerFields;
    private ArrayList<MyCheckBox> answerCheckBoxes;

    /**
     * Constructs the AnswerPanel and initializes four rows
     * for entering answers and marking them as correct or incorrect.
     */
	public AnswerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        answerFields = new ArrayList<>(4);
        answerCheckBoxes = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            add(createAnswerRow(i));
        }
    }

	/**
	 * Creates a single row containing a label, a text field, and a checkbox 
	 * to represent one possible answer.
	 *
	 * @param idx the index of the row (0-based)
	 * @return the constructed subpanel for a single answer row
	 */
    private JPanel createAnswerRow(int idx) {
        SubPanel row = new SubPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        MyLabel label = new MyLabel("Antwort " + (idx + 1));
        label.setPreferredSize(LABEL_SIZE);
        answerFields.add(new MyTextField());
        answerCheckBoxes.add(new MyCheckBox());
        row.add(label);
        row.add(answerFields.get(idx));
        row.add(Box.createHorizontalStrut(20));
        row.add(answerCheckBoxes.get(idx));
        if(idx != 3) {
        		row.setBorder(DISTANCE_BETWEEN_ELEMENTS);
        }
        return row;
    }

    /**
     * Returns the text field at the given index.
     *
     * @param i the index of the answer field (0–3)
     * @return the text field for the specified answer
     */
    public MyTextField getAnswerFields(int i) {
		return answerFields.get(i);
	}

    /**
     * Returns the checkBox at the given index.
     *
     * @param i the index of the checkBox (0–3)
     * @return the checkBox indicating whether the answer is correct
     */
	public MyCheckBox getAnswerCheckBoxes(int i) {
		return answerCheckBoxes.get(i);
	}

}
