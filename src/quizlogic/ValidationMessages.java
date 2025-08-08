package quizlogic;

/**
 * Contains all validation error and info messages used in the validation logic.
 * 
 * @author DejanKrstovski
 */
public final class ValidationMessages {

    private ValidationMessages() {}

    public static final String THEME_NULL_OR_INVALID_TITLE = "Thema ist leer oder ungültig.";
    public static final String QUESTION_NULL = "Frage ist null.";
    public static final String QUESTION_TITLE_MISSING = "Titel der Frage fehlt.";
    public static final String QUESTION_TEXT_MISSING = "Fragetext fehlt.";
    public static final String QUESTION_LESS_THAN_TWO_ANSWERS = "Mindestens zwei Antworten müssen ausgefüllt sein.";
    public static final String QUESTION_NO_CORRECT_ANSWER = "Mindestens eine Antwort muss als richtig markiert sein.";
    public static final String NO_THEME_SELECTED = "Bitte ein Thema auswählen!";

}
