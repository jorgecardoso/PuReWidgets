package org.purewidgets.shared.im;

/**
 * Provides methods for creating the REST urls for the interaction manager.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class UrlHelper {

	private String interactionServerUrl;

	/**
	 * Creates a new UrlHelper given the url of the interaction manager server.
	 * 
	 * @param interactionServerUrl The Url of the interaction manager server.
	 */
	public UrlHelper(String interactionServerUrl) {
		this.interactionServerUrl = interactionServerUrl;
	}

	/**
	 * Gets the url for the channel service for a specific application.
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the channel service.
	 */
	public String getChannelUrl(String placeId, String applicationId, String callingApplicationId) {
		return this.interactionServerUrl + "/place/"+ placeId + "/application/"+ applicationId + "/channel?appid="+ callingApplicationId;
	}
	
	/**
	 * Gets the url for the applications list service.
	 * 
	 * @param placeId The id of the place.
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the applications list service.
	 */
	public  String getApplicationsUrl(String placeId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application?appid=" +callingApplicationId ;
	}

	/**
	 * Gets the url for the application service.
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the application service.
	 */
	public  String getApplicationUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId +"?appid="+callingApplicationId;
	}
	

	/**
	 * Gets the url for the application onscreen service.
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the application service.
	 */
	public  String getApplicationOnscreenUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId + "/onscreen" + "?appid="+callingApplicationId;
	}
//	public  String getApplicationUrl(String placeId, String applicationId) {
//		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId;
//	}
	
	/**
	 * Gets the url for the place service.
	 * 
	 * @param placeId The id of the place.
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the place service.
	 */
	public  String getPlaceUrl(String placeId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "?appid=" + callingApplicationId ;
	}
	

	/**
	 * Gets the url for the place list service.
	 * 
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the place list service.
	 */
	public  String getPlacesUrl(String callingApplicationId) {
		return interactionServerUrl + "/place?appid=" + callingApplicationId ;
	}

	/**
	 * Gets the url for the widget list service.
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the widget list service.
	 */
	public  String getWidgetsUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget?appid=" +callingApplicationId ;
	}
	
	/**
	 * Gets the url for the widget input service.
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param widgetId The id of the widget.
	 * @param callingApplicationId The id of the calling application.
	 * @return The url for the widget input service.
	 */
	public  String getWidgetInputUrl(String placeId, String applicationId, String widgetId, String callingApplicationId) {
		 return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget/" + widgetId +  "/input?appid=" + callingApplicationId ;
	}
	
	/**
	 * Gets the url for the application input service
	 * 
	 * @param placeId The id of the place.
	 * @param applicationId The id of the application
	 * @param callingApplicationId The id of the calling application.
	 * @param from The oldest input timestamp to retrieve.
	 * 
	 * @return The url for the application input service.
	 */
	public  String getApplicationInputUrl(String placeId, String applicationId, String callingApplicationId, String from) {
		
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/input?appid=" + callingApplicationId + "&from="+from;
		
	}

	

	/**
	 * Gets the url of the interaction manager server.
	 * 
	 * @return the url of the interaction manager server.
	 */
	public String getInteractionServerUrl() {
		return interactionServerUrl;
	}


	/**
	 * Sets the  url of the interaction manager server.
	 * @param interactionServerUrl the url of the interaction manager server to set.
	 */
	public void setInteractionServerUrl(String interactionServerUrl) {
		this.interactionServerUrl = interactionServerUrl;
	}

	
}
