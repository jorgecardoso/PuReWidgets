package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface SightingServiceAsync {


	void sighting(String s, String date, AsyncCallback<Void> callback);

}
