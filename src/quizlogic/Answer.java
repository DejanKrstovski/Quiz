package quizlogic;

public class Answer extends QObject {
	private String text;
	private Question question;
	private boolean correct;
	
	public Answer(String text, boolean isCorrect) {
		this.text = text;
		this.correct = isCorrect;
	}
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
