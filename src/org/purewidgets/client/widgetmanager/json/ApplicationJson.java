/**
 * 
 */
package org.purewidgets.client.widgetmanager.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Application;


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


	public final native String getIconBaseUrl() /*-{ 
		return this.iconBaseUrl; 
	}-*/;
	

	public final native void setIconBaseUrl(String iconBaseUrl)  /*-{ 
		this.iconBaseUrl = iconBaseUrl; 
	}-*/;


	public final native String getApplicationId() /*-{ 
		return this.applicationId; 
	}-*/;
	

	/**
	 * Converts this application to a JSON representation 
	 * @return
	 */
	public static ApplicationJson create(Application application) {
		ApplicationJson aJSON = GenericJson.getNew();
		
		aJSON.setPlaceId( application.getPlaceId() );
		aJSON.setApplicationId( application.getApplicationId() );
		aJSON.setIconBaseUrl(application.getIconBaseUrl());
		
		return aJSON;
	}

	public final Application getApplication() {
		Application a = new Application( this.getPlaceId(), this.getApplicationId() );
		a.setIconBaseUrl(this.getIconBaseUrl());
		return a;
	}


}
