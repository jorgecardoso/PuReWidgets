package org.purewidgets.client.widgetmanager;

import org.purewidgets.shared.exceptions.InteractionManagerException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../interactionservice")
public interface InteractionService extends RemoteService {
	
	String post(String json, String url) throws InteractionManagerException;
	
	String get(String url) throws InteractionManagerException;
	
//	String getWidgetInput(String url) throws InteractionManagerException;
	
	String delete( String url) throws InteractionManagerException;
	
//	String getApplicationsFromPlace(String url) throws InteractionManagerException;
	
	
}
