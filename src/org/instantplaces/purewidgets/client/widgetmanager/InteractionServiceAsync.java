package org.instantplaces.purewidgets.client.widgetmanager;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InteractionServiceAsync {

	void getWidget(String url, AsyncCallback<String> callback);

	void getWidgetInput(String url, AsyncCallback<String> callback);

	void postWidget(String json, String url, AsyncCallback<String> callback);

	void deleteWidget( String url, AsyncCallback<String> callback);
	
	void getApplicationsFromPlace(String url, AsyncCallback<String> callback);
}
