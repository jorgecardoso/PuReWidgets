package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;

public class MultipleOptionImperativeClickHandler extends BaseClickHandler {

	private ListBox listbox;
	
	public MultipleOptionImperativeClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions, ListBox listbox) {
		super(placeName, applicationName, widgetId, widgetOptions);
		this.listbox = listbox;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		WidgetOption referenceCode;
		int selected = listbox.getSelectedIndex();
		if ( selected >= 0 ) {
			referenceCode = this.widgetOptions.get(selected);
			this.sendInput(referenceCode, "");
		}
	}

}
