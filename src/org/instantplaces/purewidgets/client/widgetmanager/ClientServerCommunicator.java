/**
 * 
 */
package org.instantplaces.purewidgets.client.widgetmanager;

import java.util.ArrayList;


import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.json.GenericJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.ApplicationJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.ApplicationListJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.PlaceListJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetInputJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetInputListJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetJson;
import org.instantplaces.purewidgets.client.widgetmanager.json.WidgetListJson;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.Callback;
import org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator;
import org.instantplaces.purewidgets.shared.widgetmanager.ServerListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetInput;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public class ClientServerCommunicator implements ServerCommunicator {

		
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
	 * The interval between input requests to the server varies between MIN_ASK_PERIOD
	 * and MAX_ASK_PERIOD, depending on the result from the last requests. If
	 * MAX_FAILURE_COUNT or more consecutive requests fail, the interval will be
	 * MAX_ASK_PERIOD otherwise it is a value interpolated between
	 * MIN_ASK_PERIOD and MAX_ASK_PERIOD.
	 * 
	 */
	private static final int MIN_ASK_PERIOD = 15000;

	private static final String TIMESTAMP = "lastTimeStamp";
	

	/**
	 * The interval between widget requests to the server
	 */
	private static final int WIDGET_REQUEST_PERIOD = 15000;

	
	private static enum NextWidgetAction {ADD, DELETE};
	
	/**
	 * The applicationId on which this ServerCommunicator will be used.
	 */
	private  String appId = "jorge";
	
	/**
	 * The application URL constructed with the INTERACTION_SERVER, placeId and appId
	 */
	private  String applicationUrl;
	
	/**
	 * The current interval between connection attempts to the server.
	 */
	private int askPeriod;
	
	/**
	 * The current number of consecutive connection failures.
	 */
	private int failureCount;

	/**
	 * Create a remote service proxy to talk to the server-side Presences
	 * service. 
	 */
	private final InteractionServiceAsync interactionService;


	/**
	 * The placeId on which this ServerCommunicator will be used. 
	 */
	private  String placeId = "dsi";
	
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

	
	/**
	 * Timer for scheduling input requests to the server.
	 */
	private Timer timerInput;
	
	
	/**
	 * Timer for scheduling widget requests to the server.
	 */
	private Timer timerWidget;

	private NextWidgetAction nextWidgetAction;
	
	/**
	 * List of widgets scheduled to be added to the server
	 */
	private ArrayList<Widget> toAddWidgetPool = new ArrayList<Widget>();

	
	/**
	 * List of widgets scheduled to be added to the server
	 */
	private ArrayList<Widget> toDeleteWidgetPool = new ArrayList<Widget>();
	
	/**
	 * The current widget request interval
	 */
	private int currentWidgetRequestInterval;
	
	
	
	public ClientServerCommunicator(String placeId, String appId) {
		this.placeId = placeId;
		this.appId = appId;
		this.applicationUrl = WidgetManager.getApplicationUrl(placeId, appId);
		this.currentWidgetRequestInterval = WIDGET_REQUEST_PERIOD;
		
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
		
		
	}
	
	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#addWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	@Override
	public void addWidget(Widget widget) {
		int index = indexOf(widget);
		if (-1 == index) {
			this.toAddWidgetPool.add(widget);
		} else {
			Log.warn(this, "Widget already exists, replacing entry " + index + ".");
			
			this.toAddWidgetPool.set(index, widget);
		}
		
		this.nextWidgetAction = NextWidgetAction.ADD;
		
		startTimerWidget();

	}
	
	private int indexOf(Widget widget) {
		for (Widget w : this.toAddWidgetPool) {
			if (w.getWidgetId().equals(widget.getWidgetId())) {
				return this.toAddWidgetPool.indexOf(w);
			}
		}
		return -1;
	}

	
	@Override
	public void deleteAllWidgets(boolean volatileOnly) {
			Log.debug(this, "Removing all volative widgets " );
		
		
		try {
			interactionService.delete(
					WidgetManager.getWidgetsUrl(this.placeId, this.appId, this.appId) + (volatileOnly ? "&volatileonly=true" : ""),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processAllWidgetDeleteResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processAllWidgetDeleteResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processAllWidgetDeleteResponse(false, null, e);
			e.printStackTrace();
		}
	}
	
	private void processAllWidgetDeleteFailure(Throwable error) {
		Log.warn(this, "Error deleting all widgets from server."
				+ error.getMessage());
	}


	private void processAllWidgetDeleteResponse(boolean success, String result, Throwable error) {
		if ( success ) {
			Log.debugFinest(this, result);
			this.processAllWidgetDeleteSuccess(result);
		} else {
			this.processAllWidgetDeleteFailure(error);
		}
	}	
	
	private void processAllWidgetDeleteSuccess(String json) {
		if ( LogConfiguration.loggingIsEnabled() ) {
			WidgetListJson widgetListJson = WidgetListJson.fromJson(json);
			ArrayList<Widget> widgetList = widgetListJson.getWidgets();
			
			StringBuilder sb = new StringBuilder();
			for ( Widget w : widgetList ) {
				sb.append(w.getWidgetId()).append(" ");
			}
			Log.debug(this, "Widgets deleted: " + sb.toString());
		}
	}		
		
	
	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#deleteWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
	@Override
	public void deleteWidget(Widget widget) {
		//this.removeWidget(widget);
		this.toDeleteWidgetPool.add(widget);
		
		/*
		 * It may happen that the app deletes a widget which has not yet been removed from the toAdd pool
		 * if the app deletes a widget that only recently was added...
		 */
		if ( this.toAddWidgetPool.contains(widget) ) {
			this.toAddWidgetPool.remove(widget);
		}
		
		this.nextWidgetAction = NextWidgetAction.DELETE;
		
		startTimerWidget();

	}

	/**
	 * 
	 */
	private void startTimerWidget() {
		if ( null == this.timerWidget ) {
			
			this.timerWidget = new Timer() {
				@Override
				public void run() {
					periodicallySendWidgetsToServer();
				}
			};
			timerWidget.schedule(1000); 		
			
		}
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
	
	
	/* (non-Javadoc)
	 * @see org.instantplaces.purewidgets.shared.widgetmanager.ServerCommunicator#setServerListener(org.instantplaces.purewidgets.shared.widgetmanager.ServerListener)
	 */
	@Override
	public void setServerListener(ServerListener listener) {
		this.serverListener = listener;

	}

	private void addWidgetToServer(ArrayList<Widget> widgets) {
		Log.debug(this, "Adding " + widgets.size() + " widgets to server");
		
		ArrayList<WidgetJson> widgetJsonList = new ArrayList<WidgetJson>();
		
		for ( Widget widget : widgets ) {
			WidgetJson widgetJSON = WidgetJson.create(widget);
			widgetJSON.setApplicationId(appId);
			widgetJSON.setPlaceId(placeId);
			
			widgetJsonList.add(widgetJSON);
		}
		
		
		
		WidgetListJson widgetListJson = GenericJson.getNew();
		//ArrayList<WidgetJson> widgets = new ArrayList<WidgetJson>();
		//widgets.add(widgetJSON);
		widgetListJson.setWidgetsFromArrayList(widgetJsonList);
		
		
		Log.debugFinest(this, "Sending " + widgetListJson.toJsonString() + " to server");

		try {
			this.interactionService.post(widgetListJson.toJsonString(),
					WidgetManager.getWidgetsUrl(this.placeId, this.appId, this.appId),
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



	

	private long getLastTimeStampAsLong() {
		try {
			return Long.parseLong(getLastTimeStampAsString());
		} catch (Exception e) {
			Log.error(this, "Could no parse timestamp: " + e.getMessage());
		}
		return 0;
	}

	private String getLastTimeStampAsString() {
		String ts = PublicDisplayApplication.getLocalStorage().getString(TIMESTAMP);
		
		if (ts == null || ts.length() < 1) {
			return "0";
		} else {
			return ts;
		}
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
	
	/**
	 * Asks for input for every widget in the application.
	 */
	private void periodicallyAskForInputFromServer() {
		try {
			Log.debugFinest(this, "Scheduling next application server connection in " + askPeriod + " ms.");
			timerInput.schedule( askPeriod );
			
			if ( !receivedLastRequest ) {
				Log.debug(this, "Haven't receive response to last request. Postponing new request.");
				
				// we just skip one, so mark as if we received the request so that in the next time we will ask again
				receivedLastRequest = true;
				return;
			}
			
			receivedLastRequest = false;
			
			String url = applicationUrl + "/input?from="+this.getLastTimeStampAsString()+"&appid="+appId;
			
			Log.debugFinest(this, "Contacting application server for input..." + url);
			
			interactionService.get(url, new AsyncCallback<String>() {
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
	
	private void periodicallySendWidgetsToServer() {
		//TODO: Send a list of widgets instead of one by one.
		
		
		if ( this.toAddWidgetPool.size() == 0 ) {
			this.nextWidgetAction = NextWidgetAction.DELETE;
		} 
		if ( this.toDeleteWidgetPool.size() == 0 ) {
			this.nextWidgetAction = NextWidgetAction.ADD;
		}
		
		
		if ( NextWidgetAction.ADD == this.nextWidgetAction ) {
		
			if (this.toAddWidgetPool.size() > 0) {
				
				/*
				 * Send the current set of widgets to the server.
				 */
				this.addWidgetToServer(this.toAddWidgetPool);
				
			} 
		}
		
		if ( NextWidgetAction.DELETE == this.nextWidgetAction ) {
			if (this.toDeleteWidgetPool.size() > 0) {
				
				
				this.removeWidget(this.toDeleteWidgetPool);
				
			}
		}
		
		/*
		 * Invert the next action
		 */
		if ( NextWidgetAction.ADD == this.nextWidgetAction ) {
			this.nextWidgetAction = NextWidgetAction.DELETE;
		} else if ( NextWidgetAction.DELETE == this.nextWidgetAction ) {
			this.nextWidgetAction = NextWidgetAction.ADD;
		}
		
		if ( this.toAddWidgetPool.size() > 0 || this.toDeleteWidgetPool.size() > 0 ) {
			timerWidget.schedule(this.currentWidgetRequestInterval);
			Log.debugFinest(this, "Scheduling next widget request in " + (this.currentWidgetRequestInterval/1000) +" seconds");
		} else {
			this.timerWidget = null;
		}
	}
	
	
	
	private void processInputFailure(Throwable caught) {
		Log.warn(this, "Problem receiving input from application server. Will retry... " + (caught != null ? caught.getMessage() : "") );
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
			Log.debugFinest(this, data);
			this.processInputSuccess(data);
			failureCount = 0;
		} else {
			this.processInputFailure(error);
			failureCount++;
		}
		
	}

	private void processInputSuccess(String result) {
		
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
			Log.error(this, e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void processWidgetAddFailure(Throwable error) {
		Log.warn(this, "Error adding widget to server."
				+ error.getMessage());
		this.currentWidgetRequestInterval *= 2;
		Log.warn(this, "Increasing request interval to " + this.currentWidgetRequestInterval);
	}
	
	private void processWidgetAddResponse(boolean success, String result, Throwable error) {
		//Log.info(result);
		if ( success ) {
			Log.debugFinest(this, result);
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
		

		this.currentWidgetRequestInterval = WIDGET_REQUEST_PERIOD;
		
		WidgetListJson widgetListJson = GenericJson.fromJson(json);
		ArrayList<Widget> widgetList = widgetListJson.getWidgets();
		
		if ( LogConfiguration.loggingIsEnabled() ) {
			if ( null != widgetList ) {
				Log.debug(this, "Received " + widgetList.size()+ " widgets from server.");
			} else {
				Log.warn(this, "Could not extract any widget from server response. " + json);
			}
		}
		
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
				Log.debug(this, "Removing widget " + toRemove.getWidgetId() + " from toAdd pool.");
				this.toAddWidgetPool.remove(toRemove);
			} else {
				Log.warn(this, "Widget not found in toAdd pool!");
			}
			
			/*
			 * Notify the WidgetManager
			 * 
			 */
			if (this.serverListener != null) {
				this.serverListener.onWidgetAdd(widget);
			} else {
				Log.warn(this, "No widget manager to notify about widget addition.");
			}
			
		}
	}
	
	private void processWidgetDeleteFailure(Throwable error) {
		Log.warn(this, "Error deleting widget from server."
				+ error.getMessage());
		
		this.currentWidgetRequestInterval *= 2;
		Log.warn(this, "Increasing request interval to " + this.currentWidgetRequestInterval);
	}


	private void processWidgetDeleteResponse(boolean success, String result, Throwable error) {
		if ( success ) {
			Log.debugFinest(this, result);
			this.processWidgetDeleteSuccess(result);
		} else {
			this.processWidgetDeleteFailure(error);
		}
	}	
	
	private void processWidgetDeleteSuccess(String json) {
		
		this.currentWidgetRequestInterval = WIDGET_REQUEST_PERIOD;
		
		WidgetListJson widgetListJson = GenericJson.fromJson(json);
		ArrayList<Widget> widgetList = widgetListJson.getWidgets();
		
		if ( LogConfiguration.loggingIsEnabled() ) {
			if ( null != widgetList ) {
				Log.debug(this, "Received " + widgetList.size()+ " widgets deleted from server.");
			} else {
				Log.warn(this, "Could not extract any widget from server response. " + json);
			}
		
		}
		
		for (Widget widget : widgetList) {
			Log.debug(this, "Widget " + widget.getWidgetId() + "deleted from server ");
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
				
				this.serverListener.onWidgetDelete(widget);
			} else {
				Log.warn(this, "No widget manager to notify about widget deletion.");
			}
			
		}
	}	
	
	public String getWidgetIdUrlEscaped(Widget widget) {
		return com.google.gwt.http.client.URL.encode(widget.getWidgetId());
	}
	
	public String getWidgetIdUrlEscaped(String widgetId) {
		return com.google.gwt.http.client.URL.encode(widgetId);
	}
	
	/**
	 * Removes a previously added Widget.
	 * 
	 * @param widget The Widget to remove.
	 */
	private  void removeWidget(ArrayList<Widget> widgets) {
		Log.debug(this, "Removing " + widgets.size() + " widgets ");
		
		/*
		 * Create the URL for the DELETE method. Widget ids are passed on the 
		 * 'widget' url parameter
		 */
		StringBuilder widgetsUrlParam = new StringBuilder();
		widgetsUrlParam.append( WidgetManager.getWidgetsUrl(this.placeId, this.appId, this.appId) ).append("&widgets=");
		
		for ( int i = 0; i < widgets.size(); i++) {
			Widget w = widgets.get(i); 
			
			/*
			 * Make sure we don't use Urls with more than 255 characters...
			 */
			if ( (widgetsUrlParam.length() + this.getWidgetIdUrlEscaped(w).length()) > 255 ) {
				widgetsUrlParam.deleteCharAt(widgetsUrlParam.length()-1);
				break;
			}
			widgetsUrlParam.append(this.getWidgetIdUrlEscaped(w));
			
			if ( i < (widgets.size()-1) ) {
				widgetsUrlParam.append(",");
			}
		}
		
		try {
			interactionService.delete(
					widgetsUrlParam.toString(),
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
	
	private void setTimeStamp(long timeStamp) {
		PublicDisplayApplication.getLocalStorage().setString(TIMESTAMP, ""+timeStamp);
	}
	
	private long toLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			Log.error(this, e.getMessage());
		}
		return 0;
	}

	/**
	 * Interpolates the time between requests.
	 */
	private void updateAskInputPeriod() {
		this.askPeriod = map(0, ClientServerCommunicator.MAX_FAILURE_COUNT,
				ClientServerCommunicator.MIN_ASK_PERIOD,
				ClientServerCommunicator.MAX_ASK_PERIOD, this.failureCount);
	}


	
	@Override
	public void getWidgetsList(String placeId, String applicationId) {
		Log.debug( this, "Getting widgets from server: " +  WidgetManager.getWidgetsUrl(placeId,  applicationId, this.appId) );
		try {
			interactionService.get( WidgetManager.getWidgetsUrl(placeId,  applicationId, this.appId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processWidgetsResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processWidgetsResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processWidgetsResponse(false, null, e);
			e.printStackTrace();
		}
		
	}
	

	private void processWidgetsFailure(Throwable error) {
		Log.warn(this, "Error getting list of widgets from server."
				+ error.getMessage());
	}
	
	private void processWidgetsResponse(boolean success, String result, Throwable error) {
		
		if ( success ) {
			Log.debugFinest(this, result);
			this.processWidgetsSuccess(result);
		} else {
			this.processWidgetsFailure(error);
		}
	}
	
	private void processWidgetsSuccess(String json) {
		
		WidgetListJson widgetListJson = GenericJson.fromJson(json);
		
		ArrayList<Widget> widgetList = widgetListJson.getWidgets();
			
		/*
		 * Notify the WidgetManager
		 * 
		 */
		if (this.serverListener != null) {
			this.serverListener.onWidgetsList(widgetListJson.getPlaceId(), widgetListJson.getApplicationId(), widgetList);
		} else {
			Log.warn(this, "No widget manager to notify about application list");
		}
			
		
	}


	@Override
	public void getPlacesList() {
		Log.debug( this, "Getting places from server: " +  WidgetManager.getPlacesUrl(this.appId) );
		try {
			interactionService.get( WidgetManager.getPlacesUrl(this.appId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							ClientServerCommunicator.this.processPlacesResponse(
									false, null, caught);

						}

						@Override
						public void onSuccess(String result) {
							ClientServerCommunicator.this.processPlacesResponse(
									true, result, null);

						}

					});
		} catch (Exception e) {
			ClientServerCommunicator.this.processPlacesResponse(false, null, e);
			e.printStackTrace();
		}
		
	}
	

	private void processPlacesFailure(Throwable error) {
		Log.warn(this, "Error getting list of places from server."
				+ error.getMessage());
	}
	
	private void processPlacesResponse(boolean success, String result, Throwable error) {
		
		if ( success ) {
			Log.debugFinest(this, result);
			this.processPlacesSuccess(result);
		} else {
			this.processPlacesFailure(error);
		}
	}
	
	private void processPlacesSuccess(String json) {
		
		PlaceListJson placeListJson = GenericJson.fromJson(json);
		
		ArrayList<Place> placeList = placeListJson.getPlaces();
			
		/*
		 * Notify the WidgetManager
		 * 
		 */
		if (this.serverListener != null) {
			this.serverListener.onPlacesList(placeList);
		} else {
			Log.warn(this, "No widget manager to notify about place list");
		}
			
		
	}

	@Override
	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, final Callback<WidgetInput> callback) {
		WidgetInputJson widgetInputJson = WidgetInputJson.create(widgetInput);
		
		Log.debug(this, "Sending widget input: " + widgetInputJson.toJsonString());
		Log.debug(this, "to: "+ WidgetManager.getWidgetInputUrl(placeName, applicationName, getWidgetIdUrlEscaped(widgetInput.getWidgetId()), this.appId));
		
		this.interactionService.post(widgetInputJson.toJsonString(), 
				WidgetManager.getWidgetInputUrl(placeName, applicationName, getWidgetIdUrlEscaped(widgetInput.getWidgetId()), this.appId), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(this, "Error posting input. " + caught.getMessage());
						if ( null != callback ) {
							callback.onFailure(caught);
						}
					}

					@Override
					public void onSuccess(String result) {
						Log.debug(this, "Successfully posted input. " + result);
						if ( null != callback ) {
							callback.onSuccess(null);
						}
					}

		});
		
	}

	@Override
	public void setApplication(String placeId, String applicationId, Application application, final Callback<Application> callback) {
		ApplicationJson applicationJson = ApplicationJson.create(application);
		
		Log.debug(this, "Sending application: " + applicationJson.toJsonString());
		Log.debug(this, "to: "+ WidgetManager.getApplicationUrl(placeId, applicationId, this.appId));
		
		this.interactionService.post(applicationJson.toJsonString(), 
				WidgetManager.getApplicationUrl(placeId, applicationId, this.appId), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(this, "Error posting application. " + caught.getMessage());
						if ( null != callback ) {
							callback.onFailure(caught);
						}
					}

					@Override
					public void onSuccess(String result) {
						Log.debug(this, "Successfully posted application. " + result);
						if ( null != callback ) {
							Application application = (Application)(ApplicationJson.fromJson(result));
							callback.onSuccess(application);
						}
					}

		});
	}

	@Override
	public void getApplication(String placeId, String applicationId, final Callback<Application> callback) {
		
		
		Log.debug(this, "Asking application: " + placeId +":"+applicationId);
		
		
		this.interactionService.get(
				WidgetManager.getApplicationUrl(placeId, applicationId, this.appId), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(this, "Error asking for  application. " + caught.getMessage());
						if ( null != callback ) {
							callback.onFailure(caught);
						}
					}

					@Override
					public void onSuccess(String result) {
						Log.debug(this, "Received data " + result + ".");
						if ( null != callback ) {
							if ( null != result && result.length() > 0) {
								Log.debug(this, "Received application " + result + ".");
								Application application = ((ApplicationJson)ApplicationJson.fromJson(result)).getApplication();
								callback.onSuccess(application);
							} else {
								callback.onFailure(new Exception("Received null application"));
							}
						}
					}

		});
		
	}

	
	@Override
	public void getApplicationsList(String placeId, final Callback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, Application.STATE.All, callback);
	}

	@Override
	public void getApplicationsList(String placeId, boolean active,
			Callback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, active?Application.STATE.Active : Application.STATE.Inactive, callback);
		
	}
	
	private void getApplicationsList(String placeId, Application.STATE state,
			final Callback<ArrayList<Application>> callback) {
		
		/*
		 * Create the request url with the proper parameters based on the intended application state
		 */
		String url = WidgetManager.getApplicationsUrl(placeId, this.placeId);
		switch (state) {
		case All: 
			break;
		case Active:
			url += "&active=true";
			break;
		case Inactive:
			url += "&active=false";
		}
		
		Log.debug( this, "Getting applications from server: " + url );
		try {
			interactionService.get(url, 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "Error getting list of applications from server."
										+ caught.getMessage());
							}

						}

						@Override
						public void onSuccess(String json) {
							Log.debug(this, "Received applications list: " + json );
							
							ApplicationListJson applicationListJson = GenericJson.fromJson(json);
					
							ArrayList<Application> applicationList = applicationListJson.getApplications();
							
							if ( null != callback ) {
								callback.onSuccess(applicationList);
							} else {
								Log.warn(this, "No callback to notify about application list");
							}

						}

					});
		} catch (Exception e) {
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "Error getting list of applications from server."
						+ e.getMessage());
			}
			e.printStackTrace();
		}
	}

	
}
