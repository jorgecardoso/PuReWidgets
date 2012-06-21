/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetParameter;

/**
 * 
 * The Download widget uses a "purewidgets-content-url" parameter to hold the target url.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Download extends Widget {

	public static final String CONTENT_URL_PARAMETER_NAME =  "purewidgets-content-url";
	
	private static ArrayList<WidgetParameter> createDownloadParameters(String url) {
		ArrayList<WidgetParameter> parameters = new ArrayList<WidgetParameter>();
		parameters.add(new WidgetParameter(CONTENT_URL_PARAMETER_NAME, url));
		return parameters;
	}
	
	
	public Download(String widgetId, String label, String url) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_DOWNLOAD, label, "", null, createDownloadParameters(url));
		
	}
	
}
