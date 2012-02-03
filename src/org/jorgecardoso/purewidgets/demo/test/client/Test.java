package org.jorgecardoso.purewidgets.demo.test.client;




import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;
import org.instantplaces.purewidgets.client.widgets.GuiTextBox;
import org.instantplaces.purewidgets.client.widgets.GuiListBox;
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
		
		PublicDisplayApplication.load(this, "Test", false);
		WidgetManager.get().setAutomaticInputRequests(false);
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			Admin.run();
			return;
		}
		WidgetManager.get().setAutomaticInputRequests(false);
		
		
		ArrayList<String> l = new ArrayList<String>();
		l.add("I don't go");
		l.add("Once a week");
		l.add("Twice a week");
		GuiListBox tb = new GuiListBox("lista", "On average, how many times to you go to the movies?", l);
		GuiListBox tb1 = new GuiListBox("lista", "On average, how many times to you go to the movies?", l);
		
		RootPanel.get("content").add(tb);
		RootPanel.get("content").add(tb1);
		
	}
}
