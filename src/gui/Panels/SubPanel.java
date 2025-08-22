package gui.panels;

import javax.swing.JPanel;

/**
 * A reusable wrapper for {@link JPanel}, intended to simplify layout management
 * and ensure consistency across the application.
 * <p>
 * Provides constructors for default or grid-based layouts.
 * </p>
 * 
 * <p>Extend this class when you need a lightweight panel with predefined layout structure.</p>
 * 
 * @author DejanKrstovski
 */
public class SubPanel extends JPanel {

    /**
     * Constructs an empty {@code SubPanel} with the default layout manager.
     */
    public SubPanel() {
        super();
    }

    /**
     * Constructs a {@code SubPanel} with a {@link java.awt.GridLayout} of the given size.
     *
     * @param rows the number of rows in the grid layout
     * @param cols the number of columns in the grid layout
     */
    public SubPanel(int rows, int cols) {
        setLayout(new java.awt.GridLayout(rows, cols));
    }
}
