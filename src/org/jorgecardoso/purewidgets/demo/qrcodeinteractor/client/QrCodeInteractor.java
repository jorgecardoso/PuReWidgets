/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.qrcodeinteractor.client;

import java.util.ArrayList;
import java.util.Date;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.PlaceInteractionWebpage;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.SightingService;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.SightingServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
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
public class QrCodeInteractor implements EntryPoint {

	public static SightingServiceAsync sightingService;

	String placeId;
	String applicationId;
	String controlType;
	String referenceCode;

	@Override
	public void onModuleLoad() {
		/*
		 * Create the sightinh service to post input to instant places
		 */
		sightingService = GWT.create(SightingService.class);
		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");

		controlType = com.google.gwt.user.client.Window.Location.getParameter("type");
		referenceCode = com.google.gwt.user.client.Window.Location.getParameter("ref");

		if (controlType
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			doImperative(referenceCode);
		} else if (controlType
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY)) {
			doEntry(referenceCode);
		}
	}

	private void doEntry(final String referenceCode) {
		final TextBox textbox = new TextBox();
		Button button = new Button("Send");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Log.debug(this, referenceCode + " " + textbox.getText());
				doInput("tag." + referenceCode + ":" + textbox.getText());
				
			}
			
		});
		RootPanel.get().add(textbox);
		RootPanel.get().add(button);
		
	}

	void doImperative(String referenceCode) {
		doInput("tag."+referenceCode);
	}

	private void doInput(String input) {

		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d = new Date();

		sightingService.sighting("QrCode" + " " + input, dtf.format(d),
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.debug(this, "An error ocurred.");
						RootPanel.get().add(new Label("An error ocurred."));
					}

					@Override
					public void onSuccess(Void result) {
						Log.debug(this, "Sent.");
						RootPanel.get().add(new Label("Sent."));
					}

				});
	}
}
