package org.jorgecardoso.purewidgets.demo.test.client;




import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Admin {
	
	static String options[] = {"allowusertags", "placetags"};
	
	static ArrayList<TextBox> values;
	
	static RemoteStorage rs;

	public static void  run() {
		rs = new RemoteStorage("DefaultPlace", "Test");
		
		values = new ArrayList<TextBox>();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		
		
		for ( String option: options ) {
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			Label l = new Label(option);
			TextBox textbox = new TextBox();
			values.add(textbox);
			
			horizontalPanel.add(l);
			horizontalPanel.add(textbox);
			
			verticalPanel.add(horizontalPanel);
		}
		RootPanel.get().add(verticalPanel);
		
		Button save = new Button("Save");
		
		RootPanel.get().add(save);
		
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveOptions();
				
			}
			
		});
		
		loadOptions();
		
	}
	
	public static void loadOptions() {
		
		for (int i = 0; i < options.length;  i++) {
			rs.getString(options[i],  new AsyncCallback<String[]>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.debug("Failed");
					Log.debug(caught.getMessage());
					RootPanel.get().add(new Label("Failed"));
					
				}

				@Override
				public void onSuccess(String[] result) {
					
					
					for (int j = 0; j < options.length; j++) {
						if ( options[j].equals(result[0]) ) {
							TextBox t = values.get(j);
							t.setText(result[1]);
									
						}
					}
					Log.debug("ok:" + result[1]);
					//RootPanel.get().add(new Label("ok: " + result));
				}
				
			});			
			
		}		
	}
	
	
	public static void saveOptions() {
		
		
		for (int i = 0; i < options.length;  i++) {
			String text = values.get(i).getText();
			rs.setString(options[i], text, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.debug("Failed");
					Log.debug(caught.getMessage());
					
					
				}

				@Override
				public void onSuccess(Void result) {
					Log.debug("ok");
					
				}
				
			});
		}
		
		/*
		
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
		*/
	
	}
}
