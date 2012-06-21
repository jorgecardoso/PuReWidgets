/**
 * 
 */
package org.purewidgets.shared.widgets;

import org.purewidgets.shared.im.Widget;


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
}
