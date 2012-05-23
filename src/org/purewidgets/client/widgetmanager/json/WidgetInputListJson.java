package org.purewidgets.client.widgetmanager.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.widgetmanager.WidgetInput;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;



public class WidgetInputListJson extends GenericJson {
	
	// Overlay types always have protected, zero-arg ctors
	   protected WidgetInputListJson() { 		   
	   }
	   
		public final native void setInputs(JsArray<WidgetInputJson> inputs) /*-{ 
			this.inputs = inputs; 
		}-*/;
		
		public final void setInputsFromArrayList(ArrayList<WidgetInputJson> inputs) {
			JsArray<WidgetInputJson> jsArray = JavaScriptObject.createArray().cast();
			
			for ( WidgetInputJson widgetInputJson : inputs ) {
				jsArray.push(widgetInputJson);
			}
			this.setInputs(jsArray);
		}
		
		public final ArrayList<WidgetInput> getInputs() {
			JsArray<WidgetInputJson> inputsJs = getInputsAsJsArray();
			ArrayList<WidgetInput> inputs = new ArrayList<WidgetInput>();//[inputsJs.length()]; 
			
			for (int i = 0; i < inputsJs.length(); i++) {
				inputs.add(inputsJs.get(i).getWidgetInput());
			}
			
			return inputs;
		}
		
		public final native JsArray<WidgetInputJson> getInputsAsJsArray() /*-{ 
			return this.inputs;
		}-*/;		
		
		
}
