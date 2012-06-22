/**
 * 
 */
package org.purewidgets.server.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Application;

/**
 * The JSON data transfer object for Application objects.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ApplicationJson extends GenericJson {
	
	private String placeId;
	
	private String applicationId;
	
	private String applicationBaseUrl;
	
	private ApplicationJson() {
	}
	
	public static ApplicationJson create(Application application) {
		ApplicationJson applicationJson = new ApplicationJson();
		
		applicationJson.setPlaceId(application.getPlaceId());
		applicationJson.setApplicationId(application.getApplicationId());
		applicationJson.setApplicationBaseUrl(application.getApplicationBaseUrl());
		
		return applicationJson;
	}
	
	public Application getApplication() {
		Application application = new Application(this.placeId, this.applicationId);
		application.setApplicationBaseUrl(this.applicationBaseUrl);
		return application;
	}
	

	/**
	 * @param placeId the placeId to set
	 */
	private void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * @param applicationId the applicationId to set
	 */
	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	/**
	 * @param applicationBaseUrl the applicationBaseUrl to set
	 */
	private void setApplicationBaseUrl(String applicationBaseUrl) {
		this.applicationBaseUrl = applicationBaseUrl;
	}

	
}
