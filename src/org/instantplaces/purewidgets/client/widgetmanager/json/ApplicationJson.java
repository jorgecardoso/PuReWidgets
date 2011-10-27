/**
 * 
 */
package org.instantplaces.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.core.client.JsArray;


/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ApplicationJson extends GenericJson {

	 
	// Overlay types always have protected, zero-arg ctors
   protected ApplicationJson() { 
	   
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
	



	public final ArrayList<Widget> getWidgets() {
		JsArray<WidgetJson> widgetsJs = getWidgetsAsJsArray();
		ArrayList<Widget> widgets = new ArrayList<Widget>();//[optionsJs.length()];
		
		for (int i = 0; i < widgetsJs.length(); i++) {
			widgets.add(widgetsJs.get(i).getWidget());
		}
		
		return widgets;
	}
	
	public final native JsArray<WidgetJson> getWidgetsAsJsArray() /*-{
		return this.widgets;
	}-*/;



	public final native void addWidget(WidgetJson widget) /*-{
		if (typeof(this.widgets) == "undefined") {
			this.widgets = new Array();
		}
		this.widget.push(widget);
	}-*/;

	/**
	 * Converts this widget to a JSON representation 
	 * @return
	 */
	public static ApplicationJson create(Application application) {
		ApplicationJson aJSON = GenericJson.getNew();
		
		aJSON.setPlaceId( application.getPlaceId() );
		aJSON.setApplicationId( application.getApplicationId() );
		
		for ( Widget widget : application.getWidgets() ) {
			aJSON.addWidget( WidgetJson.create(widget) );
		}
		return aJSON;
	}

	public final Application getApplication() {
		Application a = new Application( this.getPlaceId(), this.getApplicationId() );
		
		a.setWidgets(this.getWidgets());
		return a;
	}


}
