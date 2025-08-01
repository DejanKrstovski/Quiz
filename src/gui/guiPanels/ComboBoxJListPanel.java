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

/**
 * A custom panel combining a combo box (for theme selection) 
 * and a scrollable list (for questions or theme info).
 * <p>
 * Allows users to filter questions by theme or display theme descriptions.
 * Used in the main question editing interface.
 * 
 * Components:
 * <ul>
 *   <li>ComboBox for selecting a theme</li>
 *   <li>Scrollable JList for showing questions or theme info</li>
 * </ul>
 * 
 * @author DejanKrstovski
 */
public class ComboBoxJListPanel extends SubPanel {
	private final MyComboBox comboBox;
	private final JList<String> list;
	private final MyScrollPane scrollPane;
	private final DefaultListModel<String> listModel;
	
	/**
	 * Constructs the panel with given combo box items (themes) and initial list items (questions).
	 *
	 * @param comboBoxItems the themes to populate in the combo box
	 * @param listItems the initial list of question titles to display
	 */
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

	
	/**
	 * Adds an ActionListener to the combo box for theme selection changes.
	 *
	 * @param listener the listener to attach to the combo box
	 */
	public void addThemeSelectionListener(ActionListener listener) {
	    comboBox.addActionListener(listener);
	}
	
	/**
	 * Adds a ListSelectionListener to the question list.
	 *
	 * @param listener the listener to attach to the list
	 */
	public void addQuestionSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
	}
	
	/**
	 * Updates the combo box with a new list of themes.
	 *
	 * @param newThemes the new themes to display in the combo box
	 */
	public void updateThemes(List<String> newThemes) {
	    comboBox.removeAllItems();
	    comboBox.addItem("Alle Themen");
	    for (String theme : newThemes) {
	        comboBox.addItem(theme);
	    }
	}
	
	/**
	 * Displays the theme description text in the list,
	 * replacing the question list temporarily.
	 *
	 * @param info the multiline description text of the theme
	 */
	public void showInfo(String info) {
		listModel.clear();
		for(int i = 0; i<5; i++) {
			listModel.addElement("\n");
		}
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
	
	/**
	 * Returns the selected index of the question list.
	 *
	 * @return index of the selected question in the list
	 */
	public int getSelectedQuestion() {
		return list.getSelectedIndex();
	}
	
	/**
	 * Updates the question list with new question titles.
	 *
	 * @param newQuestions the list of new questions to display
	 */
	public void updateQuestions(List<String> newQuestions) {
	    list.setListData(newQuestions.toArray(new String[0]));
	}
	
	/**
	 * Returns the currently selected item in the combo box.
	 *
	 * @return the selected theme title
	 */
	public String getSelectedTheme() {
	    return (String) comboBox.getSelectedItem();
	}
	
	/**
	 * Returns the selected index of the combo box.
	 *
	 * @return the index of the selected theme
	 */
	public int getSelectedIndex() {
		return comboBox.getSelectedIndex();
	}
	
	/**
	 * Returns the combo box used for theme selection.
	 *
	 * @return the combo box
	 */
	public MyComboBox getComboBox() {
		return comboBox;
	}

	/**
	 * Returns the list used to display questions or theme info.
	 *
	 * @return the question/info list
	 */
	public JList<String> getList() {
		return list;
	}

	/**
	 * Returns the scroll pane that contains the list.
	 *
	 * @return the scroll pane
	 */
	public MyScrollPane getScrollPane() {
		return scrollPane;
	}
}