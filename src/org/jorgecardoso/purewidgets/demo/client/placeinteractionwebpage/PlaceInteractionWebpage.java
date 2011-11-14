/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.google.gwt.user.client.ui.StackPanel;
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
	
	ArrayList<Application> currentApplications;
	
	HashMap<String, ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>> currentWidgetsMap;
	
	String currentApplicationId;
	
	StackPanel stackPanelApplications;
	
	
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
		
		
		this.currentWidgetsMap = new HashMap<String, ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>> ();
		
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
		if ( null != this.stackPanelApplications ) {
			int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
			Log.debug(this, "Selected Tab: " + selected);
			if ( selected >= 0) {
			    currentApplicationId = this.currentApplications.get(selected).getApplicationId(); //tabPanelApplications.getTabBar().getTabHTML(selected);
				Log.debug(this, "Asking server for list of widgets for application: " + currentApplicationId );
			
				WidgetManager.get().getApplicationWidgetsList("DefaultPlace", currentApplicationId);
			}
		}

		
	}
	

	@Override
	public void onWidgetsList(
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());
		
		if ( null != widgetList  ) {
			
			
			
			ArrayList<String> widgetIds = new ArrayList<String>();
			for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
				widgetIds.add(widget.getWidgetId());
			}
			String applicationId = this.currentApplicationId; //widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);
			
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> currentWidgets = this.currentWidgetsMap.get(applicationId);
			if ( null == currentWidgets ) {
				currentWidgets = new ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>();
			}
					
			/* 
			 * Get the tab that holds this application
			 */
			VerticalPanel panel = null;
			for (int i = 0; i < this.stackPanelApplications.getWidgetCount(); i++ ) {
				String tabName = this.currentApplications.get(i).getApplicationId(); //tabPanelApplications.getTabBar().getTabHTML(i);
				if ( tabName.equals(applicationId) ) {
					panel = (VerticalPanel) stackPanelApplications.getWidget(i);
					break;
				}
			}
			
			if ( null != panel) {
				Log.debug(this, panel.toString());
				
				/*
				 * Delete widgets that no longer exist
				 */
				int i = 0;
				while ( i < currentWidgets.size() ) {
					String widgetName = currentWidgets.get(i).getWidgetId(); //panel.getWidget(0).getTitle();
					
					if ( !widgetIds.contains(widgetName) ) {
						panel.remove(i);
						currentWidgets.remove(i);
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
					for (i = 0; i < currentWidgets.size(); i++ ) {
						String existingWidgetName = currentWidgets.get(i).getWidgetId();//panel.getWidget(i).getTitle();
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
			this.currentWidgetsMap.put(applicationId, widgetList);
		}
		
		//tabPanelApplications.getWidget(index)
		
		
	}
	
	
	@Override
	public void onApplicationList(ArrayList<Application> applicationList) {
		Log.debug(this, "Received applications: " + applicationList);
		if ( null != timerWidgets ) {
			timerWidgets.cancel();
		}
		
		if ( null == this.currentApplications ) {
			this.currentApplications = new ArrayList<Application>();
		}
		
		/*
		 * Create a temporary list just with the app names
		 */
		ArrayList<String> applicationIds = new ArrayList<String>();
		for ( Application app: applicationList ) {
			applicationIds.add( app.getApplicationId() );
		}

		
		if ( null == stackPanelApplications ) {
			stackPanelApplications = new StackPanel();
			RootPanel.get("features").add(stackPanelApplications);
		}
		
		/*
		 * Delete applications that no longer exist  
		 */
		int i = 0;
		//for ()
		while ( i < this.currentApplications.size() ) {
			String tabName = this.currentApplications.get(i).getApplicationId(); //tabPanelApplications.getTabBar().getTabHTML(i);
			if ( !applicationIds.contains(tabName) ) {
				stackPanelApplications.remove(i);
				this.currentApplications.remove(i);
			} else {
				// only increment if we haven't deleted anything because if we did, indexed may have changed
				i++;
			}
		}
	
		/*
		 * Add new applications
		 */
		for ( int j = 0; j < applicationList.size(); j++ ) {
			String appName = applicationList.get(j).getApplicationId();
		
			boolean exists = false;
			for (i = 0; i < this.currentApplications.size(); i++ ) {
				String tabName = this.currentApplications.get(i).getApplicationId();
				if ( tabName.equals(appName) ) {
					exists = true;
					break;
				}
			}
			
			if ( !exists ) {
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
		if ( selected < 0 ) {
			this.stackPanelApplications.showStack(0); //.getTabBar().selectTab(0);
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
		//WidgetOption widgetOption = publicDisplayWidget.getWidgetOptions().get(0);
		
		FlowPanel flowPanel = new FlowPanel();
			Anchor a = new Anchor(publicDisplayWidget.getShortDescription(), publicDisplayWidget.getContentUrl());
			
			flowPanel.add(a);
			
		
		return flowPanel;
	}


}
