/**
 * 
 */
package org.purewidgets.shared.events;

import java.util.ArrayList;


/**
 * Interface for objects that want to listen to widget input events.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface WidgetInputListener {
	
	/**
	 * Event triggered by the InteractionManager to signal
	 * input to this widget.
	 * 
	 * @param ie A list of widget input event objects.
	 */
	public void onInput(ArrayList<WidgetInputEvent> ie);
}
