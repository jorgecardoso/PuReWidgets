package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * WidgetListJson is a Json DTO for receiving and sending a list of widgets to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class WidgetListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetListJson() {
	}

	/**
	 * Gets the list of widgets represented by this WidgetListJson, as an ArrayList.
	 * 
	 * @return The list of widgets.
	 */
	public final ArrayList<Widget> getWidgets() {
		JsArray<WidgetJson> widgetJs = getWidgetsAsJsArray();
		ArrayList<Widget> widgets = new ArrayList<Widget>();

		for (int i = 0; i < widgetJs.length(); i++) {
			widgets.add(widgetJs.get(i).getWidget());
		}

		return widgets;
	}
	
	/**
	 * Sets the list of widgets represented by this WidgetListJson, from an
	 * ArrayList of widgets.
	 * 
	 * @param widgets The list of widgets to set.
	 */
	public final void setWidgetsFromArrayList(ArrayList<WidgetJson> widgets) {
		JsArray<WidgetJson> jsArray = JavaScriptObject.createArray().cast();

		for (WidgetJson widgetJson : widgets) {
			jsArray.push(widgetJson);
		}
		this.setWidgets(jsArray);
	}
	
	/**
	 * Gets the id of the application the widgets in the list belong to.
	 * 
	 * @return The id of the application.
	 */
	public final native String getApplicationId() /*-{
		return this.applicationId;
	}-*/;

	/**
	 * Gets the id of the place of the application the widgets in the list belong to.
	 * 
	 * @return The id of the place.
	 */
	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;


	/**
	 *  The the list of widgets as a JsArray.
	 *  
	 * @return The list of widgets.
	 */
	public final native JsArray<WidgetJson> getWidgetsAsJsArray() /*-{
		return this.widgets;
	}-*/;

	/**
	 * Sets the id of the application the widgets belong to.
	 * 
	 * @param applicationId The id of the application.
	 */
	public final native void setApplicationId(String applicationId) /*-{
		this.applicationId = applicationId;
	}-*/;

	/**
	 * Sets the id of the place of the application the widgets belong to.
	 * 
	 * @param placeId The id of the place.
	 */
	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;


	/**
	 * Sets the list of widgets from a JsArray.
	 * 
	 * @param widgets The list of widgets.
	 */
	public final native void setWidgets(JsArray<WidgetJson> widgets) /*-{
		this.widgets = widgets;
	}-*/;

	

}
