package org.purewidgets.client.http;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HttpServiceAsync {

	void get(String url, AsyncCallback<String> callback);

	void post(String json, String url, AsyncCallback<String> callback);

	void delete( String url, AsyncCallback<String> callback);
}
