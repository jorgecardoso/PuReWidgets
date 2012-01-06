package org.jorgecardoso.purewidgets.demo.test.client;




import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test implements PublicDisplayApplicationLoadedListener, EntryPoint{
	
	
	@Override
	public void onModuleLoad() {
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("placetags");
		parameters.add("non");
		
		PublicDisplayApplication.load(this, "Test", false, parameters);
		WidgetManager.get().setAutomaticInputRequests(false);
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			Admin.run();
			return;
		}
		
		RootPanel.get().add(new Label(PublicDisplayApplication.getParameterString("non", "default")));
		
	}
}
