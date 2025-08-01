package quizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Temporary class for managing sample quiz data (themes, questions, and answers).
 */
public class FakeDataDeliverer {

	public ArrayList<Theme> themes;
	public HashMap<Integer, String> allThemes = new HashMap<>();
	public HashMap<Integer, Question> allQuestions = new HashMap<>();
	int globalCount = 0;

	public FakeDataDeliverer() {
		super();
		createThemes(10);
	}

	/**
	 * Returns a map of all theme IDs and their corresponding titles.
	 *
	 * @return a map where each key is a theme ID and each value is the theme title
	 */
	public HashMap<Integer, String> getAllThemesTitles() {
		for (int i = 0; i < themes.size(); i++) {
			allThemes.put(i, themes.get(i).getTitle());
		}
		return allThemes;
	}

	/**
	 * Collects and returns all questions from all available themes.
	 *
	 * @return a map where each key is a question ID and each value is the corresponding question
	 */
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

	/**
	 * Returns a list of questions that belong to a specific theme.
	 *
	 * @param themeTitle the title of the theme; if null, empty, or "Alle Themen", returns all questions
	 * @return a list of questions that belong to the specified theme
	 */
	public List<Question> getQuestionsByTheme(String themeTitle) {
		if (themeTitle == null || themeTitle.isEmpty() || themeTitle.equals("Alle Themen")) {
			return new ArrayList<>(allQuestions.values());
		}
		List<Question> allQuestionsOfTheme = allQuestions.values().stream().filter(q -> q.getTheme().getTitle().equals(themeTitle))
				.collect(Collectors.toList());
				
		return allQuestionsOfTheme;
	}

	/**
	 * Returns a map of all question IDs and their titles.
	 *
	 * @return a map where each key is a question ID and each value is the question title
	 */
	public HashMap<Integer, String> getAllQuestionTitle() {
		HashMap<Integer, String> titles = new HashMap<>();
		for (Map.Entry<Integer, Question> entry : getAllQuestions().entrySet()) {
			titles.put(entry.getKey(), entry.getValue().getTitle());
		}
		return titles;
	}

	/**
	 * Returns a randomly selected question from the available themes.
	 *
	 * @return a random question
	 */
	public Question getRandomQuestion(String theme) {
		Random r = new Random();
		Question question;
		int index;
		Theme t = getThemeByTitle(theme);
		if(t == null) {
			index = r.nextInt(themes.size());
			question = themes.get(index).getQuestions().get(index);
			return question;
		} else {
			index = r.nextInt(t.getQuestions().size());
			question = t.getQuestions().get(index);
			return question;
		}
			
		
	}

	/**
	 * Creates a specified number of themes, each populated with dummy questions and answers.
	 *
	 * @param count the number of themes to create
	 */
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
		for (int i = 0; i < 10; i++) {
			q = new Question(theme);
			q.setId(i);
			q.setTitle("Title of the question " + globalCount++);
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

	/**
	 * Adds a new theme to the list.
	 *
	 * @param title the title of the theme
	 * @param info the description or additional information of the theme
	 */
	public void addThema(String title, String info) {
		Theme t = new Theme();
		t.setTitle(title);
		t.setText(info);
		t.setQuestions(new ArrayList<>());
		themes.add(t);
	}

	/**
	 * Deletes the theme with the given title.
	 *
	 * @param title the title of the theme to be deleted
	 */
	public void deleteTheme(String title) {
		themes.removeIf(theme -> theme.getTitle().equals(title));
	}

	/**
	 * Returns a list of all theme titles.
	 *
	 * @return a list of theme titles
	 */
	public ArrayList<String> getThemesTitle() {
		ArrayList<String> allThemesTitle = new ArrayList<>();
		for (Theme t : themes) {
			allThemesTitle.add(t.getTitle());
		}
		return allThemesTitle;
	}
	
	/**
	 * Deletes all questions that belong to the specified theme.
	 *
	 * @param themeTitle the title of the theme whose questions should be deleted
	 */
	public void deleteQuestionsByTheme(String themeTitle) {
		allQuestions.entrySet().removeIf(entry -> entry.getValue().getTheme().getTitle().equals(themeTitle));
	}

	/**
	 * Deletes a question by its title from both the theme and the global question list.
	 *
	 * @param title the title of the question to delete
	 */
	public void deleteQuestionByTitle(String title) {
		for (Theme theme : themes) {
			theme.getQuestions().removeIf(q -> q.getTitle().equals(title));
		}
		allQuestions.entrySet().removeIf(entry -> entry.getValue().getTitle().equals(title));
	}

	/**
	 * Finds and returns a question by its title.
	 *
	 * @param title the title of the question to find
	 * @return the matching question, or null if not found
	 */
	public Question getQuestionByTitle(String title) {
		for (Question q : allQuestions.values()) {
			if (q.getTitle().equals(title)) {
				return q;
			}
		}
		return null;
	}

	/**
	 * Returns a theme that matches the given title.
	 *
	 * @param themeTitle the title of the theme to find
	 * @return the matching theme, or null if not found
	 */
	public Theme getThemeByTitle(String themeTitle) {
		for (Theme theme : themes) {
			if (theme.getTitle().equals(themeTitle))
				return theme;
		}
		return null;
	}

	/**
	 * Adds a new question to the appropriate theme and updates the global question list.
	 *
	 * @param q the question to be added
	 */
	public void addQuestion(Question q) {
		Theme theme = getThemeByTitle(q.getTheme().getTitle());
		if (theme != null) {
			theme.addQuestion(q);
			int newId = allQuestions.isEmpty() ? 0 : allQuestions.keySet().stream().max(Integer::compareTo).get() + 1;
			q.setId(newId);
			allQuestions.put(newId, q);
		}
	}

	/**
	 * Updates an existing question within its theme. If the theme has changed,
	 * the question is moved to the new theme.
	 *
	 * @param q the updated question
	 * @param theme the original theme where the question currently resides
	 */
	public void updateQuestion(Question q, Theme theme) {
		for (int i = 0; i < theme.getQuestions().size(); i++) {
			Question existing = theme.getQuestions().get(i);
			if (existing.getTitle().equals(q.getTitle())) {
				existing.setText(q.getText());
				existing.setAnswers(q.getAnswers());

				if (!existing.getTheme().getTitle().equals(q.getTheme().getTitle())) {
					theme.getQuestions().remove(i);
					Theme newTheme = getThemeByTitle(q.getTheme().getTitle());
					if (newTheme != null) {
						newTheme.addQuestion(q);
					}
				}

				allQuestions.entrySet().forEach(entry -> {
					if (entry.getValue().getTitle().equals(q.getTitle())) {
						entry.setValue(q);
					}
				});
				return;
			}
		}
	}

	/**
	 * Replaces the current list of themes with the provided one.
	 *
	 * @param themes the new list of themes
	 */
	public void setThemes(ArrayList<Theme> themes) {
		this.themes = themes;
	}
	
	/**
	 * Returns the current list of themes.
	 *
	 * @return the list of themes
	 */
	public ArrayList<Theme> getThemes() {
		return themes;
	}
}
