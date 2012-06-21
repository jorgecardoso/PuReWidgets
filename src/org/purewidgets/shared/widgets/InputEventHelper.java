/**
 * 
 */
package org.purewidgets.shared.widgets;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public final class InputEventHelper {

	public static void triggerActionEvents(ArrayList<WidgetInput> inputList, ArrayList<Widget>widgetList) {
		/*
		 * Widgets that have input will be added to a new List, as well as
		 * the InputEvent that will later be triggered.
		 * 
		 *  We need to do this so that we can trigger the input events while
		 *  iterating over a different list. If we didn't do this, and the 
		 *  application added another widget in response to an input, we would
		 *  be modifying the same list concurrently.
		 *  
		 *  We use a hashmap to associate a list of inputs to each widget
		 */
		//ArrayList<Widget> widgets = new ArrayList<Widget>();
		//ArrayList<InputEvent> events = new ArrayList<InputEvent>();
		HashMap<Widget, ArrayList<WidgetInputEvent>> widgetsToInputEventsMap = new HashMap<Widget, ArrayList<WidgetInputEvent>>();
		
		
		/*
		 * Go through the inputs and save the widgets that have input
		 */
		for (WidgetInput input : inputList) {
			//Log.debug(this, "Processing input: " + input.toDebugString());

			/*
			 * Go through all widgets and find the one targeted by the input
			 */
			for (Widget widget : widgetList) {
				if (input.getWidgetId().equals(widget.getWidgetId())) {
					/*
					 * Found widget, match option...
					 */
					for (WidgetOption option : widget.getWidgetOptions()) {
						if (option.getWidgetOptionId().equals(input.getWidgetOptionId())) {
							/*
							 * Found option, save widget and inputevent
							 */
							WidgetInputEvent ie = new WidgetInputEvent(input.getPersona(),
									option, input.getParameters());
							ie.setAge(input.getAge());
							
							/*
							 * Put the event in the hashmap, associated with the target widget
							 */
							if (widgetsToInputEventsMap.containsKey(widget)) {
								widgetsToInputEventsMap.get(widget).add(ie);
							} else {
								ArrayList<WidgetInputEvent> events = new ArrayList<WidgetInputEvent>();
								events.add(ie);
								widgetsToInputEventsMap.put(widget, events);
							}
							
						}
					}
				}
			}
		}
		
		/*
		 * Now trigger the input event list for each widget
		 */
		//ArrayList<InputEvent> inputEvents = new ArrayList<InputEvent>();
		
		for (Widget widget : widgetsToInputEventsMap.keySet()) {
			Log.debug(InputEventHelper.class.getName(), "Firing input event on widget: " + widget.getWidgetId());
			widget.onInput(widgetsToInputEventsMap.get(widget));
		}
		
	}
}
