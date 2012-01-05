/**
 * 
 */
package org.instantplaces.purewidgets.server.storage;

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
	public String get(String storageId, String name) {
		
		Dao.beginTransaction();
		
		StorageDao storageDao = Dao.getStorage(storageId);
		if ( null == storageDao ) {
			return null;
		}
		String value = storageDao.getString(name);
		Dao.commitOrRollbackTransaction();
		return value;
	
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
