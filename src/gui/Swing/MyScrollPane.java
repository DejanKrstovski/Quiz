package gui.swing;

import java.awt.Component;
import javax.swing.JScrollPane;

/**
 * A customized {@link JScrollPane} with consistent default styling.
 * <p>
 * This class provides a central place to apply application-specific styles
 * and behavior for scroll panes. Although it currently applies only minimal
 * styling, it ensures consistency and acts as a base for future improvements,
 * such as custom scroll bars or theme integration.
 * </p>
 */
public class MyScrollPane extends JScrollPane {

    /**
     * Constructs an empty {@code MyScrollPane} with default styling.
     */
    public MyScrollPane() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyScrollPane} that displays the specified component,
     * with default styling applied.
     *
     * @param view the component to display within the scroll pane
     */
    public MyScrollPane(Component view) {
        super(view);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyScrollPane} with the specified component and scrollbar
     * policies, applying default styling.
     *
     * @param view      the component to display
     * @param vsbPolicy vertical scrollbar policy
     * @param hsbPolicy horizontal scrollbar policy
     */
    public MyScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        applyDefaultStyling();
    }

    /**
     * Applies the default styling for this scroll pane.
     * <ul>
     *   <li>Sets the background to be non-opaque (transparent look).</li>
     *   <li>Removes the default border for a cleaner appearance.</li>
     * </ul>
     * <p>
     * Subclasses may override this method to apply additional customizations,
     * such as custom scroll bar UI or theming.
     * </p>
     */
    protected void applyDefaultStyling() {
        setOpaque(false);
        setBorder(null);
    }
}
