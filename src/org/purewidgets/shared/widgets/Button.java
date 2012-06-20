/**
 * 
 */
package org.purewidgets.shared.widgets;

import org.purewidgets.shared.interactionmanager.Widget;


/**
 * @author Jorge C. S. Cardoso
 *
 */
public class Button extends Widget {	
	
	
	String label;
	
	/*
	protected Button() {
		
	}*/
	
	public Button(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, label, null);
		this.setControlType(Widget.CONTROL_TYPE_IMPERATIVE_SELECTION);
		this.label = label;
	}
	
	/*
	@Override
	public void handleInput(InputEvent ie) {
		ActionEvent<Button> ae = new ActionEvent<Button>(this, ie.getWidgetOption());
		
		this.triggerActionEvent(ae);
	}*/

	
	
}
