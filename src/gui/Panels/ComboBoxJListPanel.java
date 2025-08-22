package gui.panels;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import gui.swing.MyComboBox;
import gui.swing.MyScrollPane;
import helpers.QuestionListItem;

/**
 * A reusable panel combining a generic combo box for theme selection and a scrollable list
 * for displaying questions or theme information.
 * <p>
 * Typically, T will be ThemeListItem and U will be QuestionListItem to provide IDs and titles.
 * </p>
 *
 * @param <T> the type for combo box items (e.g. ThemeListItem)
 * @param <U> the type for list items (e.g. QuestionListItem)
 */
public class ComboBoxJListPanel<T, U> extends SubPanel {

    /** Combo box for selecting themes (e.g. ThemeListItem). */
    private final MyComboBox<T> comboBox;
    
    /** Model backing the question or info list. */
    private DefaultListModel<U> listModel;
    
    /** List for displaying questions/info items. */
    private final JList<U> list;
    
    /** Scroll pane for the list. */
    private final MyScrollPane scrollPane;

    /**
     * Constructs the panel with combo box items and initial list items.
     * @param comboBoxItems initial items for the combo box (can be ThemeListItem)
     * @param listItems     initial items for the list (can be QuestionListItem)
     */
    public ComboBoxJListPanel(List<T> comboBoxItems, List<U> listItems) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        comboBox = new MyComboBox<>();
        for (T item : comboBoxItems) {
            comboBox.addItem(item);
        }
        add(comboBox);
        add(Box.createVerticalStrut(10));

        listModel = new DefaultListModel<>();
        if (listItems != null) {
            for (U item : listItems) {
                listModel.addElement(item);
            }
        }
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new MyScrollPane();
        scrollPane.setPreferredSize(new Dimension(300, 300));
        scrollPane.setViewportView(list);
        add(scrollPane);
    }

    /** Registers an ActionListener for combo box selection changes. */
    public void addThemeSelectionListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }

    /** Registers a ListSelectionListener for list selection. */
    public void addQuestionSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    /** Updates the combo box content with new items. */
    public void updateThemes(List<T> newThemes) {
        comboBox.removeAllItems();
        for (T theme : newThemes) {
            comboBox.addItem(theme);
        }
    }

    /** Updates the list with a new list of questions/info items. */
    public void updateQuestions(List<U> newQuestions) {
        listModel.clear();
        if (newQuestions != null) {
            for (U q : newQuestions) {
                listModel.addElement(q);
            }
        }
    }

    /** Returns the selected combo box item (full object, e.g. ThemeListItem). */
    @SuppressWarnings("unchecked")
	public T getSelectedThemeItem() {
        return (T) comboBox.getSelectedItem();
    }

    /** Returns the currently selected list item (e.g. QuestionListItem). */
    public U getSelectedQuestionItem() {
        return list.getSelectedValue();
    }

    /** Returns the index of the selected list item. */
    public int getSelectedQuestion() {
        return list.getSelectedIndex();
    }

    /** Returns the index of the selected combo box item. */
    public int getSelectedIndex() {
        return comboBox.getSelectedIndex();
    }

    /** Returns the combo box component. */
    public MyComboBox<T> getComboBox() {
        return comboBox;
    }

    /** Returns the list component. */
    public JList<U> getList() {
        return list;
    }

    /** Returns the scroll pane holding the list. */
    public MyScrollPane getScrollPane() {
        return scrollPane;
    }

    /** Shows theme info or multiline description in the list. */
    @SuppressWarnings("unchecked")
	public void showInfo(String info) {
        listModel.clear();

        for (int i = 0; i < 6; i++) {
            listModel.addElement((U) new QuestionListItem(-1, "\n"));
        }
        listModel.addElement((U) new QuestionListItem(-1, "========= Thema Info ========="));
        for (String line : info.split("\n")) {
            listModel.addElement((U) new QuestionListItem(-1, line));
        }
        listModel.addElement((U) new QuestionListItem(-1, "=============================="));
        list.ensureIndexIsVisible(0);
        revalidate();
        repaint();
    }
}
