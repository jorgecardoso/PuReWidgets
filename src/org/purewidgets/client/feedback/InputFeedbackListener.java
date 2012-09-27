/**
 * 
 */
package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PdWidget;

/**
 * The InputFeedbackListener defines methods associated with the life-cycle of a user input feedback panel.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface InputFeedbackListener {

	/**
	 * Triggered when the feedback is shown on the public display.
	 * 
	 * @param feedback The feedback that has been displayed.
	 */
	public void inputFeedbackStarted(InputFeedback<? extends PdWidget> feedback);
	
	
	/**
	 * Triggered when the feedback is removed from the public display.
	 * 
	 * @param feedback The feedback that has been removed.
	 * @param noMore If true, indicated that there is currently no more feedback for this widget; if false, indicates that
	 * thre is additional feedback to display.
	 */
	public void inputFeedbackEnded(InputFeedback<? extends PdWidget> feedback, boolean noMore);

}
