package org.jorgecardoso.purewidgets.demo.showcase.client;




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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Showcase implements PublicDisplayApplicationLoadedListener, EntryPoint, ActionListener{

	
	@Override
	public void onModuleLoad() {
		
		PublicDisplayApplication.load(this, "WidgetShowcase", false);
		
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			//Admin.run();
			return;
		}
		Application app = PublicDisplayApplication.getApplication();
		if ( "/showcase/" != app.getIconBaseUrl() ) {
			app.setIconBaseUrl("/showcase/");
			WidgetManager.get().getServerCommunicator().setApplication(app.getPlaceId(), app.getApplicationId(), app, null);
		} 
		
		WidgetManager.get().setAutomaticInputRequests(true);
		
		TabPanel tabPanel = new TabPanel();
		
		tabPanel.addStyleName("main");
		RootPanel.get().add(tabPanel);
		
		
		/* Button */
		FlowPanel buttonPanel = new FlowPanel();
		tabPanel.add(buttonPanel, "Button");
		setPanelStyle(buttonPanel);
		
		GuiButton like1 = new GuiButton("btn1", "Like");
		like1.setLongDescription("Video of Everdith Landrau at TEDxFranklinStreet");
		buttonPanel.add(like1);
		
		//GuiButton like2 = new GuiButton("btn2", "Like");
		//like1.setLongDescription("Video of Sherry Turkle: Connected, but alone?");
		
		/* Textbox */
		FlowPanel textboxPanel = new FlowPanel();
		tabPanel.add(textboxPanel, "TextBox");
		this.setPanelStyle(textboxPanel);
		
		GuiTextBox tb1 = new GuiTextBox("txt1", "Send text");
		tb1.setWidth("400px");
		tb1.setLongDescription("Contribute some tags to the tag cloud.");
		textboxPanel.add(tb1);
		
		
		
		/* listbox */
		FlowPanel listboxPanel = new FlowPanel();
		tabPanel.add(listboxPanel, "ListBox");
		this.setPanelStyle(listboxPanel);
		
		ArrayList<String> l = new ArrayList<String>();
		l.add("I don't go");
		l.add("Once a week");
		l.add("Twice a week");
		GuiListBox lb1 = new GuiListBox("poll-1", "On average, how many times to you go to the movies?", l);
		lb1.setShortDescription("Vote");
		lb1.setLongDescription("On average, how many times to you go to the movies?");
		listboxPanel.add(lb1);

		/* upload */
		FlowPanel uploadPanel = new FlowPanel();
		tabPanel.add(uploadPanel, "Upload");
		this.setPanelStyle(uploadPanel);
		
		GuiUpload guiUpload = new GuiUpload("uploadsomething", "Upload");
		guiUpload.addActionListener(this);
		uploadPanel.add(guiUpload);
		
//		
//		GuiDownloadButton download = new GuiDownloadButton("download", "Download", "http://teste");
//		download.setLongDescription("Link to video Sherry Turkle: Connected, but alone?");
//	
	}

	/**
	 * @param buttonPanel
	 */
	private void setPanelStyle(Panel buttonPanel) {
		buttonPanel.setStyleName("centred");
		buttonPanel.getElement().getStyle().clearHeight();
		buttonPanel.getElement().getStyle().clearWidth();
	}

	@Override
	public void onAction(ActionEvent<?> e) {
		Log.error(this, "On Action");
		Image img = new Image((String)e.getParam());
		RootPanel.get().add(img);
		
	}
	
	
	
}
