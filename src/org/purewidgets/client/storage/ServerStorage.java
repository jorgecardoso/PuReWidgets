/**
 * 
 */
package org.purewidgets.client.storage;


import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ServerStorage {

	private String storageId;
	private ServerStorageServiceAsync remoteStorageService;
	
	public ServerStorage(String placeName, String appName) {
		this.storageId = placeName+"-"+appName;
		remoteStorageService = GWT.create(ServerStorageService.class);
		((ServiceDefTarget)remoteStorageService).setServiceEntryPoint("/storageservice"); 
	}
	
	public void setString(String name, String value, AsyncCallback<Void> callback) {
		this.remoteStorageService.set(this.storageId, name, value, callback);
	}
	
	public void getString(String name, AsyncCallback<String[]> callback) {
		this.remoteStorageService.get(this.storageId, name, callback);
	}
	
	public void getStrings(ArrayList<String> names, AsyncCallback<String[]> callback) {
		this.remoteStorageService.get(this.storageId, names, callback);
	}
	
	public void getAll(AsyncCallback<Map<String, String>> callback) {
		this.remoteStorageService.getAll(storageId, callback);
	}
	
}
