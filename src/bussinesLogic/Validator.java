package bussinesLogic;

import java.util.List;

/**
 * Utility class to validate Quiz domain model objects such as {@link ThemeDTO} and
 * {@link QuestionDTO}.
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
public class Validator {

	static ErrorHandler errorHandler = ErrorHandler.getInstance();

	/**
	 * Validates the specified {@link ThemeDTO}.
	 *
	 * Criteria:
	 * <ul>
	 * <li>Theme is not null</li>
	 * <li>Title is not null or empty</li>
	 * <li>All contained questions are valid (see {@link #validateQuestion})</li>
	 * </ul>
	 * 
	 * @param theme the theme to validate, may be null
	 * @return {@code true} if the theme and all its questions are valid;
	 *         {@code false} otherwise
	 */
	public static boolean validateTheme(ThemeDTO theme) {

		if (theme == null || theme.getTitle() == null || theme.getTitle().trim().isEmpty()) {
			errorHandler.setError(ValidationMessages.THEME_NULL_OR_INVALID_TITLE);
			return false;
		}

//        List<QuestionDTO> questions = QuizDBDataManager.getInstance().getQuestionsFor(theme);
//        if (questions != null) {
//            for (QuestionDTO question : questions) {
//                if (!validateQuestion(question)) {
//                    return false;
//                }
//            }
//        }

		return true;
	}

	/**
	 * Validates a single {@link QuestionDTO}.
	 * <p>
	 * Ensures that the question is not null and has a non-empty title and question
	 * text. Checks that at least two answers have non-empty text and that at least
	 * one answer is marked correct.
	 * </p>
	 * 
	 * @param question the question to validate, may be null
	 * @return {@code true} if the question is valid; {@code false} otherwise
	 */
	public static boolean validateQuestion(QuestionDTO question) {
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

		return true;
	}

	/**
	 * Validates the selected index and theme.
	 * <p>
	 * Checks whether the given index is a valid selection (1 or greater) and
	 * whether the specified theme is valid.
	 * </p>
	 * 
	 * @param index the selection index, expected to be 1-based
	 * @param theme the theme to validate
	 * @return {@code true} if the index is valid and the theme passes validation;
	 *         {@code false} otherwise
	 */
	public static boolean validateIndexAndTheme(int index, ThemeDTO theme) {
		ErrorHandler errorHandler = ErrorHandler.getInstance();

		if (index < 1) {
			errorHandler.setError(ValidationMessages.NO_THEME_SELECTED);
			return false;
		}
		return validateTheme(theme);
	}

	public static boolean validateAnswers(List<AnswerDTO> answers) {
	    if (answers == null || answers.size() < 2) {
	        errorHandler.setError(ValidationMessages.QUESTION_LESS_THAN_TWO_ANSWERS);
	        return false;
	    }

	    int emptyAnswerCount = 0;
	    boolean hasCorrectAnswer = false;

	    for (AnswerDTO answer : answers) {
	        String text = answer.getText() != null ? answer.getText().trim() : "";

	        // Prüfen: richtige Antwort aber kein Text
	        if (text.isEmpty() && answer.isCorrect()) {
	            errorHandler.setError(ValidationMessages.EMPTY_TRUE_ANSWER);
	            return false;
	        }

	        // Zähle leere Antworten
	        if (text.isEmpty()) {
	            emptyAnswerCount++;
	        }

	        // Mindestens eine richtige Antwort merken
	        if (answer.isCorrect()) {
	            hasCorrectAnswer = true;
	        }
	    }

	    // Prüfen: mindestens 2 ausgefüllte Antworten
	    if (answers.size() - emptyAnswerCount < 2) {
	        errorHandler.setError(ValidationMessages.QUESTION_LESS_THAN_TWO_ANSWERS);
	        return false;
	    }

	    // Prüfen: mindestens eine richtige Antwort
	    if (!hasCorrectAnswer) {
	        errorHandler.setError(ValidationMessages.QUESTION_NO_CORRECT_ANSWER);
	        return false;
	    }

	    return true;
	}

}
