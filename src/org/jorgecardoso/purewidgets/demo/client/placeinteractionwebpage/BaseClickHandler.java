/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

import java.util.Date;

import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public abstract class BaseClickHandler implements ClickHandler {

	private Label message;

	private Timer timer;
	private PopupPanel popup;

	public BaseClickHandler() {
		message = new Label();
		popup = new PopupPanel(true, true);
		popup.add(message);
		popup.setSize("200px", "100px");

		timer = new Timer() {
			@Override
			public void run() {
				timerExpired();
			}
		};
	}

	@Override
	public abstract void onClick(ClickEvent event);

	protected void sendInput(String input) {

		String user = PlaceInteractionWebpage.userIdentity;

		Log.debug(this, "Sending input: " + input);
		message.setText("Sending input from user " + user);

		popup.center();
		popup.show();

		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d = new Date();
		
		PlaceInteractionWebpage.sightingService.sighting(user + " " + input, dtf.format(d),
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.debug(this, "An error ocurred.");
						message.setText(message.getText() + "\n Oops, an error ocurred!");
						timer.schedule(5000);
					}

					@Override
					public void onSuccess(Void result) {
						Log.debug(this, "Sent.");
						message.setText(message.getText() + "\n Sent!");
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
