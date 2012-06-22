package org.purewidgets.server.im.json;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;


/**
 * 
 * The JSON data transfer object for Widget objects.
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WidgetJson extends GenericJson {

	private String placeId;

	private String applicationId;

	private String widgetId;
	
	private String controlType;
	
	private String shortDescription;
	
	private String longDescription;
	
	private ArrayList<WidgetOptionJson> widgetOptions;

	private ArrayList<WidgetParameterJson> widgetParameters;
	
	private WidgetJson() {
		
	}
	
	public static WidgetJson create(Widget widget) {
		WidgetJson widgetJson = new WidgetJson();
		
		widgetJson.setPlaceId(widget.getPlaceId());
		widgetJson.setApplicationId(widget.getApplicationId());
		widgetJson.setWidgetId(widget.getWidgetId());
		widgetJson.setControlType(widget.getControlType());
		widgetJson.setShortDescription(widget.getShortDescription());
		widgetJson.setLongDescription(widget.getLongDescription());
		
		ArrayList<WidgetOptionJson> widgetOptionsJson = new ArrayList<WidgetOptionJson>();
		for ( WidgetOption widgetOption : widget.getWidgetOptions() ) {
			widgetOptionsJson.add(WidgetOptionJson.create(widgetOption));
		}
		widgetJson.setWidgetOptions(widgetOptionsJson);

		ArrayList<WidgetParameterJson> widgetParametersJson = new ArrayList<WidgetParameterJson>();
		for ( WidgetParameter widgetParameter : widget.getWidgetParameters() ) {
			widgetParametersJson.add(WidgetParameterJson.create(widgetParameter));
		}
		widgetJson.setWidgetParameters(widgetParametersJson);
		
		return widgetJson;
	}
	
	public Widget getWidget() {
		
		ArrayList<WidgetOption> widgetOptions = new ArrayList<WidgetOption>();
		for ( WidgetOptionJson widgetOptionJson : this.getWidgetOptions() ) {
			widgetOptions.add(widgetOptionJson.getWidgetOption());
		}
		
		ArrayList<WidgetParameter> widgetParameters = new ArrayList<WidgetParameter>();
		for ( WidgetParameterJson widgetParameterJson : this.getWidgetParameters() ) {
			widgetParameters.add(widgetParameterJson.getWidgetParameter());
		}
		
		
		Widget widget = new Widget(this.widgetId, this.controlType, this.shortDescription, 
				this.longDescription, widgetOptions, widgetParameters);
		widget.setPlaceId(this.placeId);
		widget.setApplicationId(this.applicationId);
		
		return widget;
	}


	/**
	 * @param placeId the placeId to set
	 */
	private void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * @param applicationId the applicationId to set
	 */
	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	

	/**
	 * @param widgetId the widgetId to set
	 */
	private void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	

	/**
	 * @param controlType the controlType to set
	 */
	private void setControlType(String controlType) {
		this.controlType = controlType;
	}



	/**
	 * @param shortDescription the shortDescription to set
	 */
	private void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	

	/**
	 * @param longDescription the longDescription to set
	 */
	private void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return the widgetOptions
	 */
	private ArrayList<WidgetOptionJson> getWidgetOptions() {
		return widgetOptions;
	}

	/**
	 * @param widgetOptions the widgetOptions to set
	 */
	private void setWidgetOptions(ArrayList<WidgetOptionJson> widgetOptions) {
		this.widgetOptions = widgetOptions;
	}

	/**
	 * @return the widgetParameters
	 */
	private ArrayList<WidgetParameterJson> getWidgetParameters() {
		return widgetParameters;
	}

	/**
	 * @param widgetParameters the widgetParameters to set
	 */
	private void setWidgetParameters(ArrayList<WidgetParameterJson> widgetParameters) {
		this.widgetParameters = widgetParameters;
	}

}
