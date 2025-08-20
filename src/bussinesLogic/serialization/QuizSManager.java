package bussinesLogic.serialization;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import gui.GuiConstants;
import helpers.QuizDataInterface;
import persistence.serialization.SManager;

/**
 * Singleton class that manages quiz data persistence using serialization.
 * <p>
 * This class implements {@link QuizDataInterface} to provide data access
 * methods for retrieving, saving, and deleting quiz themes, questions, and
 * answers. Themes are stored as serialized objects in individual files within a
 * dedicated folder.
 * </p>
 * <p>
 * The folder path used for data storage is currently hardcoded and should
 * ideally be configurable.
 * </p>
 * <p>
 * Uses constants from {@link GuiConstants} for consistent messages and
 * configuration.
 * </p>
 * 
 * <p>
 * <b>Note:</b> Some methods may require further implementation, especially
 * {@link #saveQuestion(Question)}.
 * </p>
 * 
 * @author
 */
public class QuizSManager implements QuizDataInterface{

	/** Singleton instance of QuizDataManager. */
	private static QuizSManager instance = null;
	private SManager sManager = SManager.getInstance();

	/**
	 * Returns the singleton instance of {@code QuizDataManager}.
	 * 
	 * @return the singleton instance
	 */
	public static synchronized QuizSManager getInstance() {
		if (instance == null) {
			instance = new QuizSManager();
		}
		return instance;
	}

    @Override
    public List<ThemeDTO> getAllThemes() {
        return sManager.getAllThemes();
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return sManager.getAllQuestions();
    }
    
	@Override
	public List<AnswerDTO> getAllAnswers() {
		return sManager.getAllAnswers();
	}
	
	@Override
    public List<PlayerAnswerDTO> getAllPlayerAnswers() {
    	return sManager.getAllPlayerAnswers();
    }

    @Override
    public List<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
        return getAllQuestions().stream()
                .filter(q -> q.getThemeId() == theme.getId())
                .toList();
    }

    @Override
    public QuestionDTO getRandomQuestion() {
        List<QuestionDTO> all = getAllQuestions();
        return all.get(new Random().nextInt(all.size()));
    }

    @Override
    public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
        List<QuestionDTO> themeQuestions = getQuestionsFor(theme);
        return themeQuestions.get(new Random().nextInt(themeQuestions.size()));
    }

    @Override
    public List<AnswerDTO> getAnswersFor(QuestionDTO question) {
        List<AnswerDTO> list = sManager.getAllAnswers().stream()
            .filter(a -> a.getQuestionId() == question.getId())
            .toList(); 
        List<AnswerDTO> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled, new SecureRandom());
        return shuffled;
    }

    @Override
    public String saveTheme(ThemeDTO theme) {
        return sManager.saveTheme(theme);
    }

    @Override
    public String deleteTheme(ThemeDTO theme) {
        return sManager.deleteTheme(theme);
    }

    @Override
    public String saveQuestion(QuestionDTO q) {
        return sManager.saveQuestion(q);
    }

    @Override
    public String deleteQuestion(QuestionDTO question) {
        return sManager.deleteQuestion(question);
    }
    
    public String savePlayerAnswer(PlayerAnswerDTO answer) {
        return sManager.savePlayerAnswer(answer);
    }

	public String saveAnswer(AnswerDTO answer) {
		return sManager.saveAnswer(answer);
	}
}