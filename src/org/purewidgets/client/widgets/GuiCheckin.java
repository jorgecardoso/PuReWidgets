package org.purewidgets.client.widgets;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Checkin;

public class GuiCheckin extends PDWidget{

	private Checkin checkin;
	
	public GuiCheckin() {
		checkin = new Checkin();
		this.setWidget(checkin);
		
		Log.debug(this, "Sending to server");
		this.sendToServer();
	}
}
