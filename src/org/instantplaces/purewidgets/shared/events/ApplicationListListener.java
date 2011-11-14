package org.instantplaces.purewidgets.shared.events;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.instantplaces.purewidgets.shared.widgets.Widget;

public interface ApplicationListListener {

	public void onApplicationList(String placeId, ArrayList<Application> applicationList);
	public void onWidgetsList(String placeId, String applicationId, ArrayList<Widget> widgetList);
	public void onPlaceList(ArrayList<Place> placeList);
}
