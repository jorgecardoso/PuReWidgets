package org.purewidgets.server.im.json;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.Place;

/**
 * The JSON data transfer object for Place objects.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlaceJson extends GenericJson {
	
	private String placeId;
	

    /**
     * The human-readable place name
     */
    private String placeName;
    
    /*
     * The reference code used for text-based interactions
     */
    private String placeReferenceCode;
    
    /*
     * The phone number users can text in order to interact.
     */
    private String placePhoneNumber;
    
    /**
     * The email address to which users can send emails to interact
     */
    private String placeEmailAddress;
    
    /**
     * The web address of the placeinteraction webpage.
     */
    private String placeInteractionUrl;
    
	
    
	protected PlaceJson() {
	}
	
	public static PlaceJson create(Place place) {
		PlaceJson placeJson = new PlaceJson();
		placeJson.setPlaceId(place.getPlaceId());
		placeJson.setPlaceEmailAddress(place.getPlaceEmailAddress());
		placeJson.setPlaceInteractionUrl(place.getPlaceInteractionUrl());
		placeJson.setPlaceName(place.getPlaceName());
		placeJson.setPlacePhoneNumber(place.getPlacePhoneNumber());
		placeJson.setPlaceReferenceCode(place.getPlaceReferenceCode());
		return placeJson;
	}
	
	public Place getPlace() {
		Place place = new Place(this.placeId);
		place.setPlaceEmailAddress(this.placeEmailAddress);
		place.setPlaceInteractionUrl(this.placeInteractionUrl);
		place.setPlaceName(this.placeName);
		place.setPlacePhoneNumber(this.placePhoneNumber);
		place.setPlaceReferenceCode(this.placeReferenceCode);		
		return place;
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
	 * @return the placeName
	 */
	public String getPlaceName() {
		return placeName;
	}

	/**
	 * @param placeName the placeName to set
	 */
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	/**
	 * @return the placeReferenceCode
	 */
	public String getPlaceReferenceCode() {
		return placeReferenceCode;
	}

	/**
	 * @param placeReferenceCode the placeReferenceCode to set
	 */
	public void setPlaceReferenceCode(String placeReferenceCode) {
		this.placeReferenceCode = placeReferenceCode;
	}

	/**
	 * @return the placePhoneNumber
	 */
	public String getPlacePhoneNumber() {
		return placePhoneNumber;
	}

	/**
	 * @param placePhoneNumber the placePhoneNumber to set
	 */
	public void setPlacePhoneNumber(String placePhoneNumber) {
		this.placePhoneNumber = placePhoneNumber;
	}

	/**
	 * @return the placeEmailAddress
	 */
	public String getPlaceEmailAddress() {
		return placeEmailAddress;
	}

	/**
	 * @param placeEmailAddress the placeEmailAddress to set
	 */
	public void setPlaceEmailAddress(String placeEmailAddress) {
		this.placeEmailAddress = placeEmailAddress;
	}

	/**
	 * @return the placeInteractionUrl
	 */
	public String getPlaceInteractionUrl() {
		return placeInteractionUrl;
	}

	/**
	 * @param placeInteractionUrl the placeInteractionUrl to set
	 */
	public void setPlaceInteractionUrl(String placeInteractionUrl) {
		this.placeInteractionUrl = placeInteractionUrl;
	}
	
}
