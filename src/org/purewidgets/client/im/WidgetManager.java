/**
 * 
 */
package org.purewidgets.client.im;

import java.util.ArrayList;

import org.purewidgets.client.im.json.WidgetJson;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.im.InputEventHelper;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jorge C. S. Cardoso
 */
public class WidgetManager {

	private static enum NextWidgetAction {
		ADD, DELETE
	}

	private static final int INPUT_REQUEST_INTERVAL = 10000;

	private static final String TIMESTAMP = "lastTimeStamp";

	/**
	 * The default interval between widget requests to the server
	 */
	private static final int WIDGET_REQUEST_INTERVAL = 10000;;

	/**
	 * 
	 */
	private static WidgetManager wm;

	private String applicationId;


	/**
	 * Communication with the Interaction manager is done via a
	 * ServerCommunicator.
	 */
	private InteractionManagerService communicator;

	/**
	 * The current interval between connection attempts to the server.
	 */
	private int currentInputRequestInterval;

	/**
	 * The current widget request interval
	 */
	private int currentWidgetRequestInterval;

	private LocalStorage localStorage;

	private NextWidgetAction nextWidgetAction;

	private String placeId;

	/**
	 * Keeps a list of the most recently processed input. Used to make sure we
	 * don't trigger the same input event more than once.
	 */
	private ArrayList<WidgetInput> processedInput;

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
	 * Creates a new Widget Manager.
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

		this.communicator = interactionManager;
		timerInput = new Timer() {
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
								"Could not create input channel. Going to use polling insted");
						WidgetManager.this.timerInput
								.schedule(WidgetManager.this.currentInputRequestInterval);
					}

					@Override
					public void onSuccess(ArrayList<WidgetInput> result) {
						WidgetManager.this.onWidgetInput(result);

					}

				});
		this.firstInputFromServer();
	}

	public static void create(String placeId, String applicationId, LocalStorage localStorage,
			InteractionManagerService interactionManager) {
		wm = new WidgetManager(placeId, applicationId, localStorage, interactionManager);
	}

	public static WidgetManager get() {
		return wm;
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
	}

	public InteractionManagerService getServerCommunicator() {
		return this.communicator;
	}

	public ArrayList<Widget> getWidgetList() {
		return widgetList;
	}

	public void removeAllWidgets() {

		this.communicator.deleteAllWidgets(this.placeId, this.applicationId, this.applicationId,
				null);

	}

	/**
	 * Removes a previously added Widget.
	 * 
	 * @param widget
	 *            The Widget to remove.
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.WidgetManagerInterface#removeWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	public void removeWidget(Widget widget) {
		Log.debug(this, "Removing widget: " + widget.getWidgetId());

		/*
		 * Register the widget locally
		 */
		int index = indexOf(this.widgetList, widget);
		if (-1 == index) {
			this.widgetList.remove(index);
		} else {
			Log.warn(this, "Widget '" + widget.getWidgetId() + "' does not exist in widget list.");
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

	private void saveToDeleteWidgetPoolToLocalStorage() {
		ArrayList<String> widgetsSerialized = new ArrayList<String>();
		for ( Widget widget : this.toDeleteWidgetPool ) {
			widgetsSerialized.add( WidgetJson.create(widget).toJsonString() );
		}
		this.localStorage.saveList("WidgetManager-deletePool", widgetsSerialized);
	}
	
	private void loadToDeleteWidgetPoolFromLocalStorage() {
		ArrayList<String> widgetsSerialized = this.localStorage.loadList("WidgetManager-deletePool");
		for ( String widgetSerialized : widgetsSerialized ) {
			WidgetJson widgetJson = WidgetJson.fromJson(widgetSerialized);
			this.toDeleteWidgetPool.add( widgetJson.getWidget() );
		}
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
						Log.debug(WidgetManager.this, "Reveived first input from server.");
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
		InputEventHelper.triggerActionEvents(inputList, this.widgetList);

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

	private void setTimeStamp(long timeStamp) {
		this.localStorage.setString(TIMESTAMP, "" + timeStamp);
	}

	/**
	 * 
	 */
	private void startTimerWidget() {
		if (null == this.timerWidget) {

			this.timerWidget = new Timer() {
				@Override
				public void run() {
					periodicallySendWidgetsToServer();
				}
			};
			this.currentWidgetRequestInterval = WIDGET_REQUEST_INTERVAL;
			timerWidget.schedule(this.currentWidgetRequestInterval);
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
