/**
 * 
 */
package org.purewidgets.shared.events;


import org.purewidgets.shared.im.WidgetOption;


/**
 * 
 * An event that widgets send to application.
 * 
 * An ActionEvent describes the user that caused the event to be triggered (user id, and nickname),
 * the source widget that triggered the event, the widget option that was the target of the user
 * input and additional widget specific parameter.
 * 
 * @author Jorge C. S. Cardoso
 */
public class ActionEvent<T> {
	private String userId;
	private String nickname;
	private T source;
	private WidgetOption optionId;
	private Object param;
	
	
	/**
	 * Creates a new ActionEvent from the specified WidgetInputEvent, source widget, and parameter.
	 * 
	 * @param widgetInputEvent The widget input event that caused the widget to trigger this ActionEvent.
	 * @param source The source widget.
	 * @param param Additional widget specific parameter.
	 */
	public ActionEvent(WidgetInputEvent widgetInputEvent, T source, Object param) {
		this(widgetInputEvent.getUserId(), widgetInputEvent.getNickname(), source, widgetInputEvent.getWidgetOption(), param);
	}
	
	/**
	 * Creates a new ActionEvent from the specified user id, nickname, source widget, and parameter.
	 * 
	 * @param userId The id of the user that caused this event.
	 * @param nichname The nickname of the user that caused this event.
	 * @param source The source widget.
	 * @param optionId The widget option that was the target of the user input
	 * @param param Additional widget specific parameter.
	 */
	public ActionEvent(String userId, String nickname, T source, WidgetOption optionId, Object param) {
		this.userId = userId;
		this.nickname = nickname;
		this.source = source;
		this.optionId = optionId;
		this.param = param;
	}
	
	

	
	/**
	 * Gets the source widget that fired the event.
	 * 
	 * @return The Widget object that fired this event.
	 */
	public T getSource() {
		return this.source;
	}
	
	/**
	 * Gets the widget option that was the target of the user input.
	 * 
	 * @return The option id that this event refers to.
	 */
	public WidgetOption getSelection() {
		return optionId;
	}

	/**
	 * Gets the widget specific parameter.
	 * 
	 * @return the widget specific parameter.
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * Get the user's nickname
	 * 
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}



	/**
	 * Gets the user's id.
	 * 
	 * @return the user's id.
	 */
	public String getUserId() {
		return userId;
	}


	
}
