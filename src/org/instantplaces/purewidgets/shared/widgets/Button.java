/**
 * 
 */
package org.instantplaces.purewidgets.shared.widgets;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author Jorge C. S. Cardoso
 *
 */
@PersistenceCapable
public class Button extends Widget {	
	
	@Persistent
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
