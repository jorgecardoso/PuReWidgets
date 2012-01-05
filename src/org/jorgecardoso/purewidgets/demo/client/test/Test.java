package org.jorgecardoso.purewidgets.demo.client.test;




import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test implements EntryPoint{
	
	
	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "Test", false);
		WidgetManager.get().setAutomaticInputRequests(false);
		
		//GuiDownloadButton gdb = new GuiDownloadButton("downloadid", "Download test video", "http://jorgecardoso.eu");
		RemoteStorage rs = new RemoteStorage("DefaultPlace", "App");
		
		rs.setString("teste1", "value", new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.debug("Failed");
				Log.debug(caught.getMessage());
				RootPanel.get().add(new Label("Failed"));
				
			}

			@Override
			public void onSuccess(Void result) {
				Log.debug("ok");
				RootPanel.get().add(new Label("ok"));
			}
			
		});
		
		rs.getString("teste",  new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.debug("Failed");
				Log.debug(caught.getMessage());
				RootPanel.get().add(new Label("Failed"));
				
			}

			@Override
			public void onSuccess(String result) {
				Log.debug("ok:" + result);
				RootPanel.get().add(new Label("ok: " + result));
			}
			
		});
	}
}
