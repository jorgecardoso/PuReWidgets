package org.purewidgets.shared.interactionmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
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
