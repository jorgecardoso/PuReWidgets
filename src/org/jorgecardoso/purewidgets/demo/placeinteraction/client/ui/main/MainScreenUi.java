package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.main;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.application.ApplicationListUi;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.login.LoginUi;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.place.PlaceListUi;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.widget.WidgetListUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenUi extends Composite    {

	@UiTemplate("MainScreenUiDesktop.ui.xml")
	interface MainScreenUiDesktopUiBinder extends UiBinder<Widget, MainScreenUi> {	}
	private static MainScreenUiDesktopUiBinder desktopUiBinder = GWT.create(MainScreenUiDesktopUiBinder.class);
	
	private UiType uiType;

	@UiField HTMLPanel features;
	
	@UiField LoginUi loginUi;
	
	private PlaceListUi placeListUi;
	
	private ApplicationListUi applicationListUi;
	
	private WidgetListUi widgetListUi;
	
	public MainScreenUi( UiType uiType ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
	}
	@UiFactory LoginUi makeLoginUi() { // method name is insignificant
	    return new LoginUi(this.uiType);
	}
	
	private UiBinder<Widget, MainScreenUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		default:
			return desktopUiBinder;
		}
	}

	public void showPlaceList() {
		Log.debug(this, "Showing places");
		if ( null != this.applicationListUi ) {
			this.applicationListUi.stop();
		}
		if ( null != this.widgetListUi ) {
			this.widgetListUi.stop();
		}
		if ( null == this.placeListUi ) {
			this.placeListUi = new PlaceListUi(this.uiType); // TODO: Check UiType
			this.placeListUi.addSelectionHandler(new SelectionHandler<String>() {
				@Override
				public void onSelection(SelectionEvent<String> event) {
					MainScreenUi.this.onPlaceSelected(event.getSelectedItem());
				}
			});
		}
		//RootPanel.get("features").clear();
		//RootPanel.get("features").add(this.placeListUi);
		this.placeListUi.start();
		this.features.clear();
		this.features.add(this.placeListUi);
	}
	
	

	
	/**
	 * Show the application list (and widgets) screen
	 */
	public void showApplicationList(final String placeId) {
		Log.debug(this, "Showing applications");
		if ( null != this.placeListUi ) {
			this.placeListUi.stop();
		} 
		if ( null != this.widgetListUi ) {
			this.widgetListUi.stop();
		}
		
		if ( null == this.applicationListUi || !this.applicationListUi.getPlaceId().equals(placeId) ) {
			this.applicationListUi = new ApplicationListUi(this.uiType, placeId); 
			this.applicationListUi.addSelectionHandler(new SelectionHandler<Application>() {
				@Override
				public void onSelection(SelectionEvent<Application> event) {
					MainScreenUi.this.onApplicationSelected(placeId, event.getSelectedItem());
				}
			});
		}
		
		this.applicationListUi.start();
		this.features.clear();
		this.features.add(this.applicationListUi);
		//RootPanel.get("features").clear();
		//RootPanel.get("features").add(this.applicationListUi);
	}

	/**
	 * Show the widget list for a given application
	 */
	public void showWidgets( String placeName, String applicationName) {
		Log.debug(this, "Showing widgets");
		if ( null != this.placeListUi ) {
			this.placeListUi.stop();
		} 
		if ( null != this.applicationListUi ) {
			this.applicationListUi.stop();
		}
		if ( null == this.widgetListUi || !this.widgetListUi.getPlaceName().equals(placeName) || !this.widgetListUi.getApplicationName().equals(applicationName) ) {
			this.widgetListUi = new WidgetListUi(this.uiType, placeName, applicationName); 
		}
		//RootPanel.get("features").clear();
		//RootPanel.get("features").add(this.widgetListUi);
		this.widgetListUi.start();
		this.features.clear();
		this.features.add(this.widgetListUi);
	}
	
	

	public void onPlaceSelected(String placeId) {
		Log.debug(this, "User selected place " + placeId);
		History.newItem(placeId);
	}
	
	public void onApplicationSelected(String placeName, Application application) {
		Log.debug(this, "User selected application" + application.getApplicationId());
		History.newItem(placeName+"-"+application.getApplicationId());
	}

	


	
	

}
