/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

import java.util.ArrayList;
import java.util.Date;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.Callback;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public abstract class BaseClickHandler implements ClickHandler {

	private Timer timer;
	private PopupUi popup;

	protected String placeName;
	protected String applicationName;
	protected String widgetId;
	protected ArrayList<WidgetOption> widgetOptions;
	
	public BaseClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions,
			PopupUi popup) {
		this.placeName = placeName;
		this.applicationName = applicationName;
		this.widgetId = widgetId;
		this.widgetOptions = widgetOptions;
		
		
		this.popup = popup;
		
		timer = new Timer() {
			@Override
			public void run() {
				timerExpired();
			}
		};
		
	}

	@Override
	public abstract void onClick(ClickEvent event);

	protected void sendInput(WidgetOption widgetOption, String input) {

		String user = UserInfo.getUserIdentity();

		Log.debug(this, "Sending input: " + input);
		popup.setText("Sending input from user " + user);

		popup.center();
		popup.show();

//		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
//		Date d = new Date();
		
		ArrayList<String>parameters = new ArrayList<String>();
		parameters.add(input);
		WidgetInput widgetInput = new WidgetInput();
		widgetInput.setPersona(user);
		widgetInput.setInputMechanism("PlaceInteractionWebpage:"+Navigator.getUserAgent());
		widgetInput.setParameters(parameters);
		widgetInput.setWidgetId(this.widgetId);
		widgetInput.setWidgetOptionId(widgetOption.getWidgetOptionId());
		
		WidgetManager.get().sendWidgetInput(this.placeName, this.applicationName, widgetInput, 
				new Callback<WidgetInput>() {

					@Override
					public void onSuccess(WidgetInput returnValue) {
						Log.debug(this, "Sent.");
						popup.setText(popup.getText() + "\n Sent!");
						timer.schedule(5000);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.debug(this, "An error ocurred.");
						popup.setText(popup.getText() + "\n Oops, an error ocurred!");
						timer.schedule(5000);
						
					}
			
		});

	}

	void timerExpired() {
		if (null != popup && popup.isShowing()) {
			popup.hide();
		}
	}
}
