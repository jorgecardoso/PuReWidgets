package org.purewidgets.shared.interactionmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
public class WidgetInputList {

	private ArrayList<WidgetInput> inputs;

	
	public void setInputs(ArrayList<WidgetInput> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<WidgetInput> getInputs() {
		return inputs;
	}
	
	
}
