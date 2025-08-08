package gui.mainPanels;

/**
 * Listener interface for reacting to changes in the list of themes.
 * <p>
 * Implement this interface if you want to be notified when themes
 * are added, removed, or otherwise modified in the system.
 * </p>
 * 
 * Typically used to update GUI components such as combo boxes or lists.
 * 
 * @author DejanKrstovski
 */
public interface ThemeChangeListener {

    /**
     * Called when the list of themes has changed.
     * <p>
     * This method should update any UI or internal state that depends
     * on the current list of available themes.
     * </p>
     */
    void onThemesChanged();
}

