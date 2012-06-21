/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;


import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.im.Widget;

/**
 * @author Jorge C. S. Cardoso
 *
 */

public class TextBox extends Widget {

	public TextBox(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_ENTRY, label, "", null, null);
	}
	
	@Override
	public void handleInput(ArrayList<InputEvent> inputEventList) {
		
		/*
		 * If the textbox received text, trigger an application event
		 */
		for (InputEvent ie : inputEventList) {
			if ( null != ie.getParameters() && ie.getParameters().size() > 0) {
				ActionEvent<TextBox> ae = new ActionEvent<TextBox>(this, ie, ie.getParameters().get(0) );
				this.fireActionEvent(ae);
			} 
		}
		
		/*
		 * Otherwise do nothing
		 * TODO: How to handle errors in the server ? 
		 */
		
	}

}
