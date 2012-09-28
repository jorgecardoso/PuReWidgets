/**
 * 
 */
package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Place;

import com.google.gwt.core.client.JsArray;

/**
 * PlaceListJson is a Json DTO for receiving and sending a list of places to the interaction manager server. 
 * 
 * 
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected PlaceListJson() {
	}

	/**
	 * Gets an ArrayList of Place objects that this PlaceListJson object is representing.
	 * 
	 * @return The ArrayList of Place objects that this PlaceListJson object is representing.
	 */	
	public final ArrayList<Place> getPlaces() {

		JsArray<PlaceJson> placesJs = getPlacesAsJsArray();
		ArrayList<Place> places = new ArrayList<Place>();

		for (int i = 0; i < placesJs.length(); i++) {
			places.add(placesJs.get(i).getPlace());
		}

		return places;

	}

	/**
	 * Gets the list of PlaceJson as a JsArray.
	 * 
	 * @return the list of PlaceJson as a JsArray.
	 */	
	public final native JsArray<PlaceJson> getPlacesAsJsArray() /*-{
		return this.places;
	}-*/;

	/**
	 * Sets the list of PlaceJson from a JsArray.
	 * 
	 * @param applications A JsArray of PlaceJson to set.
	 */
	public final native void setPlaces(JsArray<PlaceJson> places) /*-{
		this.places = places;
	}-*/;

}
