package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.place;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class PlaceListUi extends Composite  implements ApplicationListListener, ClickHandler, HasSelectionHandlers<String> {
	
	@UiTemplate("PlaceListDesktop.ui.xml")
	interface PlaceListDesktopUiBinder extends UiBinder<Widget, PlaceListUi> {
	}
	private static PlaceListDesktopUiBinder desktopUiBinder = GWT.create(PlaceListDesktopUiBinder.class);
	
	
	@UiField
	Panel panel;
	
	/**
	 * The listener for onPlaceSelected events.
	 */
	//private PlaceListListener placeListener;
	
	/**
	 * The timer used to periodically ask for the list of places to the PuReWidgets interaction manager.
	 */
	private Timer timerPlaces;
	
	/**
	 * The type of ui we will generate
	 */
	private UiType uiType;
	
	
	
	public PlaceListUi( UiType uiType ) {
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.uiType = uiType;
	}
	
	private UiBinder<Widget, PlaceListUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}
	
	/**
	 * Stops asking for the place list.
	 */
	public void stop() {
		if ( null != this.timerPlaces ) {
			this.timerPlaces.cancel();
			this.timerPlaces = null;
		}
	}
	
	/**
	 * Starts asking for the place list and updating the ui when with the list of places.
	 */
	public void start() {
		WidgetManager.get().setApplicationListListener(this);
		timerPlaces = new Timer() {
			@Override
			public void run() {
				refreshPlaces();
			}
		};
		this.refreshPlaces();
		timerPlaces.scheduleRepeating(30 * 1000);
	}

	private void refreshPlaces() {
		Log.debug(this, "Asking server for list of places");
		WidgetManager.get().getPlacesList();
	}
	

	@Override
	public void onPlaceList(ArrayList<Place> placeList) {
		
		this.panel.clear();
		
		for (Place place : placeList) {
			PlaceUi p = new PlaceUi(this.uiType);
			p.setText(place.getPlaceId());
			p.getElement().setPropertyString("id", place.getPlaceId());
			
			p.addClickHandler(this);
			// panel.get
			this.panel.add(p);
			// l.getParent().setStyleName("gwt-StackPanelItem");
		}
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		PlaceUi p = (PlaceUi) event.getSource();
		Log.debug("PlaceUi clicked:" + p.getText());	
		SelectionEvent.fire(this, p.getElement().getPropertyString("id"));
		//this.placeListener.onPlaceSelected( p.getElement().getPropertyString("id") );
		//History.newItem("app");
	}
//	
//	/**
//	 * @return the placeListener
//	 */
//	public PlaceListListener getPlaceListener() {
//		return placeListener;
//	}
//
//
//	/**
//	 * @param placeListener the placeListener to set
//	 */
//	public void setPlaceListener(PlaceListListener placeListener) {
//		this.placeListener = placeListener;
//	}

	@Override
	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
	}

	@Override
	public void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return this.addHandler(handler, SelectionEvent.getType());
	}
}
