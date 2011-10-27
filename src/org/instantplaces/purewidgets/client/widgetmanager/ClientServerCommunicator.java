/**
 * 
 */
package org.instantplaces.purewidgets.client.widgetmanager;

import java.util.ArrayList;


import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.client.storage.LocalStorage;
import org.instantplaces.purewidgets.client.widgetmanager.json.ApplicationListJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetInputListJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetListJson;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator;
import org.instantplaces.purewidgets.shared.widgetmanager.ServerListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public class ClientServerCommunicator implements ServerCommunicator {
	
	
	private static final String INTERACTION_SERVER = "http://im-instantplaces.appspot.com";
		
	private static final String TIMESTAMP = "lastTimeStamp";
	
	/**
	 * The interval between input requests to the server varies between MIN_ASK_PERIOD
	 * and MAX_ASK_PERIOD, depending on the result from the last requests. If
	 * MAX_FAILURE_COUNT or more consecutive requests fail, the interval will be
	 * MAX_ASK_PERIOD otherwise it is a value interpolated between
	 * MIN_ASK_PERIOD and MAX_ASK_PERIOD.
	 * 
	 */
	private static final int MIN_ASK_PERIOD = 10000;
	
	/**
	 * The interval between input requests to the server varies between MIN_ASK_PERIOD
	 * and MAX_ASK_PERIOD, depending on the result from the last requests. If
	 * MAX_FAILURE_COUNT or more consecutive requests fail, the interval will be
	 * MAX_ASK_PERIOD otherwise it is a value interpolated between
	 * MIN_ASK_PERIOD and MAX_ASK_PERIOD.
	 */
	private static final int MAX_ASK_PERIOD = 100000;

	/**
	 * The interval between input requests to the server varies between MIN_ASK_PERIOD
	 * and MAX_ASK_PERIOD, depending on the result from the last requests. If
	 * MAX_FAILURE_COUNT or more consecutive requests fail, the interval will be
	 * MAX_ASK_PERIOD otherwise it is a value interpolated between
	 * MIN_ASK_PERIOD and MAX_ASK_PERIOD.
	 */
	private static final int MAX_FAILURE_COUNT = 5;
	

	/**
	 * The interval between widget requests to the server
	 */
	private static final int WIDGET_REQUEST_PERIOD = 10000;

	/**
	 * The placeId on which this ServerCommunicator will be used. 
	 */
	private  String placeId = "dsi";
	
	/**
	 * The applicationId on which this ServerCommunicator will be used.
	 */
	private  String appId = "jorge";
	
	/**
	 * The application URL constructed with the INTERACTION_SERVER, placeId and appId
	 */
	private  String applicationUrl;
	
	/**
	 * The current number of consecutive connection failures.
	 */
	private int failureCount;

	/**
	 * The current interval between connection attempts to the server.
	 */
	private int askPeriod;


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
	private ArrayList<Widget> toAddWidgetPool = new ArrayList<Widget>();

	
	/**
	 * List of widgets scheduled to be added to the server
	 */
	private ArrayList<Widget> toDeleteWidgetPool = new ArrayList<Widget>();
	
	
	/**
	 * Create a remote service proxy to talk to the server-side Presences
	 * service. 
	 */
	private final InteractionServiceAsync interactionService;


	/**
	 * Input requests are only made after we get a response for the previous request.
	 * This flag indicates whether we have received the response or not.
	 */
	private boolean receivedLastRequest = true;

	
	/**
	 * The ServerListener that will receive server events (i.e. the WidgetManager)
	 * 
	 */
	private ServerListener serverListener;
	
	
	public ClientServerCommunicator(String placeId, String appId) {
		this.placeId = placeId;
		this.appId = appId;
		this.applicationUrl = INTERACTION_SERVER + "/place/" + placeId + "/application/"+ appId;
		
		askPeriod = MIN_ASK_PERIOD;
		
		failureCount = 0;
		
		interactionService = GWT.create(InteractionService.class);
			
		timerInput = new Timer() {
			@Override
			public void run() {
				periodicallyAskForInputFromServer();
			}
		};
		timerInput.schedule(askPeriod);
		
		timerWidget = new Timer() {
			@Override
			public void run() {
				periodicallySendWidgetsToServer();
			}
		};
		timerWidget.schedule(WIDGET_REQUEST_PERIOD); 		
	}
	
	/**
	 * Enables or disables the automatic input requests
	 * @param automatic
	 */
	@Override
	public void setAutomaticInputRequests(boolean automatic) {
		if ( automatic ) {
			this.timerInput.schedule(askPeriod);
		} else {
			this.timerInput.cancel();
		}
	}

	
	private void periodicallySendWidgetsToServer() {
		//TODO: Send a list of widgets instead of one by one.
		if (this.toAddWidgetPool.size() > 0) {
			
			/*
			 * Take a widget from the top and put it at the end so that all of them will be processed
			 * The widget will be definitely removed from this pool after confirmation from the server.
			 */
			Widget w = this.toAddWidgetPool.remove(0);
			this.toAddWidgetPool.add(w);
			
			this.addWidgetToServer(w);
		}
		
		
		if (this.toDeleteWidgetPool.size() > 0) {
			/*
			 * Take a widget from the top and put it at the end so that all of them will be processed
			 * The widget will be definitely removed from this pool after confirmation from the server.
			 */			
			Widget w = this.toDeleteWidgetPool.remove(0);
			
			this.toDeleteWidgetPool.add(w);
			
			/*
			 * Only try to remove a widget if it has been already added on the server.
			 * This is only a precaution because applications should keep a widget on the interface
			 * long enough so that this will not happen (trying to delete a widget which hasn't has time
			 * to be added in the first place)...
			 */
			if ( !this.toAddWidgetPool.contains(w) ) {
				this.removeWidget(w);
			}
		}
		
		if ( this.toAddWidgetPool.size() > 0 || this.toDeleteWidgetPool.size() > 0 ) {
			timerWidget.schedule(WIDGET_REQUEST_PERIOD);
			Log.debug(this, "Scheduling next widget request in 15 seconds");
		}
	}
	
	/**
	 * Asks for input for every widget in the application.
	 */
	private void periodicallyAskForInputFromServer() {
		try {
			Log.debug(this, "Scheduling next application server connection in " + askPeriod + " ms.");
			timerInput.schedule(askPeriod);
			
			if ( !receivedLastRequest ) {
				Log.debug(this, "Haven't receive response to last request. Postponing new request.");
				return;
			}
			
			receivedLastRequest = false;
			
			
			String url = applicationUrl + "/input?output=json&from="+this.getLastTimeStampAsString()+"&appid="+appId;
			
			Log.debug(this, "Contacting application server for input..." + url);
			
			interactionService.getWidgetInput(url, new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					processInputResponse(false, null, caught);
				}

				@Override
				public void onSuccess(String result) {
					processInputResponse(true, result, null);
				}
			});
		} catch (Exception e) {
			processInputResponse(false, null, e);
			e.printStackTrace();
		}
	}
	

	/**
	 * Helper method that just calls the right method to handle the response in either 
	 * success or failure case.
	 * 
	 * @param success
	 * @param data
	 * @param error
	 */
	private void processInputResponse(boolean success, String data, Throwable error) {
		receivedLastRequest = true;
		updateAskInputPeriod();
		if ( success && null != data  ) {
			this.processInputSuccess(data);
			failureCount = 0;
		} else {
			this.processInputFailure(error);
			failureCount++;
		}
		
	}

	private void processInputSuccess(String result) {
		Log.debug("Received inputs " + result);
		try {

			WidgetInputListJson inputListJSON = GenericJson.fromJson(result);

			ArrayList<WidgetInput> inputList = inputListJSON.getInputs();
			
			/*
			 * Update our most recent input timeStamp so that in the next round we ask only
			 * for newer input
			 */
			for (WidgetInput widgetInput : inputList ) {
				/*
				 * Save the new timeStamp locally
				 */
				if (toLong(widgetInput.getTimeStamp()) > this.getLastTimeStampAsLong()) {
					this.setTimeStamp(toLong(widgetInput.getTimeStamp()));
				}
			}
			
			/*
			 * Notify the widgetManager
			 */
			if (this.serverListener != null) {
				this.serverListener.onWidgetInput(inputList);
			}

			
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void processInputFailure(Throwable caught) {
		Log.warn(this, "Problem receiving input from application server. Will retry... " + (caught != null ? caught.getMessage() : "") );
	}
	
	
	/**
	 * Interpolates the time between requests.
	 */
	private void updateAskInputPeriod() {
		this.askPeriod = map(0, ClientServerCommunicator.MAX_FAILURE_COUNT,
				ClientServerCommunicator.MIN_ASK_PERIOD,
				ClientServerCommunicator.MAX_ASK_PERIOD, this.failureCount);
	}

	/**
	 * Helper method that maps a value in a source range to a value in a target
	 * range. The resulting value is truncated at the target's range limits.
	 * 
	 * @param sLow
	 *            The source range's lower limit.
	 * @param sHigh
	 *            The source range's higher limit.
	 * @param tLow
	 *            The target range's lower limit.
	 * @param tHigh
	 *            The target range's higher limit.
	 * @param sValue
	 *            The value (in source range) to map.
	 * @return The mapped value (in the target range) truncated at the range's
	 *         limits.
	 */
	private int map(int sLow, int sHigh, int tLow, int tHigh, int sValue) {
		int sRange = sHigh - sLow;
		int tRange = tHigh - tLow;

		int x = sValue * tRange / sRange;
		int r = tLow + x;
		if (r < tLow)
			r = tLow;
		if (r > tHigh)
			r = tHigh;
		return r;

	}



	
	private void addWidgetToServer(Widget widget) {
		
		WidgetJson widgetJSON = WidgetJson.create(widget);
		widgetJSON.setApplicationId(appId);
		widgetJSON.setPlaceId(placeId);
		
		WidgetListJson widgetListJson = GenericJson.getNew();
		ArrayList<WidgetJson> widgets = new ArrayList<WidgetJson>();
		widgets.add(widgetJSON);
		widgetListJson.setWidgetsFromArrayList(widgets);
		
		Log.debug("Adding " + widgetListJson.toJsonString() + " to server");

		try {
			this.interactionService.postWidget(widgetListJson.toJsonString(),
					this.getWidgetsUrl(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processWidgetAddResponse(false, null, caught);
							
						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processWidgetAddResponse(true, result, null);
						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processWidgetAddResponse(false, null, e);
			e.printStackTrace();
		}
	}

	private void processWidgetAddResponse(boolean success, String result, Throwable error) {
		if ( success ) {
			this.processWidgetAddSuccess(result);
		} else {
			this.processWidgetAddFailure(error);
		}
	}

	/**
	 * This method is called when the server replies to a widget post.
	 * This method goes through the existing widgets and
	 * checks if any reference code changed. If so, it notifies the widget. 
	 * 
	 * 
	 * @param json
	 */
	private void processWidgetAddSuccess(String json) {
		Log.debug(this, "Received widget from server: " + json);

		WidgetListJson widgetListJson = GenericJson.fromJson(json);
		ArrayList<Widget> widgetList = widgetListJson.getWidgets();
		
		for (Widget widget : widgetList) {
			
			/*
			 * Remove widget from pool 
			 */
			Widget toRemove = null;
			for (Widget widgetFromPool: this.toAddWidgetPool) {
				if(widgetFromPool.getWidgetId().equals(widget.getWidgetId())) {
					toRemove = widgetFromPool;
					break;
				}
			}
			if (null != toRemove) {
				Log.debug(this, "Removing widget from toAdd pool.");
				this.toAddWidgetPool.remove(toRemove);
			} else {
				Log.warn(this, "Widget not found in toAdd pool!");
			}
			
			/*
			 * Notify the WidgetManager
			 * 
			 */
			if (this.serverListener != null) {
				Log.debug(this, "Notifying WidgetManager of: " + widget);
				this.serverListener.onWidgetAdd(widget);
			}
			
		}
	}	
	
	
	private void processWidgetAddFailure(Throwable error) {
		Log.warn("Error adding widget to server."
				+ error.getMessage());
	}
	
	
	
	
	/**
	 * Removes a previously added Widget.
	 * 
	 * @param widget The Widget to remove.
	 */
	private  void removeWidget(Widget widget) {
		Log.debug("Removing widget: " + widget.toDebugString());
		//im.widgetList.remove(widget);

		/*WidgetJson widgetJSON = WidgetJson.create(widget);
		widgetJSON.setApplicationId(appId);
		widgetJSON.setPlaceId(placeId);
*/
		
		try {
			interactionService.deleteWidget(
					this.getWidgetsUrl(widget),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processWidgetDeleteResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processWidgetDeleteResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processWidgetDeleteResponse(false, null, e);
			e.printStackTrace();
		}
	}

	private void processWidgetDeleteResponse(boolean success, String result, Throwable error) {
		if ( success ) {
			this.processWidgetDeleteSuccess(result);
		} else {
			this.processWidgetDeleteFailure(error);
		}
	}
	
	private void processWidgetDeleteSuccess(String json) {
		Log.debug(this, "Widget deleted: " + json);

		WidgetListJson widgetListJson = GenericJson.fromJson(json);
		ArrayList<Widget> widgetList = widgetListJson.getWidgets();
		
		for (Widget widget : widgetList) {
		
			/*
			 * Remove widget from pool 
			 */
			Widget toRemove = null;
			for (Widget widgetFromPool: this.toDeleteWidgetPool) {
				if(widgetFromPool.getWidgetId().equals(widget.getWidgetId())) {
					toRemove = widgetFromPool;
					break;
				}
			}
			if (null != toRemove) {
				Log.debug(this, "Removing widget from toDelete pool.");
				this.toDeleteWidgetPool.remove(toRemove);
			} else {
				Log.warn(this, "Widget not found in toDelete pool!");
			}
			
			/*
			 * Notify the WidgetManager
			 * 
			 */
			if (this.serverListener != null) {
				Log.debug(this, "Notifying WidgetManager of: " + widget);
				this.serverListener.onWidgetDelete(widget);
			}
			
		}
	}
	
	private void processWidgetDeleteFailure(Throwable error) {
		Log.warn("Error deleting widget to server."
				+ error.getMessage());
	}
	
	private void setTimeStamp(long timeStamp) {
		LocalStorage.setString(TIMESTAMP, ""+timeStamp);
	}
	
	private String getLastTimeStampAsString() {
		return LocalStorage.getString(TIMESTAMP);
	}
	
	private long toLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return 0;
	}
	
	private long getLastTimeStampAsLong() {
		try {
			return Long.parseLong(getLastTimeStampAsString());
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#addWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	@Override
	public void addWidget(Widget widget) {
		this.toAddWidgetPool.add(widget);
		this.timerWidget.schedule(1000);
	}

	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#deleteWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	@Override
	public void deleteWidget(Widget widget) {
		//this.removeWidget(widget);
		this.toDeleteWidgetPool.add(widget);
		this.timerWidget.schedule(1000);

	}

	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#setServerListener(org.instantplaces.purewidgets.shared.widgetmanager.ServerListener)
	 */
	@Override
	public void setServerListener(ServerListener listener) {
		this.serverListener = listener;

	}
	
	private String getWidgetsUrl() {
		return ClientServerCommunicator.INTERACTION_SERVER + "/place/" + this.placeId + "/application/" + this.appId + "/widget?output=json&appid=" +this.appId ;
	}	
	
	private String getWidgetsUrl(Widget widget) {
		return ClientServerCommunicator.INTERACTION_SERVER + "/place/" + this.placeId + "/application/" + this.appId + "/widget/" + widget.getWidgetId() + "?output=json&appid=" +this.appId ;
	}
	
	private String getApplicationsUrl() {
		return ClientServerCommunicator.INTERACTION_SERVER + "/place/" + this.placeId + "/application?output=json&appid=" + this.appId;
	}


	@Override
	public void getPlaceApplicationsList(boolean active) {
		Log.debug( this, "Getting applications from server: " + getApplicationsUrl()+"&active=" + (active?"true":"false") );
		try {
			interactionService.getApplicationsFromPlace(getApplicationsUrl()+"&active=" + (active?"true":"false"), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processApplicationsResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processApplicationsResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processWidgetDeleteResponse(false, null, e);
			e.printStackTrace();
		}
	}	
	
	@Override
	public void getPlaceApplicationsList() {
		Log.debug( this, "Getting applications from server: " + getApplicationsUrl() );
		try {
			interactionService.getApplicationsFromPlace(getApplicationsUrl(), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processApplicationsResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processApplicationsResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processWidgetDeleteResponse(false, null, e);
			e.printStackTrace();
		}
	}	
	
	private void processApplicationsResponse(boolean success, String result, Throwable error) {
		if ( success ) {
			this.processApplicationsSuccess(result);
		} else {
			this.processApplicationsFailure(error);
		}
	}
	
	private void processApplicationsSuccess(String json) {
		Log.debug(this, "Applications list: " + json);

		
		
		
		ApplicationListJson applicationListJson = GenericJson.fromJson(json);
		
		/*JsArray<ApplicationJson> a = applicationListJson.getApplicationsAsJsArray(); 
		
		Log.debug(this, "Total applications: " + a.length());
		*/
		
		ArrayList<Application> applicationList = applicationListJson.getApplications();
		
		
			
		/*
		 * Notify the WidgetManager
		 * 
		 */
		if (this.serverListener != null) {
			Log.debug(this, "Notifying WidgetManager of list of applications. ");
			this.serverListener.onPlaceApplicationsList(applicationList);
		}
			
		
	}
	
	private void processApplicationsFailure(Throwable error) {
		Log.warn("Error getting list of applications from server."
				+ error.getMessage());
	}
	
}
