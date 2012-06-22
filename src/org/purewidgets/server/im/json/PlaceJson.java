package org.purewidgets.server.im.json;

import java.io.IOException;


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
	

	protected PlaceJson(String placeId) {
		this.placeId = placeId;
	}
	
	public static PlaceJson create(Place place) {
		PlaceJson placeJson = new PlaceJson(place.getPlaceId());
		return placeJson;
	}
	
	public Place getPlace() {
		Place place = new Place(this.placeId);
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
	
}
