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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceInteractionWebpage implements EntryPoint, ApplicationListListener, ClickHandler {
	private Timer timerPlaces;
	private Timer timerApplications;
	private Timer timerWidgets;

	public static String userIdentity = "Anonymous1984";

	public static SightingServiceAsync sightingService;

	private ArrayList<Place> places;

	ArrayList<Application> currentApplications;

	// HashMap<String,
	// ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>>
	// currentWidgetsMap;

	String currentPlaceId = "DefaultPlace";
	String currentApplicationId;

	StackPanel stackPanelApplications;

	public static LoginInfo loginInfo = null;
	private Anchor signInLink = new Anchor("Sign In");
	private Label labelId;

	@Override
	public void onModuleLoad() {
		/*
		 * Check/set the user's identity.
		 * 
		 * We generate a random identity name, starting with "Anonynous" and set
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
		WidgetManager.get().setApplicationListListener(this);
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
					labelId.setText("Your identity is: " + userIdentity);
					signInLink.setText("Sign out");
					signInLink.setHref(loginInfo.getLogoutUrl());
				} else {
					signInLink.setText("Sign in");
					signInLink.setHref(loginInfo.getLoginUrl());

				}
			}
		});
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
		if (historyToken.equals("app")) {
			this.showApps();

		} else if (historyToken.equals("place")) {
			this.showPlaces();
		}
	}

	/**
	 *  Show the place list screen
	 */
	private void showPlaces() {
		if (null != this.timerApplications) {
			this.timerApplications.cancel();
		}
		if (null != this.timerWidgets) {
			this.timerWidgets.cancel();
		}

		timerPlaces = new Timer() {
			@Override
			public void run() {
				refreshPlaces();
			}
		};

		timerPlaces.scheduleRepeating(30 * 1000);
		this.refreshPlaces();
		RootPanel.get("title").clear();
		RootPanel.get("title").add(new Label("Available places:"));

	}

	/**
	 * Show the application list (and widgets) screen
	 */
	private void showApps() {
		Log.debug("Currrent place: " + this.currentPlaceId);
		if (null != this.timerPlaces) {
			timerPlaces.cancel();
			timerPlaces = null;
		}

		if (null == this.currentPlaceId) {
			// Log.debug("A")
			History.newItem("place");
		}

		timerApplications = new Timer() {
			@Override
			public void run() {
				refreshApplications();
			}
		};

		timerWidgets = new Timer() {
			@Override
			public void run() {
				refreshWidgets();
			}
		};

		this.currentApplications = new ArrayList<Application>();

		this.timerApplications.scheduleRepeating(60 * 1000);
		
		this.refreshApplications();
		
		RootPanel.get("title").clear();
		RootPanel.get("title").add(
				new Label("Interact with applications in " + this.currentPlaceId + " place:"));

		RootPanel.get("features").clear();
		stackPanelApplications = new StackPanel();
		RootPanel.get("features").add(stackPanelApplications);
	}

	private void refreshPlaces() {
		Log.debug(this, "Asking server for list of places");
		WidgetManager.get().getPlacesList();

	}

	protected void refreshApplications() {
		Log.debug(this, "Asking server for list of applications");
		WidgetManager.get().getApplicationsList(currentPlaceId);
	}

	protected void refreshWidgets() {
		if (null != this.stackPanelApplications) {
			int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
			Log.debug(this, "Selected Tab: " + selected);
			if (selected >= 0) {
				currentApplicationId = this.currentApplications.get(selected).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(selected);
				Log.debug(this, "Asking server for list of widgets for application: "
						+ currentApplicationId);

				WidgetManager.get().getWidgetsList(this.currentPlaceId, currentApplicationId);
			}
		}

	}

	@Override
	public void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());

		if (null != widgetList) {

			ArrayList<String> widgetIds = new ArrayList<String>();
			for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
				widgetIds.add(widget.getWidgetId());
			}
			// String applicationId = this.currentApplicationId;
			// //widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);

			// //ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>
			// currentWidgets = this.currentWidgetsMap.get(applicationId);
			// if ( null == currentWidgets ) {
			// //currentWidgets = new
			// ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>();
			// }
			//
			/*
			 * Get the tab that holds this application
			 */
			VerticalPanel panel = null;
			for (int i = 0; i < this.stackPanelApplications.getWidgetCount(); i++) {
				String tabName = this.currentApplications.get(i).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(i);
				if (tabName.equals(applicationId)) {
					panel = (VerticalPanel) stackPanelApplications.getWidget(i);
					break;
				}
			}

			if (null != panel) {
				Log.debug(this, panel.toString());

				/*
				 * Delete widgets that no longer exist
				 */
				int i = 0;
				while (i < panel.getWidgetCount()) {
					String widgetName = panel.getWidget(i).getElement().getPropertyString("id");

					if (!widgetIds.contains(widgetName)) {
						panel.remove(i);
						// currentWidgets.remove(i);
					} else {
						// only increment if we haven't deleted anything because
						// if we did, indexed may have changed
						i++;
					}
				}

				/*
				 * Add the new widgets
				 */

				for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
					boolean exists = false;
					for (i = 0; i < panel.getWidgetCount(); i++) {
						String existingWidgetName = panel.getWidget(i).getElement()
								.getPropertyString("id");
						if (existingWidgetName.equals(widget.getWidgetId())) {
							exists = true;
						}
					}

					if (!exists) {
						Log.debug(this, "Adding " + widget.getWidgetId() + " to panel");
						panel.add(this.getHtmlWidget(widget));
					}
				}
			}
			// this.currentWidgetsMap.put(applicationId, widgetList);
		}

		// tabPanelApplications.getWidget(index)

	}

	@Override
	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		Log.debug(this, "Received applications: " + applicationList);
		if (null != timerWidgets) {
			timerWidgets.cancel();
		}

		if (null == this.currentApplications) {
			this.currentApplications = new ArrayList<Application>();
		}

		/*
		 * Create a temporary list just with the app names
		 */
		ArrayList<String> applicationIds = new ArrayList<String>();
		for (Application app : applicationList) {
			applicationIds.add(app.getApplicationId());
		}

		if (null == stackPanelApplications) {
			stackPanelApplications = new StackPanel();
			RootPanel.get("features").add(stackPanelApplications);
		}

		/*
		 * Delete applications that no longer exist
		 */
		int i = 0;
		// for ()
		while (i < this.currentApplications.size()) {
			String tabName = this.currentApplications.get(i).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(i);
			if (!applicationIds.contains(tabName)) {
				stackPanelApplications.remove(i);
				this.currentApplications.remove(i);
			} else {
				// only increment if we haven't deleted anything because if we
				// did, indexed may have changed
				i++;
			}
		}

		/*
		 * Add new applications
		 */
		for (int j = 0; j < applicationList.size(); j++) {
			String appName = applicationList.get(j).getApplicationId();

			boolean exists = false;
			for (i = 0; i < this.currentApplications.size(); i++) {
				String tabName = this.currentApplications.get(i).getApplicationId();
				if (tabName.equals(appName)) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				VerticalPanel p = new VerticalPanel();

				stackPanelApplications.insert(p, j);
				stackPanelApplications.setStackText(j, appName);
			}
		}

		this.currentApplications = applicationList;

		/*
		 * Set the selected tab
		 */
		int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
		if (selected < 0) {
			this.stackPanelApplications.showStack(0); // .getTabBar().selectTab(0);
		}

		timerWidgets.scheduleRepeating(15 * 1000);
		this.refreshWidgets();
	}

	Widget getHtmlWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {

		if (publicDisplayWidget.getControlType().equals(
				org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY)) {
			return getEntryWidget(publicDisplayWidget);
		} else if (publicDisplayWidget
				.getControlType()
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			return getImperativeWidget(publicDisplayWidget);
		} else if (publicDisplayWidget.getControlType().equals(
				org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_DOWNLOAD)) {
			return getDownloadWidget(publicDisplayWidget);
		}
		return null;
	}

	Widget getEntryWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {

		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		// for ( WidgetOption wo : widgetOptions ) {
		Label label = new Label(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]");
		flowPanel.add(label);
		TextBox textBox = new TextBox();
		flowPanel.add(textBox);
		Button btn = new Button("Submit");
		flowPanel.add(btn);
		btn.addClickHandler(new EntryClickHandler(option.getReferenceCode(), textBox));
		// }

		flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	Widget getImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();

		if (null != widgetOptions) {
			if (widgetOptions.size() == 1) {
				return getSingleOptionImperativeWidget(publicDisplayWidget);
			} else {
				return getMultipleOptionImperativeWidget(publicDisplayWidget);
			}
		} else {
			return null;
		}
	}

	Widget getMultipleOptionImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();

		FlowPanel flowPanel = new FlowPanel();
		for (WidgetOption wo : widgetOptions) {
			Label label = new Label(publicDisplayWidget.getShortDescription() + ": "
					+ wo.getReferenceCode());
			flowPanel.add(label);

			Button btn = new Button("send");
			flowPanel.add(btn);
			btn.addClickHandler(new ImperativeClickHandler(wo.getReferenceCode()));
		}
		flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	Widget getSingleOptionImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		Label label = new Label(publicDisplayWidget.getLongDescription());
		flowPanel.add(label);

		Button btn = new Button(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]");
		flowPanel.add(btn);
		btn.addClickHandler(new ImperativeClickHandler(option.getReferenceCode()));
		flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	Widget getDownloadWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		Anchor a = new Anchor(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]", publicDisplayWidget.getContentUrl());

		flowPanel.add(a);

		flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	@Override
	public void onPlaceList(ArrayList<Place> placeList) {
		if (null == this.timerPlaces) { // the timer was canceled, so the user
										// must be in the app list
			return;
		}
		this.places = placeList;

		VerticalPanel panel = new VerticalPanel();

		for (Place place : placeList) {
			Button b = new Button(place.getPlaceId());
			// l.setStyleName("gwt-StackPanelItem");

			b.addClickHandler(this);
			// panel.get
			panel.add(b);
			// l.getParent().setStyleName("gwt-StackPanelItem");
		}

		RootPanel.get("features").clear();
		RootPanel.get("features").add(panel);
		// this.timerApplications.scheduleRepeating(60*1000);
		// this.refreshApplications();
	}

	@Override
	public void onClick(ClickEvent event) {

		Button b = (Button) event.getSource();
		Log.debug("Button clicked:" + b.getText());
		this.currentPlaceId = b.getText();

		History.newItem("app");

		// this.timerApplications.scheduleRepeating(60*1000);
		// this.refreshApplications();
	}

}
