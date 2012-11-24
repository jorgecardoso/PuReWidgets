package org.purewidgets.client.http;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async version of the Http RPC service.
 * 
 * @author "Jorge C. S. Cardoso"
 */
public interface HttpServiceAsync {

	/**
	 * Executes a GET request  on the specified URL.
	 * 
	 * @param url The URL to get.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	void get(String url, AsyncCallback<String> callback);

	/**
	 * Executes a POST request with the specified post data, on the specified URL.
	 * 
	 * @param data The data to post.
	 * @param url The URL to post to.
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	void post(String json, String url, AsyncCallback<String> callback);

	
	/**
	 * Executes a PUT request with the specified post data, on the specified URL.
	 * 
	 * @param data The data to post.
	 * @param url The URL to post to.
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	void put(String json, String url, AsyncCallback<String> callback);
	
	/**
	 * Executes a DELETE request  on the specified URL.
	 * 
	 * @param url The URL to call.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	void delete(String url, AsyncCallback<String> callback);
}
