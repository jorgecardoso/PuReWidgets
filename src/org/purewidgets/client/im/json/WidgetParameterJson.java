package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.WidgetParameter;

public class WidgetParameterJson extends GenericJson  {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetParameterJson() { 
	}
	
	
	public final native void setWidgetParameterName(String name) /*-{
		this.name = name;
	}-*/;


	public final native String getWidgetParameterName() /*-{
		return this.name;
	}-*/;


	public final native void setWidgetParameterValue(String value) /*-{
		this.value = value;
	}-*/;


	public final native String getWidgetParameterValue() /*-{
		return this.value;
	}-*/;


	public static WidgetParameterJson create(WidgetParameter widgetParameter) {
		WidgetParameterJson parameterJSON = GenericJson.getNew();
		
		parameterJSON.setWidgetParameterName(widgetParameter.getName());
		parameterJSON.setWidgetParameterValue(widgetParameter.getValue());
		
		return parameterJSON;
	}
	
	public final WidgetParameter getWidgetParameter() {
		WidgetParameter parameter = new WidgetParameter(this.getWidgetParameterName(), this.getWidgetParameterValue());
		return parameter;
	}

}
