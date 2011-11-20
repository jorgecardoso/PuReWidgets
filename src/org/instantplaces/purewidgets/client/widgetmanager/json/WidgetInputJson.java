package org.instantplaces.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;

import com.google.gwt.core.client.JsArrayString;

public class WidgetInputJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetInputJson() {

	}

	public final native int getAge() /*-{
		return this.age;
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
		wi.setPersona(this.getPersona());
		wi.setTimeStamp(this.getTimeStamp());
		wi.setWidgetId(this.getWidgetId());
		wi.setWidgetOptionId(this.getWidgetOptionId());
		wi.setParameters(this.getParameters());
		wi.setAge(this.getAge());
		return wi;
	}

	public final native String getWidgetOptionId() /*-{
		return this.widgetOptionId;
	}-*/;

	public final native void setAge(int age) /*-{
		this.age = age;
	}-*/;

	public final native void setParameters(String[] parameters) /*-{
		this.parameters = parameters;
	}-*/;

	public final native void setPersona(String persona) /*-{
		this.persona = persona;
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

	public final String toDebugString() {

		return "WidgetInput(" + "persona: " + this.getPersona() + "; parameters "
				+ this.getParameters().toString() + ")";

		// return "WidgetInput(widget:"+ getWidgetId() + "; persona: " +
		// getPersona() + "; parameters: ";
	}

}
