/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.instantplaces.purewidgets.shared.Log;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;


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
		long id = 0;
		try {
			id = Long.parseLong(pollId);
		} catch  (NumberFormatException nfe) {
			Log.warn(Dao.class.getName(), "Could not parse pollId: " + pollId );
		}
		return ofy.find(EBVPollDao.class, id);
	}
	
	public static List<EBVPollDao> getPolls(String placeId) {
		/*
		 * We can't get all root entities inside a transaction, so don't use the
		 * static ofy
		 */
		Objectify ofy_ = ObjectifyService.begin();
		Query<EBVPollDao> q = ofy_.query(EBVPollDao.class).filter("placeId", placeId);
		
		return q.list();
	}
	
	public static List<EBVPollDao> getActivePolls(String placeId) {
		/*
		 * We can't get all root entities inside a transaction, so don't use the
		 * static ofy
		 */
		Objectify ofy_ = ObjectifyService.begin();
		long today = new Date().getTime();
		Query<EBVPollDao> q = ofy_.query(EBVPollDao.class).filter("placeId", placeId).filter("showAfter <" , today);
		
		/*
		 * we can only filter using one inequality op so the rest must be done here
		 * 
		 * remove polls that have closed.
		 */
		ArrayList<EBVPollDao> filtered = new ArrayList<EBVPollDao>();
		
		for ( EBVPollDao poll : q.list() ) {
			if ( poll.getClosesOn() > today && poll.getShowUntil() > today) {
				filtered.add(poll);
			}
		}
		
		return filtered;
	}
	
	public static List<EBVPollDao> getClosedPolls(String placeId) {
		/*
		 * We can't get all root entities inside a transaction, so don't use the
		 * static ofy
		 */
		Objectify ofy_ = ObjectifyService.begin();
		long today = new Date().getTime();
		Query<EBVPollDao> q = ofy_.query(EBVPollDao.class).filter("placeId", placeId).filter("closesOn <" , today);
		
		/*
		 * we can only filter using one inequality op so the rest must be done here
		 * 
		 * remove polls that have closed.
		 */
		ArrayList<EBVPollDao> filtered = new ArrayList<EBVPollDao>();
		
		for ( EBVPollDao poll : q.list() ) {
			if ( poll.getShowUntil() > today ) {
				filtered.add(poll);
			}
		}
		
		return filtered;
	}
	
	
	public static void put(java.lang.Iterable<?> objs) {
		ofy.put(objs);
	}

	public static void put(Object o) {
		ofy.put(o);
	}

}