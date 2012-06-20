/**
 * 
 */
package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Place;

import com.google.gwt.core.client.JsArray;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected PlaceListJson() {
	}

	public final ArrayList<Place> getPlaces() {

		JsArray<PlaceJson> placesJs = getPlacesAsJsArray();
		ArrayList<Place> places = new ArrayList<Place>();

		for (int i = 0; i < placesJs.length(); i++) {
			places.add(placesJs.get(i).getPlace());
		}

		return places;

	}

	public final native JsArray<PlaceJson> getPlacesAsJsArray() /*-{
		return this.places;
	}-*/;

	public final native void setPlaces(JsArray<PlaceJson> places) /*-{
		this.places = places;
	}-*/;

}
