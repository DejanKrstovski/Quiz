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
 * The list can contain any type of object, relying on {@link Object#toString()}
 * to determine how each element is displayed.
 * </p>
 * <p>
 * This is now used for {@code ThemeListItem} to allow mapping between
 * displayed titles and unique IDs.
 * </p>
 *
 * @param <T> the type of list items to display
 * 
 * @author 
 */
public class LabelJListPanel<T> extends SubPanel implements GuiConstants {

    private final MyLabel label;
    private final JList<T> list;
    private DefaultListModel<T> listModel;
    private final MyScrollPane scrollPane;

    /**
     * Constructs the panel with a given title and a list of items.
     *
     * @param labelText the text to display as the title above the list
     * @param listItems the initial items to populate the list (non-null)
     */
    public LabelJListPanel(String labelText, List<T> listItems) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        label = new MyLabel(labelText);
        label.setFont(FONT_TITLE); 
        add(label);
        add(Box.createVerticalStrut(10));

        listModel = new DefaultListModel<>();
        for (T item : listItems) {
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

    /** Adds a selection listener to the list. */
    public void addSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    /** Removes all registered selection listeners from the list. */
    public void clearSelectionListeners() {
        for (ListSelectionListener l : list.getListSelectionListeners()) {
            list.removeListSelectionListener(l);
        }
    }

    /** Adds a new item to the list. */
    public void addItem(T item) {
        listModel.addElement(item);
    }

    /** Removes the item at the specified index. */
    public void removeItem(int index) {
        if (index >= 0 && index < listModel.size()) {
            listModel.remove(index);
        }
    }

    /** Updates the item at the specified index. */
    public void updateItem(int index, T newValue) {
        if (index >= 0 && index < listModel.size()) {
            listModel.set(index, newValue);
        }
    }

    /** Replaces all items in the list. */
    public void updateList(List<T> newItems) {
        DefaultListModel<T> model = new DefaultListModel<>();
        for (T item : newItems) {
            model.addElement(item);
        }
        list.setModel(model);
        listModel = model;
    }

    /** Returns the scroll pane containing the list. */
    public MyScrollPane getScrollPane() {
        return scrollPane;
    }

    /** Returns the JList component. */
    public JList<T> getList() {
        return list;
    }

    /** Returns the title label. */
    public MyLabel getLabel() {
        return label;
    }
}
