package org.instantplaces.purewidgets.client.storage.json;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.ArrayList;
import java.util.List;

/**
 * At the time, GWT didn't have local storage capabilities.
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class LocalStorage extends JavaScriptObject {


	// Overlay types always have protected, zero-arg ctors
		protected LocalStorage() {
		}

		
	public static native String getString(String item) /*-{
		var time = localStorage.getItem(item);

		if (time == null || time == 'undefined') {
			return "";
		}
		return time;
	}-*/;
	

	public static native void setString(String item, String value) /*-{
		localStorage.setItem(item, value);
	}-*/;
	
}
