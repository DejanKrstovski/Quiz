package quizlogic;

/**
 * Singleton class for managing error and informational messages across the
 * application.
 * <p>
 * The {@code ErrorHandler} allows globally setting and retrieving error and
 * info messages. Only one type of message (error or info) can be active at a
 * time — setting an error clears any existing info message, and setting an info
 * message clears any existing error.
 * </p>
 *
 * <p>
 * <b>Thread-safety:</b> This implementation is <i>not</i> thread-safe. In
 * multi-threaded environments, consider synchronizing {@link #getInstance()} or
 * using a thread-safe singleton variant.
 * </p>
 *
 * @author DejanKrstovski
 */
public final class ErrorHandler {

	/** The singleton instance of {@code ErrorHandler}. */
	private static ErrorHandler instance = null;

	/** The current error message; empty if none is set. */
	private String error;

	/** The current informational message; empty if none is set. */
	private String info;

	/**
	 * Private constructor to prevent external instantiation. Initializes both error
	 * and info messages to empty strings.
	 */
	private ErrorHandler() {
		error = "";
		info = "";
	}

	/**
	 * Returns the singleton instance of {@code ErrorHandler}, creating it if it
	 * does not exist.
	 * <p>
	 * <b>Note:</b> This method is not thread-safe.
	 * </p>
	 *
	 * @return the singleton instance of {@code ErrorHandler}
	 */
	public static ErrorHandler getInstance() {
		if (instance == null) {
			instance = new ErrorHandler();
		}
		return instance;
	}

	/**
	 * Sets the error message and clears any existing info message. Passing
	 * {@code null} clears the error message.
	 *
	 * @param error the error message to set, or {@code null} to clear it
	 */
	public void setError(String error) {
		this.error = (error == null) ? "" : error;
		this.info = "";
	}

	/**
	 * Returns the current error message.
	 *
	 * @return the current error message, or an empty string if none is set
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the informational message and clears any existing error message. Passing
	 * {@code null} clears the info message.
	 *
	 * @param message the info message to set, or {@code null} to clear it
	 */
	public void setInfo(String message) {
		this.info = (message == null) ? "" : message;
		this.error = "";
	}

	/**
	 * Returns the current informational message.
	 *
	 * @return the current info message, or an empty string if none is set
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Checks whether an error message is currently set.
	 *
	 * @return {@code true} if an error message is set; {@code false} otherwise
	 */
	public boolean hasError() {
		return error != null && !error.isEmpty();
	}

	/**
	 * Checks whether an info message is currently set.
	 *
	 * @return {@code true} if an info message is set; {@code false} otherwise
	 */
	public boolean hasInfo() {
		return info != null && !info.isEmpty();
	}

	/**
	 * Clears both the error and info messages.
	 */
	public void clear() {
		error = "";
		info = "";
	}
}
