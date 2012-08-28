/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;


/**
 * @author Jorge C. S. Cardoso
 *
 */
public class Button extends Widget {	
	
	
	public Button(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_IMPERATIVE_SELECTION, label,"", null, null);
	}
	
	public Button(String widgetId, String shortDescription, String longDescription, ArrayList<WidgetOption> widgetOptions, ArrayList<WidgetParameter> parameters) {
		super(widgetId, Widget.CONTROL_TYPE_IMPERATIVE_SELECTION, shortDescription, longDescription, widgetOptions, parameters);
	}
}
