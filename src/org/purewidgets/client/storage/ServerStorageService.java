/**
 * 
 */
package org.purewidgets.client.storage;
import java.util.ArrayList;
import java.util.Map;

import org.purewidgets.shared.storage.KeyValue;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the ServerStorage RPC service.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
@RemoteServiceRelativePath("/storageservice")
public interface ServerStorageService extends RemoteService {
	
	/**
	 * Gets a key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param name The key.
	 * @return The key/value pair.
	 */		
	public KeyValue get(String storageId, String name);
	
	/**
	 * Gets a list of key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param names The keys to retrieve.
	 * @return An array of key/value pairs.
	 */			
	public KeyValue[] get(String storageId, ArrayList<String> names);
	
	/**
	 * Saves a string in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @param name The key.
	 * @param value The Value.
	 */	
	public void set(String storageId, String name, String value);
	
	/**
	 * Gets a list of all key/value pairs in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @return A map of key/values.
	 */
	public Map<String, String> getAll(String storageId);
}
