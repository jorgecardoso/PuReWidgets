package org.instantplaces.purewidgets.client.widgetmanager;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InteractionServiceAsync {

	void get(String url, AsyncCallback<String> callback);

	//void getWidgetInput(String url, AsyncCallback<String> callback);

	void post(String json, String url, AsyncCallback<String> callback);

	void delete( String url, AsyncCallback<String> callback);
	
	//void getApplicationsFromPlace(String url, AsyncCallback<String> callback);
}
