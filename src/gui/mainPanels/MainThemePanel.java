package gui.mainPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import gui.GuiConstants;
import gui.Panels.LabelFieldPanel;
import gui.Panels.LabelJListPanel;
import gui.Panels.LabelTextAreaPanel;
import gui.Panels.SouthPanel;
import gui.Panels.SubPanel;
import gui.Swing.MyButton;
import gui.Swing.MyLabel;
import helpers.ThemeListItem;
import persistence.serialization.QuizDataManager;
import quizlogic.ErrorHandler;
import quizlogic.Theme;

/**
 * Panel for displaying, creating, updating, and deleting quiz themes.
 * <p>
 * Uses {@link ThemeListItem} for ID-based selection instead of titles or list indexes,
 * ensuring stable references even when theme titles change.
 * </p>
 * <p>
 * Maintains a list of registered {@link ThemeChangeListener}s so that other panels
 * can be notified immediately when themes are added, updated, or deleted.
 * </p>
 *
 * <h2>UI Layout:</h2>
 * <ul>
 *   <li>West panel: Theme title and info fields</li>
 *   <li>Center panel: Scrollable list of themes</li>
 *   <li>South panel: Save / New / Delete buttons</li>
 * </ul>
 *
 * <h2>Key features:</h2>
 * <ul>
 *   <li>Duplicate title checking for both add and edit</li>
 *   <li>Selection restore after save</li>
 *   <li>Consistent refresh from {@link QuizDataManager} after modifications</li>
 * </ul>
 * 
 * @author 
 */
public class MainThemePanel extends SubPanel implements GuiConstants {

    private SouthPanel bottomPanel;
    private SubPanel westPanel;
    private SubPanel centerPanel;
    private LabelFieldPanel labelFieldPanel;
    private LabelTextAreaPanel labelTextPanel;
    private MyLabel titleLabel;

    private List<Theme> allThemes = new ArrayList<>();
    private List<ThemeListItem> themeItems = new ArrayList<>();

    private int selectedThemeId = NO_SELECTION;

    private LabelJListPanel<ThemeListItem> labelJListPanel;
    private final List<ThemeChangeListener> themeChangeListeners = new ArrayList<>();

    private final QuizDataManager dataManager = QuizDataManager.getInstance();

    /**
     * Registers a new listener to be notified when themes change.
     * 
     * @param listener The listener to notify; ignored if null.
     */
    public void addOnThemeChangeListener(ThemeChangeListener listener) {
        if (listener != null) {
            themeChangeListeners.add(listener);
        }
    }

    /** Creates the panel and initializes its layout and components. */
    public MainThemePanel() {
        super();
        init();
    }

    /** Configures layout and sub-components. */
    private void init() {
        setLayout(new BorderLayout());
        initComponents();
    }

    /** Initializes and adds all subpanels. */
    private void initComponents() {
        westPanel = initWestPanel();
        bottomPanel = new SouthPanel(DELETE_THEME, SAVE_THEME, NEW_THEME);
        centerPanel = initCenterPanel();

        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        initButtonsActions();
    }

    /** @return West panel with theme title and info fields. */
    private SubPanel initWestPanel() {
        SubPanel p = new SubPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setPreferredSize(WEST_PANEL_DIMENSIONS);
        p.setMaximumSize(WEST_PANEL_DIMENSIONS);
        p.add(initTitlePanel());
        p.add(Box.createVerticalStrut(10));

        labelFieldPanel = new LabelFieldPanel(LABEL_TITLE, EMPTY_STRING);
        labelFieldPanel.getTextField().setEditable(true);
        p.add(labelFieldPanel);

        labelTextPanel = new LabelTextAreaPanel(LABEL_THEME_INFORMATION, EMPTY_STRING);
        p.add(labelTextPanel);

        p.setBorder(OUTSIDE_BORDERS_FOR_SUBPANELS);
        return p;
    }

    /** @return Title label subpanel. */
    private SubPanel initTitlePanel() {
        SubPanel p = new SubPanel(1, 1);
        titleLabel = new MyLabel(LABEL_NEW_THEME);
        titleLabel.setFont(FONT_TITLE);
        p.add(titleLabel);
        return p;
    }

    /** @return Center panel with scrollable theme list. */
    private SubPanel initCenterPanel() {
        dataManager.getAllThemesAndQuestions();
        refreshThemesAndItems();
        labelJListPanel = new LabelJListPanel<>(LABEL_THEMES, themeItems);
        labelJListPanel.clearSelectionListeners();
        labelJListPanel.addSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        showMessage(ErrorHandler.getInstance().getInfo());
        return labelJListPanel;
    }

    /** Reloads theme data from manager and rebuilds ThemeListItem list. */
    private void refreshThemesAndItems() {
        allThemes = dataManager.getThemes();
        themeItems = allThemes.stream()
                              .map(theme -> new ThemeListItem(theme.getId(), theme.getTitle()))
                              .collect(Collectors.toList());
    }

    /** 
     * Updates UI fields based on the selected ThemeListItem from the list.
     * Resets to NO_SELECTION if no item or theme found. 
     */
    private void updateSelection() {
        ThemeListItem selectedItem = labelJListPanel.getList().getSelectedValue();
        if (selectedItem != null) {
            selectedThemeId = selectedItem.getId();
            Theme selectedTheme = getThemeById(selectedThemeId);
            if (selectedTheme != null) {
                titleLabel.setText(LABEL_THEME_INFORMATION);
                labelFieldPanel.setText(selectedTheme.getTitle());
                labelTextPanel.setTextInfo(selectedTheme.getThemeInfo());
            } else {
                resetFields();
                selectedThemeId = NO_SELECTION;
            }
        } else {
            labelJListPanel.getList().clearSelection();
            resetFields();
            selectedThemeId = NO_SELECTION;
        }
    }

    /** Configures button action listeners. */
    private void initButtonsActions() {
        MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
        buttons[0].addActionListener(e -> deleteTheme());
        buttons[1].addActionListener(e -> saveTheme());
        buttons[2].addActionListener(e -> reset());
    }

    /** Saves a newly created theme or updates the selected one. */
    private void saveTheme() {
        String title = labelFieldPanel.getText().trim();
        String info = labelTextPanel.getTextInfo().trim();
        if (title.isEmpty() || info.isEmpty()) {
            showMessage(WARNING_EMPTY_TITLE_INFO);
            return;
        }
        if (themeItems.stream()
                      .anyMatch(item -> item.getTitle().equals(title) && item.getId() != selectedThemeId)) {
            showMessage(WARNING_THEME_EXISTS);
            return;
        }

        if (selectedThemeId == NO_SELECTION) {
            Theme theme = new Theme();
            theme.setId(dataManager.createNewThemeId());
            theme.setThemeInfo(info);
            theme.setTitle(title);
            dataManager.saveTheme(theme);
            selectedThemeId = theme.getId(); // Select the new theme after saving
            showMessage(ErrorHandler.getInstance().getInfo());
        } else {
            Theme selectedTheme = getThemeById(selectedThemeId);
            if (selectedTheme != null) {
                selectedTheme.setTitle(title);
                selectedTheme.setThemeInfo(info);
                dataManager.saveTheme(selectedTheme);
                showMessage(THEME_SUCCESFULLY_UPDATED);
            }
        }

        reloadAndNotify();
        selectThemeById(selectedThemeId);
    }

    /** Deletes the selected theme after user confirmation. */
    private void deleteTheme() {
        if (selectedThemeId == NO_SELECTION) {
            showMessage(CHOOSE_A_THEME_MSG);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, THEME_DELETE_INFORMATION, DELETE_CONFIRMATION,
                                                    JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Theme toDelete = getThemeById(selectedThemeId);
            if (toDelete != null) {
                String error = dataManager.deleteTheme(toDelete);
                showMessage(error);
                selectedThemeId = NO_SELECTION;
                reset();
                reloadAndNotify();
            } else {
                showMessage(CHOOSE_A_THEME_MSG);
            }
        }
    }

    /** Clears list selection and resets form fields. */
    private void reset() {
        labelJListPanel.getList().clearSelection();
        selectedThemeId = NO_SELECTION;
        titleLabel.setText(LABEL_NEW_THEME);
        resetFields();
        showMessage(EMPTY_STRING);
    }

    /** Clears theme title and info text fields. */
    private void resetFields() {
        labelFieldPanel.setText(EMPTY_STRING);
        labelTextPanel.setTextInfo(EMPTY_STRING);
    }

    /** @return Theme object by ID, or null if not found. */
    private Theme getThemeById(int id) {
        return allThemes.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /** Selects the theme in the list by ID, or clears selection if not found. */
    private void selectThemeById(int id) {
        for (int i = 0; i < themeItems.size(); i++) {
            if (themeItems.get(i).getId() == id) {
                labelJListPanel.getList().setSelectedIndex(i);
                selectedThemeId = id;
                return;
            }
        }
        labelJListPanel.getList().clearSelection();
        selectedThemeId = NO_SELECTION;
    }

    /** Refreshes theme list from data and notifies listeners. */
    private void reloadAndNotify() {
        refreshThemesAndItems();
        labelJListPanel.updateList(themeItems);
        notifyThemeChangeListeners();
    }

    /** Notifies all registered ThemeChangeListeners that themes have changed. */
    private void notifyThemeChangeListeners() {
        for (ThemeChangeListener listener : themeChangeListeners) {
            listener.onThemesChanged();
        }
    }

    /** Displays a message in the bottom message area. */
    private void showMessage(String message) {
        bottomPanel.getMessagePanel().setMessageAreaText(message);
    }
}
