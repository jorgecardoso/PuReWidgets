/**
 * 
 */
package org.purewidgets.client.application;

/**
 * This interface defines the life-cycle methods that a public display application goes through in the
 * course of its execution.
 * 
 * GWT applications that are meant to run as public display applications must implement this interface.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public interface PDApplicationLifeCycle {
	
	/**
	 * Triggers the loaded event after the corresponding PDApplication has been fully loaded.
	 * 
	 * @param pdApplication The PDApplication that has been loaded.
	 */
	public void onPDApplicationLoaded(PDApplication pdApplication);
}
