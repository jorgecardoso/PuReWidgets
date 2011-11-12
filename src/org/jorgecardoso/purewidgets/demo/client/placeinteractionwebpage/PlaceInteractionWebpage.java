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
	private Timer timerApplications;
	private Timer timerWidgets;
	
	
	
	
	public static SightingServiceAsync sightingService;
	
	ArrayList<Application> applications;
	
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
		
		timerApplications = new Timer() {
			@Override
			public void run() {
				refreshApplications();
			}
		};
		
		timerApplications.scheduleRepeating(60*1000);
		this.refreshApplications();
		
		timerWidgets = new Timer() {
			@Override
			public void run() {
				refreshWidgets();
			}
		};
		
		timerWidgets.scheduleRepeating(15*1000);
		
		
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
	
	
	protected void refreshApplications() {
		Log.debug(this, "Asking server for list of applications");
		WidgetManager.get().getPlaceApplicationsList();	
	}

	protected void refreshWidgets() {
		if ( null != this.tabPanelApplications ) {
			int selected = tabPanelApplications.getTabBar().getSelectedTab();
			Log.debug(this, "Selected Tab: " + selected);
			String currentApplicationId = tabPanelApplications.getTabBar().getTabHTML(selected);
			Log.debug(this, "Asking server for list of widgets for application: " + currentApplicationId );
			
			
				WidgetManager.get().getApplicationWidgetsList("DefaultPlace", currentApplicationId);

		}

		
	}
	

	@Override
	public void onWidgetsList(
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());
		
		if ( null != widgetList && widgetList.size() > 0 ) {
			
			ArrayList<String> widgetIds = new ArrayList<String>();
			for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
				widgetIds.add(widget.getWidgetId());
			}
			String applicationId = widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);
			
			/* 
			 * Get the tab that holds this application
			 */
			VerticalPanel panel = null;
			for (int i = 0; i < tabPanelApplications.getWidgetCount(); i++ ) {
				String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
				if ( tabName.equals(applicationId) ) {
					panel = (VerticalPanel) tabPanelApplications.getWidget(i);
					break;
				}
			}
			
			if ( null != panel) {
				Log.debug(this, panel.toString());
				
				/*
				 * Delete widgets that no longer exist
				 */
				int i = 0;
				while ( i < panel.getWidgetCount() ) {
					String widgetName = panel.getWidget(0).getTitle();
					
					if ( !widgetIds.contains(widgetName) ) {
						panel.remove(i);
					} else {
						// only increment if we haven't deleted anything because if we did, indexed may have changed
						i++;
					}
				}
				
				/*
				 * Add the new widgets
				 */
				
				for ( org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList ) {
					boolean exists = false;
					for (i = 0; i < panel.getWidgetCount(); i++ ) {
						String existingWidgetName = panel.getWidget(i).getTitle();
						if ( existingWidgetName.equals(widget.getWidgetId()) ) {
							exists = true;
						}
					}
					
					if ( !exists ) {
						Log.debug(this,"Adding " + widget.getWidgetId() + " to panel");
						panel.add(this.getHtmlWidget(widget));
					}
				}
			}
		}
		
		//tabPanelApplications.getWidget(index)
		
		
	}
	
	
	@Override
	public void onApplicationList(ArrayList<Application> applicationList) {
		Log.debug(this, "Received applications: " + applicationList);
		if ( null != timerWidgets ) {
			timerWidgets.cancel();
		}
		this.applications = applicationList;
		
		/*
		 * Create a temporary list just with the app names
		 */
		ArrayList<String> applicationIds = new ArrayList<String>();
		for ( Application app: applicationList ) {
			applicationIds.add( app.getApplicationId() );
		}

		
		if ( null == tabPanelApplications ) {
			tabPanelApplications = new TabPanel();
			RootPanel.get("features").add(tabPanelApplications);
		}
		
		/*
		 * Delete applications that no longer exist  
		 */
		int i = 0;
		while ( i < tabPanelApplications.getWidgetCount() ) {
			String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
			if ( !applicationIds.contains(tabName) ) {
				tabPanelApplications.remove(i);
			} else {
				// only increment if we haven't deleted anything because if we did, indexed may have changed
				i++;
			}
		}
	
		/*
		 * Add new applications
		 */
		for ( String appName : applicationIds ) {
			boolean exists = false;
			for (i = 0; i < tabPanelApplications.getWidgetCount(); i++ ) {
				String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
				if ( tabName.equals(appName) ) {
					exists = true;
				}
			}
			
			if ( !exists ) {
				VerticalPanel p = new VerticalPanel();
				
				tabPanelApplications.add( p, appName );
			}
		}
		
		/*
		 * Set the selected tab
		 */
		int selected = tabPanelApplications.getTabBar().getSelectedTab();
		if ( selected < 0 ) {
			this.tabPanelApplications.getTabBar().selectTab(0);
		}

		timerWidgets.scheduleRepeating(15*1000);
		this.refreshWidgets();
	}
	
	
	Widget getHtmlWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		
		if ( publicDisplayWidget.getControlType().equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY) ) {
			return getEntryWidget(publicDisplayWidget);
		} else if (  publicDisplayWidget.getControlType().equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION) ) {
			return getImperativeWidget(publicDisplayWidget);
		} else if ( publicDisplayWidget.getControlType().equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_DOWNLOAD) ) {
			return getDownloadWidget(publicDisplayWidget);
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
	
	Widget getDownloadWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		WidgetOption widgetOption = publicDisplayWidget.getWidgetOptions().get(0);
		
		FlowPanel flowPanel = new FlowPanel();
	
			Label label = new Label(publicDisplayWidget.getShortDescription() + ": " + publicDisplayWidget.getContentUrl());
			flowPanel.add(label);
			
			Button btn = new Button("send");
			flowPanel.add(btn);
			btn.addClickHandler(new ImperativeClickHandler(widgetOption.getReferenceCode()));
		
		return flowPanel;
	}


}
