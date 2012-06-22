package org.purewidgets.server.im.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.Widget;

/**
 * 
 * The JSON data transfer object for ArrayList of Widget objects.
 * 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WidgetListJson extends GenericJson {
	
	@SuppressWarnings("unused")
	private String applicationId;
	
	@SuppressWarnings("unused")
	private String placeId;
	
	private ArrayList<WidgetJson> widgets;

	
	private WidgetListJson() {
		
	}
	
	public static WidgetListJson create(String applicationId, String placeId, ArrayList<Widget> widgets) {
		WidgetListJson widgetListJson = new WidgetListJson();
		widgetListJson.setPlaceId(placeId);
		widgetListJson.setApplicationId(applicationId);
		
		ArrayList<WidgetJson> widgetsJson = new ArrayList<WidgetJson>();
		for ( Widget widget : widgets ) {
			widgetsJson.add( WidgetJson.create(widget) );
		}
		widgetListJson.setWidgets(widgetsJson);
		
		
		return widgetListJson;
	}
	

	public ArrayList<Widget> getWidgetList() {
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		
		for ( WidgetJson widgetJson : this.widgets ) {
			widgets.add( widgetJson.getWidget() );
		}
		
		return widgets;
	}
		
	
	private void setWidgets(ArrayList<WidgetJson> widgets) {
		this.widgets = widgets;
	}

	

	/**
	 * @param applicationId the applicationId to set
	 */
	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	/**
	 * @param placeId the placeId to set
	 */
	private void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

}
