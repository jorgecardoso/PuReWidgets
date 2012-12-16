/**
 * 
 */
package org.purewidgets.client.im;

import java.util.ArrayList;

import org.purewidgets.client.im.json.WidgetJson;
import org.purewidgets.client.json.GenericJson;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.im.InputEventHelper;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The WidgetManager manages the creation, deletion, and input handling for all PdWidgets created by an application.
 * 
 * All operations run asynchronously.
 * 
 * It makes sure that widgets are sent to the interaction manager when created by the application, deleted from the 
 * interaction manager when an application deletes a widget, and that widgets receive all input from the interaction manager.
 * 
 * The InteractionManager uses queues and timers to retry failed operations and the local storage (for deleted widgets) to 
 * continue after the application is re-started.
 * 
 * @author Jorge C. S. Cardoso
 */
public class WidgetManager {

	/**
	 * The WidgetManager intercalates adding and deleting widgets operations.
	 * This enum serves to identify the next operation that will be performed.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	private static enum NextWidgetAction {
		ADD, DELETE
	}

	/**
	 * The interval between input requests (milliseconds).
	 * If a synchronous channel cannot be created, the WidgetManager will periodically
	 * ask the interaction manager for input.
	 */
	private static final int INPUT_REQUEST_INTERVAL = 10000;

	/**
	 * The current interval between input requests to the interaction manager.
	 * 
	 * If a previous request failed, the WidgetManager will double the waiting period.
	 */
	private int currentInputRequestInterval;
	
	/**
	 * The localstorage parameter name for storing the last input timestamp.
	 */
	private static final String TIMESTAMP = "lastTimeStamp";

	/**
	 * The default interval between widget requests to the server (milliseconds)
	 */
	private static final int WIDGET_REQUEST_INTERVAL = 10000;

	/**
	 * The current widget request interval.
	 * 
	 * If a previous widget operation failed, the WidgetManager will double the waiting period.
	 */
	private int currentWidgetRequestInterval;
	
	
	/**
	 * The synchronization delay
	 */
	private static final int SYNC_DELAY = 5000;
	
	/**
	 * The singleton reference. 
	 */
	private static WidgetManager wm;

	/**
	 * The place id of the application this WidgetManager is serving.
	 */	
	private String placeId;
	
	/**
	 * The application id of the application this WidgetManager is serving.
	 */
	private String applicationId;


	/**
	 * The InteractionManagerService to communicate with the interaction manager server.
	 */
	private InteractionManagerService communicator;



	/**
	 * The LocalStorage used to store the last input timestamp and the list of widgets to delete.
	 */
	private LocalStorage localStorage;

	/**
	 * The next widget operation to be performed.
	 */
	private NextWidgetAction nextWidgetAction;

	

	/**
	 * Keeps a list of the most recently processed input. Used to make sure we
	 * don't trigger the same input event more than once.
	 */
	private ArrayList<WidgetInput> processedInput;

	/**
	 * List of input which could not be matched to a widget. The WM will try to match it later when
	 * the app adds a widget.
	 */
	private ArrayList<WidgetInput> unprocessedInput;
	
	/**
	 * Timer for scheduling input requests to the server.
	 */
	private Timer timerInput;

	/**
	 * Timer for scheduling widget requests to the server.
	 */
	private Timer timerWidget;

	/**
	 * List of widgets scheduled to be added to the server
	 */
	private ArrayList<Widget> toAddWidgetPool;

	/**
	 * List of widgets scheduled to be added to the server
	 */
	private ArrayList<Widget> toDeleteWidgetPool;

	/**
	 * The registered widgets.
	 */
	private ArrayList<Widget> widgetList;

	/**
	 * Creates a new WidgetManager for the specified application, and with the given LocalStorage, and InteractionManager.
	 */
	private WidgetManager(String placeId, String applicationId, LocalStorage localStorage,
			InteractionManagerService interactionManager) {

		this.placeId = placeId;
		this.applicationId = applicationId;
		this.localStorage = localStorage;

		this.widgetList = new ArrayList<Widget>();
		this.toAddWidgetPool = new ArrayList<Widget>();
		this.toDeleteWidgetPool = new ArrayList<Widget>();
		this.loadToDeleteWidgetPoolFromLocalStorage();
		this.processedInput = new ArrayList<WidgetInput>();
		this.unprocessedInput = new ArrayList<WidgetInput>();
		this.communicator = interactionManager;
		this.timerInput = new Timer() {
			@Override
			public void run() {
				periodicallyAskForInputFromServer();
			}
		};
		this.currentInputRequestInterval = INPUT_REQUEST_INTERVAL;

		this.communicator.createChannel(this.placeId, this.applicationId, this.applicationId,
				new AsyncCallback<ArrayList<WidgetInput>>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.debug(WidgetManager.this,
								"Could not create input channel. Going to use polling instead", caught);
						WidgetManager.this.timerInput
								.schedule(WidgetManager.this.currentInputRequestInterval);
					}

					@Override
					public void onSuccess(ArrayList<WidgetInput> result) {
						WidgetManager.this.onWidgetInput(result);

					}

				});
		

		WidgetManager.this.firstInputFromServer();

		
	}

	/**
	 * Creates a new WidgetManager for the specified application, and with the given LocalStorage, and InteractionManager.
	 * This method creates only one instance of the WidgetManager. If it is called twice, the second invocation will simply
	 * return.
	 *
	 * @param placeId The place id of the application that this WidgetManager will serve.
	 * @param applicationId The application id of the application that this WidgetManager will serve.
	 * @param localStorage The LocalStorage that this WidgetManager will use to store persistent data.
	 * @param interactionManager The InteractionManager that will be used to communicate with the interaction manager server.
	 */
	public static void create(String placeId, String applicationId, LocalStorage localStorage,
			InteractionManagerService interactionManager) {
		
		if ( null == wm ) {
			wm = new WidgetManager(placeId, applicationId, localStorage, interactionManager);
		}
	}

	/**
	 * Returns a WidgetManager.
	 * 
	 * @return A WidgetManager
	 */
	public static WidgetManager get() {
		return wm;
	}

	/**
	 * Registers a new widget so that it is assigned a reference code and is
	 * able to receive input. Adding a Widget triggers the generation of
	 * reference codes for that Widget. The WidgetManager will send the added
	 * widget to the interaction manager server as soon as possible.
	 * 
	 * The same Widget object can be added multiple times. If the options for
	 * that widget have changed, new reference codes may be assigned.
	 * 
	 * @param widget
	 *            The Widget to register.
	 *            
	 */
	public void addWidget(final Widget widget) {
		Log.debug(this, "Adding widget: " + widget.getWidgetId());

		/*
		 * Register the widget locally
		 */
		int index = indexOf(this.widgetList, widget);
		if (-1 == index) {
			this.widgetList.add(widget);
		} else {
			Log.warn(this, "Widget '" + widget.getWidgetId()
					+ "' already exists in widget list, replacing entry " + index + ".");
			this.widgetList.set(index, widget);
		}

		index = indexOf(this.toAddWidgetPool, widget);
		if (-1 == index) {
			this.toAddWidgetPool.add(widget);
		} else {
			Log.warn(this, "Widget '" + widget.getWidgetId()
					+ "' already exists in to add widget list, replacing entry " + index + ".");

			this.toAddWidgetPool.set(index, widget);
		}

		this.nextWidgetAction = NextWidgetAction.ADD;

		startTimerWidget();
		
		new Timer() {
			
			@Override
			public void run() {
				processPendingInput(widget);
			}	
		}.schedule(1000);
		
	}
	

//	public InteractionManagerService getServerCommunicator() {
//		return this.communicator;
//	}

//	public ArrayList<Widget> getWidgetList() {
//		return widgetList;
//	}

	/**
	 * Removes all widgets that belong to the application this WidgetManager is serving.
	 */
	public void removeAllWidgets() {

		this.communicator.deleteAllWidgets(this.placeId, this.applicationId, this.applicationId,
				null);
	}

	/**
	 * Removes a previously added Widget.
	 * The widget will be removed from the interaction manager as soon as possible. This method stores widgets
	 * to be removed in the local storage so that they will be removed even if the application is terminated before
	 * the WidgetManager has a change to send the deleted widget to the interaction manager.
	 * 
	 * 
	 * @param widget
	 *            The Widget to remove.
	 */
	public void removeWidget(Widget widget) {
		Log.debug(this, "Removing widget: " + widget.getWidgetId());

		/*
		 * Register the widget locally
		 */
		int index = indexOf(this.widgetList, widget);
		if (-1 == index) {
			Log.warn(this, "Widget '" + widget.getWidgetId() + "' does not exist in widget list.");
		} else {
			this.widgetList.remove(index);
		}

		index = indexOf(this.toDeleteWidgetPool, widget);
		if (-1 == index) {
			this.toDeleteWidgetPool.add(widget);
			this.saveToDeleteWidgetPoolToLocalStorage();
		} else {
			Log.warn(this, "Widget '" + widget.getWidgetId()
					+ "' already exists in to delete widget list, replacing entry " + index + ".");

			this.toDeleteWidgetPool.set(index, widget);
		}

		this.nextWidgetAction = NextWidgetAction.DELETE;

		startTimerWidget();
	}

	private void addToProcessedInput(WidgetInput wi) {
		this.processedInput.add(wi);

		if (this.processedInput.size() > 50) {
			this.processedInput.remove(0);
		}
	}

	private void firstInputFromServer() {
		Log.debugFinest(this, "Contacting application server for input...");
		this.communicator.getInputFromServer(this.placeId, this.applicationId, this.applicationId,
				this.getLastTimeStampAsString(), new AsyncCallback<ArrayList<WidgetInput>>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(WidgetManager.this, "Could not get first input from server.");
					}

					@Override
					public void onSuccess(ArrayList<WidgetInput> result) {
						Log.debug(WidgetManager.this, "Received first input from server.");
						WidgetManager.this.onWidgetInput(result);
					}
				});

	}
	
	private long getLastTimeStampAsLong() {
		try {
			return Long.parseLong(getLastTimeStampAsString());
		} catch (Exception e) {
			Log.error(this, "Could no parse timestamp: " + e.getMessage());
		}
		return 0;
	}
	

	private String getLastTimeStampAsString() {
		String ts = this.localStorage.getString(TIMESTAMP);

		if (ts == null || ts.length() < 1) {
			return "0";
		} else {
			return ts;
		}
	}

	private int indexOf(ArrayList<Widget> list, Widget widget) {
		for (Widget w : list) {
			if (w.getWidgetId().equals(widget.getWidgetId())) {
				return list.indexOf(w);
			}
		}
		return -1;
	}

	private boolean isProcessed(WidgetInput wi) {
		if (this.processedInput.contains(wi)) {
			return true;
		}
		return false;
	}

	private void loadToDeleteWidgetPoolFromLocalStorage() {
		ArrayList<String> widgetsSerialized = this.localStorage.loadList("WidgetManager-deletePool");
		for ( String widgetSerialized : widgetsSerialized ) {
			WidgetJson widgetJson = GenericJson.fromJson(widgetSerialized);
			this.toDeleteWidgetPool.add( widgetJson.getWidget() );
		}
	}

	private void onWidgetAdd(Widget widgetFromServer) {
		Log.debug(this, "Received widget from server: " + widgetFromServer.getWidgetId());

		/*
		 * Remove from to add pool
		 */
		for (Widget widgetFromPool : this.toAddWidgetPool) {
			if (widgetFromPool.getWidgetId().equals(widgetFromServer.getWidgetId())) {
				this.toAddWidgetPool.remove(widgetFromPool);
				break;
			}
		}

		/*
		 * Go through all widgets
		 */
		boolean changed = false;
		Widget receivedWidget = null;

		for (Widget widget : this.widgetList) {
			if (widgetFromServer.getWidgetId().equals(widget.getWidgetId())) {

				receivedWidget = widget;

				/*
				 * Go through all the options received
				 */
				ArrayList<WidgetOption> options = widgetFromServer.getWidgetOptions();
				for (WidgetOption optionFromServer : options) {

					/*
					 * Find the matching local widget option
					 */
					for (WidgetOption option : widget.getWidgetOptions()) {
						if (option.getWidgetOptionId().equals(optionFromServer.getWidgetOptionId())) {

							/*
							 * If the ref code changed, update the local widget
							 */
							if (!optionFromServer.getReferenceCode().equals(
									option.getReferenceCode())) {
								option.setReferenceCode(optionFromServer.getReferenceCode());

								changed = true;
							}
						}
					}

				}

			}
		}
		if (changed) {
			Log.debug(this, "Updating reference code on widget" + receivedWidget.getWidgetId());
			receivedWidget.onReferenceCodesUpdated();
		}

	}

	private void onWidgetDelete(Widget widgetFromServer) {
		Log.debug(this, "Received widget deleted from server: " + widgetFromServer.getWidgetId());

		/*
		 * Remove from to add pool
		 */
		for (Widget widgetFromPool : this.toDeleteWidgetPool) {
			if (widgetFromPool.getWidgetId().equals(widgetFromServer.getWidgetId())) {
				this.toDeleteWidgetPool.remove(widgetFromPool);
				this.saveToDeleteWidgetPoolToLocalStorage();
				break;
			}
		}

	}

	/**
	 * This method is called when there is input available from the server.
	 * 
	 */

	private void onWidgetInput(ArrayList<WidgetInput> inputList) {
		/*
		 * Update our most recent input timeStamp so that in the next round we
		 * ask only for newer input.
		 * 
		 * Mark already processed input
		 */
		ArrayList<WidgetInput> toDelete = new ArrayList<WidgetInput>();
		for (WidgetInput widgetInput : inputList) {
			/*
			 * Save the new timeStamp locally
			 */
			if (toLong(widgetInput.getTimeStamp()) > this.getLastTimeStampAsLong()) {
				this.setTimeStamp(toLong(widgetInput.getTimeStamp()));
			}

			if (this.isProcessed(widgetInput)) {
				Log.debug(this, "Found already processed input for widget: " + widgetInput.getWidgetId());
				toDelete.add(widgetInput);
			} else {
				this.addToProcessedInput(widgetInput);
			}
		}

		/*
		 * Remove processed input
		 */
		for (WidgetInput wi : toDelete) {
			inputList.remove(wi);
		}

		/*
		 * Trigger the input events on the widgets
		 */
		ArrayList<WidgetInput> notProcessed = InputEventHelper.triggerWidgetInputEvents(inputList, this.widgetList);
		this.unprocessedInput.addAll(notProcessed);
		this.processedInput.removeAll(notProcessed);

		/*
		 * Delete the widgets that where the target of unprocessed input.
		 * This way we will remove stale widgets when users try to interact with them
		 * If the widget is not stale, and the application just hasn't had the chance to add them, them the widget
		 * will be added again (a bit of a waste of server resources, but I have no better alternative yet). 
		 */
		for ( WidgetInput wi : notProcessed ) {
			Widget w = new Widget(wi.getWidgetId(), "", "", null);
			this.removeWidget(w);
		}
	}

	/**
	 * Asks for input for every widget in the application.
	 */
	private void periodicallyAskForInputFromServer() {

		Log.debugFinest(this, "Asking for input from server...");

		this.communicator.getInputFromServer(this.placeId, this.applicationId, this.applicationId,
				this.getLastTimeStampAsString(), new AsyncCallback<ArrayList<WidgetInput>>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(WidgetManager.this, "Could not get input from server.");
						WidgetManager.this.currentInputRequestInterval *= 2;
						Log.debugFinest(this, "Scheduling next input request in "
								+ (WidgetManager.this.currentInputRequestInterval / 1000)
								+ " seconds");
						WidgetManager.this.timerInput
								.schedule(WidgetManager.this.currentInputRequestInterval);
					}

					@Override
					public void onSuccess(ArrayList<WidgetInput> result) {
						Log.debug(WidgetManager.this, "Reveived first input from server.");
						WidgetManager.this.onWidgetInput(result);
						Log.debugFinest(this, "Scheduling next input request in "
								+ (WidgetManager.this.currentInputRequestInterval / 1000)
								+ " seconds");
						WidgetManager.this.timerInput
								.schedule(WidgetManager.this.currentInputRequestInterval);
					}
				});

	}

	private void periodicallySendWidgetsToServer() {
		if (this.toAddWidgetPool.size() == 0 && this.toDeleteWidgetPool.size() == 0) {
			Log.debug(this, "No more widgets to add or delete. Stopping widget requets.");
			this.timerWidget = null;
			return;
		}

		if (this.toAddWidgetPool.size() == 0) {
			this.nextWidgetAction = NextWidgetAction.DELETE;
		}
		if (this.toDeleteWidgetPool.size() == 0) {
			this.nextWidgetAction = NextWidgetAction.ADD;
		}

		if (NextWidgetAction.ADD == this.nextWidgetAction) {

			if (this.toAddWidgetPool.size() > 0) {

				/*
				 * Send the current set of widgets to the server.
				 */
				this.communicator.addWidgetToServer(this.placeId, this.applicationId,
						this.applicationId, this.toAddWidgetPool,
						new AsyncCallback<ArrayList<Widget>>() {

							@Override
							public void onFailure(Throwable caught) {
								WidgetManager.this.currentWidgetRequestInterval *= 2;
								timerWidget
										.schedule(WidgetManager.this.currentWidgetRequestInterval);
								Log.debugFinest(this, "Scheduling next widget request in "
										+ (WidgetManager.this.currentWidgetRequestInterval / 1000)
										+ " seconds");
							}

							@Override
							public void onSuccess(ArrayList<Widget> result) {
								for (Widget widget : result) {
									WidgetManager.this.onWidgetAdd(widget);
								}
								WidgetManager.this.currentWidgetRequestInterval = INPUT_REQUEST_INTERVAL;
								timerWidget
										.schedule(WidgetManager.this.currentWidgetRequestInterval);
								Log.debugFinest(this, "Scheduling next widget request in "
										+ (WidgetManager.this.currentWidgetRequestInterval / 1000)
										+ " seconds");
							}

						});
			}
		}

		if (NextWidgetAction.DELETE == this.nextWidgetAction) {
			if (this.toDeleteWidgetPool.size() > 0) {

				/*
				 * Send the current set of widgets to the server.
				 */
				this.communicator.removeWidget(this.placeId, this.applicationId,
						this.applicationId, this.toDeleteWidgetPool,
						new AsyncCallback<ArrayList<Widget>>() {

							@Override
							public void onFailure(Throwable caught) {
								WidgetManager.this.currentWidgetRequestInterval *= 2;
								timerWidget
										.schedule(WidgetManager.this.currentWidgetRequestInterval);
								Log.debugFinest(this, "Scheduling next widget request in "
										+ (WidgetManager.this.currentWidgetRequestInterval / 1000)
										+ " seconds");
							}

							@Override
							public void onSuccess(ArrayList<Widget> result) {
								for (Widget widget : result) {
									WidgetManager.this.onWidgetDelete(widget);
								}
								timerWidget
										.schedule(WidgetManager.this.currentWidgetRequestInterval);
								Log.debugFinest(this, "Scheduling next widget request in "
										+ (WidgetManager.this.currentWidgetRequestInterval / 1000)
										+ " seconds");
							}

						});

			}
		}

		/*
		 * Invert the next action
		 */
		if (NextWidgetAction.ADD == this.nextWidgetAction) {
			this.nextWidgetAction = NextWidgetAction.DELETE;
		} else if (NextWidgetAction.DELETE == this.nextWidgetAction) {
			this.nextWidgetAction = NextWidgetAction.ADD;
		}

	}

	private void processPendingInput( Widget widget ) {
		/*
		 * check if we have pending input
		 */
		ArrayList<WidgetInput> pendingInput = new ArrayList<WidgetInput>();
		for ( WidgetInput input : this.unprocessedInput ) {
			if ( input.getWidgetId().equals(widget.getWidgetId()) ) {
				Log.debug(this, "Found pending input for widget: " + widget.getWidgetId() );
				pendingInput.add(input);
			}
		}
		/*
		 * Remove pending input from unprocessed list
		 */
		if ( pendingInput.size() > 0 ) {
			for ( WidgetInput input : pendingInput ) {
				this.unprocessedInput.remove(input);
			}
			Log.debug(this, "Processing pending input for widget: " + widget.getWidgetId() );
			this.onWidgetInput(pendingInput);
		}
	}

	private void saveToDeleteWidgetPoolToLocalStorage() {
		ArrayList<String> widgetsSerialized = new ArrayList<String>();
		for ( Widget widget : this.toDeleteWidgetPool ) {
			widgetsSerialized.add( WidgetJson.create(widget).toJsonString() );
		}
		this.localStorage.saveList("WidgetManager-deletePool", widgetsSerialized);
	}

	private void setTimeStamp(long timeStamp) {
		this.localStorage.setString(TIMESTAMP, "" + timeStamp);
	}

	/**
	 * Starts the timer that triggers the WidgetManager to send widget information to the 
	 * Interaction Manager server.
	 * 
	 * This method resets the timer every time it is called, so the timer will only be triggered
	 * is enough time has passed without this method being called.
	 *  
	 */
	private void startTimerWidget() {
		if (null == this.timerWidget) {
			Log.debugFinest(this, "Creating new widget timer and scheduling it.");
			this.timerWidget = new Timer() {
				@Override
				public void run() {
					periodicallySendWidgetsToServer();
				}
			};
			this.currentWidgetRequestInterval = WIDGET_REQUEST_INTERVAL;
			timerWidget.schedule(this.currentWidgetRequestInterval);
		} else {
			this.currentWidgetRequestInterval = WIDGET_REQUEST_INTERVAL;
			Log.debugFinest(this, "Delaying widget update to server by " + (SYNC_DELAY/1000) + " seconds.");
			timerWidget.schedule(SYNC_DELAY);
		}
	}

	private long toLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			Log.error(this, "Error parsing " + value + " to long." + e.getMessage());
		}
		return 0;
	}

}
