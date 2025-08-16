package persistence.mariaDB;

import java.sql.PreparedStatement;

import persistence.DataAccessObject;

/**
 * Class for all entity classes of package persistence.maria.entities
 */
public abstract class MariaAccessObject extends DataAccessObject {

	public MariaAccessObject(int id) {
		super(id);
	}

	public MariaAccessObject() {
		super();
	}

	/**
	 * Composes a command sql-select, that will be required of maria.DBManager.
	 * 
	 * @return sql-select statement
	 */
	public abstract String getSelectStatement();

	/**
	 * Composes a command sql-update, that will be required of maria.DBManager.
	 * 
	 * @return sql-update statement
	 */
	public abstract String getUpdateStatement();

	/**
	 * Composes a command sql-insert, that will be required of maria.DBManager.
	 * 
	 * @return sql-insert statement 
	 */
	public abstract String getInsertStatement();
	
	/**
	 * Composes a command sql-delete, that will be required of maria.DBManager.
	 */
	public abstract String getDeleteStatement();
	/**
	 * Prepares a sql.PreparedStatement, e.c. fills the statement with values.<br>
	 * The method will be called by maria.DBManager.
	 * 
	 * @param sql-insert statement
	 * @return
	 */

	public abstract void prepareInsert(PreparedStatement stmt);
    public abstract void prepareUpdate(PreparedStatement stmt);
    public abstract void prepareDelete(PreparedStatement stmt);
}
