package gui.guiPanels;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import gui.guiSwing.MyComboBox;
import gui.guiSwing.MyScrollPane;
import gui.guiSwing.SubPanel;

/**
 * @author DejanKrstovski
 * 
 * This panel creates the ComboBox and the scrollPane with the
 * question and theme list.
 */
public class ComboBoxJListPanel extends SubPanel {
	private final MyComboBox comboBox;
	private final JList<String> list;
	private final MyScrollPane scrollPane;
	private final DefaultListModel<String> listModel;
	
	public ComboBoxJListPanel(List<String> comboBoxItems, List<String> listItems) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		comboBox = new MyComboBox();
		for (String item : comboBoxItems) {
			comboBox.addItem(item);
		}
		add(comboBox);
		add(Box.createVerticalStrut(10));

		listModel = new DefaultListModel<String>();
		for(String item: listItems) {
			listModel.addElement(item);
		}
		list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane = new MyScrollPane();
		scrollPane.setPreferredSize(new Dimension(300, 300));
		scrollPane.setViewportView(list);
		add(scrollPane);
	}

	public void updateThemes(List<String> newThemes) {
	    comboBox.removeAllItems();
	    comboBox.addItem("Alle Themen");
	    for (String theme : newThemes) {
	        comboBox.addItem(theme);
	    }
	}
	
	public String getSelectedTheme() {
	    return (String) comboBox.getSelectedItem();
	}
	
	public int getSelectedIndex() {
		return comboBox.getSelectedIndex();
	}
	
	public void showInfo(String info) {
		listModel.clear();
		listModel.addElement("=== Thema Info ===");
		for (String line : info.split("\n")) {
	        listModel.addElement(line);
	    }
		listModel.addElement("==================");
		list.setModel(listModel);
		list.ensureIndexIsVisible(0);
		revalidate();
		repaint();
	}
	
	public int getSelectedQuestion() {
		return list.getSelectedIndex();
	}
	public void updateQuestions(List<String> newQuestions) {
	    list.setListData(newQuestions.toArray(new String[0]));
	}
	
	public void addThemeSelectionListener(ActionListener listener) {
	    comboBox.addActionListener(listener);
	}
	
	public void addQuestionSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
	}
	
	public MyComboBox getComboBox() {
		return comboBox;
	}

	public JList<String> getList() {
		return list;
	}

	public MyScrollPane getScrollPane() {
		return scrollPane;
	}
}