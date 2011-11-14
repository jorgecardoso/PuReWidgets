package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.instantplaces.purewidgets.shared.widgets.Widget;

public interface ServerListener {

	public void onWidgetInput(ArrayList<WidgetInput> inputList);
	public void onWidgetAdd(Widget widget);
	public void onWidgetDelete(Widget widget);
	
	public void onPlacesList(ArrayList<Place> placeList);
	public void onApplicationsList(String placeId, ArrayList<Application> applicationList);
	public void onWidgetsList(String placeId, String applicationId, ArrayList<Widget> widgetList);
}
