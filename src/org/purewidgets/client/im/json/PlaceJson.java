/**
 * 
 */
package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Place;


/**
 * 
 * PlaceJson is a Json DTO for receiving and sending place information to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.Place
 */
public class PlaceJson extends GenericJson {
	// Overlay types always have protected, zero-arg ctors
	protected PlaceJson() {
	}
	
	/**
	 * Creates a PlaceJson object based on a Place object
	 * 
	 * @return The PlaceJson converted object.
	 */
	public static PlaceJson create(Place place) {
		PlaceJson placeJson = GenericJson.getNew();

		placeJson.setPlaceId(place.getPlaceId());
		placeJson.setPlaceEmailAddress(place.getPlaceEmailAddress());
		placeJson.setPlaceInteractionUrl(place.getPlaceInteractionUrl());
		placeJson.setPlaceName(place.getPlaceName());
		placeJson.setPlacePhoneNumber(place.getPlacePhoneNumber());
		placeJson.setPlaceReferenceCode(place.getPlaceReferenceCode());
		
		return placeJson;
	}



	/**
	 * Gets the Place object that this PlaceJson object is representing.
	 * 
	 * @return The Place object that this PlaceJson object is representing.
	 */
	public final Place getPlace() {
		Place place = new Place(this.getPlaceId());
		place.setPlaceEmailAddress(this.getPlaceEmailAddress());
		place.setPlaceInteractionUrl(this.getPlaceInteractionUrl());
		place.setPlaceName(this.getPlaceName());
		place.setPlacePhoneNumber(this.getPlacePhoneNumber());
		place.setPlaceReferenceCode(this.getPlaceReferenceCode());
		return place;
	}

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
	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

	/**
	 * Gets the place name.
	 * 
	 * @return The place name.
	 */    
	public final native String getPlaceName() /*-{
		return this.placeName;
	}-*/;

	/**
	 * Sets the place name.
	 * 
	 * @param placeName The place name to set.
	 */
	public final native void setPlaceName(String placeName) /*-{
		this.placeName = placeName;
	}-*/;
	
	
	/**
	 * Gets the place reference code.
	 * 
	 * @return The place reference code.
	 */
	public final native String getPlaceReferenceCode() /*-{
		return this.placeReferenceCode;
	}-*/;

	/**
	 * Sets the place reference code.
	 * 
	 * @param placeReferenceCode The place reference code to set.
	 */
	public final native void setPlaceReferenceCode(String placeReferenceCode) /*-{
		this.placeReferenceCode = placeReferenceCode;
	}-*/;

    
	/**
	 * Gets the place phone number for SMS interactions.
	 * 
	 * @return The place phone number.
	 */
	public final native String getPlacePhoneNumber() /*-{
		return this.placePhoneNumber;
	}-*/;

	/**
	 * Sets the place phone number for SMS interactions.
	 * 
	 * @param placePhoneNumber The place phone number to set.
	 */
	public final native void setPlacePhoneNumber(String placePhoneNumber) /*-{
		this.placePhoneNumber = placePhoneNumber;
	}-*/;
	

	/**
	 * Gets the place email address for interactions.
	 * 
	 * @return The place email address.
	 */
	public final native String getPlaceEmailAddress() /*-{
		return this.placeEmailAddress;
	}-*/;

	/**
	 * Sets the place email address for interactions.
	 * 
	 * @param placeEmailAddress The place email address to set.
	 */
	public final native void setPlaceEmailAddress(String placeEmailAddress) /*-{
		this.placeEmailAddress = placeEmailAddress;
	}-*/;


	/**
	 * Gets the place interaction webpage url.
	 * 
	 * @return The place interaction webpage url.
	 */
	public final native String getPlaceInteractionUrl() /*-{
		return this.placeInteractionUrl;
	}-*/;

	/**
	 * Sets the place interaction webpage url.
	 * 
	 * @param placeInteractionUrl The place interaction webpage url to set.
	 */
	public final native void setPlaceInteractionUrl(String placeInteractionUrl) /*-{
		this.placeInteractionUrl = placeInteractionUrl;
	}-*/;    
}
