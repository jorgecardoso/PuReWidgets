package org.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class WidgetListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetListJson() {
	}

	public final native String getApplicationId() /*-{
		return this.applicationId;
	}-*/;

	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	public final ArrayList<Widget> getWidgets() {
		JsArray<WidgetJson> widgetJs = getWidgetsAsJsArray();
		ArrayList<Widget> widgets = new ArrayList<Widget>();

		for (int i = 0; i < widgetJs.length(); i++) {
			widgets.add(widgetJs.get(i).getWidget());
		}

		return widgets;
	}

	public final native JsArray<WidgetJson> getWidgetsAsJsArray() /*-{
		return this.widgets;
	}-*/;

	public final native void setApplicationId(String applicationId) /*-{
		this.applicationId = applicationId;
	}-*/;

	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

	// public final native void addWidgetJson(WidgetJson widgetJson)

	public final native void setWidgets(JsArray<WidgetJson> widgets) /*-{
		this.widgets = widgets;
	}-*/;

	public final void setWidgetsFromArrayList(ArrayList<WidgetJson> widgets) {
		JsArray<WidgetJson> jsArray = JavaScriptObject.createArray().cast();

		for (WidgetJson widgetJson : widgets) {
			jsArray.push(widgetJson);
		}
		this.setWidgets(jsArray);
	}

}
