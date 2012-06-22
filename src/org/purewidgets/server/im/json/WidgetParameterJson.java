package org.purewidgets.server.im.json;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.WidgetParameter;

/**
 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class WidgetParameterJson extends GenericJson {
	private String name;
	
	private String value;
	
	
	private WidgetParameterJson() {
		
	}

	public static WidgetParameterJson create(WidgetParameter widgetParameter) {
		WidgetParameterJson widgetParameterJson = new WidgetParameterJson();
		
		widgetParameterJson.setName(widgetParameter.getName());
		widgetParameterJson.setValue(widgetParameter.getValue());
		
		return widgetParameterJson;
	}

	
	public WidgetParameter getWidgetParameter() {
		WidgetParameter widgetParameter = new WidgetParameter(this.name, this.value);
		
		return widgetParameter;
	}
	
	
	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}


	

	/**
	 * @param value the value to set
	 */
	private void setValue(String value) {
		this.value = value;
	}
	
}
