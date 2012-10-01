package org.purewidgets.shared.im;

/**
 * Represents a place in the interaction manager server.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Place {
	/**
	 * the id of the place
	 */
	private String placeId;
	

    /**
     * The human-readable place name
     */
    private String placeName;
    
    /**
     * The reference code used for text-based interactions
     */
    private String placeReferenceCode;
    
    /**
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
    
	/**
	 * Creates a new place with the specified id.
	 * 
	 * @param placeId The id of the place.
	 */
	public Place(String placeId) {
		this.placeId = placeId;
	}
	
	
	/**
	 * Gets the id of the place.
	 * 
	 * @return the id of the place.
	 */
	public String getPlaceId() {
		return placeId;
	}
	

	/**
	 * Sets the id of the place.
	 * @param placeId the id to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * Gets the human-readable name of the place.
	 * 
	 * @return the name of the place.
	 */
	public String getPlaceName() {
		return placeName;
	}


	/**
	 * Sets the name of the place.
	 * 
	 * @param placeName the name to set
	 */
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}


	/**
	 * Gets the reference code of the place. The reference code is used in SMS and email interactions to 
	 * disambiguate between several places as the target of the input.
	 * 
	 * @return the placeReferenceCode
	 */
	public String getPlaceReferenceCode() {
		return placeReferenceCode;
	}


	/**
	 * Sets the reference code for the place.
	 * 
	 * @param placeReferenceCode the reference code to set
	 */
	public void setPlaceReferenceCode(String placeReferenceCode) {
		this.placeReferenceCode = placeReferenceCode;
	}


	/**
	 * Gets the phone number associated with the place for sms interactions.
	 * 
	 * @return the phone number.
	 */
	public String getPlacePhoneNumber() {
		return placePhoneNumber;
	}


	/**
	 * Sets the phone number associated with the place.
	 * 
	 * @param placePhoneNumber the phone number to set
	 */
	public void setPlacePhoneNumber(String placePhoneNumber) {
		this.placePhoneNumber = placePhoneNumber;
	}


	/**
	 * Gets the email address for email interaction.
	 * 
	 * @return the email address of the place.
	 */
	public String getPlaceEmailAddress() {
		return placeEmailAddress;
	}


	/**
	 * Sets the email address for email interaction.
	 * @param placeEmailAddress the email address to set
	 */
	public void setPlaceEmailAddress(String placeEmailAddress) {
		this.placeEmailAddress = placeEmailAddress;
	}


	/**
	 * Gets the  url for web interaction of the place.
	 * 
	 * @return the url for web interaction with the place.
	 */
	public String getPlaceInteractionUrl() {
		return placeInteractionUrl;
	}


	/**
	 * Sets the url for web interaction.
	 * 
	 * @param placeInteractionUrl the url to set
	 */
	public void setPlaceInteractionUrl(String placeInteractionUrl) {
		this.placeInteractionUrl = placeInteractionUrl;
	}
	
}
