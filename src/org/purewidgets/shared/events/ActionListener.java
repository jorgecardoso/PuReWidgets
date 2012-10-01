/**
 * 
 */
package org.purewidgets.shared.events;

/**
 * Interface for objects that want to listen to ActionEvents.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface ActionListener {
	
	/**
	 * Triggered by widgets to signal applications about a high-level input event.
	 * @param e The ActionEvent that describes the event.
	 */
	public void onAction(ActionEvent<?> e);
}
