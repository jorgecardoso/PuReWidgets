/**
 * 
 */
package org.instantplaces.purewidgets.client.application;

import org.instantplaces.purewidgets.client.storage.Storage;
import org.instantplaces.purewidgets.client.widgetmanager.ClientServerCommunicator;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;

/**
 * The PublicDisplayApplication class represents the graphical part of the public display application.
 * Its main purpose is to extract the application id from the URL query string and initialize the
 * WidgetManager.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class PublicDisplayApplication {
	/**
	 * The URL query string parameter name that holds the application id
	 */
	private static final String APP_NAME_PARAMETER = "appname";
	
	/**
	 * The URL query string parameter name that holds the place id
	 */
	private static final String PLACE_NAME_PARAMETER = "placename";	
	
	/**
	 * If the URL does not contain the application id, this application id will be used as default
	 */
	private static final String DEFAULT_APP_NAME = "DefaultApplication";

	/**
	 * If the URL does not contain the application id, this application id will be used as default
	 */
	private static final String DEFAULT_PLACE_NAME = "DefaultPlace";
	
	private static boolean loaded = false;
	
	private static String appName;
	
	private static Storage storage;
	
	private static boolean autoDeleteVolatile; 
	
	public static void load(EntryPoint entryPoint, String defaultAppName, boolean autoDeleteVolatile) {
		
		String place = com.google.gwt.user.client.Window.Location.getParameter(PLACE_NAME_PARAMETER);
		
		if (null == place) {
			place = DEFAULT_PLACE_NAME;
		}
		Log.debug(PublicDisplayApplication.class.getName(), "Using place name: " + place);
		
		appName = com.google.gwt.user.client.Window.Location.getParameter(APP_NAME_PARAMETER);
		
		if (null == appName) {
			if ( null == defaultAppName ) {
				appName = DEFAULT_APP_NAME;
			} else {
				appName = defaultAppName;
			}
		}
		Log.debug(PublicDisplayApplication.class.getName(), "Using application name: " + appName);
		
		WidgetManager.get().setServerCommunication(new ClientServerCommunicator(place, appName));
		
		/*
		 * Delete all volatile widgets that may have left on the server before
		 */
		PublicDisplayApplication.autoDeleteVolatile = autoDeleteVolatile;
		if ( autoDeleteVolatile ) {
			Log.debug(PublicDisplayApplication.class.getName(), "Removing volatile widgets");
			WidgetManager.get().removeAllWidgets(true);
		}
		
		PublicDisplayApplication.loaded = true;
		
		storage = new Storage(appName);
		
		Window.addCloseHandler(new CloseHandler<Window>() {

			@Override
			public void onClose(CloseEvent<Window> event) {
				if ( PublicDisplayApplication.autoDeleteVolatile ) {
					Log.debug(this, "Removing volatile widgets");
					WidgetManager.get().removeAllWidgets(true);
				}
			}});
	}
	
	public static Storage getStorage() {
		if ( !loaded ) {
			Log.error("org.instantplaces.purewidgets.client.aplication.PublicDisplayApplication", "Error getting Storage: application not loaded yet. Call load() first");
			return null;
		} else {
			return storage;
		}
	}
	
	
}
