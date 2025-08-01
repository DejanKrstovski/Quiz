package helpers;

/**
 * Result class for handling error and message states.
 * This class follows the Singleton design pattern to ensure only one instance exists.
 */
public class ErrorHandler {

	private static ErrorHandler single_instance = null;
	private String error;
	private String info;
	
	private ErrorHandler() {
		error = "";
		info = "";
	}
	
	public static ErrorHandler getInstance() {
		if (single_instance == null) {
			single_instance = new ErrorHandler();
		}
		return single_instance;
	}
	
	public void setError(String error) {
		this.error = error;
		info = ""; // Clear message when setting a new error
	}
	
	public String getError() {
		return error;
	}
	
	public void setInfo(String message) {
		this.info = message;
		error = ""; // Clear error when setting a new message
	}
	
	public String getInfo() {
		return info;
	}
	
	public boolean hasError() {
        return error != null && !error.isEmpty();
    }
	
	/**
	 * Clears the error and message fields.
	 */
	public void clear() {
		error = "";
		info = "";
	}
}
