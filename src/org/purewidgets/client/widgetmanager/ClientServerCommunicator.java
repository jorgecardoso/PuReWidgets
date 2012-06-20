/**
 * 
 */
package org.purewidgets.client.widgetmanager;

import java.util.ArrayList;


import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.client.json.GenericJson;
import org.purewidgets.client.widgetmanager.json.ApplicationJson;
import org.purewidgets.client.widgetmanager.json.ApplicationListJson;
import org.purewidgets.client.widgetmanager.json.ChannelTokenJson;
import org.purewidgets.client.widgetmanager.json.PlaceListJson;
import org.purewidgets.client.widgetmanager.json.WidgetInputJson;
import org.purewidgets.client.widgetmanager.json.WidgetInputListJson;
import org.purewidgets.client.widgetmanager.json.WidgetJson;
import org.purewidgets.client.widgetmanager.json.WidgetListJson;
import org.purewidgets.shared.Log;
import org.purewidgets.shared.widgetmanager.Callback;
import org.purewidgets.shared.widgetmanager.ServerListener;
import org.purewidgets.shared.widgetmanager.WidgetInput;
import org.purewidgets.shared.widgets.Application;
import org.purewidgets.shared.widgets.Place;
import org.purewidgets.shared.widgets.Widget;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public class ClientServerCommunicator {

		
	//"http://localhost:8080";//
	private static final String DEFAULT_INTERACTION_SERVER_URL = "http://pw-interactionmanager.appspot.com";
	
	private  String interactionServerUrl;
	
	
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
	
	/**
	 * Timer used for channel connection retries.
	 */
	private Timer timerChannel;

	/**
	 * The delay for the timer channel.
	 */
	private int timerChannelPeriod = DEFAULT_TIMER_CHANNEL_PERIOD;
	
	private static final int DEFAULT_TIMER_CHANNEL_PERIOD = 5000;
	
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
	
	/**
	 * Keeps a list of the most recently processed input.
	 * Used to make sure we don't trigger the same input event more than once.
	 */
	private ArrayList<WidgetInput> processedInput = new ArrayList<WidgetInput>();

	protected boolean channelOpen;
	
	public ClientServerCommunicator(String placeId, String appId) {
		this.placeId = placeId;
		this.appId = appId;
		
		this.currentWidgetRequestInterval = WIDGET_REQUEST_PERIOD;
		this.interactionServerUrl = DEFAULT_INTERACTION_SERVER_URL;
		this.applicationUrl = this.getApplicationUrl(placeId, appId);
		
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
		
		timerChannel = new Timer() {

			@Override
			public void run() {
				createChannel();
			}
			
		};
		this.createChannel();
	}
	

	public  String getServerUrl() {
		return interactionServerUrl;
	}
	
	public  String getApplicationsUrl(String placeId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application?appid=" +callingApplicationId ;
	}

	public  String getApplicationUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId +"?appid="+callingApplicationId;
	}
	
	public  String getApplicationUrl(String placeId, String applicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/"+ applicationId;
	}
	
	public  String getPlacesUrl(String callingApplicationId) {
		return interactionServerUrl + "/place?appid=" + callingApplicationId ;
	}

	public  String getWidgetsUrl(String placeId, String applicationId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget?appid=" +callingApplicationId ;
	}
	public  String getWidgetInputUrl(String placeId, String applicationId, String widgetId, String callingApplicationId) {
		return interactionServerUrl + "/place/" + placeId + "/application/" + applicationId + "/widget/" + widgetId +  "/input?appid=" + callingApplicationId ;
	}

	/* (non-Javadoc)
	 * @see org.purewidgets.shared.widgetmanager.ServerCommunicator#addWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
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

	
	public void deleteAllWidgets(boolean volatileOnly) {
			Log.debug(this, "Removing all volative widgets " );
		
		
		try {
			interactionService.delete(
					this.getWidgetsUrl(this.placeId, this.appId, this.appId) + (volatileOnly ? "&volatileonly=true" : ""),
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
			WidgetListJson widgetListJson = GenericJson.fromJson(json);
			ArrayList<Widget> widgetList = widgetListJson.getWidgets();
			
			StringBuilder sb = new StringBuilder();
			for ( Widget w : widgetList ) {
				sb.append(w.getWidgetId()).append(" ");
			}
			Log.debug(this, "Widgets deleted: " + sb.toString());
		}
	}		
		
	
	/* (non-Javadoc)
	 * @see org.purewidgets.shared.widgetmanager.ServerCommunicator#deleteWidget(org.instantplaces.purewidgets.shared.widgets.WidgetInterface)
	 */
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
	public void setAutomaticInputRequests(boolean automatic) {
		if ( automatic ) {
			this.timerInput.schedule(askPeriod);
		} else {
			this.timerInput.cancel();
		}
	}
	
	private void stopInputTimer() {
		this.timerInput.cancel();
	}
	
	private void startInputTimer() {
		this.timerInput.schedule(askPeriod);
	}
	
	
	/* (non-Javadoc)
	 * @see org.purewidgets.shared.widgetmanager.ServerCommunicator#setServerListener(org.instantplaces.purewidgets.shared.widgetmanager.ServerListener)
	 */
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
					this.getWidgetsUrl(this.placeId, this.appId, this.appId),
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
		if ( this.channelOpen ) {
			this.stopInputTimer();
		}
		try {

			WidgetInputListJson inputListJSON = GenericJson.fromJson(result);

			ArrayList<WidgetInput> inputList = inputListJSON.getInputs();
			
			/*
			 * Update our most recent input timeStamp so that in the next round we ask only
			 * for newer input.
			 * 
			 * Mark already processed input
			 */
			ArrayList<WidgetInput> toDelete = new ArrayList<WidgetInput>();
			for (WidgetInput widgetInput : inputList ) {
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
			for ( WidgetInput wi : toDelete ) {
				inputList.remove(wi);
			}
			
			/*
			 * Notify the widgetManager
			 */
			if (this.serverListener != null) {
				this.serverListener.onWidgetInput(inputList);
			}

			
		} catch (Exception e) {
			Log.error(this, "Error processing input. " +  e.getMessage(), e);
		
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
		widgetsUrlParam.append( this.getWidgetsUrl(this.placeId, this.appId, this.appId) ).append("&widgets=");
		
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
			Log.error(this, "Error parsing " + value + " to long." +  e.getMessage());
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


	
	public void getWidgetsList(String placeId, String applicationId, final Callback<ArrayList<Widget>> callback) {
		Log.debug( this, "Getting widgets from server: " +  this.getWidgetsUrl(placeId,  applicationId, this.appId) );
		try {
			interactionService.get( this.getWidgetsUrl(placeId,  applicationId, this.appId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "Error getting list of widgets from server." + caught.getMessage());
								caught.printStackTrace();
							}

						}

						@Override
						public void onSuccess(String json) {
							
							WidgetListJson widgetListJson = GenericJson.fromJson(json);
							
							ArrayList<Widget> widgetList = widgetListJson.getWidgets();
								
							/*
							 * Notify the callback
							 */
							if ( null != callback ) {
								callback.onSuccess(widgetList);
							} else {
								Log.warn(this, "No callback to notify about widget list");
							}


						}

					});
		} catch (Exception e) {
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "Error getting list of widgets from server." + e.getMessage());
				e.printStackTrace();
			}
			
		}
		
	}
	

	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, final Callback<WidgetInput> callback) {
		WidgetInputJson widgetInputJson = WidgetInputJson.create(widgetInput);
		
		Log.debug(this, "Sending widget input: " + widgetInputJson.toJsonString());
		Log.debug(this, "to: "+ this.getWidgetInputUrl(placeName, applicationName, getWidgetIdUrlEscaped(widgetInput.getWidgetId()), this.appId));
		
		this.interactionService.post(widgetInputJson.toJsonString(), 
				this.getWidgetInputUrl(placeName, applicationName, getWidgetIdUrlEscaped(widgetInput.getWidgetId()), this.appId), 
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

	public void setApplication(String placeId, String applicationId, Application application, final Callback<Application> callback) {
		ApplicationJson applicationJson = ApplicationJson.create(application);
		
		Log.debug(this, "Sending application: " + applicationJson.toJsonString());
		Log.debug(this, "to: "+ this.getApplicationUrl(placeId, applicationId, this.appId));
		
		this.interactionService.post(applicationJson.toJsonString(), 
				this.getApplicationUrl(placeId, applicationId, this.appId), 
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

	public void getApplication(String placeId, String applicationId, final Callback<Application> callback) {
		
		
		Log.debug(this, "Asking application: " + placeId +":"+applicationId);
		
		
		this.interactionService.get(
				this.getApplicationUrl(placeId, applicationId, this.appId), 
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
								Application application = ((ApplicationJson)GenericJson.fromJson(result)).getApplication();
								callback.onSuccess(application);
							} else {
								callback.onFailure(new Exception("Received null application"));
							}
						}
					}

		});
		
	}

	
	public void getApplicationsList(String placeId, final Callback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, Application.STATE.All, callback);
	}

	public void getApplicationsList(String placeId, boolean active,
			Callback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, active?Application.STATE.Active : Application.STATE.Inactive, callback);
		
	}
	
	private void getApplicationsList(String placeId, Application.STATE state,
			final Callback<ArrayList<Application>> callback) {
		/*
		 * Create the request url with the proper parameters based on the intended application state
		 */
		String url = this.getApplicationsUrl(placeId, this.placeId);
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

	public void getPlacesList(final Callback<ArrayList<Place>> callback) {
		Log.debug( this, "Getting places from server: " +  this.getPlacesUrl(this.appId) );
		try {
			interactionService.get( this.getPlacesUrl(this.appId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "Error getting list of places from server:"	+ caught.getMessage() );
							}
						}

						@Override
						public void onSuccess(String json) {
							PlaceListJson placeListJson = GenericJson.fromJson(json);
							
							ArrayList<Place> placeList = placeListJson.getPlaces();
								
							/*
							 * Notify the callback 
							 */
							if ( null != callback ) {
								callback.onSuccess(placeList);
							} else {
								Log.warn(this, "No callback to notify about place list");
							}
						}
					});
		} catch (Exception e) {
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "Error getting list of places from server: "	+ e.getMessage());
			}
			e.printStackTrace();
		}		
	}
	
	private void createChannel() {
		String token = PublicDisplayApplication.getLocalStorage().getString("ChannelToken");
		Long tokenTimestamp = PublicDisplayApplication.getLocalStorage().getLong("ChannelTokenTimestamp");
		
		/*
		 * If the token expire is due in more than one our we take the token and open the channel
		 * Otherwise, we ask a new token. 
		 * 
		 * This is needed because re-opening a channel after token has expired does not work:
		 * https://groups.google.com/forum/?fromgroups#!searchin/google-appengine-java/channel/google-appengine-java/kD3H6BWNYuA/NivXiDrqW7QJ
		 * 
		 * This assumes that the server sets the expiration time to the maximum: 24 hours!
		 */
		if ( null == tokenTimestamp ||  (System.currentTimeMillis() - tokenTimestamp.longValue()) > 23*60*60*1000 ) {
			this.getChannelToken();
		} else {
			if ( null != token && token.length() > 0 ) { 
				this.openChannel(token);
			} else {
				this.getChannelToken();
			}
		} 
	}
	
	private void openChannel(String token) {	
		ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
			  @Override
			  public void onChannelCreated(Channel channel) {
			    channel.open(new SocketListener() {
			      @Override
			      public void onOpen() {
			    	  Log.debug(this, "Channel open");
			    	  ClientServerCommunicator.this.channelOpen = true;
			    	  timerChannelPeriod = DEFAULT_TIMER_CHANNEL_PERIOD;
			      }
			      @Override
			      public void onMessage(String message) {
			    	  
			        ClientServerCommunicator.this.processInputSuccess(message);
			        
			      }
			      @Override
			      public void onError(SocketError error) {
			    	  Log.warn(this, "Error on channel. " + error.getDescription());
			    	  
			    	  
			    	  ClientServerCommunicator.this.channelOpen = false;
			    	  ClientServerCommunicator.this.startInputTimer();
			    	  
			    	  ClientServerCommunicator.this.timerChannel.schedule(timerChannelPeriod);
			    	  timerChannelPeriod *= 2;
			      }
			      @Override
			      public void onClose() {
			    	  Log.warn(this, "Channel closed");
			    	  
			 
			    	  ClientServerCommunicator.this.channelOpen = false;
			    	  ClientServerCommunicator.this.startInputTimer();
			    	  
			    	  ClientServerCommunicator.this.timerChannel.schedule(timerChannelPeriod);
			    	  timerChannelPeriod *= 2;
			      }
			    });
			  }
			});
	}

	private String getChannelUrl() {
		return this.getServerUrl()+"/place/"+this.placeId+"/application/"+this.appId + "/channel?appid="+this.appId;
	}
	
	/**
	 * Asks the server for a channel token. 
	 * 
	 * see https://developers.google.com/appengine/docs/java/channel/
	 */
	private void getChannelToken() {
		try {
			interactionService.get( getChannelUrl(), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							
							Log.warn(this, "Error getting channel token from server:", caught);
							ClientServerCommunicator.this.timerChannel.schedule(timerChannelPeriod);
					    	timerChannelPeriod *= 2;
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(this, "Received channel token data: " + json);
							timerChannelPeriod = DEFAULT_TIMER_CHANNEL_PERIOD;
							
							ChannelTokenJson channelTokenJson = GenericJson.fromJson(json);
							
							/*
							 * Store the token on local storage so that the next time, we try to reuse the 
							 * channel
							 */
							PublicDisplayApplication.getLocalStorage().setString("ChannelToken", channelTokenJson.getToken());
							PublicDisplayApplication.getLocalStorage().setString("ChannelTokenTimestamp", System.currentTimeMillis()+"");
							Log.debug(this, "Channel token: " + channelTokenJson.getToken());
							
							ClientServerCommunicator.this.openChannel(channelTokenJson.getToken());
							
						}
					});
		} catch (Exception e) {
			
			Log.warn(this, "Error getting channel token from server: ", e);
			ClientServerCommunicator.this.timerChannel.schedule(timerChannelPeriod);
	    	timerChannelPeriod *= 2;
		}	
	}
	
	
	private void addToProcessedInput(WidgetInput wi) {
		this.processedInput.add(wi);
		
		if (this.processedInput.size() > 50) {
			this.processedInput.remove(0);
		}
	}
	
	private boolean isProcessed(WidgetInput wi) {
		if ( this.processedInput.contains(wi) ) {
			return true;
		}
		return false;
	}


	/**
	 * @return the interactionServerUrl
	 */
	public String getInteractionServerUrl() {
		return interactionServerUrl;
	}


	/**
	 * @param interactionServerUrl the interactionServerUrl to set
	 */
	public void setInteractionServerUrl(String interactionServerUrl) {
		this.interactionServerUrl = interactionServerUrl;
		this.applicationUrl = this.getApplicationUrl(placeId, appId);
	}
}
