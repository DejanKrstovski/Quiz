package gui.guiSwing;

import javax.swing.JTabbedPane;

/**
 * A customized {@link JTabbedPane} with default settings used throughout the
 * application.
 * <p>
 * By default, the tab placement is at the top and the tab layout is scrollable,
 * making it suitable for interfaces with many tabs.
 * </p>
 * This class allows for consistent styling and easier customization of all
 * tabbed panes.
 */
public class MyTabPane extends JTabbedPane {

	/**
	 * Constructs a {@code MyTabPane} with tabs placed at the top and scroll tab
	 * layout policy enabled.
	 */
	public MyTabPane() {
		super(JTabbedPane.TOP);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Constructs a {@code MyTabPane} with the specified tab placement.
	 * <p>
	 * The tab layout policy is set to {@link #SCROLL_TAB_LAYOUT} by default.
	 * </p>
	 *
	 * @param tabPlacement the placement for the tabs (e.g. {@link #TOP},
	 *                     {@link #LEFT}, etc.)
	 */
	public MyTabPane(int tabPlacement) {
		super(tabPlacement);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
}
