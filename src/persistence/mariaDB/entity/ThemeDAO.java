package persistence.mariaDB.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bussinesLogic.DataTransportObject;
import bussinesLogic.ThemeDTO;
import persistence.mariaDB.MariaAccessObject;

/**
 * This class represents the entity Theme
 */
public class ThemeDAO extends MariaAccessObject {

	private final String SQL_INSERT = "INSERT INTO QUIZ.THEME (TITLE, TEXT) VALUES (?, ?);";
	private final String SQL_UPDATE = "UPDATE QUIZ.THEME SET TITLE = ?, TEXT = ? WHERE (ID = ?);";
	private final String SQL_SELECT = "SELECT * from QUIZ.THEME;";
	private final String SQL_DELETE = "DELETE FROM QUIZ.THEME WHERE (ID = ?)";

	private String title;
	private String text;

	/**
	 * Constructs a ThemeDAO-instance using the corresponding instance of the ThemeDTO class.
	 * @param dto
	 */
	public ThemeDAO(ThemeDTO dto) {
		super(dto.getId());
		title = dto.getTitle();
		text = dto.getText();
	}

	/**
	 * Set fields values using the result set of the SQL select command.
	 * @param row
	 */
	public ThemeDAO(Object[] row) {

		super((int) row[0]);
		title = (String) row[1];
		text = (String) row[2];
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
		ThemeDTO dto = new ThemeDTO();
		dto.setId(getId());
		dto.setTitle(title);
		dto.setText(text);
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

	@Override
	public void prepareDelete(PreparedStatement stmt) {
		try {
			stmt.setInt(1, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void prepareInsert(PreparedStatement stmt) {
		try {
			stmt.setString(1, title);
			stmt.setString(2, text);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void prepareUpdate(PreparedStatement stmt) {
		try {
			stmt.setString(1, title);
			stmt.setString(2, text);
			stmt.setInt(3, getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}