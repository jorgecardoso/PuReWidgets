package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;

public class MultipleOptionImperativeClickHandler extends BaseClickHandler {
	private ArrayList<WidgetOption>  referenceCodes;
	private ListBox listbox;
	
	public MultipleOptionImperativeClickHandler(ArrayList<WidgetOption> referenceCodes, ListBox listbox) {
		super();
		this.referenceCodes = referenceCodes;
		this.listbox = listbox;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		String referenceCode;
		int selected = listbox.getSelectedIndex();
		if ( selected >= 0 ) {
			referenceCode = referenceCodes.get(selected).getReferenceCode();
			this.sendInput("tag."+referenceCode);
		}
	}

}
