/**
 * 
 */
package org.purewidgets.server.im.json;

import org.purewidgets.client.json.GenericJson;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Application {
	public static enum STATE {All, Active, Inactive};
	
	private String placeId;
	
	private String applicationId;
	
	private String applicationBaseUrl;
	
	public Application(String placeId, String appId) {
		this.placeId = placeId;
		this.applicationId = appId;
	}
	
	
	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}
	

	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	

	/**
	 * @return the appId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	
	/**
	 * @param appId the appId to set
	 */
	public void setApplicationId(String appId) {
		this.applicationId = appId;
	}


	/**
	 * @return the applicationBaseUrl
	 */
	public String getApplicationBaseUrl() {
		return applicationBaseUrl;
	}


	/**
	 * @param applicationBaseUrl the applicationBaseUrl to set
	 */
	public void setApplicationBaseUrl(String applicationBaseUrl) {
		this.applicationBaseUrl = applicationBaseUrl;
	}


	public class JsApplication extends GenericJson {
		// Overlay types always have protected, zero-arg ctors
		   protected JsApplication() { 
			   
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


			public final native String getApplicationBaseUrl() /*-{ 
				return this.applicationBaseUrl; 
			}-*/;
			

			public final native void setApplicationBaseUrl(String baseUrl)  /*-{ 
				this.applicationBaseUrl = baseUrl; 
			}-*/;


			public final native String getApplicationId() /*-{ 
				return this.applicationId; 
			}-*/;
	}
	
	
}
