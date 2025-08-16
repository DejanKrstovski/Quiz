package persistence.mariaDB.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bussinesLogic.DataTransportObject;
import bussinesLogic.datenBank.PlayerAnswerDTO;
import persistence.mariaDB.MariaAccessObject;

public class PlayerAnswerDAO extends MariaAccessObject {

	private static final String SQL_INSERT = "INSERT INTO PLAYERANSWER (QUESTIONID, ANSWERID, SELECTED) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE PLAYERANSWER SET QUESTIONID = ?, ANSWERID = ?, SELECTED = ? WHERE ID = ?";
	private static final String SQL_DELETE = "DELETE FROM PLAYERANSWER WHERE ID = ?";
	private static final String SQL_SELECT = "SELECT * FROM QUIZ.PLAYERANSWER";

	private int questionId;
	private int answerId;
	private boolean selected;

	public PlayerAnswerDAO(PlayerAnswerDTO dto) {
		super(dto.getId());
		this.questionId = dto.getQuestionId();
		this.answerId = dto.getAnswerId();
		this.selected = dto.isSelected();
	}

	public PlayerAnswerDAO(Object[] row) {
		super((Integer) row[0]);
		this.questionId = (Integer) row[1];
		this.answerId = (Integer) row[2];
		this.selected = (Boolean) row[3];
	}


	@Override
	public String getSelectStatement() {
		return SQL_SELECT;
	}
	
	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	@Override
	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	@Override
	public DataTransportObject toDTO() {
		PlayerAnswerDTO dto = new PlayerAnswerDTO();
		dto.setId(getId());
		dto.setQuestionId(questionId);
		dto.setAnswerId(answerId);
		dto.setSelected(selected);
		return dto;
	}

	@Override
	public void prepareInsert(PreparedStatement stmt) {
		try {
			stmt.setInt(1, questionId);
			stmt.setInt(2, answerId);
			stmt.setBoolean(3, selected);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareUpdate(PreparedStatement stmt) {
		try {
			stmt.setInt(1, questionId);
			stmt.setInt(2, answerId);
			stmt.setBoolean(3, selected);
			stmt.setInt(4, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareDelete(PreparedStatement stmt){
		try {
			stmt.setInt(1, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
