package org.purewidgets.shared.events;

/**
 * Interface for objects that want to listen to changes in reference codes.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public interface ReferenceCodeListener {
	/**
	 * Called by the Widget Manager to notify widgets that their 
	 * Reference Codes (may) have changed. It's not guaranteed that the 
	 * Reference Codes have actually changed. 
	 * 
	 * 
	 * Widget should update their visual representation to reflect the new
	 * Reference Codes.
	 */
	public void onReferenceCodesUpdated();
}
