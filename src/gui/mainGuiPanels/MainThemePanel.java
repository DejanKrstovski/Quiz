package gui.mainGuiPanels;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
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
import gui.guiSwing.MyButton;
import gui.guiSwing.MyLabel;
import gui.guiSwing.SubPanel;
import quizlogic.FakeDataDeliverer;
import quizlogic.Theme;

/**
 * @author DejanKrstovski
 * 
 * This panel is the main theme panel
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
	private ArrayList<Theme> allThemes;
	private List<String> themeTitles;
	private OnThemeChangeListener onThemeChangeListener;

	public void setOnThemeChangeListener(OnThemeChangeListener listener) {
		this.onThemeChangeListener = listener;
	}

	/**
	 * Super constructor and the initialize method
	 */
	public MainThemePanel(FakeDataDeliverer fdd) {
		super();
		this.fdd = fdd;
		init();
	}

	/**
	 * This method set the layout for the panel and initialize <br>
	 * the Components
	 */
	public void init() {
		initLayout();
		initComponents();
	}

	/**
	 * Initialize the layout
	 */
	private void initLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Here are initialized the main three panels from the main panel
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
	 * This panel initialize the label "Neues Thema"
	 * 
	 * @return
	 */
	private SubPanel initTitlePanel() {
		SubPanel p = new SubPanel(1, 1);
		titleLabel = new MyLabel("Neues Thema");
		titleLabel.setFont(FONT_TITLE);
		p.add(titleLabel);
		return p;
	}

	/**
	 * This method initialize the west panel of the main theme panel
	 * 
	 * @return Sub panel
	 */
	private SubPanel initWestPanel() {
		SubPanel p = new SubPanel();
		p.setLayout(new BoxLayout(p, javax.swing.BoxLayout.PAGE_AXIS));
		p.setPreferredSize(WEST_PANEL_DIMENSIONS);
		p.setMaximumSize(WEST_PANEL_DIMENSIONS);
		p.add(initTitlePanel());
		p.add(Box.createVerticalStrut(10));
		labelFieldPanel = new LabelFieldPanel("Titel", "");
		labelFieldPanel.getTextField().setEditable(true);
		p.add(labelFieldPanel);
		p.add(initTextAreaPanel());
		p.setBorder(OUTSIDE_BORDERS);
		return p;
	}

	private SubPanel initTextAreaPanel() {
		labelTextPanel = new LabelTextAreaPanel("Informationen zum Thema", "");
		return labelTextPanel;
	}

	private SubPanel initCenterPanel() {
		allThemes = new ArrayList<>(fdd.getThemes());
		themeTitles = fdd.getThemes().stream().map(Theme::getTitle).collect(Collectors.toList());
		labelJListPanel = new LabelJListPanel("Themen", themeTitles);
		labelJListPanel.clearThemeSelectionListeners();
		labelJListPanel.addThemeSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            selectedIndex = labelJListPanel.getList().getSelectedIndex();
	            if (selectedIndex >= 0 && selectedIndex < themeTitles.size()) {
	                String selectedTheme = labelJListPanel.getList().getSelectedValue();
	                System.out.println("Selected theme: " + selectedTheme);
	                System.out.println("Selected index: " + selectedIndex);
	                titleLabel.setText("Information zu Thema");
	                labelFieldPanel.setText(themeTitles.get(selectedIndex));
	                labelTextPanel.setTextInfo(allThemes.get(selectedIndex).getInfoText());
	            } else {
	                labelJListPanel.getList().clearSelection();
	            }
	        }
	    });
		return labelJListPanel;
	}

	private void initButtonsActions() {
		MyButton[] buttons = bottomPanel.getButtonsPanel().getButtons();
		buttons[0].addActionListener(e -> {
			deleteTheme();
		});
		buttons[1].addActionListener(e -> {
			saveTheme();
		});
		buttons[2].addActionListener(e -> {
			reset();
		});
	}

	private void updateThemeLists() {
		allThemes = new ArrayList<>(fdd.getThemes());
		themeTitles = allThemes.stream().map(Theme::getTitle).collect(Collectors.toList());
	}
	
	private void saveTheme() {
	    String title = labelFieldPanel.getText();
	    String info = labelTextPanel.getTextInfo();
	    if (title.isEmpty() || info.isEmpty()) {
	        bottomPanel.getMessagePanel().setMessageAreaText("Titel und Informationen dürfen nicht leer sein!");
	        return;
	    }
	    
	    if (selectedIndex == -1) {
	    		if(themeTitles.contains(title)) {
	    			bottomPanel.getMessagePanel().setMessageAreaText("Das Thema ist schon vorhanden");
	    			return;
	    		}
	        fdd.addThema(title, info);
	        bottomPanel.getMessagePanel().setMessageAreaText("Neues Thema erfolgreich gespeichert.");
	    }
	    else if (selectedIndex >= 0 && selectedIndex < fdd.getThemes().size()) {
	        Theme selectedTheme = fdd.getThemes().get(selectedIndex);
	        selectedTheme.setTitle(title);
	        selectedTheme.setText(info);
	        bottomPanel.getMessagePanel().setMessageAreaText("Thema erfolgreich aktualisiert.");
	    }
	    int previousSelectedIndex = selectedIndex;
	    updateThemeLists();
	    labelJListPanel.updateList(fdd.getThemesTitle());

	    if (previousSelectedIndex >= 0 && previousSelectedIndex < fdd.getThemes().size()) {
	        labelJListPanel.getList().setSelectedIndex(previousSelectedIndex);
	        selectedIndex = previousSelectedIndex;
	    } else {
	        selectedIndex = -1;
	    }

	    if (onThemeChangeListener != null) {
	        onThemeChangeListener.onThemesChanged();
	    }
	}


	private void deleteTheme() {
		if (selectedIndex != -1) {
			String title = labelFieldPanel.getText();

			int confirm = JOptionPane.showConfirmDialog(this,
					"Mit löschen den gewählten Thema werden alle verbundenen Fragen gelöscht. Sind sie sicher?",
					"Bestätigung für löschen", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				fdd.deleteQuestionsByTheme(title);
				fdd.deleteTheme(title);
				labelJListPanel.removeItem(selectedIndex);
				reset();
				if (onThemeChangeListener != null) {
					onThemeChangeListener.onThemesChanged();
				}

				bottomPanel.getMessagePanel()
						.setMessageAreaText("Das Thema und alle Themensfragen sind erforgleich gelöscht.");
			}
		} else {
			bottomPanel.getMessagePanel().setMessageAreaText("Bitte eine Thema auswählen!");
		}
	}

	private void reset() {
		labelJListPanel.getList().clearSelection();
		titleLabel.setText("Neues Thema");
		bottomPanel.getMessagePanel().setMessageAreaText("");
		labelTextPanel.setTextInfo("");
		labelFieldPanel.setText("");
	}

}
