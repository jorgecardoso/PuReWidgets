package org.purewidgets.server.application;


/**
 * This interface defines the methods for the application life-cycle.
 * 
 * Life-cycle: onPDApplicationLoaded->(Action Events)->onPDApplicationEnded
 * 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface PDApplicationLifeCycle  {
	
	/**
	 * Loaded is called for every session after the application is loaded (or created
	 * if it is the first time)
	 * 
	 * @param application
	 */
	public void onPDApplicationLoaded(PDApplication application);
	

	/**
	 * Finish is called on every session, at the end. Before finish is called there may be several onAction calls
	 * in result to user input and this is probably where most of the action will be. Finish allows the
	 * application to display results based on its current state and after integrating the potential
	 * user input.
	 */
	public void onPDApplicationEnded();
}
