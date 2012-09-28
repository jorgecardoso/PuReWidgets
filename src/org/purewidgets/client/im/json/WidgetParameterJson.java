package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.WidgetParameter;

/**
 * 
 * WidgetParameterJson is a Json DTO for receiving and sending a widget parameter to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.WidgetParameter
 */
public class WidgetParameterJson extends GenericJson  {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetParameterJson() { 
	}
	

	/**
	 *
	 * Creates a WidgetParameterJson object based on a {@link WidgetParameter} object
	 * 
	 * @param widgetOption The WidgetParameter object to convert.
	 * @return The converted object.
	 */
	public static WidgetParameterJson create(WidgetParameter widgetParameter) {
		WidgetParameterJson parameterJSON = GenericJson.getNew();
		
		parameterJSON.setWidgetParameterName(widgetParameter.getName());
		parameterJSON.setWidgetParameterValue(widgetParameter.getValue());
		
		return parameterJSON;
	}
	
	/**
	 * Gets the WidgetParameter object that this WidgetParameterJson object is representing.
	 * 
	 * @return The WidgetParameter object that this WidgetParameterJson object is representing.
	 */		
	public final WidgetParameter getWidgetParameter() {
		WidgetParameter parameter = new WidgetParameter(this.getWidgetParameterName(), this.getWidgetParameterValue());
		return parameter;
	}

	
	/**
	 * Sets the name of the widget parameter
	 * 
	 * @param name The name to set.
	 */
	public final native void setWidgetParameterName(String name) /*-{
		this.name = name;
	}-*/;


	/**
	 * Gets the name of the widget parameter.
	 * 
	 * @return The name of the widget parameter.
	 */
	public final native String getWidgetParameterName() /*-{
		return this.name;
	}-*/;


	/**
	 * Sets the value of the widget parameter.
	 * 
	 * @param value The value of the widget parameter.
	 */
	public final native void setWidgetParameterValue(String value) /*-{
		this.value = value;
	}-*/;


	/**
	 * Gets the value of the widget parameter.
	 * 
	 * @return The value of the widget parameter.
	 */
	public final native String getWidgetParameterValue() /*-{
		return this.value;
	}-*/;

}
