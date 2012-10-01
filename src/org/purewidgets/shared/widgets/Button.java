/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;


/**
 * A Button widget that can be activated.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class Button extends Widget {	
	
	
	/**
	 * Creates a new Button widget with the given id and label.
	 * 
	 * @param widgetId The id of the widget.
	 * @param label The label for the button.
	 */
	public Button(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_IMPERATIVE_SELECTION, label,"", null, null);
	}
	
	/**
	 * Creates a new Button widget with the given id, short description, long description, options and parameters.
	 * The short description is used as the button label.
	 * In general it does not make sense to create a button with more than one widget option.
	 * 
	 * @param widgetId the id of the widget.
	 * @param shortDescription the short description for the widget.
	 * @param longDescription the long description for the widget.
	 * @param widgetOptions the widget options
	 * @param parameters the widget parameters.
	 */
	public Button(String widgetId, String shortDescription, String longDescription, ArrayList<WidgetOption> widgetOptions, ArrayList<WidgetParameter> parameters) {
		super(widgetId, Widget.CONTROL_TYPE_IMPERATIVE_SELECTION, shortDescription, longDescription, widgetOptions, parameters);
	}
}
