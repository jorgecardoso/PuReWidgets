package org.purewidgets.server.application;

import org.purewidgets.shared.events.ActionListener;

/**
 * This interface defines the methods for the application life-cycle.
 * 
 * Life-cycle: loaded->(setup)->start->(onAction)->finish
 * 
 * setup is only called the first time.
 * onAction is only called if there is any input.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface ApplicationLifeCycle extends ActionListener {
	
	/**
	 * Loaded is called for every session after the application is loaded (or created
	 * if it is the first time)
	 * 
	 * @param application
	 */
	public void loaded(PublicDisplayApplication application);
	
	/**
	 * Setup is called only once for each application and basically allows the application
	 * to initialize whatever datastructures it needs.
	 */
	public void setup();
	
	/**
	 * Called after loaded and setup. This allows applications to run code on every session after the application is loaded.
	 */
	public void start();
	
	/**
	 * Finish is called on every session, at the end. Before finish is called there may be several onAction calls
	 * in result to user input and this is probably where most of the action will be. Finish allows the
	 * application to display results based on its current state and after integrating the potential
	 * user input.
	 */
	public void finish();
}
