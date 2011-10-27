package org.instantplaces.purewidgets.client.widgetmanager.json;


import java.util.ArrayList;

import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.core.client.JsArray;

public class WidgetJson extends GenericJson {

 
	// Overlay types always have protected, zero-arg ctors
   protected WidgetJson() { 
	   
   }


   public final String getBaseURL() {
	   return  "/place/" + this.getPlaceId() + "/application/" + this.getApplicationId() + 
	   	"/widget";
   }
   
   public final String getURL() {
	   return  this.getBaseURL() + "/"+ this.getWidgetId() + "?output=json";
   }
   

	public final native void setPlaceId(String placeId)  /*-{ 
		this.placeId = placeId; 
	}-*/;


	public final native String getPlaceId() /*-{ 
		return this.placeId; 
	}-*/;
	
	  

	public final native void setApplicationId(String appId)  /*-{ 
		this.applicationId = appId; 
	}-*/;


	public final native String getApplicationId() /*-{ 
		return this.applicationId; 
	}-*/;
	


	public final native void setWidgetId(String id)  /*-{ 
		this.widgetId = id; 
	}-*/;


	public final native String getWidgetId() /*-{ 
		return this.widgetId; 
	}-*/;

	public final native void setVolatileWidget(boolean volatileWidget)  /*-{ 
		this.volatileWidget = volatileWidget; 
	}-*/;


	public final native boolean isVolatileWidget() /*-{ 
		return this.volatileWidget; 
	}-*/;	
	
	
	public final native void setControlType(String type)  /*-{ 
	this.controlType = type; 
}-*/;


public final native String getControlType() /*-{ 
	return this.controlType; 
}-*/;	
	
	
	public final native void setShortDescription(String shortDescription)  /*-{ 
	this.shortDescription = shortDescription; 
	}-*/;

	
	public final native String getShortDescription() /*-{ 
		return this.shortDescription; 
	}-*/;	
	

	
	public final native void setLongDescription(String longDescription)  /*-{ 
	this.longDescription = longDescription; 
	}-*/;

	public final native String getLongDescription() /*-{ 
	return this.longDescription; 
}-*/;

	public final ArrayList<WidgetOption> getWidgetOptions() {
		JsArray<WidgetOptionJson> optionsJs = getWidgetOptionsAsJsArray();
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();//[optionsJs.length()];
		
		for (int i = 0; i < optionsJs.length(); i++) {
			options.add(optionsJs.get(i).getWidgetOption());
		}
		
		return options;
	}
	
	public final native JsArray<WidgetOptionJson> getWidgetOptionsAsJsArray() /*-{
		return this.widgetOptions;
	}-*/;



	public final native void addWidgetOption(WidgetOptionJson widgetOption) /*-{
		if (typeof(this.widgetOptions) == "undefined") {
			this.widgetOptions = new Array();
		}
		this.widgetOptions.push(widgetOption);
	}-*/;

	/**
	 * Converts this widget to a JSON representation 
	 * @return
	 */
	public static WidgetJson create(Widget widget) {
		WidgetJson wJSON = GenericJson.getNew();
		
		wJSON.setPlaceId(widget.getPlaceId());
		wJSON.setApplicationId(widget.getApplicationId());
		wJSON.setWidgetId(widget.getWidgetId());
		
		wJSON.setVolatileWidget(widget.isVolatileWidget());
		wJSON.setControlType(widget.getControlType());
		wJSON.setShortDescription(widget.getShortDescription());
		wJSON.setLongDescription(widget.getLongDescription());
		
		for ( WidgetOption option : widget.getWidgetOptions() ) {
			wJSON.addWidgetOption( WidgetOptionJson.create(option));
		}
		return wJSON;
	}

	public final Widget getWidget() {
		Widget aw = new Widget(this.getWidgetId(), this.getShortDescription(), this.getWidgetOptions());
		aw.setLongDescription(this.getLongDescription());
		aw.setApplicationId(this.getApplicationId());
		aw.setVolatileWidget(this.isVolatileWidget());
		aw.setControlType(this.getControlType());
		aw.setPlaceId(this.getPlaceId());
		return aw;
	}
	
}
