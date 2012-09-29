package org.purewidgets.client.storage;

import java.util.ArrayList;
import java.util.Map;

import org.purewidgets.shared.storage.KeyValue;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServerStorageServiceAsync {
	/**
	 * Gets a key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param name The key.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	public void get(String storageId, String name, AsyncCallback<KeyValue> callback);
	
	/**
	 * Gets a list of key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param names The keys to retrieve.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	public void get(String storageId, ArrayList<String> names, AsyncCallback<KeyValue[]> callback);
	
	/**
	 * Saves a string in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @param name The key.
	 * @param value The Value.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	public void set(String storageId, String name, String value, AsyncCallback<Void> callback);
	
	/**
	 * Gets a list of all key/value pairs in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	public void getAll(String storageId, AsyncCallback<Map<String, String>> callback);
}
