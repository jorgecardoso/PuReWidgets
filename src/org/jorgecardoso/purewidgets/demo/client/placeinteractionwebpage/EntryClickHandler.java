package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;

public class EntryClickHandler extends BaseClickHandler {
	
	private String referenceCode;
	private TextBox textbox;
	
	public EntryClickHandler(String referenceCode, TextBox textbox) {
		super();
		this.referenceCode = referenceCode;
		this.textbox = textbox;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		
		this.sendInput("tag."+this.referenceCode+ ":" + this.textbox.getText());
	}

}
