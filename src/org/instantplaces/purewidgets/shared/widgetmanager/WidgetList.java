package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.instantplaces.purewidgets.shared.widgets.Widget;

@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
public class WidgetList {
	

	private ArrayList<Widget> widgets;

	
	public void setWidgets(ArrayList<Widget> widgets) {
		this.widgets = widgets;
	}

	public ArrayList<Widget> getWidgets() {
		return this.widgets;
	}

}
