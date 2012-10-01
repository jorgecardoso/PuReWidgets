/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;


import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.Widget;

/**
 * A TextBox widget that accepts text.
 * 
 * @author Jorge C. S. Cardoso
 *
 */

public class TextBox extends Widget {

	/**
	 * Creates a new TextBox with the given id and label
	 * 
	 * @param widgetId The id for the widget.
	 * @param label The label of the textbox.
	 */
	public TextBox(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_ENTRY, label, "", null, null);
	}
	
	/**
	 * Handles input directed at the textbox.
	 */
	@Override
	public void handleInput(ArrayList<WidgetInputEvent> inputEventList) {
		
		/*
		 * If the textbox received text, trigger an application event
		 */
		for (WidgetInputEvent ie : inputEventList) {
			if ( null != ie.getParameters() && ie.getParameters().size() > 0) {
				ActionEvent<TextBox> ae = new ActionEvent<TextBox>(ie, this, ie.getParameters().get(0) );
				this.fireActionEvent(ae);
			} 
		}
		
		/*
		 * Otherwise do nothing
		 * TODO: How to handle errors in the server ? 
		 */
		
	}

}
