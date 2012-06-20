/**
 * 
 */
package org.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.interactionmanager.Application;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class ApplicationListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected ApplicationListJson() {
	}

	public final ArrayList<Application> getApplications() {

		JsArray<ApplicationJson> applicationJs = getApplicationsAsJsArray();
		ArrayList<Application> applications = new ArrayList<Application>();

		for (int i = 0; i < applicationJs.length(); i++) {
			applications.add(applicationJs.get(i).getApplication());
		}

		return applications;

	}

	public final native JsArray<ApplicationJson> getApplicationsAsJsArray() /*-{
		return this.applications;
	}-*/;

	public final native String getPlaceId() /*-{
		return this.placeId;
	}-*/;

	public final native void setApplications(JsArray<ApplicationJson> applications) /*-{
		this.applications = applications;
	}-*/;

	public final void setApplicationsFromArrayList(ArrayList<ApplicationJson> applications) {

		JsArray<ApplicationJson> jsArray = JavaScriptObject.createArray().cast();

		for (ApplicationJson applicationJson : applications) {
			jsArray.push(applicationJson);
		}
		this.setApplications(jsArray);

	}

	public final native void setPlaceId(String placeId) /*-{
		this.placeId = placeId;
	}-*/;
}
