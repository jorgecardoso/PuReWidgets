package org.instantplaces.purewidgets.shared.events;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;

public interface ApplicationListListener {
	public void onApplicationList(ArrayList<Application> applicationList);
	public void onWidgetsList(ArrayList<Widget> widgetList);
}
