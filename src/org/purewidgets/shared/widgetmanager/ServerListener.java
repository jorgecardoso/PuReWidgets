package org.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.purewidgets.shared.widgets.Widget;

public interface ServerListener {

	public void onWidgetInput(ArrayList<WidgetInput> inputList);
	public void onWidgetAdd(Widget widget);
	public void onWidgetDelete(Widget widget);
}
