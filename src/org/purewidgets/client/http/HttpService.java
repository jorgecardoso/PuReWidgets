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
	
	String post(String data, String url) throws HttpServerException;
	
	String get(String url) throws HttpServerException;
	
	String delete( String url) throws HttpServerException;
	
	
}
