package bussinesLogic.serialization;

import java.util.List;

import bussinesLogic.DataTransportObject;
import persistence.DataAccessObject;

/**
 * Represents a quiz question with an associated theme, a title, the question text,
 * and possible answers.
 * <p>
 * Designed to be persisted via {@link DataAccessObject}.
 * </p>
 * <p>
 * Equality and hash code are based on the {@code title} field, which should be unique per question.
 * </p>
 * 
 * @author
 */
public class Question extends DataAccessObject {

    /** The title or short description of the question */
    private String title;

    /** The full text or wording of the question */
    private String text;

    /** List of possible answers for this question */
    private List<Answer> answers;

    /** The theme or category this question belongs to */
    private Theme theme;

    /**
     * Returns the theme of this question.
     * 
     * @return the {@link Theme} associated with this question
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Sets the theme of this question.
     * 
     * @param theme the {@link Theme} to associate with this question
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    /**
     * Returns the full text of this question.
     * 
     * @return the question text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the full text of this question.
     * 
     * @param text the question text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the list of possible answers for this question.
     * 
     * @return the list of {@link Answer} objects
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * Sets the list of possible answers for this question.
     * 
     * @param answers the list of answers to assign
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /**
     * Returns the title of this question.
     * 
     * @return the question title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this question.
     * 
     * @param title the title to set; should be unique per question
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Compares this question with another object for equality.
     * <p>
     * Two {@code Question} objects are considered equal if their id's are equal.
     * </p>
     * 
     * @param obj the object to compare with
     * @return {@code true} if both are {@code Question} instances having the same id; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Question other = (Question) obj;
        return this.getId() == other.getId();
    }

    /**
     * Returns a hash code value for this question based on its unique identifier (id).
     * <p>
     * This implementation returns the value of {@code getId()} directly, 
     * so equal IDs will produce the same hash code.
     * </p>
     *
     * @return the hash code, which is the id of this question
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }

    /**
     * Returns a string representation of the question.
     * 
     * @return string containing title and text of the question
     */
    @Override
    public String toString() {
        return "Question{id='" + getId() +'\'' +  ", title='" + title + '\'' + ", text='" + text + '\'' + '}';
    }

	@Override
	public DataTransportObject toDTO() {
		// TODO Auto-generated method stub
		return null;
	}
}