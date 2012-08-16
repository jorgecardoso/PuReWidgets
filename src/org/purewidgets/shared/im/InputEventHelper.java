/**
 * 
 */
package org.purewidgets.shared.im;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.logging.Log;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public final class InputEventHelper {

	public static ArrayList<WidgetInput> triggerActionEvents(ArrayList<WidgetInput> inputList, ArrayList<Widget>widgetList) {
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
		 * Holds the input which does not have a matching widget.
		 */
		ArrayList<WidgetInput> unprocessed = new ArrayList<WidgetInput>();
		
		/*
		 * Go through the inputs and save the widgets that have input
		 */
		for (WidgetInput input : inputList) {
			//Log.debug(this, "Processing input: " + input.toDebugString());

			/*
			 * Go through all widgets and find the one targeted by the input
			 */
			boolean foundWidget = false;
			for (Widget widget : widgetList) {
				if (input.getWidgetId().equals(widget.getWidgetId())) {
					/*
					 * Found widget, match option...
					 */
					foundWidget = true;
					for (WidgetOption option : widget.getWidgetOptions()) {
						if (option.getWidgetOptionId().equals(input.getWidgetOptionId())) {
							/*
							 * Found option, save widget and inputevent
							 */
							WidgetInputEvent ie = new WidgetInputEvent(input,
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
			
			if ( !foundWidget ) {
				unprocessed.add(input);
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
		
		return unprocessed;
	}
}
