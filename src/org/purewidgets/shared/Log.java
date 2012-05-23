package org.purewidgets.shared;


import java.util.logging.Level;
import java.util.logging.Logger;


public class Log {
	/*
	 * SEVERE (highest value)
WARNING
INFO
CONFIG
FINE
FINER
FINEST (lowest value)
	 */
	
	public static Logger get() {
		return Logger.getLogger("PuReWidgets");
	}
	
	
	/* 
	 * SEVERE 
	 */
	
	private static void error(String msg) {
		Log.get().log(Level.SEVERE, msg);
	}
	
	public static void error(Object s, String msg) {
		error(s.getClass().getName() + ": " + msg);
	}
	
	public static void error(String s, String msg) {
		error(s + ": " + msg);
	}
	
	/*
	 * WARNING
	 */
	private static void warn(String msg) {
		Log.get().log(Level.WARNING, msg);
	}
	
	public static void warn(Object s, String msg) {
		warn(s.getClass().toString() + ": " + msg);
	}
	
	public static void warn(String s, String msg) {
		warn(s + ": " + msg);
	}
	
	
	/*
	 * INFO
	 */
	private static void info(String msg) {
		Log.get().log(Level.INFO, msg);
	}	
	
	public static void info(Object s, String msg) {
		info(s.getClass().getName() + ": " + msg);
	}
	
	public static void info(String s, String msg) {
		info(s + ": " + msg);
	}
	
	/*
	 * FINE
	 */
	public static void debug(String msg) {
		Log.get().log(Level.FINE, msg);
	}
	
	public static void debug(Object s, String msg) {
		debug(s.getClass().getName() + ": " + msg);
	}	
	
	public static void debug(String s, String msg) {
		debug(s + ": " + msg);	
	}	
	
	
	
	/*
	 * FINEST
	 */
	public static void debugFinest(String msg) {
		Log.get().log(Level.FINEST, msg);
	}
	
	public static void debugFinest(Object s, String msg) {
		debugFinest(s.getClass().getName() + ": " + msg);
	}	
	
	public static void debugFinest(String s, String msg) {
		debugFinest(s + ": " + msg);	
	}
	
	
}
