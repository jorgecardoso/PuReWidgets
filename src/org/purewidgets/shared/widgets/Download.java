/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;

import com.google.gwt.user.client.rpc.core.java.util.Collections;

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
	
	private static ArrayList<WidgetParameter> combine(ArrayList<WidgetParameter> p1, ArrayList<WidgetParameter> p2) {
		ArrayList<WidgetParameter> p = new ArrayList<WidgetParameter>();
		p.addAll(p1);
		p.addAll(p2);
		return p;
	}
	
	public Download(String widgetId, String label, String url) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_DOWNLOAD, label, "", null, createDownloadParameters(url));
		
	}
	
	public Download(String widgetId, String label, String url, String longDescription, ArrayList<WidgetOption> options, ArrayList<WidgetParameter> parameters) {
		/* 
		 * Use the label as the short description
		 */
		//parameters.addAll(createDownloadParameters(url));
		super(widgetId, Widget.CONTROL_TYPE_DOWNLOAD, label, longDescription, options,  combine(parameters, createDownloadParameters(url)));
		
	}
	
}
