/**
 * 
 */
package org.purewidgets.shared.widgetmanager;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.shared.Log;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.widgets.Widget;


/**
 * @author Jorge C. S. Cardoso
 */
public class WidgetManager implements ServerListener {

	/**
	 * "There can be only one"! Singleton pattern.
	 */
	private static WidgetManager wm = new WidgetManager();
	
	//private static HashMap<String, WidgetManager> wmMap = new HashMap<String, WidgetManager>();

	public static WidgetManager get() {
		return wm;
	}

	
	/**
	 * Communication with the Interaction manager is done via a
	 * ServerCommunicator.
	 */

	private ServerCommunicator communicator;

	/**
	 * The registered widgets.
	 */

	private ArrayList<Widget> widgetList;

	private boolean automaticWidgetRequests = true;
	
	/**
	 * Creates a new Widget Manager.
	 */
	protected WidgetManager() {
		setWidgetList(new ArrayList<Widget>());
	}
	
	/**
	 * 
	 * Registers a new widget so that it is assigned a reference code and is
	 * able to receive input. Adding a Widget triggers the generation of
	 * reference codes for that Widget.
	 * 
	 * The same Widget object can be added multiple times. If the options for
	 * that widget have changed, new reference codes may be assigned.
	 * 
	 * @param widget
	 *            The Widget to register.
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.WidgetManagerInterface#addWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 * 
	 */
	public void addWidget(Widget widget) {
		Log.debug(this, "Adding widget: " + widget.getWidgetId());
		
		/*
		 * Register the widget locally
		 */
		int index = indexOf(widget);
		if (-1 == index) {
			this.widgetList.add(widget);
			//Log.debug(this, "Adding new widget to widget list.");
		} else {
			
			Log.warn(this, "Widget '" + widget.getWidgetId() + "' already exists, replacing entry " + index + ".");
			
			this.widgetList.set(index, widget);
		}

		/*
		 * Register it on the server. TODO: We could check if anything really
		 * changed to decide if we need to send it to the server
		 */
		if (this.communicator != null) {
			if (this.automaticWidgetRequests) {
				this.communicator.addWidget(widget);
			} else {
				Log.debug(this, "Not sending widget to server because 'automaticWidgetRequests' is false");
			}
		} else {
			Log.error(this, "WidgetManager does not have a ServerCommunicator! Cannot communicate with Interaction Manager.");
		}
		
	}
	
//	public void getPlacesList() {
//		Log.debug(this, "Asking for place list");
//		this.communicator.getPlacesList();
//	}
	
	public ServerCommunicator getServerCommunicator() {
		return this.communicator;
	}

	public ArrayList<Widget> getWidgetList() {
		return widgetList;
	}


//	@Override
//	public void onPlacesList(ArrayList<Place> placeList) {
//		if ( null != this.applicationListListener ) {
//			this.applicationListListener.onPlaceList( placeList );
//		}
//	}
	
	@Override
	public  void onWidgetAdd(Widget widgetFromServer) {
		Log.debug(this, "Received widget from server: " + widgetFromServer.getWidgetId());
		/*
		 * Go through all widgets
		 */
		boolean changed = false;
		Widget receivedWidget = null;

		for (Widget widget : this.getWidgetList()) {
			if (widgetFromServer.getWidgetId().equals(widget.getWidgetId())) {
				

				receivedWidget = widget;

				/*
				 * Go through all the options received
				 */
				ArrayList<WidgetOption> options = widgetFromServer
						.getWidgetOptions();
				for (WidgetOption optionFromServer : options) {

					/*
					 * Find the matching local widget option
					 */
					for (WidgetOption option : widget.getWidgetOptions()) {
						if (option.getWidgetOptionId().equals(optionFromServer.getWidgetOptionId())) {

							/*
							 * If the ref code changed, update the local widget
							 */
							if ( !optionFromServer.getReferenceCode().equals( option.getReferenceCode() ) ) {
								option.setReferenceCode(optionFromServer
										.getReferenceCode());
								
								changed = true;
							}
						}
					}

				}

			}
		}
		if (changed) {
			Log.debug(this, "Updating reference code on widget"
					+ receivedWidget.getWidgetId());
			receivedWidget.onReferenceCodesUpdated();
		}

	}

	
	@Override
	public void onWidgetDelete(Widget widget) {

	}

	
	/**
	 * This method is called when there is input available from the server.
	 * 
	 */
	@Override
	public  void onWidgetInput(ArrayList<WidgetInput> inputList) {

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
		HashMap<Widget, ArrayList<InputEvent>> widgetsToInputEventsMap = new HashMap<Widget, ArrayList<InputEvent>>();
		
		
		/*
		 * Go through the inputs and save the widgets that have input
		 */
		for (WidgetInput input : inputList) {
			//Log.debug(this, "Processing input: " + input.toDebugString());

			/*
			 * Go through all widgets and find the one targeted by the input
			 */
			for (Widget widget : this.getWidgetList()) {
				if (input.getWidgetId().equals(widget.getWidgetId())) {
					/*
					 * Found widget, match option...
					 */
					for (WidgetOption option : widget.getWidgetOptions()) {
						if (option.getWidgetOptionId().equals(input.getWidgetOptionId())) {
							/*
							 * Found option, save widget and inputevent
							 */
							InputEvent ie = new InputEvent(input.getPersona(),
									option, input.getParameters());
							ie.setAge(input.getAge());
							
							/*
							 * Put the event in the hashmap, associated with the target widget
							 */
							if (widgetsToInputEventsMap.containsKey(widget)) {
								widgetsToInputEventsMap.get(widget).add(ie);
							} else {
								ArrayList<InputEvent> events = new ArrayList<InputEvent>();
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
			Log.debug(this, "Firing input event on widget: " + widget.toDebugString());
			widget.onInput(widgetsToInputEventsMap.get(widget));
		}
		

	}
	
	public void removeAllWidgets(boolean volatileOnly) {
		if ( null != this.communicator ) {
			this.communicator.deleteAllWidgets(volatileOnly);
		}
	}
	
	/**
	 * Removes a previously added Widget.
	 * 
	 * @param widget
	 *            The Widget to remove.
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.WidgetManagerInterface#removeWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	public void removeWidget(Widget widget) {
		Log.debug(this, "Removing widget: " + widget.getWidgetId() );
		this.getWidgetList().remove(widget);

		if (null != this.communicator) {
			if (this.automaticWidgetRequests) {
				this.communicator.deleteWidget(widget);
			}
		} else {
			Log.error(this, "WidgetManager does not have a ServerCommunicator! Cannot communicate with Interaction Manager.");
		}
	}	
	
	/**
	 * Enables or disables the automatic input requests
	 * @param automatic
	 */
	public void setAutomaticInputRequests(boolean automatic) {
		if ( this.communicator != null ) {
			this.communicator.setAutomaticInputRequests(automatic);
		}
	}
	
	/**
	 * Enables or disables the automatic input requests
	 * @param automatic
	 */
	public void setAutomaticWidgetRequests(boolean automatic) {
		Log.debug(this, "Setting 'automaticWidgetRequests' to: " + automatic);
		this.automaticWidgetRequests = automatic;
	}	
	
	public void setServerCommunication(ServerCommunicator serverCommunication) {
		this.communicator = serverCommunication;
		this.communicator.setServerListener(this);
	}
	
	public void setWidgetList(ArrayList<Widget> widgetList) {
		this.widgetList = widgetList;
	}

	private int indexOf(Widget widget) {
		for (Widget w : this.widgetList) {
			if (w.getWidgetId().equals(widget.getWidgetId())) {
				return this.widgetList.indexOf(w);
			}
		}
		return -1;
	}
	
	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, Callback<WidgetInput> callback) {
		this.communicator.sendWidgetInput(placeName, applicationName, widgetInput, callback);
	}
}
