package org.purewidgets.shared.events;

import java.util.ArrayList;

import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;

import com.google.gwt.core.client.GWT;



/**
 * Represents an input event.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class WidgetInputEvent {

	
	/**
	 * The identity which generated the input.
	 */
	private String userId;
	
	/**
	 * The user name that can be displayed on the PD
	 */
	private String nickname;
	
	
	/**
	 * The WidgetOption which was selected by the user.
	 */
	private WidgetOption widgetOption;
	
	
	/**
	 * The command issued by the user.
	 */
	private ArrayList<String> parameters;
	
	
	/**
	 * The age of the input, in milliseconds.
	 */
	private long age;
	
	InputEventAgeMessages messages;
	
	public WidgetInputEvent(WidgetOption widgetOption, ArrayList<String> parameters) {
		this("Anonymous", "Anonymous", widgetOption, parameters);
	}	
	
	/**
	 * Creates a new InputEvent with the given WidgetOption and parameters.
	 * This constructor is used when there is no persona associated (i.e., the input was
	 * generated locally at the Gui).
	 * 
	 * @param userId The identity that was responsible for generating the input.
	 * @param widgetOption The WidgetOption that was selected by the user.
	 * @param parameters The parameters the user sent in the input.
	 */	
	public WidgetInputEvent(WidgetInput widgetInput, WidgetOption widgetOption, ArrayList<String> parameters) {
		this(widgetInput.getUserId(), widgetInput.getNickname(), widgetOption, parameters);
	}	
	
	
	/**
	 * Creates a new InputEvent with the given UserId, nickname, WidgetOption and parameters.
	 * 
	 * @param persona The identity that was responsible for generating the input.
	 * @param widgetOption The WidgetOption that was selected by the user.
	 * @param parameters The parameters the user sent in the input.
	 */	
	public WidgetInputEvent(String userId, String nickname, WidgetOption widgetOption, ArrayList<String> parameters) {
		this.userId = userId;
		this.nickname = nickname;
		this.widgetOption = widgetOption;
		this.parameters =  parameters;
		this.age = 0;
		this.messages = GWT.create(InputEventAgeMessages.class);
	}	
	

	
	/**
	 *  
	 * @return The parameters associated with this InputEvent.
	 */
	public ArrayList<String> getParameters() {
		return this.parameters;
	}
	
	/**
	 * @return the optionID
	 */
	public WidgetOption getWidgetOption() {
		return this.widgetOption;
	}

	public String getAgeString() {
		float ageSeconds = this.age/1000;
		
		float ageMinutes = ageSeconds/60;
		float ageHours = ageMinutes/60;
		float ageDays = ageHours/24;
		float ageWeeks = ageDays/7;
		
		if ( ageWeeks > 1 ) {
			return messages.ageWeek( (int)ageWeeks );
		} else if ( ageDays > 1 ) {
			return messages.ageDay( (int)ageDays );
		} else if ( ageHours > 1 ) {
			return messages.ageHour( (int)ageHours );
		} else if ( ageMinutes > 1 ) {
			return messages.ageMinute( (int)ageMinutes );
		} else {
			return messages.ageSecond( (int)ageSeconds );
		}
		
	}
	

	/**
	 * @return the age
	 */
	public long getAge() {
		return age;
	}


	/**
	 * @param age the age to set
	 */
	public void setAge(long age) {
		this.age = age;
	}


	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	
}
