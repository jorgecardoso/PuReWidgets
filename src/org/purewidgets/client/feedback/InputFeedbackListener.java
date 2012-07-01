/**
 * 
 */
package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PDWidget;

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
	public void inputFeedbackStarted(InputFeedback<? extends PDWidget> feedback);
	
	
	/**
	 * Stops the processing of the specified feedback.
	 * This method will always be called with the same InputFeedback that
	 * the previous start method and will always be interleaved with start calls.
	 * 
	 * @param feedback The feedback to stop showing. 
	 */
	public void inputFeedbackEnded(InputFeedback<? extends PDWidget> feedback, boolean noMore);


	/**
	 * Triggered when the widget stops being visible
	 */
	public void widgetVisibilityChanged();
}
