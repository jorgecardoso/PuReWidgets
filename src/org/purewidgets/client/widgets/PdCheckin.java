package org.purewidgets.client.widgets;

import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Checkin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * A check-in widget that reacts to users' check-ins. When a user checks-in, it triggers an ActionEvent. 
 * By default, a PdCheckin is graphically represented by a simple static image.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwCheckin</dt>
 * <dd>the outer element. </dd>
 * </dl> 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class PdCheckin extends PdWidget{
	private final String USER_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME + " checked-in "+MessagePattern.PATTERN_INPUT_AGE;
	private final String USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME;
	private final String USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN = "Checked-in "+ MessagePattern.PATTERN_INPUT_AGE;
	
	
	@UiTemplate("PdCheckin.ui.xml")
	interface PdCheckinUiBinder extends UiBinder<Widget, PdCheckin> {	}
	
	private static PdCheckinUiBinder uiBinder = GWT.create(PdCheckinUiBinder.class);
	
	private Checkin checkin;
	
	/**
	 * Creates a new check-in widget. A PdWidget has an internally defined widget id ("checkin"), 
	 * since it does not make sense
	 * to have multiple check-in widgets in an application.
	 * 
	 */
	public PdCheckin() {
		super("checkin");
		initWidget(uiBinder.createAndBindUi(this));
		
		checkin = new Checkin();
		this.setWidget(checkin);
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setOnScreenFeedbackInfo(USER_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackTitle(USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackInfo(USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN);
		
		Log.debug(this, "Sending to server");
		this.sendToServer();
	}
	
}
