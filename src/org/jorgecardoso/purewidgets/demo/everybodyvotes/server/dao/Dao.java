/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao;

import java.util.List;

import org.instantplaces.purewidgets.shared.Log;


import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */

public class Dao extends DAOBase {
	private static Objectify ofy;

	static {
		ObjectifyService.register(EBVPollDao.class);
		
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
			Log.warn("Could not commit transaction: " + e.getMessage());
		} finally {
			if (ofy.getTxn().isActive()) {
				try {
					ofy.getTxn().rollback();
				} catch (Exception e) {
					Log.warn("Problem rolling back:" + e.getMessage());
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


	public static EBVPollDao getPoll(String pollId) {
		return ofy.find(EBVPollDao.class, pollId);
	}
	
	public static List<EBVPollDao> getPolls(String placeId) {
		/*
		 * We can't get all root entities inside a transaction, so don't use the
		 * static ofy
		 */
		Objectify ofy_ = ObjectifyService.begin();
		Query<EBVPollDao> q = ofy_.query(EBVPollDao.class);//.filter("placeId", placeId);
		
		return q.list();
	}
	
	
	public static void put(java.lang.Iterable<?> objs) {
		ofy.put(objs);
	}

	public static void put(Object o) {
		ofy.put(o);
	}

}