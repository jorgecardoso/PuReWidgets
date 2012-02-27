package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.application;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.Callback;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.place.PlaceUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationListUi extends Composite implements ClickHandler, HasSelectionHandlers<Application>  {
	/**
	 * The period for updating the list of applications from the server
	 */
	private static final int PERIOD_MILLIS = 2 * 60 * 1000; // every 2 minutes

	/*interface Style extends CssResource {
	    String list();
	    //String disabled();
	  }

	 */
	@UiTemplate("ApplicationListDesktop.ui.xml")
	interface ApplicationListDesktopUiBinder extends UiBinder<Widget, ApplicationListUi> {	}
	private static ApplicationListDesktopUiBinder desktopUiBinder = GWT.create(ApplicationListDesktopUiBinder.class);

	private UiType uiType;

	//@UiField Style style;
	
	private String placeId;
	
	private Timer timerApplications;
	
	
	@UiField
	HTMLPanel panel;
	
	@UiField 
	SpanElement placeName;

	public ApplicationListUi( UiType uiType, String placeId ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.placeId = placeId;
		this.placeName.setInnerText(placeId);
	}
	
	private UiBinder<Widget, ApplicationListUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}
	
	public void start() {
		timerApplications = new Timer() {
			@Override
			public void run() {
				refreshApplications();
			}
		};
		
		this.timerApplications.scheduleRepeating(PERIOD_MILLIS);
		this.refreshApplications();
	}
	

	protected void refreshApplications() {
		Log.debug(this, "Asking server for list of applications");
		final String placeId = this.placeId;
		PublicDisplayApplication.getServerCommunicator().getApplicationsList(this.placeId, new Callback<ArrayList<Application>> () {

			@Override
			public void onSuccess(ArrayList<Application> applicationList) {
				ApplicationListUi.this.onApplicationList(placeId, applicationList);
				
			}

			@Override
			public void onFailure(Throwable exception) {
				Log.warn(ApplicationListUi.this, "Could not get application list: " + exception.getMessage());
				
			}
			
		});
		
	}

	

	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		
		this.panel.clear();
		
		for (Application application : applicationList) {
			ApplicationUi appUi = new ApplicationUi(this.uiType, application);
			
			appUi.getElement().setPropertyString("id", application.getApplicationId());
			
			appUi.addClickHandler(this);
			// panel.get
			this.panel.add(appUi);
			// l.getParent().setStyleName("gwt-StackPanelItem");
		}
//		Log.debug(this, "Received applications: " + applicationList);
//
////		if (null == this.currentApplications) {
////			this.currentApplications = new ArrayList<Application>();
////		}
//
//		/*
//		 * Create a temporary list just with the app names
//		 */
//		ArrayList<String> applicationIds = new ArrayList<String>();
//		for (Application app : applicationList) {
//			applicationIds.add(app.getApplicationId());
//		}
//
//		
//		/*
//		 * Delete applications that no longer exist
//		 */
//		int i = 0;
//		// for ()
//		while (i <  this.stackPanelApplications.getWidgetCount()) {
//			String tabName = this.stackPanelApplications.getWidget(i).getElement().getPropertyString("id"); // this.currentApplications.get(i).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(i);
//			if (!applicationIds.contains(tabName)) {
//				stackPanelApplications.remove(i);
//				
//			} else {
//				// only increment if we haven't deleted anything because if we
//				// did, indexed may have changed
//				i++;
//			}
//		}
//
//		/*
//		 * Add new applications
//		 */
//		for (int j = 0; j < applicationList.size(); j++) {
//			String appName = applicationList.get(j).getApplicationId();
//
//			boolean exists = false;
//			for (i = 0; i < this.stackPanelApplications.getWidgetCount(); i++) {
//				String tabName = this.stackPanelApplications.getWidget(i).getElement().getPropertyString("id");//this.currentApplications.get(i).getApplicationId();
//				if ( tabName.equals(appName) ) {
//					exists = true;
//					break;
//				}
//			}
//
//			if ( !exists ) {
//				VerticalPanel p = new VerticalPanel();
//				p.getElement().setPropertyString("id", appName);
//				
//				Image img = new Image("placeinteractiondemo/ajax-loader.gif");
//				p.add(img);
//				stackPanelApplications.insert(p, j);
//				stackPanelApplications.setStackText(j, appName);
//			}
//		}
//
//		//this.currentApplications = applicationList;
//
//		/*
//		 * Set the selected tab
//		 */
//		int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
//		if (selected < 0) {
//			this.stackPanelApplications.showStack(0); // .getTabBar().selectTab(0);
//		}

		
	}

	
	public void stop() {
		if ( null != this.timerApplications ) {
			this.timerApplications.cancel();
			this.timerApplications = null;
		}
	}
	
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Application> handler) {
		return this.addHandler(handler, SelectionEvent.getType());
	}

	@Override
	public void onClick(ClickEvent event) {
		ApplicationUi p = (ApplicationUi) event.getSource();
		Log.debug("ApplicationUi clicked:" + p.getApplication().getApplicationId());	
		SelectionEvent.fire(this, p.getApplication());//p.getElement().getPropertyString("id"));
	}

	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}
}
