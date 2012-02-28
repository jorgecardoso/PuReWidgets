package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.event.dom.client.ClickEvent;

public class ImperativeClickHandler extends  BaseClickHandler {
	private String referenceCode;
	
	
	public ImperativeClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions,
			PopupUi popup) {
		super(placeName, applicationName, widgetId, widgetOptions, popup);
		this.referenceCode = referenceCode;
		
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		
		
		//this.sendInput("tag." + this.referenceCode);
		this.sendInput(widgetOptions.get(0), "");
	}

	
}
