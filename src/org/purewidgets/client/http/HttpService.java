package org.purewidgets.client.http;

import org.purewidgets.shared.exceptions.HttpServerException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Http RPC service.
 * 
 * This service allows applications to invoke HTTP methods on any HTTP server (be carefull).
 * It is used by the PuReWidgets toolkit to contact the Interaction Manager server.
 * 
 */
@RemoteServiceRelativePath("../httpservice")
public interface HttpService extends RemoteService {
	
	/**
	 * Executes a POST request with the specified post data, on the specified URL.
	 * 
	 * @param data The data to post.
	 * @param url The URL to post to.
	 * @return The HTTP response.
	 * @throws HttpServerException In case of any IO exception, or if the HTTP response is not 200 Ok.
	 */
	String post(String data, String url) throws HttpServerException;
	
	/**
	 * Executes a GET request  on the specified URL.
	 * 
	 * @param url The URL to get.
	 * @return The HTTP response.
	 * @throws HttpServerException In case of any IO exception, or if the HTTP response is not 200 Ok.
	 */
	String get(String url) throws HttpServerException;
	
	/**
	 * Executes a DELETE request  on the specified URL.
	 * 
	 * @param url The URL to call.
	 * @return The HTTP response.
	 * @throws HttpServerException In case of any IO exception, or if the HTTP response is not 200 Ok.
	 */	
	String delete(String url) throws HttpServerException;
}
