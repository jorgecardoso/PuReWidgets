package org.purewidgets.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class GenericJson extends JavaScriptObject {

	// Overlay types always have protected, zero-arg ctors
	  protected GenericJson() { }
	  
	  public final static native <T> T getNew() /*-{
	  	return new Object();
	  }-*/; 
	  
	  public final static native <T> T fromJson(String json) /*-{
	  	return eval('('+json+')');
	  }-*/;
	  
	public final native String toJsonString() /*-{
	  return JSON.stringify(this);
	}-*/;
}
