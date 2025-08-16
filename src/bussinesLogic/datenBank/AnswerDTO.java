package bussinesLogic.datenBank;

import bussinesLogic.DataTransportObject;
import persistence.DataAccessObject;

/**
 * Represents a possible answer option for a quiz question.
 * <p>
 * Each {@code Answer} has a textual label and a flag indicating whether it is the correct choice.
 * This class is a simple data holder and performs no validation; the text may be {@code null} or empty.
 * Validation should be handled externally (e.g., by a validator utility).
 * </p>
 * <p>
 * Extends {@link DataAccessObject} to support persistence operations.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>
 * Answer answer = new Answer();
 * answer.setText("42");
 * answer.setCorrect(true);
 * </pre>
 *
 * @author DejanKrstovski
 */
public class AnswerDTO extends DataTransportObject {

	
    /** The text label of this answer; may be {@code null} or empty. */
    private String text;

    /** Whether this answer is the correct choice for its related question. */
    private boolean isCorrect;
    
    private int questionId;

    public AnswerDTO() {
    	super();
    }
    
    public AnswerDTO(int id) {
    	super(id);
    }
    
    /**
     * Returns the text label of this answer.
     *
     * @return the answer text; may be {@code null} if unset
     */
    public String getText() {
        return text;
    }
    
    /**
     * Sets the text label of this answer.
     *
     * @param text the answer text to set; may be {@code null} to indicate no text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns whether this answer is marked as correct.
     *
     * @return {@code true} if this answer is correct; {@code false} otherwise
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     * Marks this answer as correct or incorrect.
     *
     * @param correct {@code true} to mark as correct; {@code false} to mark as incorrect
     */
    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }

    public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
     * Returns a string representation of this answer, including its text and correctness flag.
     * <p>
     * Intended for debugging and logging, not for display in a user interface.
     * </p>
     *
     * @return a string representation of this answer
     */
    @Override
    public String toString() {
        return "Answer{id='" + getId() + ", text=" + text + '\'' + ", correct=" + isCorrect + ", questionId=" + questionId +'}';
    }

    /**
     * Compares this answer with another object for equality.
     * Two answers are considered equal if they have the same text (including both being {@code null})
     * and the same correctness flag.
     * <p>
     * <b>Note:</b> Any persistence identifier defined in {@link DataAccessObject} is not considered
     * in this equality check.
     * </p>
     *
     * @param obj the object to compare to
     * @return {@code true} if the objects represent the same answer; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AnswerDTO)) return false;

        AnswerDTO other = (AnswerDTO) obj;
        if (isCorrect != other.isCorrect) return false;
        if (text == null) return other.text == null;
        return text.equals(other.text);
    }

    /**
     * Returns a hash code for this answer based on its text and correctness flag.
     * <p>
     * <b>Note:</b> Any persistence identifier in {@link DataAccessObject} is not included.
     * </p>
     *
     * @return the hash code for this answer
     */
    @Override
    public int hashCode() {
        int result = (text == null) ? 0 : text.hashCode();
        result = 31 * result + (isCorrect ? 1 : 0);
        return result;
    }

}
