package org.instantplaces.purewidgets.shared.events;

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
