package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PdWidget;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;

/**
 * 
 * Represents the feedback information given by PdWidgets to the users.<p>
 * 
 * The feedback information consists of a title and info for an on-screen (widgets that are visible 
 * on the public display)
 * widget feedback and a 
 * title and description for off-screen (widgets that are not visible on the public display) widget feedback.
 * 
 * Feedback can be presented to users in two ways, depending on the visual state of the corresponding
 * PdWidget.<br>
 * For on-screen PdWidgets the feedback is shown on a panel next to the PdWidget. <br>
 * For off-screen PdWidgets the feedback is shown on a panel at the bottom of the screen.<br>
 * 
 * The feedback messages (title and info) typically use {@link MessagePattern} to define various 
 * variables related to the user input.<p>
 * 
 * 
 * An InputFeedback object has references to other input related objects: the WidgetInputEvent that was triggered 
 * in the PdWidget; the ActionEvent that will be triggered to the application; and the PdWidget itself. This allows the toolkit
 * to trigger the ActionEvent to the PdWidget asynchronously when the input feedback is shown on the display and not immediately
 * when the widget input is received.<p>  
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class InputFeedback<T extends PdWidget> {

	/**
	 * The status of the feedback.
	 * The feedback can be Accepted by the PdWidget, which will then trigger an application event; or it
	 * can be Not Accepted (due to an error in the parameters, for example).
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	public enum Type {ACCEPTED, NOT_ACCEPTED};
	
	/**
	 * The InputEvent that originated this feedback.
	 */
	private WidgetInputEvent inputEvent;
	
	/**
	 * For on-screen PdWidgets, the information that is going to be displayed as a title in the 
	 * feedback panel next to the
	 * PdWidget that received the input.
	 */
	private String onScreenWidgetFeedbackTitle;
	
	
	/**
	 * For on-screen PdWidgets, the information that is going to be displayed in a feedback panel next to the
	 * PdWidget that received the input.
	 */
	private String onScreenWidgetFeedbackInfo;
	
	/**
	 * The title displayed for feedback for off-screen widgets.
	 */
	private String offScreenWidgetFeedbackTitle;
	
	/**
	 * The info line displayed for feedback for off-screen widgets.
	 */
	private String offScreenWidgetFeedbackInfo;
	
	/**
	 * The type of feedback (Accepted, not accepted)
	 */
	private Type type; 
	
	/**
	 * The action event to trigger if the input was accepted.
	 */
	private ActionEvent<T> actionEvent;
	
	/**
	 * The widget to which this feedback is associated.
	 */
	private T widget;
	
	
	/**
	 * Creates a new InputFeedback object for the specified widget and widget input event, with the specified
	 * Type, and ActionEvent.
	 * 
	 * @param widget The PdWidget that received the input.
	 * @param event The WidgetInputEvent that originated the feedback.
	 * @param type The Type of this input feedback.
	 * @param ae The ActionEvent associated with the input that originated this feedback.
	 */
	public InputFeedback(T widget, WidgetInputEvent event,  Type type, ActionEvent<T> ae) {
		this.widget = widget;
		this.inputEvent =  event;
		this.actionEvent = ae;
		this.onScreenWidgetFeedbackInfo = "";
		this.type = (null == type ? Type.ACCEPTED : type);
		//this.time = System.currentTimeMillis(); //getTimeStampMilli();
	}
	
	/*
	
	private long getTimeStampMilli() {
        Date date = new Date();
        return date.getTime();
	}*/

//	/**
//	 * Gets the age, in milliseconds, of this 
//	 * @return The age of this info in milliseconds.
//	 */
//	public long age() {
//		//Log.debug("Now: " + getTimeStampMilli() + "Info time:" + time);
//		//return (getTimeStampMilli()-time);
//		return System.currentTimeMillis()-this.time;
//	}
	
	/**
	 * Sets the feedback info text.
	 * 
	 * @param info the info to set.
	 */
	public void setOnScreenWidgetFeedbackInfo(String info) {
		this.onScreenWidgetFeedbackInfo = info;
	}

	/**
	 * Gets the feedback info.
	 * 
	 * @return the info
	 */
	public String getOnScreenWidgetFeedbackInfo() {
		return onScreenWidgetFeedbackInfo;
	}

//	/**
//	 * 
//	 * @param time the time to set
//	 */
//	public void setTime(long time) {
//		this.time = time;
//	}

//	/**
//	 * @return the time
//	 */
//	public long getTime() {
//		return time;
//	}


	/**
	 * Sets the input feedback status type.
	 * 
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Gets the input feedback status type.
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	
	@Override
	public String toString() {
		return this.onScreenWidgetFeedbackInfo+": " + this.type.toString();
	}


	/**
	 * Sets the WidgetInputEvent associated with this feedback.
	 * 
	 * @param inputEvent The WidgetInputEvent to set
	 */
	public void setInputEvent(WidgetInputEvent inputEvent) {
		this.inputEvent = inputEvent;
	}

	/**
	 * Gets the WidgetInputEvent associated with this feedback.
	 * 
	 * @return the inputEvent
	 */
	public WidgetInputEvent getInputEvent() {
		return inputEvent;
	}

	/**
	 * Sets the ActionEvent associated with this feedback.
	 * 
	 * @param actionEvent The ActionEvent associated with this feedback.
	 */
	public void setActionEvent(ActionEvent<T> actionEvent) {
		this.actionEvent = actionEvent;
	}


	/**
	 * Gets the ActionEvent associated with this feedback.
	 * 
	 * @return The ActionEvent associated with this feedback.
	 */
	public ActionEvent<T> getActionEvent() {
		return actionEvent;
	}


	/**
	 * Gets the PdWidget associated with this feedback.
	 * 
	 * @return The PdWidget associated with this feedback.
	 */
	public T getWidget() {
		return widget;
	}


	/**
	 * Sets the PdWdget associated with this feedback.
	 * 
	 * @param widget the widget to set
	 */
	public void setWidget(T widget) {
		this.widget = widget;
	}


	/**
	 * Gets the title for the shared panel of this feedback.
	 * 
	 * @return the sharedFeedbackTitle
	 */
	public String getOffScreenWidgetFeedbackTitle() {
		return offScreenWidgetFeedbackTitle;
	}


	/**
	 * Sets the title for the shared panel feedback.
	 * @param sharedFeedbackTitle the sharedFeedbackTitle to set
	 */
	public void setOffScreenWidgetFeedbackTitle(String sharedFeedbackTitle) {
		this.offScreenWidgetFeedbackTitle = sharedFeedbackTitle;
	}


	/**
	 * Gets the info for the shared panel feedback.
	 * @return the sharedFeedbackInfo
	 */
	public String getOffScreenWidgetFeedbackInfo() {
		return offScreenWidgetFeedbackInfo;
	}


	/**
	 * Sets the info for the shared panel feedback.
	 * 
	 * @param sharedFeedbackInfo the sharedFeedbackInfo to set
	 */
	public void setOffScreenWidgetFeedbackInfo(String sharedFeedbackInfo) {
		this.offScreenWidgetFeedbackInfo = sharedFeedbackInfo;
	}

	/**
	 * Gets the feedback title for on-screen widgets.
	 * 
	 * @return The feedback title for on-screen widgets.
	 */
	public String getOnScreenWidgetFeedbackTitle() {
		return onScreenWidgetFeedbackTitle;
	}

	/**
	 * Sets the feedback title for on-screen widgets. 
	 * 
	 * @param onScreenWidgetFeedbackTitle The title to set
	 */
	public void setOnScreenWidgetFeedbackTitle(String onScreenWidgetFeedbackTitle) {
		this.onScreenWidgetFeedbackTitle = onScreenWidgetFeedbackTitle;
	}
}
