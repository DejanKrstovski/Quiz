package persistence;

import java.util.ArrayList;
import java.util.List;

import quizlogic.Answer;
import quizlogic.Question;
import quizlogic.Theme;

/**
 * Interface defining the data access operations for the quiz application.
 * <p>
 * Provides methods to retrieve, save, and delete quiz themes, questions, and answers.
 * The interface abstracts the data source and allows flexible implementation such as 
 * file-based, database, or in-memory storage.
 * </p>
 * 
 * @author 
 */
public interface QuizDataInterface {

    /**
     * Retrieves a random {@link Question} from all available questions.
     * 
     * @return a randomly selected {@code Question}, or {@code null} if no questions exist
     */
    public Question getRandomQuestion();

    /**
     * Retrieves a list of all available quiz {@link Theme}s.
     * 
     * @return an {@link ArrayList} containing all themes; empty list if none found
     */
    public void getAllThemes();

    /**
     * Retrieves all {@link Question}s associated with the specified {@link Theme}.
     * 
     * @param theme the theme for which to fetch questions; must not be null
     * @return an {@link ArrayList} of questions belonging to the given theme; empty list if none
     */
    public List<Question> getQuestionsForTheme(Theme theme);

    /**
     * Retrieves all {@link Answer}s associated with the specified {@link Question}.
     * 
     * @param q the question for which to fetch answers; must not be null
     * @return an {@link ArrayList} of answers belonging to the given question; empty list if none
     */
    public List<Answer> getAnswersForQuestion(Question q);

    /**
     * Saves or updates the specified {@link Theme} to the data store.
     * <p>
     * This operation may create a new entry or overwrite an existing one identified by the theme's ID.
     * </p>
     * 
     * @param theme the theme to save; must not be null
     * @return a status message indicating success or failure of the operation
     */
    public String saveTheme(Theme theme);

    /**
     * Deletes the specified {@link Theme} and all associated questions and answers from the data store.
     * 
     * @param theme the theme to delete; must not be null
     * @return a status message indicating success or failure of the deletion
     */
    public String deleteTheme(Theme theme);

    /**
     * Saves or updates the specified {@link Question} to the data store.
     * <p>
     * This operation may create a new entry or overwrite an existing one identified by the question's ID.
     * </p>
     * 
     * @param q the question to save; must not be null
     * @return a status message indicating success or failure of the operation
     */
    public String saveQuestion(Question q);

    /**
     * Deletes the {@link Question} identified by the given ID from the data store.
     * <p>
     * Note that answers associated with this question should also be deleted accordingly.
     * </p>
     * 
     * @param id the unique identifier of the question to delete
     * @return a status message indicating success or failure of the deletion
     */
    public String deleteQuestion(int id);
}
