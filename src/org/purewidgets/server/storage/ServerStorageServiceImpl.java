/**
 * 
 */
package org.purewidgets.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.purewidgets.client.storage.ServerStorageService;
import org.purewidgets.server.dao.Dao;
import org.purewidgets.server.dao.StorageDao;
import org.purewidgets.shared.storage.KeyValue;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ServerStorageServiceImpl extends RemoteServiceServlet implements ServerStorageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets a key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param name The key.
	 * @return The key/value pair.
	 */	
	@Override
	public KeyValue get(String storageId, String name) {
		KeyValue keyValue = new KeyValue(name, null);
		
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		String value = storageDao.getString(name);
		Dao.commitOrRollbackTransaction();
		keyValue.setValue(value);
		return keyValue;
	
	}

	/**
	 * Gets a list of key/value from the server storage.
	 * 
	 * @param storageId The id of the storage where the key/value is saved.
	 * @param names The keys to retrieve.
	 * @return An array of key/value pairs.
	 */		
	@Override
	public KeyValue[] get(String storageId, ArrayList<String> names) {
		KeyValue[] result = new KeyValue[names.size()];
		
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		int i = 0;
		for ( String name: names ) {
			String value = storageDao.getString(name);
			result[i++] = new KeyValue(name, value);
		}
		
		Dao.commitOrRollbackTransaction();
		
		return result;
	}
	
	
	/**
	 * Saves a string in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @param name The key.
	 * @param value The Value.
	 */	
	@Override
	public void set(String storageId, String name, String value) {
		Dao.beginTransaction();
		StorageDao storageDao = Dao.getStorage(storageId);
		
		if ( null == storageDao ) {
			storageDao = new StorageDao(storageId);
		}
		storageDao.setString(name, value);
		Dao.put(storageDao);
		Dao.commitOrRollbackTransaction();
	}
	
	/**
	 * Gets a list of all key/value pairs in the server storage.
	 * 
	 * @param storageId The id of the storage where the key/values are saved.
	 * @return A map of key/values.
	 */	
	@Override
	public Map<String, String> getAll(String storageId) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		ArrayList<String> keys = storageDao.getKeys();
		
		for ( String key: keys ) {
			map.put( key, storageDao.getString(key) );
		}
		
		Dao.commitOrRollbackTransaction();
		
		return map;
		
	}

}
