package quizlogic;

/**
 * Singleton class for managing error and informational messages across the application.
 * <p>
 * Provides methods to set and retrieve error and info messages, ensuring that only
 * one instance exists globally. Setting an error clears the info message and vice versa.
 * </p>
 *
 * <p>This class is not thread-safe by default; if used in a multi-threaded environment,
 * consider synchronizing the {@link #getInstance()} method or using other concurrency controls.</p>
 * 
 * @author  
 */
public final class ErrorHandler {

    /** The singleton instance of {@code ErrorHandler}. */
    private static ErrorHandler instance = null;

    /** The current error message; empty if none. */
    private String error;

    /** The current informational message; empty if none. */
    private String info;

    /**
     * Private constructor to prevent instantiation.
     * Initializes error and info messages to empty strings.
     */
    private ErrorHandler() {
        error = "";
        info = "";
    }

    /**
     * Returns the singleton instance of {@code ErrorHandler}, creating it if necessary.
     * <p>
     * Note: This method is not thread-safe.
     * </p>
     * 
     * @return the singleton {@code ErrorHandler} instance
     */
    public static ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }

    /**
     * Sets the error message.
     * Setting an error message clears any informational message.
     * <p>
     * The given message may be {@code null}, which will be treated as empty.
     * </p>
     * 
     * @param error the error message to set, or {@code null} to clear
     */
    public void setError(String error) {
        this.error = (error == null) ? "" : error;
        this.info = ""; // Clear info when error is set
    }

    /**
     * Returns the current error message.
     * 
     * @return the error message; empty string if none
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the informational message.
     * Setting an informational message clears any error message.
     * <p>
     * The given message may be {@code null}, which will be treated as empty.
     * </p>
     * 
     * @param message the informational message to set, or {@code null} to clear
     */
    public void setInfo(String message) {
        this.info = (message == null) ? "" : message;
        this.error = ""; // Clear error when info is set
    }

    /**
     * Returns the current informational message.
     * 
     * @return the informational message; empty string if none
     */
    public String getInfo() {
        return info;
    }

    /**
     * Checks whether there is a current error message set.
     * 
     * @return {@code true} if there is an error message; {@code false} if empty or unset
     */
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    /**
     * Checks whether there is a current informational message set.
     * 
     * @return {@code true} if there is an informational message; {@code false} if empty or unset
     */
    public boolean hasInfo() {
        return info != null && !info.isEmpty();
    }

    /**
     * Clears both error and informational messages.
     */
    public void clear() {
        error = "";
        info = "";
    }
}
