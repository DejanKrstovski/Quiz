package persistence.mariaDB.createDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreator implements DBConstants {

	
	public static void createDB() {
		try (Connection conn = DriverManager.getConnection(URL_NO_DB, USER, PASSWORD);
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(CREATE_DATABASE);
			conn.close();
			createTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void createTables() {
		try (Connection conn = DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD);
				Statement stmt = conn.createStatement()) {
			stmt.execute(CREATE_THEME_TABLE);
			stmt.execute(CREATE_QUESTION_TABLE);
			stmt.execute(CREATE_ANSWER_TABLE);
			stmt.execute(CREATE_PLAYER_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
