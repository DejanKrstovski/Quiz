package persistence.mariaDB.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bussinesLogic.DataTransportObject;
import bussinesLogic.QuestionDTO;
import persistence.mariaDB.MariaAccessObject;

public class QuestionDAO extends MariaAccessObject{
	
	private final String SQL_SELECT = "SELECT * FROM QUIZ.QUESTION;";
	private final String SQL_INSERT = "INSERT INTO QUIZ.QUESTION (TITLE, TEXT, THEMEID) VALUES (?, ?, ?);";
	private final String SQL_UPDATE = "UPDATE QUIZ.QUESTION SET TITLE = ?, TEXT = ?, THEMEID = ? WHERE (ID = ?);";
	private final String SQL_DELETE = "DELETE FROM QUIZ.QUESTION WHERE (ID = ?);";

    /** The title or short description of the question */
    private String title;

    /** The full text or wording of the question */
    private String text;

    /** The theme or category this question belongs to */
    private int themeId;

	public QuestionDAO(QuestionDTO dto) {
		super(dto.getId());
		title = dto.getTitle();
		text = dto.getText();
		themeId = dto.getThemeId();
	}
	
	public QuestionDAO(Object[] row) {
		super((int) row[0]);
		title = (String) row[1];
		text = (String) row[2];
		themeId = (int) row[3];
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
		QuestionDTO dto = new QuestionDTO();
		dto.setId(getId());
		dto.setTitle(title);
		dto.setText(text);
		dto.setThemeId(themeId);
		return dto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	@Override
	public void prepareInsert(PreparedStatement stmt) {
		try {
			stmt.setString(1, title);
			stmt.setString(2, text);
			stmt.setInt(3, themeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareUpdate(PreparedStatement stmt) {
		try {
			stmt.setString(1, title);
			stmt.setString(2, text);
			stmt.setInt(3, themeId);
			stmt.setInt(4, id);
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
}
