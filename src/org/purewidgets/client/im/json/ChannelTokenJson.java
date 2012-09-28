package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;

/**
 * ChannelTokenJson is a Json DTO for receiving channel token information from the interaction manager server. 
 * 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ChannelTokenJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected ChannelTokenJson() {
	}
	
	/**
	 * Gets the channel token.
	 * 
	 * @return The channel token.
	 */
	public final native String getToken() /*-{
		return this.token;
	}-*/;
	
}
