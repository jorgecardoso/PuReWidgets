/**
 * 
 */
package org.instantplaces.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.InputEvent;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;

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
	
	public ListBox(String widgetId, String listLabel, java.util.List<String> options) {
		/*
		 * Use the label as the short description
		 */
		super(widgetId, listLabel, null, false);
		this.setControlType(Widget.CONTROL_TYPE_IMPERATIVE_SELECTION);
		this.listOptions = options;
		
		updateListOptions(options);
		
	}

	/**
	 * @param options
	 */
	private void updateListOptions(java.util.List<String> options) {
		ArrayList<WidgetOption> widgetOptions = new ArrayList<WidgetOption>();
		for ( String option : options ) {
			WidgetOption wo = new WidgetOption(option);
			wo.setShortDescription(option);
			widgetOptions.add(wo);
		}
		this.setWidgetOptions(widgetOptions);
		this.sendToServer();
	}
	
	@Override
	public void handleInput(ArrayList<InputEvent> inputEventList) {
		
		/*
		 * If the textbox received text, trigger an application event
		 */
		for (InputEvent ie : inputEventList) {
			if ( null != ie.getParameters() && ie.getParameters().size() > 0) {
				String selectedOption;
				WidgetOption wo = ie.getWidgetOption();
				int index = this.getWidgetOptions().indexOf(wo);
				selectedOption = this.listOptions.get(index);
				ActionEvent<ListBox> ae = new ActionEvent<ListBox>(this, ie, selectedOption );
				this.fireActionEvent(ae);
			} 
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

	/**
	 * @param listOptions the listOptions to set
	 */
	public void setListOptions(java.util.List<String> listOptions) {
		this.listOptions = listOptions;
		this.updateListOptions(listOptions);
	}

}
