package helpers;

import java.util.List;

import bussinesLogic.datenBank.AnswerDTO;
import bussinesLogic.datenBank.QuestionDTO;
import bussinesLogic.datenBank.ThemeDTO;

public interface QuizDataInterfaceDB {


	public QuestionDTO getRandomQuestion();
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme);

	public List<ThemeDTO> getAllThemes();
	public List<QuestionDTO> getAllQuestions();

	public List<QuestionDTO> getQuestionsFor(ThemeDTO theme);

	public List<AnswerDTO> getAnswersFor(QuestionDTO question);
	
	
	public String saveTheme(ThemeDTO theme);
	public String deleteTheme(ThemeDTO theme);
	
	public String saveQuestion(QuestionDTO question);
	public String deleteQuestion(QuestionDTO question);

}