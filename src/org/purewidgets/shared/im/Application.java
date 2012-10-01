/**
 * 
 */
package org.purewidgets.shared.im;


/**
 * Represents an application in the interaction manager server.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Application {
	/**
	 * The place id of the application.
	 */
	private String placeId;
	
	/**
	 * The application id.
	 */
	private String applicationId;
	
	/**
	 * The application's base url
	 */
	private String applicationBaseUrl;
	
	/**
	 * Creates a new Application with the specified place id and application id.
	 * @param placeId The place id of the new application.
	 * @param appId The application id of the new application.
	 */
	public Application(String placeId, String appId) {
		this.placeId = placeId;
		this.applicationId = appId;
	}
	
	
	/**
	 * Getes the place id of the application.
	 * 
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}
	

	/**
	 * Sets the place id of the new application.
	 * 
	 * @param placeId the place id to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	

	/**
	 * Gets the application id of the application.
	 * 
	 * @return the application id.
	 */
	public String getApplicationId() {
		return applicationId;
	}

	
	/**
	 * Sets the application id.
	 * 
	 * @param appId the application id to set.
	 */
	public void setApplicationId(String appId) {
		this.applicationId = appId;
	}


	/**
	 * Gets the application base url.
	 * 
	 * @return the application base url.
	 */
	public String getApplicationBaseUrl() {
		return applicationBaseUrl;
	}


	/**
	 * Sets the application base url.
	 * 
	 * @param applicationBaseUrl the application base Url to set
	 */
	public void setApplicationBaseUrl(String applicationBaseUrl) {
		this.applicationBaseUrl = applicationBaseUrl;
	}

	
}
