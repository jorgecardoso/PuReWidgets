/**
 * 
 */
package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.Application;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * ApplicationListJson is a Json DTO for receiving and sending a list of applications to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class ApplicationListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected ApplicationListJson() {
	}

	/**
	 * Gets an ArrayList of Application objects that this ApplicationListJson object is representing.
	 * 
	 * @return The ArrayList of Application objects that this ApplicationListJson object is representing.
	 */
	public final ArrayList<Application> getApplications() {

		JsArray<ApplicationJson> applicationJs = getApplicationsAsJsArray();
		ArrayList<Application> applications = new ArrayList<Application>();

		for (int i = 0; i < applicationJs.length(); i++) {
			applications.add(applicationJs.get(i).getApplication());
		}

		return applications;

	}

	/**
	 * Gets the list of ApplicationJson as a JsArray.
	 * 
	 * @return the list of ApplicationJson as a JsArray.
	 */
	public final native JsArray<ApplicationJson> getApplicationsAsJsArray() /*-{
		return this.applications;
	}-*/;

	/**
	 * Gets the place id.
	 * 
	 * @return The place id.
	 */
	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	/**
	 * Sets the list of ApplicatonJson from a JsArray.
	 * 
	 * @param applications A JsArray of ApplicationJson to set.
	 */
	public final native void setApplications(JsArray<ApplicationJson> applications) /*-{
		this.applications = applications;
	}-*/;

	/**
	 * Sets the list of ApplicatonJson from an ArrayList.
	 * 
	 * @param applications An ArrayList of ApplicationJson to set.
	 */
	public final void setApplicationsFromArrayList(ArrayList<ApplicationJson> applications) {

		JsArray<ApplicationJson> jsArray = JavaScriptObject.createArray().cast();

		for (ApplicationJson applicationJson : applications) {
			jsArray.push(applicationJson);
		}
		this.setApplications(jsArray);

	}
	
	/**
	 * Sets the place id.
	 * 
	 * @param placeId The place id to set.
	 */
	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;
}
