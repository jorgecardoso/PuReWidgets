/**
 * 
 */
package org.purewidgets.client.storage;


import java.util.ArrayList;
import java.util.Map;

import org.purewidgets.shared.storage.KeyValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * 
 * ServerStorage provides an interface similar to LocalStorage, but the key/value pairs are saved on the application's server.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ServerStorage {

	/**
	 * The id of the storage.
	 */
	private String storageId;
	
	/**
	 * The proxy to the ServerStorage service.
	 */
	private ServerStorageServiceAsync remoteStorageService;
	
	/**
	 * Creates a new ServerStorage with the given id.
	 * @param storageId
	 */
	public ServerStorage(String storageId) {
		this.storageId = storageId;
		remoteStorageService = GWT.create(ServerStorageService.class);
		((ServiceDefTarget)remoteStorageService).setServiceEntryPoint("/storageservice"); 
	}
	
	/**
	 * Saves a string in the server storage.
	 * 
	 * @param name The key.
	 * @param value The Value.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void setString(String name, String value, AsyncCallback<Void> callback) {
		this.remoteStorageService.set(this.storageId, name, value, callback);
	}
	
	/**
	 * Gets a string from the server storage.
	 * 
	 * @param name The key to retrieve.
	 * 
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void getString(String name, AsyncCallback<KeyValue> callback) {
		this.remoteStorageService.get(this.storageId, name, callback);
	}
	
	/**
	 * Gets a list of string from the server storage.
	 * 
	 * @param names The keys to retrieve.
	 * 
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	public void getStrings(ArrayList<String> names, AsyncCallback<KeyValue[]> callback) {
		this.remoteStorageService.get(this.storageId, names, callback);
	}
	
	
	/**
	 * Gets all key/values from the server storage.
	 * 
	 * TODO: Change the return value to use the new KeyValue class..
	 * 
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	public void getAll(AsyncCallback<Map<String, String>> callback) {
		this.remoteStorageService.getAll(storageId, callback);
	}
	
}
