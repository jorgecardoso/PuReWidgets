/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.qrcodeinteractor.client;

import java.util.ArrayList;
import java.util.Date;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.Callback;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;


/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class QrCodeInteractor implements EntryPoint, PublicDisplayApplicationLoadedListener {

	String placeId;
	String applicationId;
	String widgetId;
	String controlType;
	String optionId;

	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "QRCodeInteractor", false);
	}
	
	@Override
	public void onApplicationLoaded() {

//		/*
//		 * Create the sightinh service to post input to instant places
//		 */
//		sightingService = GWT.create(SightingService.class);
//		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");

		WidgetManager.get().setAutomaticInputRequests(false);
		placeId = com.google.gwt.user.client.Window.Location.getParameter("place");
		applicationId = com.google.gwt.user.client.Window.Location.getParameter("app");
		widgetId = com.google.gwt.user.client.Window.Location.getParameter("widget");
		controlType = com.google.gwt.user.client.Window.Location.getParameter("type");
		optionId = com.google.gwt.user.client.Window.Location.getParameter("opid");

		if (controlType
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			Log.info(this, "Processing imperative widget");
			doImperative();
		} else if (controlType
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY)) {
			Log.info(this, "Processing entry widget");
			doEntry();
		} else {
			Log.info(this, "Could not understand widget type");
			
		}
		
	}

	private void doEntry() {
		final TextBox textbox = new TextBox();
		Button button = new Button("Send");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Log.debug(this,  textbox.getText());
				doInput(textbox.getText());
				
			}
			
		});
		RootPanel.get().add(textbox);
		RootPanel.get().add(button);
		
	}

	void doImperative() {
		doInput("");
	}

	private void doInput(String input) {

		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d = new Date();
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(input);
		
		WidgetInput widgetInput = new WidgetInput();
		widgetInput.setWidgetId(this.widgetId);
		widgetInput.setWidgetOptionId(this.optionId);
		widgetInput.setInputMechanism("QRCode");
		// TODO: Allow users to log in. Set cookie with random id as in the desktop version
		widgetInput.setPersona("Anonymous");
		widgetInput.setParameters(parameters);
		
		WidgetManager.get().sendWidgetInput(this.placeId, this.applicationId, widgetInput, 
				new Callback<WidgetInput>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.debug(this, "An error ocurred.");
						RootPanel.get().add(new Label("An error ocurred."));
					}

					@Override
					public void onSuccess(WidgetInput result) {
						Log.debug(this, "Sent.");
						RootPanel.get().add(new Label("Sent."));
					}
				});
	}

	
}
