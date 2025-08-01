package quizlogic;

import java.util.ArrayList;

public class Theme extends QObject {
	
	private String title;
	private String infoText;
	
	private ArrayList<Question> questions = new ArrayList<>();
	
	public void addQuestion(Question question) {
		if (questions == null) {
			questions = new ArrayList<>();
		}
		questions.add(question);
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getInfoText() {
		return infoText;
	}
	
	public void setText(String text) {
		this.infoText = text;
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder(); 
		info.append("Info zu Instanz der Klasse Thema:\n");
		info.append("Title: " + title);
		info.append("\nText: " + infoText);
		info.append("\n\nFragen\n");
		for (Question question : questions) {
			
			info.append("Title: " + question.getTitle() + "\n");
			info.append("Text: " + question.getText() + "\n");

			for (Answer a : question.getAnswers()) {
				info.append("Mögliche Antwort: " + a.getText() + " Is Correct: " +  a.isCorrect() + "\n");
				
			}
		}
		return info.toString();
	}
}
