package persistence.mariaDB.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bussinesLogic.DataTransportObject;
import bussinesLogic.datenBank.AnswerDTO;
import persistence.mariaDB.MariaAccessObject;

public class AnswerDAO extends MariaAccessObject {

	private final String SQL_SELECT = "SELECT * from QUIZ.ANSWER;";
	private final String SQL_INSERT = "INSERT INTO QUIZ.ANSWER (TEXT, ISCORRECT, QUESTIONID) VALUES (?, ?, ?);";
	private final String SQL_UPDATE = "UPDATE QUIZ.ANSWER SET TEXT = ?, ISCORRECT = ?, QUESTIONID = ? WHERE (ID = ?));";
	private final String SQL_DELETE = "DELETE FROM QUIZ.ANSWER WHERE (QUESTIONID = ?)";

	/** The text label of this answer; may be {@code null} or empty. */
	private String text;

	/** Whether this answer is the correct choice for its related question. */
	private boolean isCorrect;

	/**
	 * This is the id from the question
	 */
	private int questionId;

	public AnswerDAO(AnswerDTO dto) {
		super(dto.getId());
		text = dto.getText();
		isCorrect = dto.isCorrect();
		questionId = dto.getQuestionId();
	}

	public AnswerDAO(Object[] row) {
		super((int) row[0]);
		text = (String) row[1];
		isCorrect = (Boolean) row[2];
		questionId = (int) row[3];
	}

	@Override
	public String getSelectStatement() {
		return SQL_SELECT;
	}

	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	@Override
	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	@Override
	public DataTransportObject toDTO() {
		AnswerDTO dto = new AnswerDTO();
		dto.setId(getId());
		dto.setText(text);
		dto.setCorrect(isCorrect);
		dto.setQuestionId(questionId);
		return dto;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	@Override
	public void prepareInsert(PreparedStatement stmt) {
		try {
			stmt.setString(1, text);
			stmt.setBoolean(2, isCorrect);
			stmt.setInt(3, questionId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void prepareUpdate(PreparedStatement stmt) {
        try {
            stmt.setString(1, text);
            stmt.setBoolean(2, isCorrect);
            stmt.setInt(3, questionId);
            stmt.setInt(4, getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void prepareDelete(PreparedStatement stmt) {
        try {
            stmt.setInt(1, getQuestionId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
