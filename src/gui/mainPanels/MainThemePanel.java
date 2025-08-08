package gui.mainPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import persistence.serialization.QuizDataManager;
import quizlogic.ErrorHandler;
import quizlogic.Theme;

/**
 * MainThemePanel manages the user interface for displaying, creating,
 * updating, and deleting quiz themes. It synchronizes UI components
 * with the underlying data model through FakeDataDeliverer.
 * 
 * It notifies registered listeners of theme changes to keep other
 * components in sync.
 * 
 * <p>Supports selecting a theme to edit its title and information,
 * creating new themes, and deleting existing themes with confirmation.</p>
 * 
 * @author DejanKrstovski
 */
public class MainThemePanel extends SubPanel implements GuiConstants{

    private SouthPanel bottomPanel;
    private SubPanel westPanel;
    private SubPanel centerPanel;
    private LabelFieldPanel labelFieldPanel;
    private LabelTextAreaPanel labelTextPanel;
    private MyLabel titleLabel;
    private int selectedIndex = -1;
    private LabelJListPanel labelJListPanel;
    private List<Theme> allThemes;
    private List<String> themeTitles;
    private ThemeChangeListener onThemeChangeListener;
    private boolean isUpdating = false;
    QuizDataManager dataManager = QuizDataManager.getInstance();
    /**
     * Sets the listener to notify when themes are changed.
     * 
     * @param listener listener for theme change events
     */
    public void setOnThemeChangeListener(ThemeChangeListener listener) {
        this.onThemeChangeListener = listener;
    }

    /**
     * Constructs the MainThemePanel with the provided data deliverer.
     * 
     * @param fdd the data source managing themes and questions
     */
    public MainThemePanel() {
        super();
        init();
        
    }

    /**
     * Initializes the layout and components.
     */
    private void init() {
        setLayout(new BorderLayout());
        initComponents();
    }

    /**
     * Initializes the main child panels and adds them to this panel.
     */
    private void initComponents() {
        westPanel = initWestPanel();
        bottomPanel = new SouthPanel(DELETE_THEME, SAVE_THEME, NEW_THEME);
        centerPanel = initCenterPanel();
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        initButtonsActions();
    }

    /**
     * Initializes the west panel, which contains the title label,
     * the theme title input, and the theme information text area.
     * 
     * @return the west panel as SubPanel
     */
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

    /**
     * Initializes the title panel shown at the top of the west panel.
     * 
     * @return a SubPanel containing the title label
     */
    private SubPanel initTitlePanel() {
        SubPanel p = new SubPanel(1, 1);
        titleLabel = new MyLabel(LABEL_NEW_THEME);
        titleLabel.setFont(FONT_TITLE);
        p.add(titleLabel);
        return p;
    }

    /**
     * Initializes the center panel, containing the list of themes.
     * 
     * @return the center panel as SubPanel
     */
    private SubPanel initCenterPanel() {
        allThemes = new ArrayList<>(dataManager.getThemes());
        themeTitles = (ArrayList<String>) allThemes.stream().map(Theme::getTitle)
        	    .collect(Collectors.toCollection(ArrayList::new));
        labelJListPanel = new LabelJListPanel(LABEL_THEMES, themeTitles);
        labelJListPanel.clearThemeSelectionListeners();

        labelJListPanel.addThemeSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        showMessage(ErrorHandler.getInstance().getInfo());
        return labelJListPanel;
    }

    /**
     * Updates the selected theme display when the selection changes.
     */
    private void updateSelection() {
        if (isUpdating) return;
        isUpdating = true;
        
        selectedIndex = labelJListPanel.getList().getSelectedIndex();
        System.out.println(selectedIndex);
        if (selectedIndex >= 0 && selectedIndex < themeTitles.size()) {
            String selectedTheme = labelJListPanel.getList().getSelectedValue();
            titleLabel.setText(LABEL_THEME_INFORMATION);
            labelFieldPanel.setText(selectedTheme);
            labelTextPanel.setTextInfo(allThemes.get(selectedIndex).getThemeInfo());
        } else {
            labelJListPanel.getList().clearSelection();
            resetFields();
        }
        
        isUpdating = false;
    }

    /**
     * Initializes action listeners for the buttons in the bottom panel.
     */
    private void initButtonsActions() {
        MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
        buttons[0].addActionListener(e -> deleteTheme());
        buttons[1].addActionListener(e -> saveTheme());
        buttons[2].addActionListener(e -> reset());
    }

    /**
     * Refreshes the cached list of themes and their titles from the data source.
     */
    private void refreshThemes() {
        allThemes.clear();
        dataManager.getAllThemes();
        allThemes = dataManager.getThemes();
        themeTitles = (ArrayList<String>) allThemes.stream().map(Theme::getTitle).collect(Collectors.toList());
    }

    /**
     * Saves a new theme or updates an existing theme with the
     * current input from the user. Displays messages to the user
     * for validation and operation result.
     */
    private void saveTheme() {
        String title = labelFieldPanel.getText().trim();
        String info = labelTextPanel.getTextInfo().trim();
        if (title.isEmpty() || info.isEmpty()) {
            showMessage(WARNING_EMPTY_TITLE_INFO);
            return;
        }

        if (selectedIndex == -1) {
            if (themeTitles.contains(title)) {
                showMessage(WARNING_THEME_EXISTS);
                return;
            }
            Theme theme = new Theme();
            theme.setId(dataManager.createNewThemeId());
            theme.setThemeInfo(info);
            theme.setTitle(title);
            dataManager.saveTheme(theme);
            refreshThemes();
            showMessage(ErrorHandler.getInstance().getInfo());
        } else if (selectedIndex >= 0 && selectedIndex < allThemes.size()) {
            Theme selectedTheme = allThemes.get(selectedIndex);
            selectedTheme.setTitle(title);
            selectedTheme.setThemeInfo(info);
            dataManager.saveTheme(selectedTheme);
            refreshThemes();
            showMessage(THEME_SUCCESFULLY_UPDATED);
        }

        int previousSelectedIndex = selectedIndex;
        labelJListPanel.updateList(themeTitles);

        if (previousSelectedIndex >= 0 && previousSelectedIndex < allThemes.size()) {
            labelJListPanel.getList().setSelectedIndex(previousSelectedIndex);
            selectedIndex = previousSelectedIndex;
        } else {
            selectedIndex = -1;
            resetFields();
        }
        if (onThemeChangeListener != null) {
            onThemeChangeListener.onThemesChanged();
        }
    }

    /**
     * Deletes the currently selected theme after user confirmation.
     * Also deletes all questions associated with the theme.
     * Displays messages to the user about the outcome.
     */
    private void deleteTheme() {
        if (selectedIndex == -1) {
            showMessage(CHOOSE_A_THEME_MSG);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, THEME_DELETE_INFORMATION, DELETE_CONFIRMATION, JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String error = dataManager.deleteTheme(allThemes.get(selectedIndex));
            themeTitles.remove(selectedIndex);
            labelJListPanel.removeItem(selectedIndex);
            refreshThemes();
            reset();
            Optional.ofNullable(onThemeChangeListener).ifPresent(ThemeChangeListener::onThemesChanged);
            showMessage(error);
        }
    }

    /**
     * Resets the input fields and selection to default state.
     */
    private void reset() {
        labelJListPanel.getList().clearSelection();
        selectedIndex = -1;
        titleLabel.setText(LABEL_NEW_THEME);
        resetFields();
        showMessage(EMPTY_STRING);
    }

    /**
     * Clears text fields for theme title and information.
     */
    private void resetFields() {
        labelFieldPanel.setText(EMPTY_STRING);
        labelTextPanel.setTextInfo(EMPTY_STRING);
    }

    /**
     * Displays a message in the message panel.
     * 
     * @param message the message to show
     */
    private void showMessage(String message) {
        bottomPanel.getMessagePanel().setMessageAreaText(message);
    }
}
