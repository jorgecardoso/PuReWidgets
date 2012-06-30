/**
 * 
 */
package org.purewidgets.server.storage;


import org.purewidgets.server.dao.Dao;
import org.purewidgets.server.dao.StorageDao;



/**
 * The RemoteStorage is a helper class for storing the application's 
 * widgets and name/value pairs on the serverstore.
 * 
 * @author Jorge C. S. Cardoso
 */
public class ServerStorage {
	
	private String storageId;
	private StorageDao storage;
	
	/**
	 * Creates and empty RemoteStorage object.
	 */
	private ServerStorage(String placeId, String applicationName) {
		this.storageId = placeId+"-"+applicationName;
		
		/*
		 * Check if exists, if not create
		 * 
		 */
		Dao.beginTransaction();
		StorageDao storage = Dao.getStorage(this.storageId);
		if ( null == storage ) {
			storage = new StorageDao(this.storageId);
			Dao.put(storage);
		}
		Dao.commitOrRollbackTransaction();
	}
	
	public static ServerStorage get(String placeId, String applicationName) {
		ServerStorage rs = new ServerStorage(placeId, applicationName);
		
		
		return rs;
	}
	
	public void open() {
		Dao.beginTransaction();
		this.storage = Dao.getStorage(storageId);
	}
	
	public void close() {
		Dao.put(this.storage);
		Dao.commitOrRollbackTransaction();
	}
	
	public void setString(String name, String value) {
		Dao.beginTransaction();
		this.storage = Dao.getStorage(storageId);
		this.storage.setString(name, value);
		Dao.put(this.storage);
		Dao.commitOrRollbackTransaction();
	}
	
	public String getString(String name, String defaultValue) {
		String r = getString(name);
		if ( null != r ) {
			return r;
		}
		return defaultValue;
	}
	public String getString(String name) {
		Dao.beginTransaction();
		this.storage = Dao.getStorage(storageId);
		
		String value = this.storage.getString(name);
		Dao.commitOrRollbackTransaction();
		return value;
	}
	
	
	
	public void setLong(String name, long value) {
		this.setString(name, Long.toString(value));
	}
	
	public long getLong(String name) {
		String value = this.getString(name);
		return Long.parseLong(value);
	}
	
}
