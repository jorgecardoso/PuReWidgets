package org.purewidgets.shared.events;

import java.util.ArrayList;

import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;

/**
 * Represents a low-level input event. The WidgetManager directs WidgetInputEvents to individual widgets
 * when there is input from the interaction manager.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class WidgetInputEvent {

	
	/**
	 * The id of the user which generated the input.
	 */
	private String userId;
	
	/**
	 * The nickname of user which generated the input.
	 */
	private String nickname;
	
	
	/**
	 * The WidgetOption which was selected by the user.
	 */
	private WidgetOption widgetOption;
	
	
	/**
	 * The parameters issued by the user.
	 */
	private ArrayList<String> parameters;
	
	
	/**
	 * The age of the input, in milliseconds.
	 */
	private long age;
	

	/**
	 * Creates a new WidgetInputEvent with the specified widget option and parameters.
	 * @param widgetOption The widget option that is the target of this input event.
	 * @param parameters The parameters of this input.
	 */
	public WidgetInputEvent(WidgetOption widgetOption, ArrayList<String> parameters) {
		this("Anonymous", "Anonymous", widgetOption, parameters);
	}	
	
	/**
	 * Creates a new WidgetInputEvent from the specified widget input data, widget option and parameters.
	 * @param widgetInput The widget input that caused the event.
	 * @param widgetOption The widget option that is the target of this input event.
	 * @param parameters The parameters of this input.
	 */
	public WidgetInputEvent(WidgetInput widgetInput, WidgetOption widgetOption, ArrayList<String> parameters) {
		this(widgetInput.getUserId(), widgetInput.getNickname(), widgetOption, parameters);
	}	
	
	
	/**
	 * Creates a new InputEvent with the given WidgetOption and parameters.
	 * 
	 * @param userId The id of the user that was responsible for generating the input.
	 * @param nickname The nickname of the user that was responsible for generating the input.
	 * @param widgetOption The WidgetOption that was selected by the user.
	 * @param parameters The parameters the user sent in the input.
	 */	
	public WidgetInputEvent(String userId, String nickname, WidgetOption widgetOption, ArrayList<String> parameters) {
		this.userId = userId;
		this.nickname = nickname;
		this.widgetOption = widgetOption;
		this.parameters =  parameters;
		this.age = 0;
		
	}	
	

	
	/**
	 * Gets the parameters associated with this widget input event.
	 *  
	 * @return The parameters associated with this widget input event.
	 */
	public ArrayList<String> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Gets the widget option associated with this widget input event.
	 * @return the widget option associated with this widget input event.
	 */
	public WidgetOption getWidgetOption() {
		return this.widgetOption;
	}

	
	

	/**
	 * Gets the age of this widget input event.
	 * 
	 * @return the age
	 */
	public long getAge() {
		return age;
	}


	/**
	 * Sets the age of this widget input event.
	 * 
	 * @param age the age to set
	 */
	public void setAge(long age) {
		this.age = age;
	}


	/**
	 * Gets the nickname of the user associated with this widget input event.
	 * 
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}


	/**
	 * Gets the id of the  user associated with this widget input event.
	 * @return the id of the user.
	 */
	public String getUserId() {
		return userId;
	}
	
}
