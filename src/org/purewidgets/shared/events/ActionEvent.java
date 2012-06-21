/**
 * 
 */
package org.purewidgets.shared.events;
import org.purewidgets.client.widgets.GuiWidget;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;



/**
 * Represents the event that is sent to applications.
 * 
 * @author Jorge C. S. Cardoso
 */
public class ActionEvent<T> {
	private String userId;
	private String nickname;
	private T source;
	private WidgetOption optionId;
	private Object param;
	
	
	
	public ActionEvent(WidgetInputEvent widgetInputEvent, T source, Object param) {
		this(widgetInputEvent.getUserId(), widgetInputEvent.getNickname(), source, widgetInputEvent.getWidgetOption(), param);
	}
	
	/**
	 * Creates an ActionEvent object.
	 * @param 
	 * @param source The object (Widget object) that fired the action event.
	 * @param optionId The option id of the widget that this event refers to.
	 * @param param Optional widget specific object.
	 */
	public ActionEvent(String userId, String nickname, T source, WidgetOption optionId, Object param) {
		this.userId = userId;
		this.nickname = nickname;
		this.source = source;
		this.optionId = optionId;
		this.param = param;
	}
	
	

	
	/**
	 * 
	 * @return The Widget object that fired this event.
	 */
	public T getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return The option id that this event refers to.
	 */
	public WidgetOption getSelection() {
		return optionId;
	}

	/**
	 * @return the param
	 */
	public Object getParam() {
		return param;
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
