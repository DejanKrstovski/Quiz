package persistence;

import java.io.Serializable;

import bussinesLogic.DataTransportObject;

/**
 * A basic data access object (DAO) class that implements {@link Serializable}.
 * <p>
 * This class holds a unique identifier which can be used to represent
 * persistent entities or records in a data storage context.
 * </p>
 * <p>
 * The class is serializable, allowing instances to be serialized for 
 * persistence or transmission.
 * </p>
 * 
 * @author 
 */
public abstract class DataAccessObject{

    /** Unique identifier for this data object. Defaults to -1 to indicate uninitialized state. */
    protected int id = -1;
    
	public DataAccessObject(int id) {
		super();
		this.id = id;
	}

	public DataAccessObject() {
		super();
	}
	
	/**
	 * Converts a DataAccesObject to a DataAccesObject.<br>
	 * Evry class that extens this class have to implement this method. 
	 * @return
	 */
	public abstract DataTransportObject toDTO();
	
    /**
     * Returns the identifier of this data object.
     *
     * @return the current unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier of this data object.
     *
     * @param id the new unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
