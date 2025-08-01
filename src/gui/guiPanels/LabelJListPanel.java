package gui.guiPanels;

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
import gui.guiSwing.MyLabel;
import gui.guiSwing.MyScrollPane;
import gui.guiSwing.SubPanel;

public class LabelJListPanel extends SubPanel implements GuiConstants {
	private final MyLabel label;
	private final JList<String> list;
	private DefaultListModel<String> listModel;
	private final MyScrollPane scrollPane;

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
		setBorder(OUTSIDE_BORDERS);
	}

	public void addThemeSelectionListener(ListSelectionListener listener) {
	    list.addListSelectionListener(listener);
	}
	
	public void clearThemeSelectionListeners() {
	    for (ListSelectionListener l : list.getListSelectionListeners()) {
	        list.removeListSelectionListener(l);
	    }
	}
	
	public void addItem(String item) {
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		model.addElement(item);
	}
	
	public void removeItem(int index) {
	    DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
	    if (index >= 0 && index < model.size()) {
	        model.remove(index);
	    }
	}
	
	public void updateItem(int index, String newValue) {
	    DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
	    model.set(index, newValue);
	}
	
	public void updateList(List<String> newItems) {
	    DefaultListModel<String> model = new DefaultListModel<>();
	    for (String item : newItems) {
	        model.addElement(item);
	    }
	    getList().setModel(model);
	}
	
	public MyScrollPane getScrollPane() {
		return scrollPane;
	}

	public JList<String> getList() {
		return list;
	}

	public MyLabel getLabel() {
		return label;
	}
}
