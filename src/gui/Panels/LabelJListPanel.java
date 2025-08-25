package gui.panels;

import static gui.GuiConstants.FONT_TITLE;
import static gui.GuiConstants.LIST_PANE_SIZE;
import static gui.GuiConstants.OUTSIDE_BORDERS_FOR_SUBPANELS;
import static gui.GuiConstants.V_GAP_SMALL;

import java.awt.Component;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import gui.swing.MyLabel;
import gui.swing.MyScrollPane;

/**
 * A reusable panel that displays a titled label above a scrollable list.
 * <p>
 * The list can contain any type of object; by default, items are displayed
 * using {@link Object#toString()}. A custom cell renderer can be provided
 * via {@link #setListCellRenderer(javax.swing.ListCellRenderer)}.
 * </p>
 *
 * <p>
 * Default behavior:
 * <ul>
 *   <li>Title label uses {@link gui.GuiConstants#FONT_TITLE} and left alignment.</li>
 *   <li>List selection mode is single-selection.</li>
 *   <li>Scrollable area uses a standardized preferred size ({@link gui.GuiConstants#LIST_PANE_SIZE}).</li>
 *   <li>Panel padding uses {@link gui.GuiConstants#OUTSIDE_BORDERS_FOR_SUBPANELS}.</li>
 * </ul>
 * </p>
 *
 * @param <T> the type of list items to display
 * 
 * @author DejanKrstovski
 */
public class LabelJListPanel<T> extends SubPanel {

    private final MyLabel label;
    private final JList<T> list;
    private DefaultListModel<T> listModel;
    private final MyScrollPane scrollPane;

    /**
     * Constructs the panel with a given title and a list of items.
     *
     * @param labelText the text to display as the title above the list
     * @param listItems the initial items to populate the list; if null, an empty list is used
     */
    public LabelJListPanel(String labelText, List<T> listItems) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.label = new MyLabel(labelText);
        label.setFont(FONT_TITLE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);

        add(Box.createVerticalStrut(V_GAP_SMALL));

        this.listModel = new DefaultListModel<>();
        if (listItems != null) {
            listItems.forEach(listModel::addElement);
        }

        this.list = new JList<>(listModel);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        this.scrollPane = new MyScrollPane(list);
        scrollPane.setPreferredSize(LIST_PANE_SIZE);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(scrollPane);

        setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
    }

    /**
     * Adds a selection listener to the list.
     *
     * @param listener the listener to add
     */
    public void addSelectionListener(javax.swing.event.ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    /**
     * Removes all registered selection listeners from the list.
     */
    public void clearSelectionListeners() {
        for (javax.swing.event.ListSelectionListener l : list.getListSelectionListeners()) {
            list.removeListSelectionListener(l);
        }
    }

    /**
     * Adds a new item to the list.
     *
     * @param item the item to add
     */
    public void addItem(T item) {
        listModel.addElement(item);
    }

    /**
     * Removes the item at the specified index, if the index is valid.
     *
     * @param index the index of the item to remove
     */
    public void removeItem(int index) {
        if (index >= 0 && index < listModel.size()) {
            listModel.remove(index);
        }
    }

    /**
     * Updates the item at the specified index, if the index is valid.
     *
     * @param index    the index of the item to update
     * @param newValue the new value to set
     */
    public void updateItem(int index, T newValue) {
        if (index >= 0 && index < listModel.size()) {
            listModel.set(index, newValue);
        }
    }

    /**
     * Replaces all items in the list with the provided collection.
     *
     * @param newItems the new items to display; if null, the list becomes empty
     */
    public void updateList(List<T> newItems) {
        DefaultListModel<T> model = new DefaultListModel<>();
        if (newItems != null) {
            newItems.forEach(model::addElement);
        }
        list.setModel(model);
        listModel = model;
    }

    /**
     * Sets a custom cell renderer for the list. Use this to control how items are displayed.
     *
     * @param renderer a ListCellRenderer instance
     */
    public void setListCellRenderer(javax.swing.ListCellRenderer<? super T> renderer) {
        list.setCellRenderer(renderer);
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
