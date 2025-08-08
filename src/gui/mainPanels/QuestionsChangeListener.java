package gui.mainPanels;

/**
 * Listener interface to be implemented by classes that want to be notified
 * when the list of questions has changed.
 * <p>
 * Typically used to update UI components or perform actions whenever
 * questions are added, removed, or modified.
 * </p>
 * 
 * <p>
 * Implementing classes should override the {@code onQuestionsChanged} method
 * to define behavior triggered by question list updates.
 * </p>
 * 
 * @author DejanKrstovski
 */
public interface QuestionsChangeListener {

    /**
     * Callback method invoked when the questions list has changed.
     * <p>
     * This method should contain logic to refresh UI components,
     * reload data, or handle any state changes related to the questions.
     * </p>
     */
    void onQuestionsChanged();
}
