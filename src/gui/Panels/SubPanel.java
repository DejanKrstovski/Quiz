package gui.panels;

import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * A lightweight base panel that extends {@link JPanel} to provide
 * small conveniences and consistent usage patterns across the application.
 * <p>
 * Subclasses can use this as a common foundation for UI panels, while callers
 * can choose a simple grid layout via convenience constructors.
 * </p>
 *
 * <p>
 * Usage notes:
 * <ul>
 *   <li>Use the no-arg constructor and set a layout manager explicitly if your panel
 *       requires a specific arrangement (e.g., {@code BoxLayout}, {@code BorderLayout}).</li>
 *   <li>Use the grid constructors for simple row/column arrangements without additional boilerplate.</li>
 *   <li>Override {@link #applyDefaultStyling()} in subclasses to add panel-specific styling
 *       (e.g., borders or background), and call it from your subclass constructor.</li>
 * </ul>
 * </p>
 * 
 * @author DejanKrstovski
 */
public class SubPanel extends JPanel {

    /**
     * Constructs an empty {@code SubPanel} with the default layout manager
     * (inherited from {@link JPanel}, typically {@code FlowLayout}).
     */
    public SubPanel() {
        super();
    }

    /**
     * Constructs a {@code SubPanel} with a {@link GridLayout} of the given size.
     *
     * @param rows the number of rows (non-negative)
     * @param cols the number of columns (non-negative)
     * @throws IllegalArgumentException if both {@code rows} and {@code cols} are zero
     */
    public SubPanel(int rows, int cols) {
        super();
        setLayout(new GridLayout(rows, cols));
    }

    /**
     * Constructs a {@code SubPanel} with a {@link GridLayout} of the given size
     * and the specified horizontal and vertical gaps.
     *
     * @param rows the number of rows (non-negative)
     * @param cols the number of columns (non-negative)
     * @param hgap the horizontal gap (pixels) between components
     * @param vgap the vertical gap (pixels) between components
     * @throws IllegalArgumentException if both {@code rows} and {@code cols} are zero
     */
    public SubPanel(int rows, int cols, int hgap, int vgap) {
        super();
        setLayout(new GridLayout(rows, cols, hgap, vgap));
    }

    /**
     * Hook method for subclasses to apply default styling (e.g., padding, background).
     * <p>
     * This method is intentionally empty in the base class to avoid imposing
     * styling on all panels. Subclasses can override and call it from their
     * constructors once they have configured the layout and components.
     * </p>
     */
    protected void applyDefaultStyling() {
    }
}
