package bussinesLogic.datenBank;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import helpers.QuizDataInterfaceDB;
import persistence.mariaDB.DBManager;

public class QuizDBDataManager implements QuizDataInterfaceDB {

    private static QuizDBDataManager instance;
    private DBManager dbManager = DBManager.getInstance();

    private QuizDBDataManager() {}

    public static QuizDBDataManager getInstance() {
        if (instance == null) {
            instance = new QuizDBDataManager();
        }
        return instance;
    }

    @Override
    public List<ThemeDTO> getAllThemes() {
        return dbManager.getAllThemes();
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return dbManager.getAllQuestions();
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
        List<AnswerDTO> list = dbManager.getAllAnswers().stream()
            .filter(a -> a.getQuestionId() == question.getId())
            .toList(); 
        List<AnswerDTO> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled, new SecureRandom());
        return shuffled;
    }

    @Override
    public String saveTheme(ThemeDTO theme) {
        return dbManager.saveTheme(theme);
    }

    @Override
    public String deleteTheme(ThemeDTO theme) {
        return dbManager.deleteTheme(theme);
    }

    @Override
    public String saveQuestion(QuestionDTO q) {
        return dbManager.saveQuestion(q);
    }

    @Override
    public String deleteQuestion(QuestionDTO question) {
        return dbManager.deleteQuestion(question);
    }
    
    public String savePlayerAnswer(PlayerAnswerDTO answer) {
        return dbManager.savePlayerAnswer(answer);
    }
}