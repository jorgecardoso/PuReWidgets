/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

/**
 * A widget that provides a list of options to users.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ListBox extends Widget {

	/**
	 * The list of options for this list.
	 */
	private java.util.List<String> listOptions;
	
	
	/**
	 * @param options
	 */
	private static ArrayList<WidgetOption> createListOptions(java.util.List<String> options) {
		ArrayList<WidgetOption> widgetOptions = new ArrayList<WidgetOption>();
		for ( String option : options ) {
			WidgetOption wo = new WidgetOption(option);
			wo.setShortDescription(option);
			widgetOptions.add(wo);
		}
		return widgetOptions;
	}
	
	public ListBox(String widgetId, String listLabel, java.util.List<String> options) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, Widget.CONTROL_TYPE_IMPERATIVE_SELECTION, listLabel, "", createListOptions(options), null);
		this.listOptions = options;
	}

	
	
	@Override
	public void handleInput(ArrayList<WidgetInputEvent> inputEventList) {
		Log.debug(this, "Handling input");
		/*
		 * If the textbox received text, trigger an application event
		 */
		for (WidgetInputEvent ie : inputEventList) {
			
			//if ( null != ie.getParameters() ) {
				Log.debug(this, "Firing event to app");
				String selectedOption;
				WidgetOption wo = ie.getWidgetOption();
				int index = this.getWidgetOptions().indexOf(wo);
				selectedOption = this.listOptions.get(index);
				ActionEvent<ListBox> ae = new ActionEvent<ListBox>(ie, this, selectedOption );
				this.fireActionEvent(ae);
		//	} 
		}
		
		/*
		 * Otherwise do nothing
		 * TODO: How to handle errors in the server ? 
		 */
		
	}

	/**
	 * @return the listOptions
	 */
	public java.util.List<String> getListOptions() {
		return listOptions;
	}
}
