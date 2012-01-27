/**
 * 
 */
package org.instantplaces.purewidgets.server.dao;

import org.instantplaces.purewidgets.shared.Log;


import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */

public class Dao extends DAOBase {
	private static Objectify ofy;

	static {
		ObjectifyService.register(StorageDao.class);
	}

	public static void beginTransaction() {
		ofy = ObjectifyService.beginTransaction();
	}

	public static boolean commitOrRollbackTransaction() {
		boolean success = false;
		try {
			ofy.getTxn().commit();
			success = true;
		} catch (Exception e) {
			Log.warn(Dao.class.getName(), "Could not commit transaction: " + e.getMessage());
		} finally {
			if (ofy.getTxn().isActive()) {
				try {
					ofy.getTxn().rollback();
				} catch (Exception e) {
					Log.warn(Dao.class.getName(), "Problem rolling back:" + e.getMessage());
				}
			}
		}

		return success;
	}

	
	public static void delete(java.lang.Iterable<?> keysOrEntities) {
		ofy.delete(keysOrEntities);
	}
	
	
	public static void delete(Object o) {
		ofy.delete(o);
	}


	public static StorageDao getStorage(String storageId) {
		return ofy.find(StorageDao.class, storageId);
	}

	
	public static void put(java.lang.Iterable<?> objs) {
		ofy.put(objs);
	}

	public static void put(Object o) {
		ofy.put(o);
	}

}