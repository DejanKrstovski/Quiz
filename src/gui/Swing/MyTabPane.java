package gui.swing;

import static gui.GuiConstants.DISTANCE_BETWEEN_LABEL_TABS;
import static gui.GuiConstants.FONT_TABS;
import static gui.GuiConstants.TABS_LABEL_SIZE;
import static gui.GuiConstants.COLOR_RANDOM;
import javax.swing.JTabbedPane;

/**
 * A customized {@link JTabbedPane} with consistent default styling.
 * <p>
 * By default, tabs are placed at the top and use {@link #SCROLL_TAB_LAYOUT},
 * making it suitable for interfaces with many tabs.
 * </p>
 * <p>
 * This class also provides utility methods to customize tab labels
 * (e.g. font, size, alignment, background color, etc.)
 * for consistent visual styling across the application.
 * </p>
 */
public class MyTabPane extends JTabbedPane {

    /**
     * Constructs a {@code MyTabPane} with default styling:
     * tabs at the top and scrollable layout enabled.
     */
    public MyTabPane() {
        super(JTabbedPane.TOP);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTabPane} with the specified tab placement.
     * <p>
     * The tab layout policy is set to {@link #SCROLL_TAB_LAYOUT} by default.
     * </p>
     *
     * @param tabPlacement position of the tabs (e.g., {@link #TOP}, {@link #LEFT}, etc.)
     */
    public MyTabPane(int tabPlacement) {
        super(tabPlacement);
        applyDefaultStyling();
    }

    /**
     * Applies default styling rules for this tabbed pane.
     * <ul>
     *   <li>Scroll tab layout enabled</li>
     *   <li>Top placement as default (unless specified by constructor)</li>
     * </ul>
     */
    protected void applyDefaultStyling() {
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    /**
     * Replaces the default tab header with a customized {@link MyLabel}.
     * <p>
     * The label styling is standardized (font, alignment, color, spacing).
     * </p>
     *
     * @param tabIndex the index of the tab to customize
     * @param title    the label text for the tab
     */
    public void setCustomTabLabel(int tabIndex, String title) {
        MyLabel tabLabel = new MyLabel(title);
        tabLabel.setFont(FONT_TABS);
        tabLabel.setPreferredSize(TABS_LABEL_SIZE);
        tabLabel.setHorizontalAlignment(MyLabel.CENTER);
        tabLabel.setVerticalAlignment(MyLabel.CENTER);
        tabLabel.setOpaque(true);
        tabLabel.setBackground(COLOR_RANDOM);
        tabLabel.setBorder(DISTANCE_BETWEEN_LABEL_TABS);
        setTabComponentAt(tabIndex, tabLabel);
    }

    /**
     * Applies custom labels to all tabs using {@link #setCustomTabLabel(int, String)}.
     */
    public void applyCustomLabels() {
        for (int i = 0; i < getTabCount(); i++) {
            setCustomTabLabel(i, getTitleAt(i));
        }
    }
}
