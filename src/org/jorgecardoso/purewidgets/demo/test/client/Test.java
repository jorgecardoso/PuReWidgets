package org.jorgecardoso.purewidgets.demo.test.client;




import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgets.GuiButton;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;
import org.instantplaces.purewidgets.client.widgets.GuiTextBox;
import org.instantplaces.purewidgets.client.widgets.GuiListBox;
import org.instantplaces.purewidgets.client.widgets.GuiUpload;
import org.instantplaces.purewidgets.client.widgets.youtube.JsonVideoList;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Upload;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test implements PublicDisplayApplicationLoadedListener, EntryPoint, ActionListener{
	
	String postUrl = "";
	String uploadId = "";
	
	@Override
	public void onModuleLoad() {
		
		PublicDisplayApplication.load(this, "Test", false);
		
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			Admin.run();
			return;
		}
		Application app = PublicDisplayApplication.getApplication();
		if ( "/test/" != app.getIconBaseUrl() ) {
			app.setIconBaseUrl("/test/");
			WidgetManager.get().getServerCommunicator().setApplication(app.getPlaceId(), app.getApplicationId(), app, null);
		} 
		
		WidgetManager.get().setAutomaticInputRequests(true);
		
		
		GuiButton like1 = new GuiButton("btn1", "Like");
		like1.setLongDescription("Video of Everdith Landrau at TEDxFranklinStreet");
		
		GuiButton like2 = new GuiButton("btn2", "Like");
		like1.setLongDescription("Video of Sherry Turkle: Connected, but alone?");
		
		GuiTextBox tb1 = new GuiTextBox("txt1", "Send text");
		tb1.setLongDescription("Contribute some tags to the tag cloud.");
		
		ArrayList<String> l = new ArrayList<String>();
		l.add("I don't go");
		l.add("Once a week");
		l.add("Twice a week");
		GuiListBox lb1 = new GuiListBox("poll-1", "On average, how many times to you go to the movies?", l);
		lb1.setShortDescription("Vote");
		lb1.setLongDescription("On average, how many times to you go to the movies?");
		RootPanel.get("content").add(lb1);

		l = new ArrayList<String>();
		l.add("One");
		l.add("Two");
		l.add("Three");
		GuiListBox lb2 = new GuiListBox("poll-2", "What is your favourite number?", l);
		lb2.setShortDescription("Vote");
		lb2.setLongDescription("What is your favourite number?");
		RootPanel.get("content").add(lb2);
		
		GuiUpload guiUpload = new GuiUpload("uploadsomething", "Upload");
		guiUpload.addActionListener(this);
		
		GuiDownloadButton download = new GuiDownloadButton("download", "Download", "http://teste");
		download.setLongDescription("Link to video Sherry Turkle: Connected, but alone?");
	
	}

	@Override
	public void onAction(ActionEvent<?> e) {
		Log.error(this, "On Action");
		Image img = new Image((String)e.getParam());
		RootPanel.get().add(img);
		
	}
	
	
	
}
