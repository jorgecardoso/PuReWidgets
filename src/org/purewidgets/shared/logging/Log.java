package org.purewidgets.shared.logging;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to the logging mechanism for the PuReWidgets toolkit.
 * 
 * @author Jorge C. S. Cardoso
 */
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
	static Logger logger;
	
	static {
		logger = Logger.getLogger("PuReWidgets");
	}
	
	/**
	 * Returns a "PuReWidgets" logger.
	 * @return
	 */
	public static Logger get() {
		return logger;
	}
	
	/**
	 * Sets the level for the "PuReWidgets" logger.
	 * @param level
	 */
	public static void setLevel(Level level) {
		Log.get().setLevel(level);
	}
	
	/* 
	 * SEVERE 
	 */
   private static void logStackTrace(Throwable e) {
	    error(e.getMessage()); 
		StringBuilder sb = new StringBuilder();
		if ( e.getStackTrace() == null || e.getStackTrace().length == 0 ) {
			sb.append("No stack trace to print");
		} else {
			sb.append("Stack trace:\n");
		}
		for ( StackTraceElement s : e.getStackTrace() ) {
			sb.append(s.toString()+"\n");
		}
		error(sb.toString());
	}

	private static void error(String msg) {
		Log.get().log(Level.SEVERE, msg);
	}
	
	/**
	 * Logs an error message.
	 * 
	 * @param s The object where the error occurred.
	 * @param msg The log message.
	 */
	public static void error(Object s, String msg) {
		error(s.getClass().getName() + ": " + msg);
	}
	
	/**
	 * Logs an error message.
	 * 
	 * @param s The class name of the object where the error occurred.
	 * @param msg The log message.
	 */
	public static void error(String s, String msg) {
		error(s + ": " + msg);
	}
	
	/**
	 * Logs an error message.
	 * 
	 * @param s The class name of the object where the error occurred.
	 * @param msg The log message.
	 * @param e The exception that caused the error. 
	 */	
	public static void error(Object s, String msg, Throwable e) {
		error(s.getClass().getName() + ": " + msg);
		logStackTrace(e);
	}
	
	/*
	 * WARNING
	 */
	private static void warn(String msg) {
		Log.get().log(Level.WARNING, msg);
	}
	
	/**
	 * Logs a warn message.
	 * 
	 * @param s The object where the warning occurred.
	 * @param msg The log message.
	 */	
	public static void warn(Object s, String msg) {
		warn(s.getClass().toString() + ": " + msg);
	}
	
	/**
	 * Logs a warn message.
	 * 
	 * @param s The class name of the object where the warning occurred.
	 * @param msg The log message.
	 */	
	public static void warn(String s, String msg) {
		warn(s + ": " + msg);
	}
	
	
	/**
	 * Logs a warn message.
	 * 
	 * @param s The class name of the object where the warning occurred.
	 * @param msg The log message.
	 * @param e The exception that caused the warning. 
	 */		
	public static void warn(Object s, String msg, Throwable e) {
		warn(s.getClass().getName() + ": " + msg);
		logStackTrace(e);
	}
	
	/*
	 * INFO
	 */
	private static void info(String msg) {
		Log.get().log(Level.INFO, msg);
	}	
	
	/**
	 * Logs an info message.
	 * 
	 * @param s The object where the info occurred.
	 * @param msg The log message.
	 */		
	public static void info(Object s, String msg) {
		info(s.getClass().getName() + ": " + msg);
	}
	
	/**
	 * Logs an info message.
	 * 
	 * @param s The class name of the object where the info occurred.
	 * @param msg The log message.
	 */	
	public static void info(String s, String msg) {
		info(s + ": " + msg);
	}
	
	/*
	 * FINE
	 */
	public static void debug(String msg) {
		Log.get().log(Level.FINE, msg);
	}
	
	/**
	 * Logs a debug message.
	 * 
	 * @param s The object where the debug  occurred.
	 * @param msg The log message.
	 */		
	public static void debug(Object s, String msg) {
		debug(s.getClass().getName() + ": " + msg);
	}	
	
	/**
	 * Logs a debug message.
	 * 
	 * @param s The class name of the object where the debug occurred.
	 * @param msg The log message.
	 */		
	public static void debug(String s, String msg) {
		debug(s + ": " + msg);	
	}	
	
	/**
	 * Logs a debug message.
	 * 
	 * @param s The class name of the object where the debug occurred.
	 * @param msg The log message.
	 * @param e The exception that caused the debug. 
	 */		
	public static void debug(Object s, String msg, Throwable e) {
		debug(s.getClass().getName() + ": " + msg);
		logStackTrace(e);
	}
	
	/*
	 * FINEST
	 */

	private static void debugFinest(String msg) {
		Log.get().log(Level.FINEST, msg);
	}
	
	/**
	 * Logs a fine debug message.
	 * 
	 * @param s The object where the debug  occurred.
	 * @param msg The log message.
	 */		
	public static void debugFinest(Object s, String msg) {
		debugFinest(s.getClass().getName() + ": " + msg);
	}	
	
	/**
	 * Logs a fine debug message.
	 * 
	 * @param s The class name of the object where the debug occurred.
	 * @param msg The log message.
	 */			
	public static void debugFinest(String s, String msg) {
		debugFinest(s + ": " + msg);	
	}
	
	
}
