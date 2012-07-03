package org.purewidgets.client.widgets;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Checkin;

public class PdCheckin extends PdWidget{
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U% checked-in";
	
	private Checkin checkin;
	
	public PdCheckin() {
		super("checkin");
		checkin = new Checkin();
		this.setWidget(checkin);
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setUserInputFeedbackPattern(DEFAULT_USER_INPUT_FEEDBACK_PATTERN);
		
		Log.debug(this, "Sending to server");
		this.sendToServer();
	}
	
}
