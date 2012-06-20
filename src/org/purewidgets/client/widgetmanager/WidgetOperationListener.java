package org.purewidgets.client.widgetmanager;

import java.util.ArrayList;

import org.purewidgets.shared.widgetmanager.WidgetInput;
import org.purewidgets.shared.widgets.Widget;

public interface WidgetOperationListener {

        public void onWidgetInput(ArrayList<WidgetInput> inputList);
        public void onWidgetAdd(Widget widget);
        public void onWidgetDelete(Widget widget);
}