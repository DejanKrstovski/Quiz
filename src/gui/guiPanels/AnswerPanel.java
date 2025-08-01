package gui.guiPanels;

import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiSwing.MyCheckBox;
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyTextField;
import gui.guiSwing.SubPanel;

/**
 * @author DejanKrstovski
 * 
 * This panel makes the labels, the textFields and the chechBoxes <br>
 * for the answers
 */
public class AnswerPanel extends SubPanel implements GuiConstants{
    private ArrayList<MyTextField> answerFields;
    private ArrayList<MyCheckBox> answerCheckBoxes;

    /**
     * Here are made the 4 rows of the Answer panel
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
	 * Here is the creation of the label, TextField and the checkBox
	 * 
	 * @param idx the number of the row
	 * @return sub panel
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

    public MyTextField getAnswerFields(int i) {
		return answerFields.get(i);
	}

	public MyCheckBox getAnswerCheckBoxes(int i) {
		return answerCheckBoxes.get(i);
	}

}
