package org.instantplaces.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;
import com.google.gwt.core.client.JsArrayString;

public class WidgetInputJson extends GenericJson {

	/**
	 * Converts a WidgetInput to a WidgetInputJson representation
	 * 
	 * @return
	 */
	public static WidgetInputJson create(WidgetInput widgetInput) {
		WidgetInputJson widgetInputJson = GenericJson.getNew();
		
		widgetInputJson.setUserIdentifier(widgetInput.getUserIdentifier());
		widgetInputJson.setPersona(widgetInput.getPersona());
		widgetInputJson.setParametersFromJavaArray(widgetInput.getParameters().toArray(new String[widgetInput.getParameters().size()]));
		widgetInputJson.setTimeStamp(widgetInput.getTimeStamp());
		widgetInputJson.setWidgetId(widgetInput.getWidgetId());
		widgetInputJson.setWidgetOptionId(widgetInput.getWidgetOptionId());
		widgetInputJson.setInputMechanism(widgetInput.getInputMechanism());
		
		return widgetInputJson;
	}

	// Overlay types always have protected, zero-arg ctors
	protected WidgetInputJson() {

	}
	
	public final native int getAge() /*-{
		return this.age;
	}-*/;

	public final native String getInputMechanism() /*-{
		return this.inputMechanism;
	}-*/;

	public final ArrayList<String> getParameters() {
		JsArrayString jsonArray = getParametersAsJsArrayString();
		ArrayList<String> p = new ArrayList<String>();// [jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			p.add(jsonArray.get(i));
		}
		return p;

	}

	public final native JsArrayString getParametersAsJsArrayString() /*-{
		return this.parameters;
	}-*/;

	public final native String getUserIdentifier() /*-{
	return this.userIdentifier;
}-*/;
	
	public final native String getPersona() /*-{
		return this.persona;
	}-*/;

	public final native String getTimeStamp() /*-{
		return this.timeStamp;
	}-*/;

	public final native String getWidgetId() /*-{
		return this.widgetId;
	}-*/;

	public final WidgetInput getWidgetInput() {
		WidgetInput wi = new WidgetInput();
		wi.setUserIdentifier(this.getUserIdentifier());
		wi.setPersona(this.getPersona());
		wi.setTimeStamp(this.getTimeStamp());
		wi.setWidgetId(this.getWidgetId());
		wi.setWidgetOptionId(this.getWidgetOptionId());
		wi.setParameters(this.getParameters());
		wi.setAge(this.getAge());
		wi.setInputMechanism(this.getInputMechanism());
		return wi;
	}
	
	public final native String getWidgetOptionId() /*-{
		return this.widgetOptionId;
	}-*/;

	public final native void setAge(int age) /*-{
		this.age = age;
	}-*/;

	
	public final native void setInputMechanism(String inputMechanism) /*-{
		this.inputMechanism = inputMechanism;
	}-*/;

	public final native void setParameters(JsArrayString parameters) /*-{
		this.parameters = parameters;
	}-*/;


	public final void setParametersFromJavaArray(String[] parameters) {
		JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();

		for (String param : parameters) {
			jsArray.push(param);
		}
		this.setParameters(jsArray);
	}

	public final native void setPersona(String persona) /*-{
		this.persona = persona;
	}-*/;
	
	public final native void setUserIdentifier(String userIdentifier) /*-{
	this.userIdentifier = userIdentifier;
}-*/;	

	public final native void setTimeStamp(String timeStamp) /*-{
		this.timeStamp = timeStamp;
	}-*/;
	
	public final native void setWidgetId(String widgetId) /*-{
		this.widgetId = widgetId;
	}-*/;

	public final native void setWidgetOptionId(String widgetOptionId) /*-{
		this.widgetOptionId = widgetOptionId;
	}-*/;
}
