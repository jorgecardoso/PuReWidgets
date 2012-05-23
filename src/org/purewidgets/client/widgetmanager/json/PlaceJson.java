/**
 * 
 */
package org.purewidgets.client.widgetmanager.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.widgets.Place;

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

		return placeJson;
	}

	// Overlay types always have protected, zero-arg ctors
	protected PlaceJson() {

	}

	public final Place getPlace() {
		Place place = new Place(this.getPlaceId());

		return place;
	}

	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;

}
