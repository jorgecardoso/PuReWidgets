package org.purewidgets.shared.widgets;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;

/**
 * A check-in widget.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Checkin extends Widget {	
	
	
	private static ArrayList<WidgetOption> createCheckinOptions() {
		ArrayList<WidgetOption> options = new ArrayList<WidgetOption>();
		options.add(new WidgetOption("purewidgets-check-in", "checkin"));
		return options;
	}
	
	/**
	 * Creates a new Checkin widget.
	 * 
	 */
	public Checkin() {
		super("purewidgets-check-in", CONTROL_TYPE_PRESENCE, "Check-in", "", createCheckinOptions(), null);
		
	}

}
