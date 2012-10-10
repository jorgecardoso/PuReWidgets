package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Application;
import org.purewidgets.shared.im.Place;
import org.purewidgets.shared.im.Widget;
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
public class InputResponseJson extends GenericJson {
	// Overlay types always have protected, zero-arg ctors
	protected InputResponseJson() {

	}


	public final Place getPlace() {
		PlaceJson placeJson = getPlaceJson();
		if ( null != placeJson ) {
			return placeJson.getPlace();
		} else {
			return null;
		}
	}
	
	public final Application getApplication() {
		ApplicationJson applicationJson = getApplicationJson();
		if ( null != applicationJson ) {
			return applicationJson.getApplication();
		} else {
			return null;
		}
	}
	
	public final Widget getWidget() {
		WidgetJson widgetJson = getWidgetJson();
		if ( null != widgetJson ) {
			return widgetJson.getWidget();
		} else {
			return null;
		}
	}
		

	public final native PlaceJson getPlaceJson() /*-{
		return this.place;
	}-*/;

	
	public final native ApplicationJson getApplicationJson() /*-{
		return this.application;
	}-*/;

	public final native WidgetJson getWidgetJson() /*-{
		return this.widget;
	}-*/;
	
}
