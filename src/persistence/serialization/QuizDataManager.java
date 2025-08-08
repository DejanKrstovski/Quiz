package persistence.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import gui.GuiConstants;
import persistence.Constants;
import persistence.QuizDataInterface;
import quizlogic.Answer;
import quizlogic.ErrorHandler;
import quizlogic.Question;
import quizlogic.Theme;

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
public class QuizDataManager implements QuizDataInterface, Constants {

	/** Singleton instance of QuizDataManager. */
	private static QuizDataManager instance = null;

	/** Cached list of all themes loaded from data store. */
	private List<Theme> themes;

	/** Cached list of all questions across all themes. */
	private List<Question> allThemeQuestions;

	/** List of questions chosen for a specific theme. */
	private List<Question> chosenThemeQuestions;

	/** List of answers related to a particular question. */
	private List<Answer> answers;

	ErrorHandler errorHandler = ErrorHandler.getInstance();

	private int counter_za_prasanja;

	private int counterZaTemi;

	private int counterRandom;

	/**
	 * Returns the singleton instance of {@code QuizDataManager}.
	 * 
	 * @return the singleton instance
	 */
	public static synchronized QuizDataManager getInstance() {
		if (instance == null) {
			instance = new QuizDataManager();
		}
		return instance;
	}

	/**
	 * Retrieves a random question from all available questions.
	 * 
	 * @return a randomly selected {@link Question}
	 * @throws IllegalStateException if no questions are loaded
	 */
	@Override
	public Question getRandomQuestion() {
		counterRandom++;
		if (allThemeQuestions == null || allThemeQuestions.isEmpty()) {
			errorHandler.setError(NO_QUESTIONS_EXISTS);
			return null;
		}
		Random random = new Random();
		int index = random.nextInt(allThemeQuestions.size());
		System.out.println("Random question " + counterRandom);
		return allThemeQuestions.get(index);
	}

	/**
	 * Loads and returns all themes by reading serialized theme files from the data
	 * folder.
	 * 
	 * If the data folder does not exist, it will be created and an informational
	 * message is set.
	 * 
	 * @return list of all {@link Theme} objects; empty list if none found
	 */
	@Override
	public void getAllThemes() {
		counterZaTemi++;
		File folder = new File(DATA_FOLDER);
		themes = new ArrayList<>();

		if (!folder.exists()) {
			if (folder.mkdir()) {
				errorHandler.setInfo(NEW_FOLDER_CREATED);
			} else {
				errorHandler.setError(FAILED_TO_CREATE_FOLDER);
			}
		}
		File[] files = folder.listFiles((dir, name) -> name.startsWith(THEME));
		if (files != null) {
			for (File file : files) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					Theme theme = (Theme) ois.readObject();
					themes.add(theme);
				} catch (Exception e) {
					errorHandler.setError(ERROR_LOADING_FILE + file.getName() + ": " + e.getMessage());
				}
			}
		}
		System.out.println("Citam files " + counterZaTemi);
	}

	/**
	 * Retrieves all questions from all themes.
	 * 
	 * @return list of all {@link Question} objects
	 */
	public void getAllQuestions() {
		counter_za_prasanja++;
		if (allThemeQuestions == null) {
			allThemeQuestions = new ArrayList<>();
		} else {
			allThemeQuestions.clear();
		}

		for (Theme theme : themes) {
			List<Question> themeQuestions = theme.getQuestions();
			if (themeQuestions != null) {
				allThemeQuestions.addAll(themeQuestions);
			}
		}
		System.out.println("Site prasanja od file " + counter_za_prasanja);
	}

	/**
	 * Retrieves all questions associated with a given theme.
	 * 
	 * @param theme the {@link Theme} whose questions are to be returned; must not
	 *              be null
	 * @return list of questions for the theme; empty if none or theme not found
	 * @throws IllegalArgumentException if theme is null
	 */
	@Override
	public List<Question> getQuestionsForTheme(Theme theme) {
		chosenThemeQuestions = new ArrayList<>();
		for (Theme t : themes) {
			if (t.getId() == theme.getId()) {
				List<Question> themeQuestions = t.getQuestions();
				if (themeQuestions != null) {
					chosenThemeQuestions.addAll(themeQuestions);
				}
				break;
			}
		}
		System.out.println("Prasanja za tema: " + theme.getTitle());
		return chosenThemeQuestions;
	}

	/**
	 * Retrieves all answers for a given question.
	 * 
	 * @param question the {@link Question} whose answers are requested; must not be
	 *                 null
	 * @return list of answers, or null if question or answers not found
	 * @throws IllegalArgumentException if question is null
	 */
	@Override
	public List<Answer> getAnswersForQuestion(Question question) {
		answers = null;
		System.out.println("Odgovori za prasanje " + question.getTitle());
		for (Theme theme : themes) {
			List<Question> themeQuestions = theme.getQuestions();
			if (themeQuestions != null && themeQuestions.contains(question)) {
				int index = themeQuestions.indexOf(question);
				answers = themeQuestions.get(index).getAnswers();
				break;
			}
		}
		return answers;
	}

	/**
	 * Saves or updates the given theme by serializing it to a file. If the theme ID
	 * is less than zero, assigns a new ID.
	 * 
	 * @param theme the {@link Theme} to save; must not be null
	 * @return success message if saved, error message otherwise
	 * @throws IllegalArgumentException if theme is null
	 */
	@Override
	public String saveTheme(Theme theme) {
		try {
			if (theme.getId() < 0) {
				theme.setId(createNewThemeId());
			}

			try (FileOutputStream fos = new FileOutputStream(THEME_FILE_PREFIX + theme.getId());
					ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(theme);
				oos.flush();
			}

			return THEME_SUCCESFULLY_SAVED;
		} catch (IOException e) {
			return ERROR_SAVING_THEME + e.getMessage();
		}
	}

	/**
	 * Deletes the file corresponding to the given theme's ID.
	 * 
	 * @param theme the {@link Theme} to delete; must not be null
	 * @return success message if deleted, error message otherwise
	 * @throws IllegalArgumentException if theme is null
	 */
	@Override
	public String deleteTheme(Theme theme) {
		int id = theme.getId();
		File folder = new File(DATA_FOLDER);
		File[] files = folder.listFiles((dir, name) -> name.startsWith(THEME + id));

		if (files != null && files.length > 0) {
			boolean allDeleted = true;
			for (File file : files) {
				if (!file.delete()) {
					allDeleted = false;
				}
			}
			if (allDeleted) {
				return THEME_AND_QUESTIONS_SUCCESFULLY_DELETED;
			}
		}
		return ERROR_THEME_DELETE;
	}

	/**
	 * Saves the question.
	 * <p>
	 * Current implementation only loads themes without saving; method needs
	 * implementation.
	 * </p>
	 * 
	 * @param question the {@link Question} to save; must not be null
	 * @return success message (currently always returns
	 *         {@link GuiConstants#QUESTION_SAVED})
	 * @throws IllegalArgumentException if question is null
	 */
	@Override
	public String saveQuestion(Question question) {
		Theme questionTheme = question.getTheme();
		if (questionTheme == null) {
			return QUESTION_ASSIGN_THEME;
		}

		Theme storedTheme = null;
		for (Theme theme : themes) {
			if (theme.getId() == questionTheme.getId()) {
				storedTheme = theme;
				break;
			}
		}

		if (storedTheme == null) {
			return THEME_FROM_QUESTION;
		}

		List<Question> questions = storedTheme.getQuestions();
		if (questions == null) {
			questions = new ArrayList<>();
			storedTheme.setQuestions(questions);
		}

		if (question.getId() < 0) {
			question.setId(createNewQuestionId());
			questions.add(question);
		} else {
			boolean found = false;
			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).getId() == question.getId()) {
					questions.set(i, question);
					found = true;
					break;
				}
			}
			if (!found) {
				questions.add(question);
			}
		}

		String saveResult = saveTheme(storedTheme);
		if (THEME_SUCCESFULLY_SAVED.equals(saveResult)) {
			return QUESTION_SAVED;
		} else {
			return ERROR_SAVING_QUESTION + saveResult;
		}
	}

	/**
	 * Deletes a question by its unique ID. Iterates over themes to find and remove
	 * the question, then saves updated theme.
	 * 
	 * @param id the question ID to delete
	 * @return confirmation message if question deleted or warning if not found
	 */
	@Override
	public String deleteQuestion(int id) {
		for (Theme theme : themes) {
			List<Question> themeQuestions = theme.getQuestions();
			if (themeQuestions != null) {
				Iterator<Question> iterator = themeQuestions.iterator();
				while (iterator.hasNext()) {
					Question question = iterator.next();
					if (question.getId() == id) {
						iterator.remove();
						saveTheme(theme);
						return QUESTION_DELETED;
					}
				}
			}
		}
		return QUESTION_DELETING_NOT_POSSIBLE;
	}

	/**
	 * Reads and returns a theme by its unique ID.
	 * 
	 * @param id the ID of the theme to read
	 * @return the {@link Theme} object if found, null otherwise
	 */
	public Theme readThemeById(int id) {
		File file = new File(THEME_FILE_PREFIX + id);
		if (!file.exists()) {
			ErrorHandler.getInstance().setError(FILE_FOR_THEME_WITH_ID + id + DOES_NOT_EXISTS);
			return null;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (Theme) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			ErrorHandler.getInstance().setError(ERROR_BY_LOADING_THEME + e.getMessage());
			return null;
		}
	}

	/**
	 * Creates a new unique ID for a theme by scanning existing files.
	 * 
	 * @return the next available unique theme ID
	 */
	public int createNewThemeId() {
		File folder = new File(DATA_FOLDER);
		int maxId = 0;

		if (folder.exists() && folder.listFiles() != null) {
			for (File file : folder.listFiles()) {
				String name = file.getName();
				if (name.startsWith(THEME)) {
					try {
						int id = Integer.parseInt(name.substring(THEME.length()));
						if (id > maxId) {
							maxId = id;
						}
					} catch (NumberFormatException ignored) {
					}
				}
			}
		}
		return maxId + 1;
	}

	/**
	 * Creates a new unique ID for a question by scanning existing questions across
	 * all themes.
	 * 
	 * @return the next available unique question ID
	 */
	public int createNewQuestionId() {
		int maxId = 0;
		List<Theme> themeList = themes;

		for (Theme theme : themeList) {
			List<Question> questions = theme.getQuestions();
			if (questions != null) {
				for (Question question : questions) {
					if (question.getId() > maxId) {
						maxId = question.getId();
					}
				}
			}
		}
		return maxId + 1;
	}

	/**
	 * Retrieves a question by its unique ID.
	 * 
	 * @param id the ID of the question to find
	 * @return the {@link Question} if found, or null if not found
	 */
	public Question getQuestionById(int id) {
		for (Question question : allThemeQuestions) {
			if (question.getId() == id) {
				return question;
			}
		}
		return null;
	}
	
	public List<Theme> getThemes() {
		return themes;
	}

	public void updateThemes() {
		getAllThemes();
	}
	
	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public List<Question> getAllThemeQuestions() {
		return allThemeQuestions;
	}

	public void reloadAllThemeQuestions() {
		getAllQuestions();
	}

	
}
