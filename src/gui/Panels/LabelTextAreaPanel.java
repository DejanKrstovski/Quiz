package gui.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.Swing.MyLabel;
import gui.Swing.MyScrollPane;
import gui.Swing.MyTextArea;

/**
 * A reusable panel combining a label and a scrollable text area.
 * <p>
 * This panel can be used to display or edit large text content such as
 * question texts. It supports both horizontal and vertical layouts.
 * 
 * The layout type depends on the constructor used:
 * - LINE_AXIS: label on the left of the text area
 * - BorderLayout: label on top of the text area
 * 
 * @author DejanKrstovski
 */
public class LabelTextAreaPanel extends SubPanel implements GuiConstants {

	private MyLabel label;
	private MyTextArea questionTextArea;
	private MyScrollPane scrollPane;

	/**
	 * Constructs the panel with a label and an empty text area, arranged horizontally.
	 *
	 * @param labelText the text to display as the label
	 */
	public LabelTextAreaPanel(String labelText) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		initComponents(labelText);
		add(label);
		add(scrollPane);
	}

	/**
	 * Constructs the panel with a label and pre-filled text area, arranged vertically.
	 *
	 * @param labelText the label text
	 * @param areaText the initial text content of the text area
	 */
	public LabelTextAreaPanel(String labelText, String areaText) {
		setLayout(new BorderLayout());
		initComponents(labelText);
		questionTextArea.setText(areaText);
		add(label, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Initializes and configures internal components.
	 *
	 * @param labelText the text to use for the label
	 */
	private void initComponents(String labelText) {
		label = new MyLabel(labelText);
		label.setPreferredSize(LABEL_SIZE);

		questionTextArea = new MyTextArea();
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setLineWrap(true);

		scrollPane = new MyScrollPane(questionTextArea, 22, 31);
		scrollPane.setPreferredSize(new Dimension(300, 220));
	}

	/**
	 * Returns the internal text area component.
	 *
	 * @return the {@link MyTextArea} component
	 */
	public MyTextArea getQuestionTextArea() {
		return questionTextArea;
	}

	/**
	 * Returns the current text from the text area.
	 *
	 * @return the text content
	 */
	public String getTextInfo() {
		return questionTextArea.getText();
	}

	/**
	 * Sets the text displayed in the text area.
	 *
	 * @param text the new text content
	 */
	public void setTextInfo(String text) {
		this.questionTextArea.setText(text);
	}
}
