/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;


/**
 * A Download widget.
 * 
 * The Download widget uses a "purewidgets-content-url" parameter to hold the target url.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Download extends Widget {

	/**
	 * The widget parameter name used to hold the Url of the content that can be downloaded
	 */
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
	
	/**
	 * Creates a new Download widget with the given id, label and url.
	 * 
	 * @param widgetId the id of the widget.
	 * @param label the label for the download button (short description)
	 * @param url the url where the content associated with the widget can be downloaded
	 */
	public Download(String widgetId, String label, String url) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_DOWNLOAD, label, "", null, createDownloadParameters(url));
		
	}
	
	/**
	 * Creates a new Download widget with the given id, label, url, long description, options and parameters.
	 * The short description is used as the button label.
	 * 
	 * @param widgetId the id of the widget.
	 * @param label the label for the download button (short description)
	 * @param url the url where the content associated with the widget can be downloaded
	 * @param longDescription the long description for the widget.
	 * @param widgetOptions the widget options
	 * @param parameters the widget parameters.
	 */	
	public Download(String widgetId, String label, String url, String longDescription, ArrayList<WidgetOption> options, ArrayList<WidgetParameter> parameters) {
		/* 
		 * Use the label as the short description
		 */
		//parameters.addAll(createDownloadParameters(url));
		super(widgetId, Widget.CONTROL_TYPE_DOWNLOAD, label, longDescription, options,  combine(parameters, createDownloadParameters(url)));
		
	}
	
}
