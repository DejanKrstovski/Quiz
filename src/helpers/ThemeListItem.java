package helpers;

/**
 * Represents a theme item with an identifier and a title,
 * used for displaying themes in GUI components such as lists.
 */
public class ThemeListItem {
    private final int id;
    private final String title;

    public ThemeListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }

    @Override
    public String toString() { return title; } // JList will display title.
}
