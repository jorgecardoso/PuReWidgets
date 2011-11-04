/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Application;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class PlaceInteractionWebpage implements EntryPoint, ApplicationListListener {
	Timer timer;
	public static SightingServiceAsync sightingService;
	
	ArrayList<Application> apps;
	
	TabPanel tabPanelApplications;
	public static LoginInfo loginInfo = null;
	private Anchor signInLink = new Anchor("Sign In");
	private Label labelId;
	@Override
	public void onModuleLoad() {
		sightingService = GWT.create(SightingService.class);
		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");
		
		PublicDisplayApplication.load(this, "PlaceInteractionWebpage", true);
		
		WidgetManager.get().setApplicationListListener(this);
		WidgetManager.get().setAutomaticInputRequests(false);
		
		timer = new Timer() {
			@Override
			public void run() {
				refresh();
			}
		};
		
		timer.scheduleRepeating(15*1000);
		
		labelId = new Label();
		RootPanel.get().add(labelId);
	    RootPanel.get().add(signInLink);
	    
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
	        if(loginInfo.isLoggedIn()) {
	          Log.debug(this, loginInfo.getEmailAddress());
	          labelId.setText(loginInfo.getEmailAddress());
	          signInLink.setText("Sign out");
	          signInLink.setHref(loginInfo.getLogoutUrl());
	        } else {
	        	signInLink.setText("Sign in");
	        	signInLink.setHref(loginInfo.getLoginUrl());
	        
	        }
	      }
	    });
	}
	
	
	protected void refresh() {
		Log.debug(this, "Asking server for list of applications");
		WidgetManager.get().getPlaceApplicationsList();	
	}


	@Override
	public void onApplicationList(ArrayList<Application> applicationList) {
		/*
		 * Brute force... 
		 * TODO: Improve efficiency by checking if the widget is already there and
		 * if any widget needs to be deleted or updated.
		 */
		RootPanel.get("features").clear();
		int selectedTab = 0;
		if ( tabPanelApplications != null ) {
			selectedTab = tabPanelApplications.getTabBar().getSelectedTab();
		}
		tabPanelApplications = new TabPanel();
		
		RootPanel.get("features").add(tabPanelApplications);
		
		for ( Application app : applicationList ) {
			VerticalPanel p = new VerticalPanel();
			
			tabPanelApplications.add(p, app.getApplicationId());
//			for ( org.instantplaces.purewidgets.shared.widgets.Widget w : app.getWidgets() ) {
//				p.add(getHtmlWidget(w));
//			}
		}
		tabPanelApplications.selectTab(selectedTab);
	}
	
	Widget getHtmlWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		
		if ( publicDisplayWidget.getControlType().equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY) ) {
			return getEntryWidget(publicDisplayWidget);
		} else if (  publicDisplayWidget.getControlType().equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION) ) {
			return getImperativeWidget(publicDisplayWidget);
		}
		return null;
	}

	Widget getEntryWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		FlowPanel flowPanel = new FlowPanel();
		for ( WidgetOption wo : widgetOptions ) {
			Label label = new Label(publicDisplayWidget.getShortDescription() + ": " + wo.getReferenceCode());
			flowPanel.add(label);
			TextBox textBox = new TextBox(); 
			flowPanel.add(textBox);
			Button btn = new Button("send");
			flowPanel.add(btn);
			btn.addClickHandler(new EntryClickHandler(wo.getReferenceCode(), textBox));
		}
		return flowPanel;
	}

	Widget getImperativeWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		FlowPanel flowPanel = new FlowPanel();
		for ( WidgetOption wo : widgetOptions ) {
			Label label = new Label(publicDisplayWidget.getShortDescription() + ": " + wo.getReferenceCode());
			flowPanel.add(label);
			
			Button btn = new Button("send");
			flowPanel.add(btn);
			btn.addClickHandler(new ImperativeClickHandler(wo.getReferenceCode()));
		}
		return flowPanel;
	}
}
