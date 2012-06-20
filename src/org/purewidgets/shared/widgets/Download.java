/**
 * 
 */
package org.purewidgets.shared.widgets;

import org.purewidgets.shared.interactionmanager.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Download extends Widget {

	
	public Download(String widgetId, String label, String url) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, label, null);
		this.setControlType(Widget.CONTROL_TYPE_DOWNLOAD);
		this.setContentUrl(url);
		this.setShortDescription(label);
		//this.label = label;
	}
	
}
