package org.purewidgets.client.widgets;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Checkin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwCheckin</dt>
 * <dd>the outer element. </dd>
 *
 * </dl> 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class PdCheckin extends PdWidget{
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U% checked-in";
	
	@UiTemplate("PdCheckin.ui.xml")
	interface PdCheckinUiBinder extends UiBinder<Widget, PdCheckin> {	}
	private static PdCheckinUiBinder uiBinder = GWT.create(PdCheckinUiBinder.class);
	
	private Checkin checkin;
	
	public PdCheckin() {
		super("checkin");
		initWidget(uiBinder.createAndBindUi(this));
		
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
