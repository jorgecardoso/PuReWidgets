package org.purewidgets.client.json;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * GenericJson provides auxiliary methods common to all Json DTO objects.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public abstract class GenericJson extends JavaScriptObject {

	// Overlay types always have protected, zero-arg ctors
	protected GenericJson() {
	}

	/**
	 * Gets a new JavaScriptObject of the specified class.
	 * @return
	 */
	public final static native <T> T getNew() /*-{
		return new Object();
	}-*/;

	/**
	 * Convers a JSON string to a JavaScriptObject of the specified class.
	 * @param json The JSON string.
	 * @return A JavaScriptObject 
	 */
	public final static native <T> T fromJson(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	/**
	 * Converts this object to a JSON string.  
	 * @return
	 */
	public final native String toJsonString() /*-{
		return JSON.stringify(this);
	}-*/;

	/**
	 * Converts a JavaScriptObject to a JSON string.
	 * 
	 * @param jso The JavaScriptObject to convert.
	 * @return The JSON string 
	 */
	public final static native String toJsonString(JavaScriptObject jso) /*-{
		return JSON.stringify(jso);
	}-*/;
}
