package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;

public class ChannelTokenJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected ChannelTokenJson() {
	}
	
	public final native String getToken() /*-{
		return this.token;
	}-*/;
	
}
