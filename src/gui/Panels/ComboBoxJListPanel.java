package gui.Panels;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import gui.Swing.MyComboBox;
import gui.Swing.MyScrollPane;
import helpers.QuestionListItem;

/**
 * A custom panel combining a combo box for theme selection and a scrollable list 
 * displaying questions or theme information.
 * <p>
 * This panel allows users to filter questions by selecting a theme or to show detailed 
 * descriptions of a selected theme. It is used primarily in the main question editing interface.
 * </p>
 * 
 * <p>
 * The combo box enables theme selection, while the list displays the relevant questions 
 * or informational text based on the current context.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class ComboBoxJListPanel extends SubPanel {

    /** Combo box for selecting quiz themes. */
    private final MyComboBox comboBox;

    /** Model backing the question or info list. */
    private final DefaultListModel<QuestionListItem> listModel;

    /** List component for displaying questions or theme descriptions. */
    private final JList<QuestionListItem> list;

    /** Scroll pane containing the list for scrolling support. */
    private final MyScrollPane scrollPane;

    /**
     * Constructs the panel with provided themes for the combo box and initial questions for the list.
     *
     * @param comboBoxItems the list of theme titles to populate the combo box
     * @param listItems the initial list of questions or items to display in the list; may be null
     */
    public ComboBoxJListPanel(List<String> comboBoxItems, List<QuestionListItem> listItems) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        comboBox = new MyComboBox();
        for (String item : comboBoxItems) {
            comboBox.addItem(item);
        }
        add(comboBox);
        add(Box.createVerticalStrut(10));

        listModel = new DefaultListModel<>();
        if (listItems != null) {
            for (QuestionListItem item : listItems) {
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

    /**
     * Registers an {@link ActionListener} to listen for theme selection changes in the combo box.
     *
     * @param listener the listener to attach to the combo box
     */
    public void addThemeSelectionListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }

    /**
     * Registers a {@link ListSelectionListener} to listen for selection changes in the question list.
     *
     * @param listener the listener to attach to the list
     */
    public void addQuestionSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    /**
     * Updates the themes displayed in the combo box to a new list.
     * The combo box will have an initial item "Alle Themen" ("All Themes") followed by the new themes.
     *
     * @param newThemes the new list of themes to populate in the combo box
     */
    public void updateThemes(List<String> newThemes) {
        comboBox.removeAllItems();
        comboBox.addItem("Alle Themen");
        for (String theme : newThemes) {
            comboBox.addItem(theme);
        }
    }

    /**
     * Displays a multiline theme description in the list, temporarily replacing any existing question list.
     * Uses placeholder empty items for padding and decorates the info with header and footer lines.
     *
     * @param info the multiline description text of the theme to display
     */
    public void showInfo(String info) {
        listModel.clear();

        // Add blank padding entries to keep the list visually balanced
        for (int i = 0; i < 5; i++) {
            listModel.addElement(new QuestionListItem(-1, "\n"));
        }

        listModel.addElement(new QuestionListItem(-1, "=== Thema Info ==="));
        for (String line : info.split("\n")) {
            listModel.addElement(new QuestionListItem(-1, line));
        }
        listModel.addElement(new QuestionListItem(-1, "=================="));

        // Ensure the list view scrolls to the top
        list.ensureIndexIsVisible(0);
        revalidate();
        repaint();
    }

    /**
     * Returns the currently selected index in the question list.
     *
     * @return the index of the selected question, or -1 if no selection
     */
    public int getSelectedQuestion() {
        return list.getSelectedIndex();
    }

    /**
     * Returns the selected {@link QuestionListItem} from the question list.
     *
     * @return the currently selected item, or null if no selection
     */
    public QuestionListItem getSelectedQuestionItem() {
        return list.getSelectedValue();
    }

    /**
     * Updates the question list content with a new list of {@link QuestionListItem}s.
     *
     * @param newQuestions the new list of questions to display; if null, the list is cleared
     */
    public void updateQuestions(List<QuestionListItem> newQuestions) {
        listModel.clear();
        if (newQuestions != null) {
            for (QuestionListItem q : newQuestions) {
                listModel.addElement(q);
            }
        }
    }

    /**
     * Returns the currently selected theme title from the combo box.
     *
     * @return the selected theme as a string, or null if none selected
     */
    public String getSelectedTheme() {
        return (String) comboBox.getSelectedItem();
    }

    /**
     * Returns the index of the currently selected item in the combo box.
     *
     * @return the selected index of the combo box, or -1 if no selection
     */
    public int getSelectedIndex() {
        return comboBox.getSelectedIndex();
    }

    /**
     * Provides access to the underlying combo box component.
     *
     * @return the {@link MyComboBox} used for theme selection
     */
    public MyComboBox getComboBox() {
        return comboBox;
    }

    /**
     * Provides access to the underlying question list component.
     *
     * @return the {@link JList} displaying questions or theme info
     */
    public JList<QuestionListItem> getList() {
        return list;
    }

    /**
     * Provides access to the scroll pane containing the question list.
     *
     * @return the {@link MyScrollPane} wrapping the question list
     */
    public MyScrollPane getScrollPane() {
        return scrollPane;
    }
}
