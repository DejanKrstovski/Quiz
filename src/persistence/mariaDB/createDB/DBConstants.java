package persistence.mariaDB.createDB;

/**
 * Constants used for configuring the connection to the MariaDB database.
 * <p>
 * This interface holds the URL, username, and password required to establish a
 * database connection. These values should be set appropriately before use.
 * </p>
 * <p>
 * Typically, these constants are populated with actual connection information
 * such as:
 * <ul>
 * <li><b>DB_URL</b>: the JDBC URL to connect to the MariaDB instance</li>
 * <li><b>USER</b>: the username for database authentication</li>
 * <li><b>PASSWORD</b>: the password for database authentication</li>
 * </ul>
 * </p>
 * <p>
 * <b>Note:</b> For security reasons, it is recommended to externalize these
 * credentials in configuration files or environment variables rather than
 * hardcoding them.
 * </p>
 * 
 * @author
 */
public interface DBConstants {

	/**
	 * The JDBC URL for connecting and creating the database.
	 */
	public static final String URL_NO_DB = "jdbc:mysql://localhost:3306?serverTimezone=UTC";

	/**
	 * The JDBC URL for connecting to the created MariaDB database.
	 */
	public static final String URL_WITH_DB = "jdbc:mysql://localhost:3306/QUIZ?serverTimezone=UTC";

	/**
	 * Username used to authenticate with the MariaDB database.
	 */
	public static final String USER = "root";

	/**
	 * Password corresponding to the database user.
	 */
	public static final String PASSWORD = "";
	
	public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS QUIZ DEFAULT CHARACTER SET utf8";

	public static final String CREATE_THEME_TABLE = """
			    CREATE TABLE IF NOT EXISTS THEME (
			      ID INT NOT NULL AUTO_INCREMENT,
			      TITLE VARCHAR(45) NOT NULL,
			      TEXT VARCHAR(5000) NULL,
			      PRIMARY KEY (ID)
			    )
			""";

	public static final String CREATE_QUESTION_TABLE = """
			    CREATE TABLE IF NOT EXISTS QUESTION (
			      	ID INT NOT NULL AUTO_INCREMENT,
			      	TITLE VARCHAR(45) NOT NULL,
			      	TEXT VARCHAR(5000) NULL,
			      	THEMEID INT NOT NULL,
			      	PRIMARY KEY (ID),
			      	FOREIGN KEY (THEMEID) REFERENCES THEME (ID)
			      	ON DELETE CASCADE
			    )
			""";

	public static final String CREATE_ANSWER_TABLE = """
			    CREATE TABLE IF NOT EXISTS ANSWER (
			      	ID INT NOT NULL AUTO_INCREMENT,
			      	TEXT VARCHAR(500) NOT NULL,
			      	ISCORRECT BOOLEAN NOT NULL,
			      	QUESTIONID INT NOT NULL,
			      	PRIMARY KEY (ID),
			      	FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID)
			      	ON DELETE CASCADE
			    )
			""";
	public static final String CREATE_PLAYER_TABLE = """
				CREATE TABLE IF NOT EXISTS PLAYERANSWER (
			  		ID INT AUTO_INCREMENT PRIMARY KEY,
			 		QUESTIONID INT NOT NULL,
			  		ANSWERID INT NOT NULL,
			  		SELECTED BOOLEAN NOT NULL,
			  		CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

			  		FOREIGN KEY (QUESTIONID) REFERENCES QUESTION(ID) ON DELETE CASCADE,
			  		FOREIGN KEY (ANSWERID) REFERENCES ANSWER(ID) ON DELETE CASCADE
				)
			""";
}
