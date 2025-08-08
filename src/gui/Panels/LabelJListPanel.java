package gui.Panels;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import gui.GuiConstants;
import gui.Swing.MyLabel;
import gui.Swing.MyScrollPane;

/**
 * A reusable panel that displays a titled label above a scrollable list.
 * <p>
 * It uses a {@link MyLabel} for the title and a {@link JList} to show items.
 * The list is backed by a {@link DefaultListModel} to allow dynamic updates.
 * <p>
 * Commonly used for theme or question listings within the quiz GUI.
 * 
 * @author DejanKrstovski
 */
public class LabelJListPanel extends SubPanel implements GuiConstants {

	private final MyLabel label;
	private final JList<String> list;
	private DefaultListModel<String> listModel;
	private final MyScrollPane scrollPane;

	/**
	 * Constructs the panel with a given title and a list of items.
	 *
	 * @param labelText the text to display as the title above the list
	 * @param listItems the initial items to populate the list
	 */
	public LabelJListPanel(String labelText, List<String> listItems) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		label = new MyLabel(labelText);
		label.setFont(FONT_TITLE); 
		add(label);
		add(Box.createVerticalStrut(10));

		listModel = new DefaultListModel<>();
		for (String item : listItems) {
			listModel.addElement(item);
		}

		list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane = new MyScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(500, 300));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane);
		setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
	}

	/**
	 * Adds a selection listener to the list.
	 *
	 * @param listener the ListSelectionListener to add
	 */
	public void addThemeSelectionListener(ListSelectionListener listener) {
	    list.addListSelectionListener(listener);
	}

	/**
	 * Removes all registered selection listeners from the list.
	 * Useful when refreshing event bindings dynamically.
	 */
	public void clearThemeSelectionListeners() {
	    for (ListSelectionListener l : list.getListSelectionListeners()) {
	        list.removeListSelectionListener(l);
	    }
	}

	/**
	 * Adds a new item to the end of the list.
	 *
	 * @param item the string item to be added
	 */
	public void addItem(String item) {
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		model.addElement(item);
	}

	/**
	 * Removes the item at the specified index from the list.
	 *
	 * @param index the index of the item to be removed
	 */
	public void removeItem(int index) {
	    DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
	    if (index >= 0 && index < model.size()) {
	        model.remove(index);
	    }
	}

	/**
	 * Updates the item at the specified index with a new value.
	 *
	 * @param index the index of the item to update
	 * @param newValue the new value to replace the existing one
	 */
	public void updateItem(int index, String newValue) {
	    DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
	    model.set(index, newValue);
	}

	/**
	 * Replaces the entire list content with a new set of items.
	 *
	 * @param newItems the new list of strings to display
	 */
	public void updateList(List<String> newItems) {
	    DefaultListModel<String> model = new DefaultListModel<>();
	    for (String item : newItems) {
	        model.addElement(item);
	    }
	    getList().setModel(model);
	}

	/**
	 * Returns the scroll pane that wraps the list.
	 *
	 * @return the {@link MyScrollPane} instance
	 */
	public MyScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * Returns the JList displaying the items.
	 *
	 * @return the {@link JList} of strings
	 */
	public JList<String> getList() {
		return list;
	}

	/**
	 * Returns the label component used as the title.
	 *
	 * @return the {@link MyLabel} component
	 */
	public MyLabel getLabel() {
		return label;
	}
}
