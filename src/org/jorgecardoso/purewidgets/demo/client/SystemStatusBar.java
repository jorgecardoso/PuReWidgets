/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author "Jorge C. S. Cardoso"
 */
public class SystemStatusBar implements EntryPoint, ApplicationListListener {

	Timer timer;
	
	int currentApp = 0;
	ArrayList<Application> apps;
	
	long lastApplicationsUpdate = -1;
	
	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "SystemStatusBar", true);
		
		
		WidgetManager.get().setApplicationListListener(this);
		WidgetManager.get().setAutomaticInputRequests(false);
		lastApplicationsUpdate = System.currentTimeMillis();
		
		timer = new Timer() {
			@Override
			public void run() {
				refresh();
			}
		};
		
		timer.scheduleRepeating(15*1000);
		//refresh();
	
	}

	
	protected void refresh() {
		
		/*
		 * Update the list of apps from the IM
		 */
		if ( System.currentTimeMillis()-this.lastApplicationsUpdate > 30*1000 ) {
			Log.debug(this, "Asking server for list of applications");
			WidgetManager.get().getPlaceApplicationsList(false);
			this.lastApplicationsUpdate = System.currentTimeMillis();
		}
		
		
		
		/*
		 * Update the currently displayed features on the status bar
		 */
		Log.debug(this, "Updating features displayed");
		if ( null == this.apps || this.apps.size() == 0 ) {
			return;
		}
		if ( this.currentApp >= this.apps.size() ) {
			this.currentApp = 0;
		}
		Application app = this.apps.get(this.currentApp);
		this.currentApp++;
		
		/*
		 * Clear the panel that shows the features for this app
		 * and recreate it.
		 */
		RootPanel.get("features").clear();
		RootPanel.get("features").add(new Label("Application: " + app.getApplicationId()));
//		for ( Widget widget : app.getWidgets() ) {
//			if ( widget.isVolatileWidget() ) { 
//				continue;
//			}
//			
//			if ( widget.getWidgetOptions().size() == 1 ) {
//				RootPanel.get("features").add(new Label(widget.getShortDescription() + " [" + widget.getWidgetOptions().get(0).getReferenceCode()  + "] : " + widget.getLongDescription() + "<" + widget.getControlType() + ">"));
//			} else if ( widget.getWidgetOptions().size() > 1 ) { 
//				
//				
//			}
//		}
		
	}

	@Override
	public void onApplicationList(ArrayList<Application> applications) {
		Log.debug(this, "Received list of applications: " + applications.size() );
		/*
		 * Remove apps with no widgets
		 */
		Iterator<Application> it = applications.iterator();
		while ( it.hasNext() ) {
			Application ap = it.next();
			
			
//			if ( ap.getWidgets() != null ) {
//				boolean delete = true;
//				for (Widget w : ap.getWidgets() ) {
//					if ( !w.isVolatileWidget() ) {
//						delete = false;
//					}
//				}
//				if ( delete ) {
//					it.remove();
//				}
//			} else {
//				it.remove();
//			}
		}
		
		this.apps = applications;
		
		/*
		for ( Application app : applications ) {
			Log.debug(this, "Application: " + app.getApplicationId() );
			RootPanel.get().add(new Label(app.getApplicationId() + ":" ) );
			
			for ( Widget widget : app.getWidgets() ) {
				RootPanel.get().add(new Label(widget.getWidgetId() + " - " + widget.getShortDescription() + " [" + widget.getLongDescription() + "]"));
			}
		}*/
		
	}

	
}
