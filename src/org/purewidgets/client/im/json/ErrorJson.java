package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;


public class ErrorJson extends GenericJson  {
	// Overlay types always have protected, zero-arg ctors
	protected ErrorJson() { }
	  
	public final native String getErrorMessage() /*-{ return this.errorMessage; }-*/;
	 
}
