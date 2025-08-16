package bussinesLogic.datenBank;

import java.util.List;

import bussinesLogic.DataTransportObject;
import persistence.DataAccessObject;

/**
 * Represents a thematic group for quiz questions.
 * <p>
 * Each Theme instance is identified uniquely by an integer ID inherited from {@link DataAccessObject}.
 * It has a title, descriptive info, and a modifiable list of associated {@link QuestionDTO}s.
 * </p>
 * <p>
 * The questions list is lazily initialized to guarantee non-null return from {@link #getQuestions()}.
 * If {@link #setQuestions(List)} is called with {@code null}, the list is reset to an empty modifiable list.
 * </p>
 * <p>
 * Equality and hash code are based solely on the ID.
 * Instances with unset (default) IDs may not behave as expected in collections.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class ThemeDTO extends DataTransportObject {

    private String title;
    private String text;

    /**
     * Returns the title of this theme.
     *
     * @return the theme title, can be {@code null}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the theme's title.
     *
     * @param title the title to set, can be {@code null}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns extra descriptive information for this theme.
     *
     * @return the theme info, can be {@code null}
     */
    public String getText() {
        return text;
    }

    /**
     * Sets extra descriptive information for this theme.
     *
     * @param info the information string to set, can be {@code null}
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Checks equality with another object.
     * <p>
     * Two Themes are considered equal if they have the same unique ID.
     * </p>
     *
     * @param obj the object to compare to
     * @return {@code true} if the IDs match, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ThemeDTO)) return false;
        ThemeDTO other = (ThemeDTO) obj;
        return this.getId() == other.getId();
    }

    /**
     * Computes the hash code for this Theme based on its unique ID.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }

    /**
     * Returns a string representation containing the ID, title, info, and 
     * question count of this theme. Useful for debugging and logging.
     *
     * @return a string summary of this Theme
     */
    @Override
    public String toString() {
        return "Theme{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", info='" + text + '\'' +
                '}';
    }
}
