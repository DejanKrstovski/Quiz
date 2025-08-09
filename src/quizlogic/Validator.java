package quizlogic;

import java.util.List;

/**
 * Utility class to validate Quiz domain model objects such as {@link Theme} and {@link Question}.
 * <p>
 * Provides static methods to check basic validity rules, e.g. non-null fields,
 * minimum number of answers, correct answer requirement, and more.
 * <p>
 * Validation failures are reported via the {@link ErrorHandler} singleton,
 * where an error message is set for retrieval and display.
 * </p>
 * <p>
 * This class does not throw exceptions upon validation failure but signals
 * problems via error messages and boolean return values.
 * </p>
 * 
 * @author DejanKrstovski
 */
public class Validator{

	/**
	 * Validates the specified {@link Theme}.
	 *
	 * Criteria:
	 * <ul>
	 *   <li>Theme is not null</li>
	 *   <li>Title is not null or empty</li>
	 *   <li>All contained questions are valid (see {@link #validateQuestion})</li>
	 * </ul>
	 * @param theme the theme to validate, may be null
	 * @return {@code true} if the theme and all its questions are valid; {@code false} otherwise
	 */
    public static boolean validateTheme(Theme theme) {
        ErrorHandler errorHandler = ErrorHandler.getInstance();

        if (theme == null || theme.getTitle() == null || theme.getTitle().trim().isEmpty()) {
            errorHandler.setError(ValidationMessages.THEME_NULL_OR_INVALID_TITLE);
            return false;
        }

        List<Question> questions = theme.getQuestions();
        if (questions != null) {
            for (Question question : questions) {
                if (!validateQuestion(question)) {
                    // Error message is set inside validateQuestion()
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Validates a single {@link Question}.
     * <p>
     * Ensures that the question is not null and has a non-empty title and question text.
     * Checks that at least two answers have non-empty text and that at least one answer is marked correct.
     * </p>
     * 
     * @param question the question to validate, may be null
     * @return {@code true} if the question is valid; {@code false} otherwise
     */
    public static boolean validateQuestion(Question question) {
        ErrorHandler errorHandler = ErrorHandler.getInstance();

        if (question == null) {
            errorHandler.setError(ValidationMessages.QUESTION_NULL);
            return false;
        }

        if (question.getTitle() == null || question.getTitle().trim().isEmpty()) {
            errorHandler.setError(ValidationMessages.QUESTION_TITLE_MISSING);
            return false;
        }

        if (question.getText() == null || question.getText().trim().isEmpty()) {
            errorHandler.setError(ValidationMessages.QUESTION_TEXT_MISSING);
            return false;
        }

        List<Answer> answers = question.getAnswers();
        if (answers == null || answers.isEmpty()) {
            errorHandler.setError(ValidationMessages.QUESTION_LESS_THAN_TWO_ANSWERS);
            return false;
        }

        int emptyAnswerCount = 0;
        for (Answer answer : answers) {
            if (answer.getText() == null || answer.getText().trim().isEmpty()) {
                emptyAnswerCount++;
            }
        }

        if (emptyAnswerCount > 2) { // More than 2 empty answers means less than 2 valid answers
            errorHandler.setError(ValidationMessages.QUESTION_LESS_THAN_TWO_ANSWERS);
            return false;
        }

        boolean hasCorrectAnswer = answers.stream().anyMatch(Answer::isCorrect);
        if (!hasCorrectAnswer) {
            errorHandler.setError(ValidationMessages.QUESTION_NO_CORRECT_ANSWER);
            return false;
        }

        return true;
    }

    /**
     * Validates the selected index and theme.
     * <p>
     * Checks whether the given index is a valid selection (1 or greater)
     * and whether the specified theme is valid.
     * </p>
     * 
     * @param index the selection index, expected to be 1-based
     * @param theme the theme to validate
     * @return {@code true} if the index is valid and the theme passes validation; {@code false} otherwise
     */
    public static boolean validateIndexAndTheme(int index, Theme theme) {
        ErrorHandler errorHandler = ErrorHandler.getInstance();

        if (index < 1) {
            errorHandler.setError(ValidationMessages.NO_THEME_SELECTED);
            return false;
        }
        return validateTheme(theme);
    }
}
