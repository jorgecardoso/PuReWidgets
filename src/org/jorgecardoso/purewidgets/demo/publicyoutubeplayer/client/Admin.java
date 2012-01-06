package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.shared.Log;
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
	
	static String options[] = {PublicYoutubePlayer.URL_PARAMETER_MAX_VIDEO_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_ACTIVITY_SCREEN_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_TOPLAYNEXT_CONFIRMATION_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION,
								PublicYoutubePlayer.URL_PARAMETER_ALLOW_USER_TAGS,
								PublicYoutubePlayer.URL_PARAMETER_PLACE_TAGS};
	
	static String descriptions[] = {"(seconds) The maximum video duration for played videos",
									"(seconds) How much time to stay at the activity screen",
									"(seconds) unused",
									"(seconds) How much time to stay at the results screen",
									"(true/false) Allow users to suggest tags?",
									"(comma separated list) base place tags that are always present in the tag cloud"
		
	};
	
	static ArrayList<TextBox> values;
	
	static RemoteStorage rs;

	public static void  run() {
		rs = PublicDisplayApplication.getRemoteStorage();
		
		values = new ArrayList<TextBox>();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		
		
		for (int i = 0; i < options.length; i++) {
			String option = options[i];
		
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			Label l = new Label(option);
			TextBox textbox = new TextBox();
			values.add(textbox);
			
			horizontalPanel.add(l);
			horizontalPanel.add(textbox);
			horizontalPanel.add(new Label(descriptions[i]));
			
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
					if ( null == result ) return;
					
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
