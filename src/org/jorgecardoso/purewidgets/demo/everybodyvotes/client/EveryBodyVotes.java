/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;


/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EveryBodyVotes implements PublicDisplayApplicationLoadedListener, EntryPoint{
	@Override
	public void onModuleLoad() {
		
		PublicDisplayApplication.load(this, "EveryBodyVotes", false);
		WidgetManager.get().setAutomaticInputRequests(false);
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			new Admin().run();
			return;
		}
		
		
	}
}
