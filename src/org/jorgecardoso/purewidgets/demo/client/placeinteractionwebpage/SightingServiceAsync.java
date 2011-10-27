package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface SightingServiceAsync {


	void sighting(String s, String date, AsyncCallback<Void> callback);

}
