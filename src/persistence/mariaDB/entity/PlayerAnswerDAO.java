package persistence.mariaDB.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import bussinesLogic.DataTransportObject;
import bussinesLogic.PlayerAnswerDTO;
import persistence.mariaDB.MariaAccessObject;

public class PlayerAnswerDAO extends MariaAccessObject {
	private static final String SQL_INSERT = "INSERT INTO PLAYERANSWER (QUESTIONID, ANSWERID, CREATED_AT) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE PLAYERANSWER SET QUESTIONID = ?, ANSWERID = ?, CREATED_AT = ? WHERE ID = ?";
	private static final String SQL_DELETE = "DELETE FROM PLAYERANSWER WHERE ID = ?";
	private static final String SQL_SELECT = "SELECT * FROM QUIZ.PLAYERANSWER";

	private int questionId;
	private int answerId;
	private LocalDateTime createdAt;

	public PlayerAnswerDAO(PlayerAnswerDTO dto) {
		super(dto.getId());
		this.questionId = dto.getQuestionId();
		this.answerId = dto.getAnswerId();
		this.createdAt = dto.getCreatedAt();
	}

	public PlayerAnswerDAO(Object[] row) {
		super((Integer) row[0]);
		this.questionId = (Integer) row[1];
		this.answerId = (Integer) row[2];
		Timestamp ts = (Timestamp) row[3];
		this.createdAt = (ts != null ? ts.toLocalDateTime() : null);
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
		dto.setCreatedAt(createdAt);
		return dto;
	}

	@Override
	public void prepareInsert(PreparedStatement stmt) {
		try {
			stmt.setInt(1, questionId);
			stmt.setInt(2, answerId);
			if (createdAt != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(createdAt));
			} else {
				stmt.setTimestamp(3, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareUpdate(PreparedStatement stmt) {
		try {
			stmt.setInt(1, questionId);
			stmt.setInt(2, answerId);
			if (createdAt != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(createdAt));
			} else {
				stmt.setTimestamp(3, null);
			}
			stmt.setInt(4, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareDelete(PreparedStatement stmt) {
		try {
			stmt.setInt(1, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Getters and setters
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
