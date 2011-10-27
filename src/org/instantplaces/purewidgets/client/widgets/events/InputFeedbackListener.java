/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets.events;

import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.client.widgets.feedback.InputFeedback;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public interface InputFeedbackListener {

	/**
	 * Triggers the processing of the specified InputFeedback.
	 * 
	 * @param feedback The feedback to process.
	 */
	public void inputFeedbackStarted(InputFeedback<? extends GuiWidget> feedback);
	
	
	/**
	 * Stops the processing of the specified feedback.
	 * This method will always be called with the same InputFeedback that
	 * the previous start method and will always be interleaved with start calls.
	 * 
	 * @param feedback The feedback to stop showing. 
	 */
	public void inputFeedbackEnded(InputFeedback<? extends GuiWidget> feedback, boolean noMore);
}
