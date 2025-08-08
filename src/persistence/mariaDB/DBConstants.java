package persistence.mariaDB;

/**
 * Constants used for configuring the connection to the MariaDB database.
 * <p>
 * This interface holds the URL, username, and password required to establish 
 * a database connection. These values should be set appropriately before use.
 * </p>
 * <p>
 * Typically, these constants are populated with actual connection information such as:
 * <ul>
 *   <li><b>DB_URL</b>: the JDBC URL to connect to the MariaDB instance</li>
 *   <li><b>USER</b>: the username for database authentication</li>
 *   <li><b>PASSWORD</b>: the password for database authentication</li>
 * </ul>
 * </p>
 * <p>
 * <b>Note:</b> For security reasons, it is recommended to externalize these 
 * credentials in configuration files or environment variables rather than hardcoding them.
 * </p>
 * 
 * @author 
 */
public interface DBConstants {

    /**
     * The JDBC URL for connecting to the MariaDB database.
     */
    public static final String DB_URL = null;

    /**
     * Username used to authenticate with the MariaDB database.
     */
    public static final String USER = null;

    /**
     * Password corresponding to the database user.
     */
    public static final String PASSWORD = null;
}
