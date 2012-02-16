package org.jorgecardoso.purewidgets.demo.helloworld.client;


import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.widgets.GuiButton;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class HelloWorld implements PublicDisplayApplicationLoadedListener, EntryPoint{
	
	@Override
	public void onModuleLoad() {
		
		/*
		 * Give a name to the application and initialize some 
		 * background processes.
		 */
		PublicDisplayApplication.load(this, "HelloWorld", true);
	
	
	}

	@Override
	public void onApplicationLoaded() {
		Application app = PublicDisplayApplication.getApplication();
		if ( null != app ) {
			if ( null == app.getIconBaseUrl() ) {
				app.setIconBaseUrl("http://www.thetechcheck.com/wp-content/uploads/2011/02/GMail.png");
				WidgetManager.get().getServerCommunicator().setApplication(app.getPlaceId(), app.getApplicationId(), app, null);
			}
		}
		
		/*
		 * Create a PuReWidgets button with associated graphical 
		 * representation.
		 */
		GuiButton guiButton = new GuiButton("hello-Button", "Hello World");
		
		/*
		 * Give some context information
		 */
		guiButton.setShortDescription("Say hello!");
		guiButton.setLongDescription("Say hello to be greeted by the HelloWorld application");
		
		/*
		 * Register the action listener for the button and show a popup 
		 * when a user activates it.
		 */
		guiButton.addActionListener(new ActionListener() {
			@Override
			public void onAction(ActionEvent<?> e) {
				
				PopupPanel popup = new PopupPanel();
				popup.add( new Label("Hello " + e.getPersona() + "!") );
				popup.center();
				popup.show();
			}
		});
		
		/*
		 * Add the graphical representation of the button to the browser
		 * window.
		 */
		RootPanel.get("main").add(guiButton);
		
	}
}
