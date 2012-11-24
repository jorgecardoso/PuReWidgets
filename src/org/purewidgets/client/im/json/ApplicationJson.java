/**
 * 
 */
package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Application;


/**
 * ApplicationJson is a Json DTO for receiving and sending application information to the interaction manager server. 
 *  
 *  
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.Application
 */
public class ApplicationJson extends GenericJson {

	 
	// Overlay types always have protected, zero-arg ctors
   protected ApplicationJson() { 
	   
   }


	/**
	 * Sets the place id.
	 * 
	 * @param placeId The place id to set.
	 */
	public final native void setPlaceId(String placeId)  /*-{ 
		this.placeId = placeId; 
	}-*/;

	/**
	 * Gets the place id.
	 * 
	 * @return The place id.
	 */
	public final native String getPlaceId() /*-{ 
		return this.placeId; 
	}-*/;
	
	/**
	 * Sets the place id.
	 * 
	 * @param placeId The place id to set.
	 */
	public final native void setOnScreen(boolean onScreen)  /*-{ 
		this.onScreen = onScreen; 
	}-*/;

	/**
	 * Gets the place id.
	 * 
	 * @return The place id.
	 */
	public final native boolean isOnScreen() /*-{ 
		return this.onScreen; 
	}-*/;	
	  
	/**
	 * Sets the application id.
	 * 
	 * @param appId The application id to set.
	 */
	public final native void setApplicationId(String appId)  /*-{ 
		this.applicationId = appId; 
	}-*/;

	/**
	 * Gets the application base url.
	 * 
	 * @return The application's base url.
	 */
	public final native String getApplicationBaseUrl() /*-{ 
		return this.applicationBaseUrl; 
	}-*/;
	
	/**
	 * Sets the application base url.
	 * 
	 * @param baseUrl The application base url to set.
	 */
	public final native void setApplicationBaseUrl(String baseUrl)  /*-{ 
		this.applicationBaseUrl = baseUrl; 
	}-*/;

	/**
	 * Gets the application id.
	 * 
	 * @return The application id.
	 */
	public final native String getApplicationId() /*-{ 
		return this.applicationId; 
	}-*/;
	

	/**
	 * Creates a ApplicationJson object based on an Application object
	 * 
	 * @return The ApplicationJson converted object.
	 */
	public static ApplicationJson create(Application application) {
		ApplicationJson aJSON = GenericJson.getNew();
		
		aJSON.setPlaceId( application.getPlaceId() );
		aJSON.setApplicationId( application.getApplicationId() );
		aJSON.setApplicationBaseUrl(application.getApplicationBaseUrl());
		aJSON.setOnScreen(application.isOnScreen());
		
		return aJSON;
	}

	/**
	 * Gets the Application object that this ApplicationJson object is representing.
	 * 
	 * @return The Application object that this ApplicationJson object is representing.
	 */
	public final Application getApplication() {
		Application a = new Application( this.getPlaceId(), this.getApplicationId() );
		a.setApplicationBaseUrl(this.getApplicationBaseUrl());
		a.setOnScreen(this.isOnScreen());
		
		return a;
	}


}
