package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * The WidgetInput class represents an input from a user to an option of a widget.
 * An input is issued by a persona and consists of a list  of zero or more parameters.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
public class WidgetInput {
	

	private String widgetId;
	

	private String widgetOptionId;
	

	private String timeStamp;	
	

	private ArrayList<String> parameters;
	

	private String persona;	
	


	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}


	public String getWidgetId() {
		return this.widgetId;
	}

	
	public void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}


	public String getWidgetOptionId() {
		return this.widgetOptionId;
	}


	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	public String getTimeStamp() {
		return this.timeStamp;
	}


	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}


	public ArrayList<String> getParameters() {
		return this.parameters;
	}


	public void setPersona(String persona) {
		this.persona = persona;
	}


	public String getPersona() {
		return this.persona;
	}
	
	public final String toDebugString() {
		return "WidgetInput(" + "persona: " + this.persona + "; parameters " + this.parameters.toString() + ")";
	}

}
