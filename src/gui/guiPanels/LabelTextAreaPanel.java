package gui.guiPanels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;

import gui.GuiConstants;
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyScrollPane;
import gui.guiSwing.MyTextArea;
import gui.guiSwing.SubPanel;

public class LabelTextAreaPanel extends SubPanel implements GuiConstants{
	private MyLabel label;
	private MyTextArea questionTextArea;
	private MyScrollPane scrollPane;

	public LabelTextAreaPanel(String labelText) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		init(labelText);
		questionTextArea.setEditable(false);
		add(label);
		add(scrollPane);
	}

	public void init(String labelText) {
		this.label = new MyLabel(labelText);
		label.setPreferredSize(LABEL_SIZE);
		this.questionTextArea = new MyTextArea();
		
		questionTextArea.setWrapStyleWord(true); // Wrap at word boundaries
		questionTextArea.setLineWrap(true); // Enable line wrapping
		this.scrollPane = new MyScrollPane(questionTextArea, 22, 31);
		scrollPane.setPreferredSize(new Dimension(200, 220));
	}
	
	public LabelTextAreaPanel(String labelText, String areaText) {
		setLayout(new BorderLayout());
		init(labelText);
		questionTextArea.setText(areaText);
		add(label, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	
	public MyTextArea getQuestionTextArea() {
		return questionTextArea;
	}

	public String getTextInfo() {
		return questionTextArea.getText();
	}
	public void setTextInfo(String text) {
		this.questionTextArea.setText(text);
	}
}
