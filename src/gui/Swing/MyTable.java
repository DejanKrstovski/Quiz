package gui.swing;

import static gui.GuiConstants.ROW_HEIGHT;

import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A customized {@link JTable} with consistent default styling.
 * <p>
 * This class standardizes the appearance of all tables across the application
 * by applying shared UI settings such as row height, selection behavior,
 * and focus properties.
 * </p>
 */
public class MyTable extends JTable {

    /**
     * Constructs an empty {@code MyTable} with default styling.
     */
    public MyTable() {
        super();
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTable} with the given row/column count.
     *
     * @param numRows    number of rows
     * @param numColumns number of columns
     */
    public MyTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTable} with the given row data and column names.
     *
     * @param rowData     table row data
     * @param columnNames table column headers
     */
    public MyTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTable} with the given model, column model, and selection model.
     *
     * @param dm table model
     * @param cm column model
     * @param sm selection model
     */
    public MyTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTable} with the given model and column model.
     *
     * @param dm table model
     * @param cm column model
     */
    public MyTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        applyDefaultStyling();
    }

    /**
     * Constructs a {@code MyTable} with the given model.
     *
     * @param dm table model
     */
    public MyTable(TableModel dm) {
        super(dm);
        applyDefaultStyling();
    }

    /**
     * Applies the default UI styling for this table.
     * <ul>
     *   <li>Row height set to {@link gui.GuiConstants#ROW_HEIGHT}</li>
     *   <li>Focus disabled ({@code setFocusable(false)})</li>
     *   <li>Row selection disabled</li>
     * </ul>
     * Subclasses may override this to apply additional styling rules.
     */
    protected void applyDefaultStyling() {
        setRowHeight(ROW_HEIGHT);
        setFocusable(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Utility method to center text in specified columns.
     *
     * @param columns the indexes of the columns to center
     */
    public void centerColumns(int... columns) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        Arrays.stream(columns).forEach(c -> getColumnModel().getColumn(c).setCellRenderer(centerRenderer));
    }

    /**
     * Utility method to hide the table header (used e.g. in summary tables).
     */
    public void hideHeader() {
        setTableHeader(null);
    }
}
