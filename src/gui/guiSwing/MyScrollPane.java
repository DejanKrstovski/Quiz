package gui.guiSwing;

import java.awt.Component;
import javax.swing.JScrollPane;

/**
 * A custom {@link JScrollPane} subclass intended for consistent styling and
 * easier extension across the application.
 * <p>
 * While this class currently behaves like a standard {@code JScrollPane}, it
 * serves as a placeholder for future customization of scroll behavior or visual
 * style.
 * </p>
 */
public class MyScrollPane extends JScrollPane {

	/**
	 * Constructs an empty {@code MyScrollPane} with default settings.
	 */
	public MyScrollPane() {
		super();
	}

	/**
	 * Constructs a {@code MyScrollPane} that displays the specified component.
	 *
	 * @param view the component to display within the scroll pane
	 */
	public MyScrollPane(Component view) {
		super(view);
	}

	/**
	 * Constructs a {@code MyScrollPane} with the specified component and scrollbar
	 * policies.
	 *
	 * @param view      the component to display
	 * @param vsbPolicy vertical scrollbar policy
	 * @param hsbPolicy horizontal scrollbar policy
	 */
	public MyScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}
}
