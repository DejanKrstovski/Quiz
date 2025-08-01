package gui.mainGuiPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.GuiConstants;
import gui.guiPanels.LabelFieldPanel;
import gui.guiPanels.LabelJListPanel;
import gui.guiPanels.LabelTextAreaPanel;
import gui.guiPanels.OnThemeChangeListener;
import gui.guiPanels.SouthPanel;
import gui.guiPanels.SubPanel;
import gui.guiSwing.MyButton;
import gui.guiSwing.MyLabel;
import quizlogic.FakeDataDeliverer;
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
public class MainThemePanel extends JPanel implements GuiConstants {

    private SouthPanel bottomPanel;
    private SubPanel westPanel;
    private SubPanel centerPanel;
    private LabelFieldPanel labelFieldPanel;
    private LabelTextAreaPanel labelTextPanel;
    private FakeDataDeliverer fdd;
    private MyLabel titleLabel;
    private int selectedIndex = -1;
    private LabelJListPanel labelJListPanel;
    private List<Theme> allThemes;
    private List<String> themeTitles;
    private OnThemeChangeListener onThemeChangeListener;

    /**
     * Sets the listener to notify when themes are changed.
     * 
     * @param listener listener for theme change events
     */
    public void setOnThemeChangeListener(OnThemeChangeListener listener) {
        this.onThemeChangeListener = listener;
    }

    /**
     * Constructs the MainThemePanel with the provided data deliverer.
     * 
     * @param fdd the data source managing themes and questions
     */
    public MainThemePanel(FakeDataDeliverer fdd) {
        super();
        this.fdd = fdd;
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
        centerPanel = initCenterPanel();
        bottomPanel = new SouthPanel(DELETE_THEME, SAVE_THEME, NEW_THEME);
        
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

        labelFieldPanel = new LabelFieldPanel(TITLE, EMPTY_STRING);
        labelFieldPanel.getTextField().setEditable(true);
        p.add(labelFieldPanel);

        labelTextPanel = new LabelTextAreaPanel(THEME_INFORMATION, EMPTY_STRING);
        p.add(labelTextPanel);

        p.setBorder(OUTSIDE_BORDERS);
        return p;
    }

    /**
     * Initializes the title panel shown at the top of the west panel.
     * 
     * @return a SubPanel containing the title label
     */
    private SubPanel initTitlePanel() {
        SubPanel p = new SubPanel(1, 1);
        titleLabel = new MyLabel(NEW_THEME_LABEL);
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
        allThemes = new ArrayList<>(fdd.getThemes());
        themeTitles = allThemes.stream().map(Theme::getTitle).collect(Collectors.toList());
        labelJListPanel = new LabelJListPanel(THEMES, themeTitles);
        labelJListPanel.clearThemeSelectionListeners();

        labelJListPanel.addThemeSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        return labelJListPanel;
    }

    /**
     * Updates the selected theme display when the selection changes.
     */
    private void updateSelection() {
        selectedIndex = labelJListPanel.getList().getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < themeTitles.size()) {
            String selectedTheme = labelJListPanel.getList().getSelectedValue();
            titleLabel.setText(THEME_INFORMATION);
            labelFieldPanel.setText(selectedTheme);
            labelTextPanel.setTextInfo(allThemes.get(selectedIndex).getInfoText());
        } else {
            labelJListPanel.getList().clearSelection();
            resetFields();
        }
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
        allThemes = new ArrayList<>(fdd.getThemes());
        themeTitles = allThemes.stream().map(Theme::getTitle).collect(Collectors.toList());
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
            fdd.addThema(title, info);
            showMessage(NEW_THEME_SUCCESFULLY_SAVED);
        } else if (selectedIndex >= 0 && selectedIndex < allThemes.size()) {
            Theme selectedTheme = allThemes.get(selectedIndex);
            selectedTheme.setTitle(title);
            selectedTheme.setText(info);
            showMessage(THEME_SUCCESFULLY_UPDATED);
        }

        int previousSelectedIndex = selectedIndex;
        refreshThemes();
        labelJListPanel.updateList(themeTitles);

        if (previousSelectedIndex >= 0 && previousSelectedIndex < allThemes.size()) {
            labelJListPanel.getList().setSelectedIndex(previousSelectedIndex);
            selectedIndex = previousSelectedIndex;
        } else {
            selectedIndex = -1;
            resetFields();
        }
        Optional.ofNullable(onThemeChangeListener).ifPresent(OnThemeChangeListener::onThemesChanged);
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

        String title = labelFieldPanel.getText();
        int confirm = JOptionPane.showConfirmDialog(this, THEME_DELETE_INFORMATION, DELETE_CONFIRMATION, JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            fdd.deleteQuestionsByTheme(title);
            fdd.deleteTheme(title);
            labelJListPanel.removeItem(selectedIndex);
            reset();
            Optional.ofNullable(onThemeChangeListener).ifPresent(OnThemeChangeListener::onThemesChanged);
            showMessage(SUCCESFULLY_DELETED_THEME_AND_QUESTIONS);
        }
    }

    /**
     * Resets the input fields and selection to default state.
     */
    private void reset() {
        labelJListPanel.getList().clearSelection();
        selectedIndex = -1;
        titleLabel.setText(NEW_THEME_LABEL);
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
