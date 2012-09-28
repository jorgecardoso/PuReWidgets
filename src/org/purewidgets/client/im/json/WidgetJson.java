package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;

import com.google.gwt.core.client.JsArray;

/**
 * WidgetJson is a Json DTO for receiving and sending widgets to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.Widget
 */
public class WidgetJson extends GenericJson {
	
	// Overlay types always have protected, zero-arg ctors
	protected WidgetJson() {
	}
	
	/**
	 * Creates a WidgetJson object based on a Widget object
	 * 
	 * @return The WidgetJson converted object.
	 */
	public static WidgetJson create(Widget widget) {
		WidgetJson widgetJson = GenericJson.getNew();

		widgetJson.setPlaceId(widget.getPlaceId());
		widgetJson.setApplicationId(widget.getApplicationId());
		widgetJson.setWidgetId(widget.getWidgetId());

		widgetJson.setControlType(widget.getControlType());
		widgetJson.setShortDescription(widget.getShortDescription());
		widgetJson.setLongDescription(widget.getLongDescription());


		for (WidgetOption option : widget.getWidgetOptions()) {
			widgetJson.addWidgetOption(WidgetOptionJson.create(option));
		}
		
		for (WidgetParameter parameter : widget.getWidgetParameters() ) {
			widgetJson.addWidgetParameter(WidgetParameterJson.create(parameter));
		}
		return widgetJson;
	}

	
	/**
	 * Gets the Widget object that this WidgetJson object is representing.
	 * 
	 * @return The Widget object that this WidgetJson object is representing.
	 */
	public final Widget getWidget() {
		Widget aw = new Widget(this.getWidgetId(), this.getControlType(), this.getShortDescription(),
				this.getLongDescription(),
				this.getWidgetOptions(),
				this.getWidgetParameters());
		
		aw.setApplicationId(this.getApplicationId());
		aw.setPlaceId(this.getPlaceId());

		return aw;
	}
	
	/**
	 * Adds a widget option to the widget represented by this WidgetJson.
	 * 
	 * @param widgetOption The widget option to add.
	 */
	public final native void addWidgetOption(WidgetOptionJson widgetOption) /*-{
		if (typeof (this.widgetOptions) == "undefined") {
			this.widgetOptions = new Array();
		}
		this.widgetOptions.push(widgetOption);
	}-*/;
	
	/**
	 * Adds a widget parameter to the widget represented by this WidgetJson
	 * 
	 * @param widgetParameter The widget parameter to add
	 */
	public final native void addWidgetParameter(WidgetParameterJson widgetParameter) /*-{
		if (typeof (this.widgetParameters) == "undefined") {
			this.widgetParameters = new Array();
		}
		
		this.widgetParameters.push(widgetParameter);
	}-*/;

	
	/**
	 * Gets the id of the application to which the widget represented by this WidgetJson belongs.
	 * 
	 * @return The id of the application.
	 */
	public final native String getApplicationId() /*-{
		return this.applicationId;
	}-*/;


	/**
	 * Gets the type of control of the widget represented by this WidgetJson.
	 * 
	 * @return The type of control.
	 */
	public final native String getControlType() /*-{
		return this.controlType;
	}-*/;

	/**
	 * Gets the long description of the widget.
	 * @return The long description of the widget.
	 */
	public final native String getLongDescription() /*-{
		return this.longDescription;
	}-*/;

	/**
	 * Gets the id of the place of the application of this widget.
	 * 
	 * @return The id of the place of the application of this widget.
	 */
	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	
	/**
	 * Gets the short description of the widget.
	 * 
	 * @return The short description of the widget.
	 */
	public final native String getShortDescription() /*-{
		return this.shortDescription;
	}-*/;



	/**
	 * Gets the widget id.
	 * 
	 * @return The widget id.
	 */
	public final native String getWidgetId() /*-{
		return this.widgetId;
	}-*/;

	/**
	 * Gets the list of widget options as an ArrayList.
	 * 
	 * @return The list of widget options.
	 */
	public final ArrayList<WidgetOption> getWidgetOptions() {
		JsArray<WidgetOptionJson> optionsJs = getWidgetOptionsAsJsArray();
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();// [optionsJs.length()];

		for (int i = 0; i < optionsJs.length(); i++) {
			options.add(optionsJs.get(i).getWidgetOption());
		}

		return options;
	}
	
	/**
	 * Gets the list of widget parameters as an ArrayList.
	 * 
	 * @return The list of widget parameters.
	 */
	public final ArrayList<WidgetParameter> getWidgetParameters() {
		JsArray<WidgetParameterJson> parametersJs = getWidgetParametersAsJsArray();
		
		ArrayList<WidgetParameter> parameters = new ArrayList<WidgetParameter>();// [optionsJs.length()];
		
		if ( parametersJs != null ) {
			for (int i = 0; i < parametersJs.length(); i++) {
				parameters.add(parametersJs.get(i).getWidgetParameter());
			}
		}

		return parameters;
	}

	/**
	 * Gets the list of widget options as a JsArray.
	 * 
	 * @return The list of widget options.
	 */
	public final native JsArray<WidgetOptionJson> getWidgetOptionsAsJsArray() /*-{
		return this.widgetOptions;
	}-*/;
	
	/**
	 * Gets the list of widget parameters as a JsArray
	 * 
	 * @return The list of widget parameters.
	 */
	public final native JsArray<WidgetParameterJson> getWidgetParametersAsJsArray() /*-{
		if ( 'undefined' == typeof(this.widgetParameters) ) {
			return new Array();
		}
		return this.widgetParameters;
	}-*/;

	/**
	 * Sets the id of the application that this widget belongs to.
	 * 
	 * @param appId The id of the application.
	 */
	public final native void setApplicationId(String appId) /*-{
		this.applicationId = appId;
	}-*/;



	/**
	 * Sets the type of control of this widget.
	 * 
	 * @param type The type of control
	 * @see org.purewidgets.shared.im.Widget
	 */
	public final native void setControlType(String type) /*-{
		this.controlType = type;
	}-*/;

	/**
	 * Sets the long description of this widget.
	 * 
	 * @param longDescription The long description.
	 */
	public final native void setLongDescription(String longDescription) /*-{
		this.longDescription = longDescription;
	}-*/;

	/**
	 * Sets the place id of the application this widget belongs to.
	 * 
	 * @param placeId The id of the place.
	 */
	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

	/**
	 * Sets the short description of this widget.
	 * 
	 * @param shortDescription The short description.
	 */
	public final native void setShortDescription(String shortDescription) /*-{
		this.shortDescription = shortDescription;
	}-*/;

	
	/**
	 * Sets the id of this widget.
	 * 
	 * @param id The id of the widget.
	 */
	public final native void setWidgetId(String id) /*-{
		this.widgetId = id;
	}-*/;

}
