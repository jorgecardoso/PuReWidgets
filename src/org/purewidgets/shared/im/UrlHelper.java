package org.purewidgets.shared.im;

public class UrlHelper {

	private String interactionServerUrl;
	
	public UrlHelper(String interactionServerUrl) {
		this.interactionServerUrl = interactionServerUrl;
	}

	public String getChannelUrl(String placeId, String applicationId, String callingApplicationId) {
		return this.interactionServerUrl + "/place/"+ placeId + "/application/"+ applicationId + "/channel?appid="+ callingApplicationId;
	}
	
	public  String getApplicationsUrl(String placeId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application?appid=" +callingApplicationId ;
	}

	public  String getApplicationUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId +"?appid="+callingApplicationId;
	}
	
	public  String getApplicationUrl(String placeId, String applicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId;
	}
	
	public  String getPlaceUrl(String placeId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "?appid=" + callingApplicationId ;
	}
	
	public  String getPlacesUrl(String callingApplicationId) {
		return interactionServerUrl + "/place?appid=" + callingApplicationId ;
	}

	public  String getWidgetsUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget?appid=" +callingApplicationId ;
	}
	
	public  String getWidgetInputUrl(String placeId, String applicationId, String widgetId, String callingApplicationId) {
		 return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget/" + widgetId +  "/input?appid=" + callingApplicationId ;
	}
	
	public  String getApplicationInputUrl(String placeId, String applicationId, String callingApplicationId, String from) {
		
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/input?appid=" + callingApplicationId + "&from="+from;
		
	}

	

	/**
	 * @return the interactionServerUrl
	 */
	public String getInteractionServerUrl() {
		return interactionServerUrl;
	}


	/**
	 * @param interactionServerUrl the interactionServerUrl to set
	 */
	public void setInteractionServerUrl(String interactionServerUrl) {
		this.interactionServerUrl = interactionServerUrl;
	}

	
}
