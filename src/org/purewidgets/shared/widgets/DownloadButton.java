/**
 * 
 */
package org.purewidgets.shared.widgets;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class DownloadButton extends Widget {

	
	public DownloadButton(String widgetId, String label, String url) {
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
