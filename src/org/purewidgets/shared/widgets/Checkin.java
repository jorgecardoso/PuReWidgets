package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;

public class Checkin extends Widget {	
	
	
	private static ArrayList<WidgetOption> createCheckinOptions() {
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();
		options.add(new WidgetOption("purewidgets-check-in", "checkin"));
		return options;
	}
	
	public Checkin() {
		super("purewidgets-check-in", CONTROL_TYPE_PRESENCE, "Check-in", "", createCheckinOptions(), null);
		
	}

}
