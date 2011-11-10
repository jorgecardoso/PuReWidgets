/**
 * 
 */
package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;
import java.util.HashMap;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.events.InputEvent;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;


/**
 * @author Jorge C. S. Cardoso
 * 
 */
public class WidgetManager implements ServerListener {

	/**
	 * "There can be only one"! Singleton pattern.
	 */
	private static WidgetManager wm = new WidgetManager();

	/**
	 * Communication with the Interaction manager is done via a
	 * ServerCommunicator.
	 */

	private ServerCommunicator communicator;

	/**
	 * The registered widgets.
	 */

	private ArrayList<Widget> widgetList;

	
	private ApplicationListListener applicationListListener;
	
	/**
	 * Creates a new Widget Manager.
	 */
	protected WidgetManager() {
		setWidgetList(new ArrayList<Widget>());
	}

	public static WidgetManager get() {
		return wm;
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
			this.widgetList.set(index, widget);
			Log.warn(this, "Widget already exists, replacing entry " + index + ".");
		}

		/*
		 * Register it on the server. TODO: We could check if anything really
		 * changed to decide if we need to send it to the server
		 */
		if (this.communicator != null) {
			this.communicator.addWidget(widget);
		} else {
			Log.error("WidgetManager does not have a ServerCommunicator! Cannot communicate with Interaction Manager.");
		}
		
	}
	

	private int indexOf(Widget widget) {
		for (Widget w : this.widgetList) {
			if (w.getWidgetId().equals(widget.getWidgetId())) {
				return this.widgetList.indexOf(w);
			}
		}
		return -1;
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
			this.communicator.deleteWidget(widget);
		} else {
			Log.error(this, "WidgetManager does not have a ServerCommunicator! Cannot communicate with Interaction Manager.");
		}
	}
	
	public void removeAllWidgets(boolean volatileOnly) {
		if ( null != this.communicator ) {
			this.communicator.deleteAllWidgets(volatileOnly);
		}
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
	
	public ServerCommunicator getServerCommunicator() {
		return this.communicator;
	}
	
	public void setServerCommunication(ServerCommunicator serverCommunication) {
		this.communicator = serverCommunication;
		this.communicator.setServerListener(this);
	}

	
	public void setWidgetList(ArrayList<Widget> widgetList) {
		this.widgetList = widgetList;
	}

	
	public ArrayList<Widget> getWidgetList() {
		return widgetList;
	}

	public void setApplicationListListener(ApplicationListListener listener) {
		this.applicationListListener = listener;
	}
	
	
	/**
	 * Returns the list of active or inactive applications.
	 * @param active
	 */
	public void getPlaceApplicationsList(boolean active) {
		Log.debug(this, "Getting applications list");
		this.communicator.getPlaceApplicationsList( active );
	}
	
	/**
	 * Returns the list of all applications.
	 * @param active
	 */
	public void getPlaceApplicationsList() {
		Log.debug(this, "Getting applications list");
		this.communicator.getPlaceApplicationsList(  );
	}	
	
	/**
	 * Returns the list of widgets from the specified application.
	 * @param active
	 */
	public void getApplicationWidgetsList(String placeId, String applicationId) {
		Log.debug(this, "Getting widgets from application: " + applicationId);
		this.communicator.getApplicationWidgetsList( placeId, applicationId );
	}	
	
	
	@Override
	public void onPlaceApplicationsList(ArrayList<Application> applications) {
		if ( null != this.applicationListListener ) {
			this.applicationListListener.onApplicationList(applications);
		}
	}
	
	@Override
	public void onApplicationWidgetsList(ArrayList<Widget> widgets) {
		if ( null != this.applicationListListener ) {
			this.applicationListListener.onWidgetsList( widgets );
		}
	}
}
