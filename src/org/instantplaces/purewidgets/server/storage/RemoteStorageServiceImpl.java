/**
 * 
 */
package org.instantplaces.purewidgets.server.storage;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.storage.RemoteStorageService;
import org.instantplaces.purewidgets.server.dao.Dao;
import org.instantplaces.purewidgets.server.dao.StorageDao;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class RemoteStorageServiceImpl extends RemoteServiceServlet implements RemoteStorageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.client.storage.RemoteStorageService#get(java.lang.String)
	 */
	@Override
	public String[] get(String storageId, String name) {
		String[] result = new String[2];
		result[0] = name;
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		String value = storageDao.getString(name);
		Dao.commitOrRollbackTransaction();
		result[1] = value;
		return result;
	
	}

	@Override
	public String[] get(String storageId, ArrayList<String> names) {
		String[] result = new String[names.size()];
		
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		int i = 0;
		for ( String name: names ) {
			String value = storageDao.getString(name);
			result[i++] = value;
		}
		
		Dao.commitOrRollbackTransaction();
		
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.client.storage.RemoteStorageService#set(java.lang.String, java.lang.String)
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

	

}
