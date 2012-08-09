package org.purewidgets.client.storage.json;

import com.google.gwt.core.client.JavaScriptObject;

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

	/**
	 * Clears all keys that start with storeid, effectively clearing the
	 * apps localstore.
	 * 
	 * @param storeid
	 */
	public static native void clear(String storeid) /*-{
		var toDelete = Array();
		for (var i=0, l=localStorage.length; i<l; i++){
        	var key = localStorage.key(i);
        	if ( key.indexOf(storeid) == 0 ) {
        		toDelete.push(key);
        	}
		}       
		for ( var i = 0; i < toDelete.length; i++) {
			
			localStorage.removeItem(toDelete[i]);
		}
	}-*/;
	
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
	
	public static native void removeItem(String item) /*-{
	localStorage.removeItem(item);
}-*/;
	
}
