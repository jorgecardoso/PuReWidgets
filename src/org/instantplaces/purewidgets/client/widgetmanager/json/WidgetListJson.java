package org.instantplaces.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class WidgetListJson extends GenericJson {
	
	// Overlay types always have protected, zero-arg ctors
	   protected WidgetListJson() { 		   
	   }
	   
		public final native void setWidgets(JsArray<WidgetJson> widgets) /*-{ 
			this.widgets = widgets; 
		}-*/;
		
		public final void setWidgetsFromArrayList(ArrayList<WidgetJson> widgets) {
			JsArray<WidgetJson> jsArray = JavaScriptObject.createArray().cast();
			
			for ( WidgetJson widgetJson : widgets ) {
				jsArray.push(widgetJson);
			}
			this.setWidgets(jsArray);
		}
		
		//public final native void addWidgetJson(WidgetJson widgetJson) 
		
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
		

}
