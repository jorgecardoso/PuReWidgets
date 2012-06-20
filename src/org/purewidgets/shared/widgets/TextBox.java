/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;


import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.interactionmanager.Widget;

/**
 * @author Jorge C. S. Cardoso
 *
 */

public class TextBox extends Widget {

	private String caption;
	
	
	public TextBox(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, label, null);
		this.setControlType(Widget.CONTROL_TYPE_ENTRY);
		
		this.caption = label;
		//this.sendToServer();
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

	/**
	 * @return the label
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param label the label to set
	 */
	public void setCaption(String label) {
		this.caption = label;
	}
	
}
