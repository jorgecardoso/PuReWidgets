package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.Date;

import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImperativeClickHandler implements ClickHandler {
	private String referenceCode;

	
	public ImperativeClickHandler(String referenceCode) {
		this.referenceCode = referenceCode;
	
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		Log.debug("Sending input: " + this.referenceCode );
		//PlaceInteractionWebpage.sightingService.sighting(s, date, callback)
		
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d = new Date();
		//SightingReport.this.feedback.setText("Sending.");
		LoginInfo loginInfo = PlaceInteractionWebpage.loginInfo;
		String user = "";
		if (loginInfo.isLoggedIn()) {
			user = loginInfo.getEmailAddress();
		} else {
			user = "Anonymous";
		}
			
		PlaceInteractionWebpage.sightingService.sighting(user +" tag."+this.referenceCode, dtf.format(d),new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.debug(this, "An error ocurred.");
				
			}

			@Override
			public void onSuccess(Void result) {
				Log.debug(this, "Sent.");
				
			}
			
		});
	}

}
