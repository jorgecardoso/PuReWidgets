package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.instantplaces.purewidgets.shared.widgets.Widget;

@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
public class WidgetList {
	
	private String applicationId;
	
	private String placeId;
	
	private ArrayList<Widget> widgets;

	
	public void setWidgets(ArrayList<Widget> widgets) {
		this.widgets = widgets;
	}

	public ArrayList<Widget> getWidgets() {
		return this.widgets;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}

	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

}
