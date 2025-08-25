package helpers;

/**
 * Represents a question item with an identifier and a title,
 * used for displaying questions in GUI components such as lists.
 * <p>
 * Typically used to encapsulate basic information about a question,
 * enabling easy mapping between the displayed title and its unique ID.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class QuestionListItem {
	
    /** Unique identifier of the question. */
    private int id;

    /** Title or text of the question. */
    private String title;

    /**
     * Constructs a {@code QuestionListItem} with the given ID and title.
     *
     * @param id the unique identifier of the question
     * @param title the display title or text of the question
     */
    public QuestionListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * Returns the unique identifier of the question.
     *
     * @return the question's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the question.
     *
     * @return the question's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the string representation of this item,
     * which is the title of the question.
     * <p>
     * This method is especially useful when the item is displayed
     * in UI components like lists or combo boxes.
     * </p>
     *
     * @return the question title as string
     */
    @Override
    public String toString() {
        return title;
    }
}
