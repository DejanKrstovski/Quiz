package persistence.mariaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import quizlogic.ErrorHandler;

/**
 * This class manages and create the database connection.
 * It implements the DBConstants interface to use the constants defined there.
 * It provides a method to get the connection and handles exceptions
 * related to the database connection.
 */
public class DBConnection implements DBConstants {

	private static Connection connection = null;
	
	/**
	 * Private constructor to prevent instantiation.
	 * This class should only be used statically.
	 */
	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
				ErrorHandler.getInstance().setError(null);
			} catch (ClassNotFoundException e) {
				ErrorHandler.getInstance().setError("MySQL JDBC-Treiber nicht gefunden!");
			} catch (SQLException e) {
				ErrorHandler.getInstance().setError("Fehler beim Herstellen der Verbindung zur Datenbank: " + e.getMessage());
			}
		}
		return connection;
	}
	
	/**
	 * Closes the database connection if it is open.
	 * Sets the connection to null after closing it.
	 * Handles any SQL exceptions that may occur during the close operation.
	 */
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				ErrorHandler.getInstance().setError(null);
			} catch (SQLException e) {
				ErrorHandler.getInstance().setError("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
			}
		}
	}
}
