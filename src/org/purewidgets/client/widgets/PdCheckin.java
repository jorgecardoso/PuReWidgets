package org.purewidgets.client.widgets;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Checkin;

public class PdCheckin extends PDWidget{

	private Checkin checkin;
	
	public PdCheckin() {
		checkin = new Checkin();
		this.setWidget(checkin);
		
		Log.debug(this, "Sending to server");
		this.sendToServer();
	}
}
