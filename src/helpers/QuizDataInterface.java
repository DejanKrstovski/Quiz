package helpers;

import java.util.List;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;

public interface QuizDataInterface {

	public QuestionDTO getRandomQuestion();
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme);

	public List<ThemeDTO> getAllThemes();
	public List<QuestionDTO> getAllQuestions();
	public List<AnswerDTO> getAllAnswers();
	public List<PlayerAnswerDTO> getAllPlayerAnswers();
	public List<QuestionDTO> getQuestionsFor(ThemeDTO theme);
	public List<AnswerDTO> getAnswersFor(QuestionDTO question);
	

	public String savePlayerAnswer(PlayerAnswerDTO answer);
	
	public String saveTheme(ThemeDTO theme);
	public String deleteTheme(ThemeDTO theme);
	
	public String saveQuestion(QuestionDTO question);
	public String deleteQuestion(QuestionDTO question);
}
