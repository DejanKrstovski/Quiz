package persistence.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.platform.console.options.Theme;

import bussinesLogic.AnswerDTO;
import bussinesLogic.ErrorHandler;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import gui.GuiConstants;
import helpers.QuizDataInterface;

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
public class SManager implements Folders, Constants {

	/** Singleton instance of QuizDataManager. */
	private static SManager instance = null;

	private final File baseFolder;
	private final File themesFolder;
	private final File questionsFolder;
	private final File answersFolder;
	private final File playerAnswersFolder;
	private int maxThemeId = 0;
	private int maxQuestionId = 0;
	private int maxAnswerId = 0;
	private int maxPlayerAnswerId = 0;

	/** Cached list of all themes loaded from data store. */
	private List<ThemeDTO> allThemesCache = new ArrayList<>();

	/** Cached list of all questions across all themes. */
	private List<QuestionDTO> allQuestionsCache = new ArrayList<>();

	/** List of answers related to a particular question. */
	private List<AnswerDTO> allAnswersCache = new ArrayList<>();

	private List<PlayerAnswerDTO> allPlayerAnswersCache = new ArrayList<>();

	ErrorHandler errorHandler = ErrorHandler.getInstance();

	private SManager() {

		this.baseFolder = new File(DATA_FOLDER);
		this.themesFolder = new File(THEMES_FOLDER);
		this.questionsFolder = new File(QUESTIONS_FOLDER);
		this.answersFolder = new File(ANSWERS_FOLDER);
		this.playerAnswersFolder = new File(PLAYERANSWERS_FOLDER);

		baseFolder.mkdir();
		themesFolder.mkdirs();
		questionsFolder.mkdirs();
		answersFolder.mkdirs();
		playerAnswersFolder.mkdirs();

		refreshAll();
	}

	public static synchronized SManager getInstance() {
		if (instance == null) {
			instance = new SManager();
		}
		return instance;
	}

	private <T> T readObj(File f, Class<T> type) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
			Object o = ois.readObject();
			return type.cast(o);
		} catch (Exception e) {
			// Datei defekt? Ignorieren oder loggen
			return null;
		}
	}

	private void writeObj(File f, Object o) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
			oos.writeObject(o);
		}
	}

	private void refreshThemes() {
		List<ThemeDTO> list = new ArrayList<>();
		File[] files = themesFolder.listFiles((d, n) -> n.endsWith(".ser"));
		if (files != null) {
			for (File f : files) {
				ThemeDTO t = readObj(f, ThemeDTO.class);
				if (t != null) {
					if (t.getId() > maxThemeId) {
						maxThemeId = t.getId();
					}
					list.add(t);
				}

			}
		}
		this.allThemesCache = list;
	}

	private void refreshQuestions() {
		List<QuestionDTO> list = new ArrayList<>();
		File[] files = questionsFolder.listFiles((d, n) -> n.endsWith(".ser"));
		if (files != null) {
			for (File f : files) {
				QuestionDTO q = readObj(f, QuestionDTO.class);
				if (q != null) {
					if (q.getId() > maxQuestionId) {
						maxQuestionId = q.getId();
					}
					list.add(q);
				}
			}
		}
		this.allQuestionsCache = list;
	}

	private void refreshAnswers() {
		List<AnswerDTO> list = new ArrayList<>();
		File[] files = answersFolder.listFiles((d, n) -> n.endsWith(".ser"));
		if (files != null) {
			for (File f : files) {
				AnswerDTO a = readObj(f, AnswerDTO.class);
				if (a != null) {
					if (a.getId() > maxAnswerId) {
						maxAnswerId = a.getId();
					}
					list.add(a);
				}

			}
		}
		this.allAnswersCache = list;
	}

	private void refreshPlayerAnswers() {
		List<PlayerAnswerDTO> list = new ArrayList<>();
		File[] files = playerAnswersFolder.listFiles((d, n) -> n.endsWith(".ser"));
		if (files != null) {
			for (File f : files) {
				PlayerAnswerDTO a = readObj(f, PlayerAnswerDTO.class);
				if (a != null) {
					if (a.getId() > maxPlayerAnswerId) {
						maxPlayerAnswerId = a.getId();
					}
					list.add(a);
				}

			}
		}
		this.allPlayerAnswersCache = list;
	}

	private void refreshAll() {
		refreshThemes();
		refreshQuestions();
		refreshAnswers();
		refreshPlayerAnswers();
	}

	public List<ThemeDTO> getAllThemes() {
		return allThemesCache;
	}

	public List<QuestionDTO> getAllQuestions() {
		return allQuestionsCache;
	}

	public List<AnswerDTO> getAllAnswers() {
		return allAnswersCache;
	}

	public List<PlayerAnswerDTO> getAllPlayerAnswers() {
		return allPlayerAnswersCache;
	}

	/**
	 * Retrieves all answers for a given question.
	 * 
	 * @param question the {@link Question} whose answers are requested; must not be
	 *                 null
	 * @return list of answers, or null if question or answers not found
	 * @throws IllegalArgumentException if question is null
	 */
	public List<AnswerDTO> getAnswersForQuestion(QuestionDTO question) {
		List<AnswerDTO> answersForQuestion = new ArrayList<>();
		for (AnswerDTO answer : allAnswersCache) {
			if (answer != null && answer.getQuestionId() == question.getId()) {
				answersForQuestion.add(answer);
			}
		}
		return answersForQuestion;
	}

	private File fileFor(File folder, int id) {
		return new File(folder, id + ".ser");
	}

	/**
	 * Saves or updates the given theme by serializing it to a file. If the theme ID
	 * is less than zero, assigns a new ID.
	 * 
	 * @param theme the {@link Theme} to save; must not be null
	 * @return success message if saved, error message otherwise
	 * @throws IllegalArgumentException if theme is null
	 */
	public String saveTheme(ThemeDTO theme) {
		try {
			if (theme.getId() < 0) {
				theme.setId(maxThemeId + 1);
			}
			writeObj(fileFor(themesFolder, theme.getId()), theme);
			refreshThemes();
			return null; // analog: null = Erfolg
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	public String deleteTheme(ThemeDTO theme) {
		File f = fileFor(themesFolder, theme.getId());
		if (!f.exists())
			return "Not found";
		if (f.delete()) {
			// Kaskade: zugehörige Fragen/Antworten entfernen
			List<QuestionDTO> toDeleteQuestions = allQuestionsCache.stream()
					.filter(q -> q.getThemeId() == theme.getId()).collect(Collectors.toList());
			for (QuestionDTO question : toDeleteQuestions) {
				deleteQuestion(question);
			}
			refreshThemes();
			return "Success";
		}
		return "Delete failed";
	}

	public String saveQuestion(QuestionDTO question) {
        try {
            if (question.getId() < 1) {
                question.setId(maxQuestionId+1);
            }
            // persistiere Question
            writeObj(fileFor(questionsFolder, question.getId()), question);

            // Answers neu setzen: erst alte zu question löschen
            if (question.getAnswers() != null) {
                List<AnswerDTO> old = allAnswersCache.stream()
                        .filter(a -> a.getQuestionId() == question.getId())
                        .collect(Collectors.toList());
                for (AnswerDTO a : old) deleteAnswer(a);

                for (AnswerDTO a : question.getAnswers()) {
                    if (a.getId() < 1) a.setId(nextId(maxAnswerId));
                    a.setQuestionId(question.getId());
                    writeObj(fileFor(answersFolder, a.getId()), a);
                }
            }

            refreshQuestions();
            refreshAnswers();
            return "QUESTION_SAVED";
        } catch (IOException e) {
            return e.getMessage();
        }
    }
	public String deleteQuestion(QuestionDTO question) {
        File f = fileFor(questionsFolder, question.getId());
        if (!f.exists()) return "Not found";
        if (f.delete()) {
            // zugehörige Antworten löschen
            List<AnswerDTO> toDelete = allAnswersCache.stream()
                    .filter(a -> a.getQuestionId() == question.getId())
                    .collect(Collectors.toList());
            for (AnswerDTO a : toDelete) {
                deleteAnswer(a);
            }
            refreshQuestions();
            refreshAnswers();
            return "Success";
        }
        return "Delete failed";
    }

    public String saveAnswer(AnswerDTO answer) {
        try {
            if (answer.getId() < 1) answer.setId(nextId(maxAnswerId));
            writeObj(fileFor(answersFolder, answer.getId()), answer);
            refreshAnswers();
            return null;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String deleteAnswer(AnswerDTO answer) {
        File f = fileFor(answersFolder, answer.getId());
        if (!f.exists()) return "Not found";
        if (f.delete()) {
            refreshAnswers();
            return "Success";
        }
        return "Delete failed";
    }

    public String savePlayerAnswer(PlayerAnswerDTO pa) {
        try {
            if (pa.getId() < 1) pa.setId(nextId(maxPlayerAnswerId));
            writeObj(fileFor(playerAnswersFolder, pa.getId()), pa);
            refreshPlayerAnswers();
            return null;
        } catch (IOException e) {
            return e.getMessage();
        }
    }


	/**
	 * Reads and returns a theme by its unique ID.
	 * 
	 * @param id the ID of the theme to read
	 * @return the {@link Theme} object if found, null otherwise
	 */
//	public ThemeDTO readThemeById(int id) {
//		File file = new File(THEME_FILE_PREFIX + id);
//		if (!file.exists()) {
//			ErrorHandler.getInstance().setError(FILE_FOR_THEME_WITH_ID + id + DOES_NOT_EXISTS);
//			return null;
//		}
//
//		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
//			return (ThemeDTO) ois.readObject();
//		} catch (ClassNotFoundException | IOException e) {
//			ErrorHandler.getInstance().setError(ERROR_BY_LOADING_THEME + e.getMessage());
//			return null;
//		}
//	}

//	public QuestionDTO getQuestionById(int id) {
//		for (QuestionDTO question : allThemeQuestions) {
//			if (question.getId() == id) {
//				return question;
//			}
//		}
//		return null;
//	}

	private int nextId(int id) {
		return id + 1;
	}
}
