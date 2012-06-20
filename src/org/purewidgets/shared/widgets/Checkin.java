package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;

public class Checkin extends Widget {	
	
	
	public Checkin() {
		super("purewidgets-check-in", "Check-in", null);
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();
		options.add(new WidgetOption("purewidgets-check-in", "checkin"));
		this.setWidgetOptions(options);
		this.setControlType(CONTROL_TYPE_PRESENCE);
	}

}
