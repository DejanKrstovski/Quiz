package quizlogic;

import java.util.ArrayList;
import java.util.List;

import persistence.DataAccessObject;

/**
 * Represents a quiz Theme, which groups related questions.
 * <p>
 * Each Theme has a unique identifier (inherited from {@link DataAccessObject}), a title,
 * descriptive information, and a list of associated {@link Question}s.
 * </p>
 * <p>
 * The equality and hash code computations rely on the unique ID,
 * assuming it is immutable and unique across instances.
 * </p>
 * 
 * <p>This class lazily initializes the internal question list to avoid null references.</p>
 * 
 * @author 
 */
public class Theme extends DataAccessObject {

    /** The title of this theme. */
    private String themeTitle;

    /** Additional information or description for this theme. */
    private String themeInfo;

    /** The list of questions associated with this theme. Lazily initialized. */
    private List<Question> questions;

    /**
     * Returns the title of this theme.
     * 
     * @return the theme title
     */
    public String getTitle() {
        return themeTitle;
    }

    /**
     * Sets the title of this theme.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.themeTitle = title;
    }

    /**
     * Returns additional information or description about this theme.
     * 
     * @return the info string
     */
    public String getThemeInfo() {
        return themeInfo;
    }

    /**
     * Sets additional information or description for this theme.
     * 
     * @param info the info string to set
     */
    public void setThemeInfo(String info) {
        this.themeInfo = info;
    }

    /**
     * Returns the list of questions associated with this theme.
     * <p>
     * This list is lazily initialized and never returns null.
     * </p>
     * 
     * @return a modifiable list of {@link Question} objects belonging to this theme
     */
    public List<Question> getQuestions() {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        return questions;
    }

    /**
     * Sets the list of questions for this theme.
     * 
     * @param questions the list of questions to assign; may be null
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Checks equality between this and another object.
     * <p>
     * Equality is based solely on the unique {@code id} inherited from {@link DataAccessObject}.
     * Two themes are equal if they share the same ID.
     * </p>
     * 
     * @param obj the object to compare with
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Theme)) return false;

        Theme other = (Theme) obj;

        return this.getId() == other.getId();
    }

    /**
     * Returns hash code based on the unique ID.
     * 
     * @return the hash code value for this theme
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }

    /**
     * Returns a string representation of the theme.
     * Useful for debugging and logging.
     * 
     * @return a string describing this theme including id, title, and info
     */
    @Override
    public String toString() {
        return "Theme{" +
                "id=" + getId() +
                ", title='" + themeTitle + '\'' +
                ", info='" + themeInfo + '\'' +
                ", questionsCount=" + (questions == null ? 0 : questions.size()) +
                '}';
    }
}
