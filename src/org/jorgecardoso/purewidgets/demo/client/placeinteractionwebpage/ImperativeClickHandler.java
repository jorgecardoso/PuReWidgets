package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import com.google.gwt.event.dom.client.ClickEvent;

public class ImperativeClickHandler extends  BaseClickHandler {
	private String referenceCode;
	
	
	public ImperativeClickHandler(String referenceCode) {
		super();
		this.referenceCode = referenceCode;
		
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		
		
		this.sendInput("tag." + this.referenceCode);
	}

	
}
