package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.im.Widget;

public class Upload  extends Widget {
	
	public Upload(String widgetId, String label) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_UPLOAD, label, "", null, null);
	}
	
	
	@Override
	public void handleInput(ArrayList<InputEvent> inputEventList) {
		
		/*
		 * If the upload received a file, trigger an application event
		 */
		for (InputEvent ie : inputEventList) {
			if ( null != ie.getParameters() && ie.getParameters().size() > 0) {
				ActionEvent<Upload> ae = new ActionEvent<Upload>(this, ie, ie.getParameters().get(0) );
				this.fireActionEvent(ae);
			} 
		}
	}
}
