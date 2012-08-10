/**
 * 
 */
package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Place;

import com.googlecode.objectify.annotation.Unindexed;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceJson extends GenericJson {

	/**
	 * Converts this application to a JSON representation
	 * 
	 * @return
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

	// Overlay types always have protected, zero-arg ctors
	protected PlaceJson() {

	}

	public final Place getPlace() {
		Place place = new Place(this.getPlaceId());
		place.setPlaceEmailAddress(this.getPlaceEmailAddress());
		place.setPlaceInteractionUrl(this.getPlaceInteractionUrl());
		place.setPlaceName(this.getPlaceName());
		place.setPlacePhoneNumber(this.getPlacePhoneNumber());
		place.setPlaceReferenceCode(this.getPlaceReferenceCode());
		return place;
	}

	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

    
	public final native String getPlaceName() /*-{
		return this.placeName;
	}-*/;

	public final native void setPlaceName(String placeName) /*-{
		this.placeName = placeName;
	}-*/;
	
	
    
	public final native String getPlaceReferenceCode() /*-{
		return this.placeReferenceCode;
	}-*/;

	public final native void setPlaceReferenceCode(String placeReferenceCode) /*-{
		this.placeReferenceCode = placeReferenceCode;
	}-*/;

    
	public final native String getPlacePhoneNumber() /*-{
		return this.placePhoneNumber;
	}-*/;

	public final native void setPlacePhoneNumber(String placePhoneNumber) /*-{
		this.placePhoneNumber = placePhoneNumber;
	}-*/;
	

	public final native String getPlaceEmailAddress() /*-{
		return this.placeEmailAddress;
	}-*/;

	public final native void setPlaceEmailAddress(String placeEmailAddress) /*-{
		this.placeEmailAddress = placeEmailAddress;
	}-*/;


    
	public final native String getPlaceInteractionUrl() /*-{
		return this.placeInteractionUrl;
	}-*/;

	public final native void setPlaceInteractionUrl(String placeInteractionUrl) /*-{
		this.placeInteractionUrl = placeInteractionUrl;
	}-*/;    
}
