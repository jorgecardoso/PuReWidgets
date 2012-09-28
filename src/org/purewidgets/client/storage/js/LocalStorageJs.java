package org.purewidgets.client.storage.js;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * LocalStorageJs provides an interface to the LocalStorage Javascript object. 
 * 
 * Note: GWT has since provided a class for LocalStorage access, but I haven't refactored my code yet.
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class LocalStorageJs extends JavaScriptObject {

	// Overlay types always have protected, zero-arg ctors
	protected LocalStorageJs() {
	}

	/**
	 * Clears all localstorage items whose keys begin with a specified string
	 * Clears all keys that start with storeid, effectively clearing the apps
	 * localstore.
	 * 
	 * TODO: This class should contain only methods available in the Javascript object. This method should be refactored...
	 * 
	 * @param storeid
	 */
	public static native void clear(String storeid) /*-{
		var toDelete = Array();
		for ( var i = 0, l = localStorage.length; i < l; i++) {
			var key = localStorage.key(i);
			if (key.indexOf(storeid) == 0) {
				toDelete.push(key);
			}
		}
		for ( var i = 0; i < toDelete.length; i++) {

			localStorage.removeItem(toDelete[i]);
		}
	}-*/;

	/**
	 * Gets the value of the specified key.
	 * 
	 * @param item The key.
	 * @return The value.
	 */
	public static native String getString(String item) /*-{
		var time = localStorage.getItem(item);

		if (time == null || time == 'undefined') {
			return "";
		}
		return time;
	}-*/;

	/**
	 * Sets a key/value pair in the localstorage.
	 * 
	 * @param item The key.
	 * @param value The value.
	 */
	public static native void setString(String item, String value) /*-{
		localStorage.setItem(item, value);
	}-*/;

	/**
	 * Removes a key/value pair.
	 * 
	 * @param item The key of the key/value pair to remove.
	 */
	public static native void removeItem(String item) /*-{
		localStorage.removeItem(item);
	}-*/;

}
