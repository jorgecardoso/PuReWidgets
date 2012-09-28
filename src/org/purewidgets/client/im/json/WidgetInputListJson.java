package org.purewidgets.client.im.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.WidgetInput;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * WidgetInputListJson is a Json DTO for receiving and sending a list of widget input to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class WidgetInputListJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetInputListJson() {
	}

	/**
	 * Sets the list of widget input as a JsArray.
	 * 
	 * @param inputs JsArray of WidgetInputJson to set.
	 */
	public final native void setInputs(JsArray<WidgetInputJson> inputs) /*-{
		this.inputs = inputs;
	}-*/;

	/**
	 * Sets the list of widget input as an ArrayList of WidgetInputJson
	 * 
	 * @param inputs The  ArrayList of WidgetInputJson to set.
	 */
	public final void setInputsFromArrayList(ArrayList<WidgetInputJson> inputs) {
		JsArray<WidgetInputJson> jsArray = JavaScriptObject.createArray().cast();

		for (WidgetInputJson widgetInputJson : inputs) {
			jsArray.push(widgetInputJson);
		}
		this.setInputs(jsArray);
	}

	/**
	 * Gets an ArrayList with the list of WidgetInput
	 * 
	 * @return  An ArrayList with the list of WidgetInput
	 */
	public final ArrayList<WidgetInput> getInputs() {
		JsArray<WidgetInputJson> inputsJs = getInputsAsJsArray();
		ArrayList<WidgetInput> inputs = new ArrayList<WidgetInput>();// [inputsJs.length()];

		for (int i = 0; i < inputsJs.length(); i++) {
			inputs.add(inputsJs.get(i).getWidgetInput());
		}

		return inputs;
	}

	/**
	 * Gets a JsArray with the list of WidgetInputJson
	 * 
	 * @return a JsArray with the list of WidgetInputJson.
	 */
	public final native JsArray<WidgetInputJson> getInputsAsJsArray() /*-{
		return this.inputs;
	}-*/;

}
