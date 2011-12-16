/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.ui.ApplicationList;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.ui.PlaceList;
import org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage.ui.PlaceListListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceInteractionWebpage implements EntryPoint, PlaceListListener {
	

	public static String userIdentity = "Anonymous1984";

	public static SightingServiceAsync sightingService;

	
	public static LoginInfo loginInfo = null;
	private Anchor signInLink = new Anchor("Sign In");
	private Label labelId;
	
	
	private PlaceList placeListUi;
	private ApplicationList applicationListUi;

	@Override
	public void onModuleLoad() {
		
		//RootPanel.get().add(new Label(com.google.gwt.user.client.Window.Navigator.getUserAgent()));
		//MyResources.INSTANCE.css().ensureInjected();
		
		/*
		 * Check/set the user's identity.
		 * 
		 * We generate a random identity name, starting with "Anonymous" and set
		 * a cookie with it so that we can retrieve it later if the user uses
		 * the webpage again.
		 */
		userIdentity = Cookies.getCookie("userIdentity");
		if (null == userIdentity) {
			userIdentity = "Anonymous" + ((int) (Math.random() * 10000));
			/*
			 * The cookie is valid for one week
			 */
			Date future = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // 7
																							// days
																							// in
																							// the
																							// future
			Cookies.setCookie("userIdentity", userIdentity, future);
		}
		
		/*
		 * Put the user id at the bottom of the page, and a link so that the
		 * user can sign in
		 */
		labelId = new Label("Your identity is " + userIdentity);
		RootPanel.get("user").add(labelId);
		RootPanel.get("user").add(signInLink);

		/*
		 * Create the sightinh service to post input to instant places
		 */
		sightingService = GWT.create(SightingService.class);
		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");

		PublicDisplayApplication.load(this, "PlaceInteractionWebpage", false);
		
		WidgetManager.get().setAutomaticInputRequests(false);

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				show(historyToken);
			}
		});

		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				Log.debug(this, "error; " + error.getMessage());
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				Log.debug(this, loginInfo.getLoginUrl());
				if (loginInfo.isLoggedIn()) {
					Log.debug(this, loginInfo.getEmailAddress());
					userIdentity = loginInfo.getEmailAddress();
					//todo: remove this
					int at = userIdentity.indexOf('@');
					String toReplace = userIdentity.substring(at-5, at);
					userIdentity = userIdentity.replaceFirst(toReplace, "...");
					labelId.setText("Your identity is: " + userIdentity);
					signInLink.setText("Sign out");
					signInLink.setHref(loginInfo.getLogoutUrl());
				} else {
					signInLink.setText("Sign in");
					signInLink.setHref(loginInfo.getLoginUrl());

				}
			}
		});
		
		this.placeListUi = new PlaceList();
		this.placeListUi.setPlaceListener(this);
		
		this.show(History.getToken());
		
	}

	/**
	 * Show a different screen based on the history token
	 * @param historyToken
	 */
	protected void show(String historyToken) {
		if (null == historyToken || historyToken.length() == 0) {
			this.showPlaces();
		}
	  if ( historyToken.startsWith("place-") ) {
		 
			this.showApps(historyToken.substring(6) );
		}
	}

	/**
	 *  Show the place list screen
	 */
	private void showPlaces() {
		if ( null != this.applicationListUi ) {
			this.applicationListUi.stop();
		}
		RootPanel.get("features").clear();
		RootPanel.get("features").add(this.placeListUi);
		this.placeListUi.start();
	}

	/**
	 * Show the application list (and widgets) screen
	 */
	private void showApps(String placeId) {
		if ( null != this.placeListUi ) {
			this.placeListUi.stop();
		}
		applicationListUi = new ApplicationList(placeId);
		RootPanel.get("features").clear();
		RootPanel.get("features").add(this.applicationListUi);
		this.applicationListUi.start();
	}

	


	@Override
	public void onPlaceSelected(String placeId) {
		Log.debug(this, "User selected " + placeId);
		
		History.newItem("place-"+placeId);
	}


	

}
