package org.purewidgets.client.widgetmanager;

import java.util.ArrayList;

import org.purewidgets.shared.interactionmanager.Widget;
import org.purewidgets.shared.interactionmanager.WidgetInput;

public interface WidgetOperationListener {

        public void onWidgetInput(ArrayList<WidgetInput> inputList);
        public void onWidgetAdd(Widget widget);
        public void onWidgetDelete(Widget widget);
}