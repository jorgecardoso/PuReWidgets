package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;

public class EntryClickHandler extends BaseClickHandler {
	
	private String referenceCode;
	private TextBox textbox;
	
	public EntryClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions, TextBox textbox,
			PopupUi popup) {
		super(placeName, applicationName, widgetId, widgetOptions, popup);
		
		this.referenceCode = referenceCode;
		this.textbox = textbox;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		
		//this.sendInput("tag.\""+this.referenceCode+ ":" + this.textbox.getText()+"\"");
		this.sendInput(this.widgetOptions.get(0), this.textbox.getText());
	}

}
