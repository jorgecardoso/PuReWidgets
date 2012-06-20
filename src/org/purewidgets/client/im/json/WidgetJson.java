package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.JsArray;

public class WidgetJson extends GenericJson {

	/**
	 * Converts this widget to a JSON representation
	 * 
	 * @return
	 */
	public static WidgetJson create(Widget widget) {
		WidgetJson widgetJson = GenericJson.getNew();

		widgetJson.setPlaceId(widget.getPlaceId());
		widgetJson.setApplicationId(widget.getApplicationId());
		widgetJson.setWidgetId(widget.getWidgetId());

		widgetJson.setControlType(widget.getControlType());
		widgetJson.setShortDescription(widget.getShortDescription());
		widgetJson.setLongDescription(widget.getLongDescription());
		widgetJson.setContentUrl(widget.getContentUrl());
		widgetJson.setUserResponse(widget.getUserResponse());

		for (WidgetOption option : widget.getWidgetOptions()) {
			widgetJson.addWidgetOption(WidgetOptionJson.create(option));
		}
		return widgetJson;
	}

	// Overlay types always have protected, zero-arg ctors
	protected WidgetJson() {

	}

	public final native void addWidgetOption(WidgetOptionJson widgetOption) /*-{
		if (typeof (this.widgetOptions) == "undefined") {
			this.widgetOptions = new Array();
		}
		this.widgetOptions.push(widgetOption);
	}-*/;

	public final native String getApplicationId() /*-{
		return this.applicationId;
	}-*/;



	public final native String getContentUrl() /*-{
		return this.contentUrl;
	}-*/;

	public final native String getControlType() /*-{
		return this.controlType;
	}-*/;

	public final native String getLongDescription() /*-{
		return this.longDescription;
	}-*/;

	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	public final native String getShortDescription() /*-{
		return this.shortDescription;
	}-*/;



	public final native String getUserResponse() /*-{
		return this.userResponse;
	}-*/;

	public final Widget getWidget() {
		Widget aw = new Widget(this.getWidgetId(), this.getShortDescription(),
				this.getWidgetOptions());
		aw.setLongDescription(this.getLongDescription());
		aw.setApplicationId(this.getApplicationId());
		aw.setControlType(this.getControlType());
		aw.setPlaceId(this.getPlaceId());
		aw.setContentUrl(this.getContentUrl());
		aw.setUserResponse(this.getUserResponse());
		return aw;
	}

	public final native String getWidgetId() /*-{
		return this.widgetId;
	}-*/;

	public final ArrayList<WidgetOption> getWidgetOptions() {
		JsArray<WidgetOptionJson> optionsJs = getWidgetOptionsAsJsArray();
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();// [optionsJs.length()];

		for (int i = 0; i < optionsJs.length(); i++) {
			options.add(optionsJs.get(i).getWidgetOption());
		}

		return options;
	}

	public final native JsArray<WidgetOptionJson> getWidgetOptionsAsJsArray() /*-{
		return this.widgetOptions;
	}-*/;


	public final native void setApplicationId(String appId) /*-{
		this.applicationId = appId;
	}-*/;

	public final native void setContentUrl(String url) /*-{
		this.contentUrl = url;
	}-*/;

	public final native void setControlType(String type) /*-{
		this.controlType = type;
	}-*/;

	public final native void setLongDescription(String longDescription) /*-{
		this.longDescription = longDescription;
	}-*/;

	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

	public final native void setShortDescription(String shortDescription) /*-{
		this.shortDescription = shortDescription;
	}-*/;

	public final native void setUserResponse(String response) /*-{
		this.userResponse = response;
	}-*/;


	public final native void setWidgetId(String id) /*-{
		this.widgetId = id;
	}-*/;

}
