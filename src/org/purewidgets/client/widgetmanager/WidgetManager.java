/**
 * 
 */
package org.purewidgets.client.widgetmanager;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.interactionmanager.Widget;
import org.purewidgets.shared.interactionmanager.WidgetInput;
import org.purewidgets.shared.interactionmanager.WidgetOption;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.InputEventHelper;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author Jorge C. S. Cardoso
 */
public class WidgetManager implements WidgetOperationListener {

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

	private ClientServerCommunicator communicator;

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
	
	public ClientServerCommunicator getServerCommunicator() {
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
		InputEventHelper.triggerActionEvents(inputList, this.widgetList);
	

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
	
	public void setServerCommunication(ClientServerCommunicator serverCommunication) {
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
	
	//TODO: remove from widgetmanager
	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, AsyncCallback<WidgetInput> callback) {
		this.communicator.sendWidgetInput(placeName, applicationName, widgetInput, callback);
	}
}
