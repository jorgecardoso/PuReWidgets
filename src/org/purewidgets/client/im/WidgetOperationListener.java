package org.purewidgets.client.im;

import java.util.ArrayList;

import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;

public interface WidgetOperationListener {

        public void onWidgetInput(ArrayList<WidgetInput> inputList);
        public void onWidgetAdd(Widget widget);
        public void onWidgetDelete(Widget widget);
}