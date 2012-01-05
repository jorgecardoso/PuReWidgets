package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlaceList extends Composite  implements ApplicationListListener, ClickHandler {

	private static PlaceListUiBinder uiBinder = GWT.create(PlaceListUiBinder.class);

	private PlaceListListener placeListener;
	
	private Timer timerPlaces;
	
	
	interface PlaceListUiBinder extends UiBinder<Widget, PlaceList> {
	}

	public PlaceList() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	VerticalPanel verticalPanel;

	public PlaceList(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}

	public void stop() {
		if ( null != this.timerPlaces ) {
			this.timerPlaces.cancel();
			this.timerPlaces = null;
		}
	}
	
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
		//RootPanel.get("title").clear();
		//RootPanel.get("title").add(new Label("Available places:"));

	}

	private void refreshPlaces() {
		Log.debug(this, "Asking server for list of places");
		WidgetManager.get().getPlacesList();
	}
	
	

	@Override
	public void onPlaceList(ArrayList<Place> placeList) {
		
		this.verticalPanel.clear();
		
		for (Place place : placeList) {
			Button b = new Button(place.getPlaceId());
			// l.setStyleName("gwt-StackPanelItem");
			b.getElement().setPropertyString("id", place.getPlaceId());
			b.setStyleName("placebutton");
			b.addClickHandler(this);
			// panel.get
			this.verticalPanel.add(b);
			// l.getParent().setStyleName("gwt-StackPanelItem");
		}
	}
	
	@Override
	public void onClick(ClickEvent event) {
			
		Button b = (Button) event.getSource();
		Log.debug("Button clicked:" + b.getText());
		
		this.placeListener.onPlaceSelected( b.getElement().getPropertyString("id") );
		//History.newItem("app");
	}
	
	/**
	 * @return the placeListener
	 */
	public PlaceListListener getPlaceListener() {
		return placeListener;
	}


	/**
	 * @param placeListener the placeListener to set
	 */
	public void setPlaceListener(PlaceListListener placeListener) {
		this.placeListener = placeListener;
	}

	@Override
	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		// TODO Auto-generated method stub
		
	}

	
	

}
