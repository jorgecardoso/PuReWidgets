package org.jorgecardoso.purewidgets.demo.client.stresstest;




import java.util.ArrayList;
import java.util.Date;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.widgets.GuiButton;
import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.shared.Log;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.LoginInfo;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.PlaceInteractionWebpage;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.SightingService;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.SightingServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StressTest implements EntryPoint{
	public static SightingServiceAsync sightingService;
	
	Timer timerAddDelete;
	Timer timerInput;
	
	ArrayList<GuiWidget> widgets; 
	
	int widgetIdIndex;
	
	@Override
	public void onModuleLoad() {
		Date d = new Date();
		String date = DateTimeFormat.getFormat("y-M-d:H:m:s").format(d);
		
		PublicDisplayApplication.load(this, "StressTest-" + date, true);
		
		//GuiButton b = new GuiButton("button", "button");
		//b.setVolatile(true);
		
		
		sightingService = GWT.create(SightingService.class);
		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");
		
		widgets = new ArrayList<GuiWidget>();
		
		widgetIdIndex = 0;
		
		timerAddDelete = new Timer() {
			@Override
			public void run() {
				addDelete();
			}
		};
		timerAddDelete.scheduleRepeating(60000);
		
		
		timerInput = new Timer() {
			@Override
			public void run() {
				input();
			}
		};
		timerInput.scheduleRepeating(10000);
		addDelete();
		
	}
	
	private void input() {
		if (this.widgets.size() < 1 ) return;
		Log.debug("Sending input");
		//PlaceInteractionWebpage.sightingService.sighting(s, date, callback)
		
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d = new Date();
		//SightingReport.this.feedback.setText("Sending.");
		
		String user = "StressTest"+(int)(Math.random()*100);
		
		int randomIndex = (int) (Math.random()*this.widgets.size());
		
		String referenceCode = this.widgets.get(randomIndex).getWidgetOptions().get(0).getReferenceCode();
		Log.debug("Sending input to reference code: " + referenceCode);
		sightingService.sighting(user + " tag."+ referenceCode, dtf.format(d), new AsyncCallback<Void>() {

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
	
	private void addDelete() {
		/*
		 * Delete all current widgets
		 */
		for ( GuiWidget widget : widgets ) {
			widget.removeFromServer();
		}
		
		widgets.clear();
		/*
		 * Add new ones
		 */
		
		for (int i = 0; i < 5; i++) {
			
			GuiButton b = new GuiButton("button "+widgetIdIndex, "Button "+widgetIdIndex);
			b.setShortDescription("Test button " + widgetIdIndex);
			b.setVolatile(true);
			widgetIdIndex++;
			
			widgets.add(b);
		}
		
	}
}
