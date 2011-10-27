package org.instantplaces.purewidgets.client.widgetmanager;

import org.instantplaces.purewidgets.shared.exceptions.InteractionManagerException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../interactionservice")
public interface InteractionService extends RemoteService {
	
	String postWidget(String json, String url) throws InteractionManagerException;
	
	String getWidget(String url) throws InteractionManagerException;
	
	String getWidgetInput(String url) throws InteractionManagerException;
	
	String deleteWidget( String url) throws InteractionManagerException;
	
	String getApplicationsFromPlace(String url) throws InteractionManagerException;
	
}
