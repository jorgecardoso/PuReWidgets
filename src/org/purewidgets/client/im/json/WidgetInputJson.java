package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.WidgetInput;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 *  
 * WidgetInputJson is a Json DTO for receiving and sending widget input information to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.WidgetInput
 */
public class WidgetInputJson extends GenericJson {
	// Overlay types always have protected, zero-arg ctors
	protected WidgetInputJson() {

	}
	
	/**
	 * Creates a WidgetInputJson object based on a {@link WidgetInput} object
	 * 
	 * @return The WidgetInputJson converted object.
	 */
	public static WidgetInputJson create(WidgetInput widgetInput) {
		WidgetInputJson widgetInputJson = GenericJson.getNew();
		
		widgetInputJson.setUserId(widgetInput.getUserId());
		widgetInputJson.setNickname(widgetInput.getNickname());
		widgetInputJson.setParametersFromJavaArray(widgetInput.getParameters().toArray(new String[widgetInput.getParameters().size()]));
		widgetInputJson.setTimeStamp(widgetInput.getTimeStamp());
		widgetInputJson.setWidgetId(widgetInput.getWidgetId());
		widgetInputJson.setWidgetOptionId(widgetInput.getWidgetOptionId());
		widgetInputJson.setInputMechanism(widgetInput.getInputMechanism());
		widgetInputJson.setReferenceCode(widgetInput.getReferenceCode());
		
		return widgetInputJson;
	}

	/**
	 * Gets the WidgetInput object that this WidgetInputJson object is representing.
	 * 
	 * @return The WidgetInput object that this WidgetInputJson object is representing.
	 */	
	public final WidgetInput getWidgetInput() {
		WidgetInput wi = new WidgetInput();
		wi.setUserId(this.getUserId());
		wi.setNickname(this.getNickname());
		wi.setTimeStamp(this.getTimeStamp());
		wi.setWidgetId(this.getWidgetId());
		wi.setWidgetOptionId(this.getWidgetOptionId());
		wi.setParameters(this.getParameters());
		wi.setAge(this.getAge());
		wi.setInputMechanism(this.getInputMechanism());
		return wi;
	}

	
	/**
	 * Gets the age of the widget input
	 * 
	 * @return The the age of the widget input.
	 */	
	public final native int getAge() /*-{
		return this.age;
	}-*/;

	/**
	 * Gets the name of the input mechanism used by the user
	 * 
	 * @return The name of input mechanism used by the user.
	 */		
	public final native String getInputMechanism() /*-{
		return this.inputMechanism;
	}-*/;

	/**
	 * Gets an ArrayList with the parameters of this widget input.
	 * 
	 * @return An ArrayList with the parameters of this widget input.
	 */		
	public final ArrayList<String> getParameters() {
		JsArrayString jsonArray = getParametersAsJsArrayString();
		ArrayList<String> p = new ArrayList<String>();// [jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			p.add(jsonArray.get(i));
		}
		return p;

	}

	/**
	 * Gets an JsArray with the parameters of this widget input.
	 * 
	 * @return A JsArray with the parameters of this widget input.
	 */	
	public final native JsArrayString getParametersAsJsArrayString() /*-{
		return this.parameters;
	}-*/;

	
	/**
	 * Gets the id of the user that issued this widget input.
	 * 
	 * @return The id of the user that issued this widget input.
	 */		
	public final native String getUserId() /*-{
		return this.userId;
	}-*/;
	
	/**
	 * Gets the nickname of the user that issued this widget input.
	 * 
	 * @return The nickname of the user that issued this widget input.
	 */		
	public final native String getNickname() /*-{
		return this.nickname;
	}-*/;

	
	/**
	 * Gets the timestamp of the creation of this widget input.
	 * 
	 * @return The timestamp of the creation of this widget input.
	 */		
	public final native String getTimeStamp() /*-{
		return this.timeStamp;
	}-*/;

	/**
	 * Gets the id of the widget to which this widget input is directed.
	 * 
	 * @return The id of the widget to which this widget input is directed.
	 */		
	public final native String getWidgetId() /*-{
		return this.widgetId;
	}-*/;
	
	
	/**
	 * Gets the reference code of the widget option to which this widget input is directed.
	 * 
	 * @return The reference code of the widget option to which this widget input is directed.
	 */		
	public final native String getReferenceCode() /*-{
		return this.referenceCode;
	}-*/;		

	

	/**
	 * Gets the widget option id to which this widget input is directed.
	 * 
	 * @return The widget option id to which this widget input is directed.
	 */		
	public final native String getWidgetOptionId() /*-{
		return this.widgetOptionId;
	}-*/;

	/**
	 * Sets the age of this widget input
	 * 
	 * @param age The age of this widget input
	 */
	public final native void setAge(int age) /*-{
		this.age = age;
	}-*/;

	/**
	 * Sets the input mechanism name.
	 * 
	 * @param inputMechanism The input mechanism name to set.
	 */	
	public final native void setInputMechanism(String inputMechanism) /*-{
		this.inputMechanism = inputMechanism;
	}-*/;
	
	/**
	 * Sets the parameters of this widget input as a JsArrayString.
	 * 
	 * @param parameters The JsArrayString parameters to set.
	 */
	public final native void setParameters(JsArrayString parameters) /*-{
		this.parameters = parameters;
	}-*/;

	/**
	 * Sets the parameters of this widget input.
	 * 
	 * @param parameters The parameters to set.
	 */
	public final void setParametersFromJavaArray(String[] parameters) {
		JsArrayString jsArray = (JsArrayString) JavaScriptObject.createArray();

		for (String param : parameters) {
			jsArray.push(param);
		}
		this.setParameters(jsArray);
	}

	/**
	 * Sets the nickname of the user that issued this widget input.
	 * 
	 * @param persona The nickname of the user to set.
	 */	
	public final native void setNickname(String persona) /*-{
		this.nickname = persona;
	}-*/;
	
	/**
	 * Sets the id of the user that issued this widget input.
	 * 
	 * @param userIdentifier The id of the user to set.
	 */		
	public final native void setUserId(String userIdentifier) /*-{
		this.userId = userIdentifier;
	}-*/;	

	/**
	 * Sets the timestamp of the creation of this widget input.
	 * 
	 * @param timeStamp The timestamp to set.
	 */		
	public final native void setTimeStamp(String timeStamp) /*-{
		this.timeStamp = timeStamp;
	}-*/;
	
	/**
	 * Sets the id of the widget to which this widget input is directed.
	 * 
	 * @param widgetId The id of the widget to set.
	 */		
	public final native void setWidgetId(String widgetId) /*-{
		this.widgetId = widgetId;
	}-*/;

	/**
	 * Sets the id of the widget option to which this widget input is directed.
	 * 
	 * @param widgetOptionId The id of the widget option to set.
	 */		
	public final native void setWidgetOptionId(String widgetOptionId) /*-{
		this.widgetOptionId = widgetOptionId;
	}-*/;
	
	/**
	 * Sets the reference code to which this widget input is directed.
	 * 
	 * @param referenceCode The reference code to set.
	 */		
	public final native void setReferenceCode(String referenceCode) /*-{
		this.referenceCode = referenceCode;
	}-*/;	
}
