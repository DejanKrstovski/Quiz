package quizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FakeDataDeliverer {

	public ArrayList<Theme> themes;
	public HashMap<Integer, String> allThemes = new HashMap<>();
	public HashMap<Integer, Question> allQuestions = new HashMap<>();

	public FakeDataDeliverer() {
		super();
		createThemes(5);
	}

	public HashMap<Integer, String> getAllThemesTitles() {
		for (int i = 0; i < themes.size(); i++) {
			allThemes.put(i, themes.get(i).getTitle());
		}
		return allThemes;
	}

	public HashMap<Integer, Question> getAllQuestions() {
		allQuestions.clear();
		int id = 0;
		for (Theme t : themes) {
			if (t.getQuestions() != null) {
				for (Question q : t.getQuestions()) {
					allQuestions.put(id++, q);
				}
			}
		}
		return allQuestions;
	}

	public List<Question> getQuestionsByTheme(String themeTitle) {
		if (themeTitle == null || themeTitle.isEmpty() || themeTitle.equals("Alle Themen")) {
			return new ArrayList<>(allQuestions.values());
		}
		return allQuestions.values().stream().filter(q -> q.getTheme().getTitle().equals(themeTitle))
				.collect(Collectors.toList());
	}

	public HashMap<Integer, String> getAllQuestionTitle() {
		HashMap<Integer, String> titles = new HashMap<>();
		for (Map.Entry<Integer, Question> entry : getAllQuestions().entrySet()) {
			titles.put(entry.getKey(), entry.getValue().getTitle());
		}
		return titles;
	}

	public Question getRandomQuestion() {
		Random r = new Random();
		int index = r.nextInt(themes.size());
		Theme theme = themes.get(index);
		index = r.nextInt(theme.getQuestions().size());
		Question question = theme.getQuestions().get(index);
		return question;
	}

	private void createThemes(int count) {
		themes = new ArrayList<>();
		Theme theme;
		for (int i = 0; i < count; i++) {
			theme = new Theme();
			theme.setId(i);
			theme.setTitle("Theme " + (i + 1));
			theme.setText("Description for theme " + (i + 1));
			createQuestions(theme);
			themes.add(theme);
		}
	}

	private void createQuestions(Theme theme) {
		Question q;
		for (int i = 1; i < 7; i++) {
			q = new Question(theme);
			q.setId(i);
			q.setTitle("Title of the question " + i);
			q.setText("Text of the question " + i);
			q.setAnswers(createAnswers(q));
			theme.addQuestion(q);
		}
	}

	private ArrayList<Answer> createAnswers(Question q) {
		ArrayList<Answer> answer = new ArrayList<>();
		Answer a;
		Random r = new Random();
		String text;
		boolean correct;
		for (int i = 0; i < 4; i++) {
			text = ("Das ist " + (i + 1) + " mögliche Antwort.\n");
			correct = r.nextBoolean();
			a = new Answer(text, correct);
			a.setId(i);
			a.setQuestion(q);
			answer.add(a);
		}
		return answer;
	}

	public void addThema(String title, String info) {
		Theme t = new Theme();
		t.setTitle(title);
		t.setText(info);
		t.setQuestions(new ArrayList<>());
		themes.add(t);
	}

	public void deleteTheme(String title) {
		themes.removeIf(theme -> theme.getTitle().equals(title));
	}

	public void deleteQuestionsByTheme(String themeTitle) {
		allQuestions.entrySet().removeIf(entry -> entry.getValue().getTheme().getTitle().equals(themeTitle));
	}

	public void deleteQuestionByTitle(String title) {
		for (Theme theme : themes) {
			theme.getQuestions().removeIf(q -> q.getTitle().equals(title));
		}

		allQuestions.entrySet().removeIf(entry -> entry.getValue().getTitle().equals(title));
	}

	public ArrayList<Theme> getThemes() {
		return themes;
	}

	public ArrayList<String> getThemesTitle() {
		ArrayList<String> allThemesTitle = new ArrayList<>();
		for (Theme t : themes) {
			allThemesTitle.add(t.getTitle());
		}
		return allThemesTitle;
	}

	public void setThemes(ArrayList<Theme> themes) {
		this.themes = themes;
	}

	public Question getQuestionByTitle(String title) {
		for (Question q : allQuestions.values()) {
			if (q.getTitle().equals(title)) {
				return q;
			}
		}
		return null;
	}

	public Theme getThemeByTitle(String themeTitle) {
		for (Theme t : themes) {
			if (t.getTitle().equals(themeTitle))
				return t;
		}
		return null;
	}

	public void addQuestion(Question q) {
		Theme theme = getThemeByTitle(q.getTheme().getTitle());
		if (theme != null) {
			theme.addQuestion(q);
			int newId = allQuestions.isEmpty() ? 0 : allQuestions.keySet().stream().max(Integer::compareTo).get() + 1;
			q.setId(newId);
			allQuestions.put(newId, q);
		}
	}

	public void updateQuestion(Question q, Theme theme) {
		for (int i = 0; i < theme.getQuestions().size(); i++) {
			Question existing = theme.getQuestions().get(i);
			if (existing.getTitle().equals(q.getTitle())) {
				existing.setText(q.getText());
				existing.setAnswers(q.getAnswers());

				// Ако темата се смени, премести го во новата тема
				if (!existing.getTheme().getTitle().equals(q.getTheme().getTitle())) {
					theme.getQuestions().remove(i);
					Theme newTheme = getThemeByTitle(q.getTheme().getTitle());
					if (newTheme != null) {
						newTheme.addQuestion(q);
					}
				}

				// Update и во allQuestions
				allQuestions.entrySet().forEach(entry -> {
					if (entry.getValue().getTitle().equals(q.getTitle())) {
						entry.setValue(q);  // новата верзија
					}
				});
				return;
			}
		}
	}
}
