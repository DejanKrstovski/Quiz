package persistence.mariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bussinesLogic.AnswerDTO;
import bussinesLogic.DataTransportObject;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;
import bussinesLogic.ThemeDTO;
import persistence.mariaDB.createDB.DBConnection;
import persistence.mariaDB.entity.AnswerDAO;
import persistence.mariaDB.entity.PlayerAnswerDAO;
import persistence.mariaDB.entity.QuestionDAO;
import persistence.mariaDB.entity.ThemeDAO;

public class DBManager {

	private static DBManager instance;
	private Connection connection = DBConnection.getConnection();

	private List<ThemeDTO> themeCache;
	private List<QuestionDTO> questionCache;
	private List<AnswerDTO> answerCache;
	private List<PlayerAnswerDTO> playerAnswerCache;

	private DBManager() {
		refreshThemes();
		refreshQuestions();
		refreshAnswers();
		refreshPlayerAnswers();
	}

	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private <T extends MariaAccessObject> List<DataTransportObject> getAllFromDAO(Class<T> daoClass, String tableName) {
		List<DataTransportObject> results = new ArrayList<>();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

			int colCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Object[] row = new Object[colCount];
				for (int i = 0; i < colCount; i++) {
					row[i] = rs.getObject(i + 1);
				}
				results.add(daoClass.getConstructor(Object[].class).newInstance((Object) row).toDTO());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	private String saveDAO(MariaAccessObject dao) {
		boolean isNew = dao.getId() < 1;
		try {
			connection.setAutoCommit(false);
			String sql = isNew ? dao.getInsertStatement() : dao.getUpdateStatement();
			try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				if (isNew)
					dao.prepareInsert(stmt);
				else
					dao.prepareUpdate(stmt);
				stmt.executeUpdate();
				if (isNew) {
					ResultSet keys = stmt.getGeneratedKeys();
					if (keys.next())
						dao.setId(keys.getInt(1));
				}
			}
			connection.commit();
			return null;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ignore) {
			}
			return e.getMessage();
		}
	}

	private String deleteDAO(MariaAccessObject dao) {
		try (PreparedStatement stmt = connection.prepareStatement(dao.getDeleteStatement())) {
			dao.prepareDelete(stmt);
			stmt.executeUpdate();
			return "Success";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	private void refreshThemes() {
		themeCache = getAllFromDAO(ThemeDAO.class, "Theme").stream().map(t -> (ThemeDTO) t)
				.collect(Collectors.toList());
	}

	private void refreshQuestions() {
		questionCache = getAllFromDAO(QuestionDAO.class, "Question").stream().map(q -> (QuestionDTO) q)
				.collect(Collectors.toList());
	}

	private void refreshAnswers() {
		answerCache = getAllFromDAO(AnswerDAO.class, "Answer").stream().map(a -> (AnswerDTO) a)
				.collect(Collectors.toList());
	}
	
	private void refreshPlayerAnswers() {
		playerAnswerCache = getAllFromDAO(PlayerAnswerDAO.class, "PlayerAnswer").stream().map(a -> (PlayerAnswerDTO) a)
				.collect(Collectors.toList());
	}

	public List<ThemeDTO> getAllThemes() {
		return themeCache;
	}

	public List<QuestionDTO> getAllQuestions() {
		return questionCache;
	}

	public List<AnswerDTO> getAllAnswers() {
		return answerCache;
	}
	
	public List<PlayerAnswerDTO> getAllPlayerAnswers() {
		return playerAnswerCache;
	}

	public String saveTheme(ThemeDTO theme) {
		String result = saveDAO(new ThemeDAO(theme));
		if (result == null)
			refreshThemes();
		return result;
	}

	public String deleteTheme(ThemeDTO theme) {
		String result = deleteDAO(new ThemeDAO(theme));
		if ("Success".equals(result)) {
			refreshThemes();
			refreshQuestions();
			refreshAnswers();
		}
		return result;
	}

	public String saveQuestion(QuestionDTO question) {
		QuestionDAO dao = new QuestionDAO(question);
		String result = saveDAO(dao);
		
		if (result != null) {
			return result;
		}
		int questionId = dao.getId();
		question.setId(questionId);

		for (AnswerDTO answer : question.getAnswers()) {
			answer.setQuestionId(questionId);
			deleteAnswer(answer);
		}

		if (question.getAnswers() != null) {
			for (AnswerDTO answer : question.getAnswers()) {
				answer.setQuestionId(questionId);
				saveDAO(new AnswerDAO(answer));
			}
		}
		refreshQuestions();
		
		refreshAnswers();
		
		return "QUESTION_SAVED";
	}

	public String deleteQuestion(QuestionDTO question) {
		String result = deleteDAO(new QuestionDAO(question));
		if ("Success".equals(result)) {
			refreshQuestions();
			refreshAnswers();
		}
		return result;
	}

	public String saveAnswer(AnswerDTO answer) {
		String result = saveDAO(new AnswerDAO(answer));
		if (result == null)
			refreshAnswers();
		return result;
	}

	public String deleteAnswer(AnswerDTO answer) {
		String result = deleteDAO(new AnswerDAO(answer));
		if ("Success".equals(result))
			refreshAnswers();
		return result;
	}
	
	public String savePlayerAnswer(PlayerAnswerDTO playerAnswer) {
	    return saveDAO(new PlayerAnswerDAO(playerAnswer));
	}
}
