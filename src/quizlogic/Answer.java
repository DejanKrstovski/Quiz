package quizlogic;

import persistence.DataAccessObject;

/**
 * Represents an answer option for a quiz question.
 * <p>
 * Each answer has textual content and a boolean flag indicating whether it is the correct answer.
 * </p>
 * <p>
 * This class extends {@link DataAccessObject} for persistence integration.
 * </p>
 * 
 * <p><b>Note:</b> No validation is performed on the text; it can be null or empty.</p>
 * 
 * @author  
 */
public class Answer extends DataAccessObject {

    /** The text content of the answer option. */
    private String text;

    /** Indicates if the answer is the correct choice for its question. */
    private boolean correct;

    /**
     * Returns the text content of this answer.
     * 
     * @return the answer text, may be null
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content of this answer.
     * 
     * @param text the answer text to set; may be null
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Indicates whether this answer is marked as correct.
     * 
     * @return {@code true} if correct, {@code false} otherwise
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * Sets whether this answer is correct.
     * 
     * @param correct {@code true} marks the answer as correct; {@code false} otherwise
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    /**
     * Returns a string representation of this answer.
     * Useful for debugging and logging.
     * 
     * @return a string describing this answer
     */
    @Override
    public String toString() {
        return "Answer{text='" + text + '\'' + ", correct=" + correct + '}';
    }

    /**
     * Compares this answer to another object for equality.
     * Two answers are equal if they have the same text and correctness flag.
     * 
     * @param obj the object to compare with
     * @return {@code true} if equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Answer)) return false;

        Answer other = (Answer) obj;
        if (correct != other.correct) return false;
        if (text == null) return other.text == null;
        return text.equals(other.text);
    }

    /**
     * Returns a hash code for this answer.
     * 
     * @return a hash code derived from text and correctness
     */
    @Override
    public int hashCode() {
        int result = (text == null) ? 0 : text.hashCode();
        result = 31 * result + (correct ? 1 : 0);
        return result;
    }
}
