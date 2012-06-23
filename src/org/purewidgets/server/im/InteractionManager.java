/**
 *
 */
package org.purewidgets.server.im;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;



import org.purewidgets.server.http.HttpServiceImpl;
import org.purewidgets.server.im.json.WidgetInputListJson;
import org.purewidgets.server.im.json.WidgetListJson;
import org.purewidgets.server.storage.ServerStorage;
import org.purewidgets.shared.exceptions.HttpServerException;
import org.purewidgets.shared.im.UrlHelper;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.logging.Log;




/**
 * @author Jorge C. S. Cardoso
 *
 */
public class InteractionManager  {		
	private static final String DEFAULT_INTERACTION_SERVER_URL = "http://pw-interactionmanager.appspot.com";
	
		
	/**
	 * The name of the name/value pair that stores the last input time stamp
	 * received from the server. 
	 */
	private static final String TIMESTAMP_NAME = "lastTimeStamp";

	/**
	 * The applicationId on which this ServerCommunicator will be used.
	 */
	private  String appId = "jorge";
	
	
	private HttpServiceImpl interactionService;
	
	
	/**
	 * The placeId on which this ServerCommunicator will be used. 
	 */
	private  String placeId = "dsi";

	private ServerStorage remoteStorage;

	private UrlHelper urlHelper;

	public InteractionManager(ServerStorage remoteStorage, String placeId, String appId) {
		this.placeId = placeId;
		this.appId = appId;
		
		this.urlHelper = new UrlHelper(DEFAULT_INTERACTION_SERVER_URL);
		
		
		interactionService = new HttpServiceImpl();
		this.remoteStorage = remoteStorage;
	}

	
	
	public ArrayList<Widget> addWidget(Widget widget) {
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		widgets.add(widget);
		
		
		WidgetListJson wl = WidgetListJson.create(this.appId, placeId, widgets);
	
		
		String json = wl.toJsonString();

		
		Log.debug("Adding " + json + " to server");
		String response = null;
		try {
			response = interactionService.post(json,
					this.urlHelper.getWidgetsUrl(this.placeId, this.appId, this.appId) );
		} catch (HttpServerException e) {
			Log.error(this, "", e);
			return new ArrayList<Widget>();
		}
		Log.debug(response);

		wl = WidgetListJson.fromJson(WidgetListJson.class, response);
		
		if ( null != wl ) {
			return wl.getWidgetList();
		} else {
			return new ArrayList<Widget>();
		}
	}
	
	
	/**
	 * Checks input from the InteractionManager service
	 */
	public ArrayList<WidgetInput> askForInputFromServer() {
		
	
			String lastTimeStamp = this.getLastTimeStampAsString();
			if ( null == lastTimeStamp ) {
				lastTimeStamp = "0";
			}
			String url = this.urlHelper.getApplicationInputUrl(this.placeId, this.appId, this.appId, lastTimeStamp);
			
			Log.debug(this, "Contacting application server for input..." + url);
			String response = null;
			
			try {
				response = interactionService.get(url);
			} catch (HttpServerException e) {
				Log.error(this, e.getMessage());
				e.printStackTrace();
				return new ArrayList<WidgetInput>();
			}
			Log.debug(this, response);
			
			

			
			WidgetInputListJson inputList = WidgetInputListJson.fromJson(WidgetInputListJson.class, response);
			if ( null != inputList ) {
				/*
				 * Update our most recent input timeStamp so that in the next round we ask only
				 * for newer input
				 */
				for (WidgetInput widgetInput : inputList.getWidgetInputList() ) {
					/*
					 * Save the new timeStamp locally
					 */
					if (toLong(widgetInput.getTimeStamp()) > this.getLastTimeStampAsLong()) {
						this.setTimeStamp(toLong(widgetInput.getTimeStamp()));
					}
				}
				
				return inputList.getWidgetInputList();
			} else {
		
			
				return new ArrayList<WidgetInput>();
			}
	}
	
	
	
	public void deleteAllWidgets(boolean volatileOnly) {
		// TODO Auto-generated method stub
		
	}
	   
	   
	public ArrayList<Widget> deleteWidget(Widget widget) {
		Log.debug(this, "Removing widget:" + widget.getWidgetId() );
		
		/*
		 * Create the URL for the DELETE method. Widget ids are passed on the 
		 * 'widget' url parameter
		 */
		StringBuilder widgetsUrlParam = new StringBuilder();
		widgetsUrlParam.append( this.urlHelper.getWidgetsUrl(this.placeId, this.appId, this.appId) ).append("&widgets=");
		
		
		try {
			widgetsUrlParam.append(URLEncoder.encode(widget.getWidgetId(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			Log.error(this, "Could not URLencode.",  e1);
			return new ArrayList<Widget>();
		}
			
		String response;
		
		try {
			response = interactionService.delete(widgetsUrlParam.toString());
			
		} catch (Exception e) {
			Log.error(this, "",  e );
			
			return new ArrayList<Widget>();
		}
		
	
		WidgetListJson widgetList = WidgetListJson.fromJson(WidgetListJson.class, response);
		
		if ( null != widgetList ) {
			return widgetList.getWidgetList();
		} else {
			return new ArrayList<Widget>();
		}

	}

	
	
	/**
	 * Enables or disables the automatic input requests
	 * @param automatic
	 */
	public void setAutomaticInputRequests(boolean automatic) {
		
	}
	
	
	private long getLastTimeStampAsLong() {
		try {
			return Long.parseLong(getLastTimeStampAsString());
		} catch (Exception e) {
			Log.error(this, "Could not parse timestamp: " + e.getMessage());
		}
		return 0;
	}
	
	private String getLastTimeStampAsString() {
		return remoteStorage.getString(TIMESTAMP_NAME);
	}	
	

	private void setTimeStamp(long timeStamp) {
		Log.debug(this, "Storing timestamp: " + timeStamp);
		
		remoteStorage.setString(TIMESTAMP_NAME, ""+timeStamp);
	}

	private long toLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			Log.error(this, "Could not parse long value:" + e.getMessage());
		}
		return 0;
	}

	public ArrayList<Widget> getWidgetsList(String placeId, String applicationId) {		
		String url = this.urlHelper.getWidgetsUrl(placeId, applicationId, this.appId);
		
		Log.debug(this, "Asking application server for the widget list..." + url);
		String response = null;
		
		try {
			response = interactionService.get(url);
		} catch (HttpServerException e) {
			Log.error(this,  e.getMessage(), e);
			
			return new ArrayList<Widget>();
		}
		
		WidgetListJson widgetList = WidgetListJson.fromJson(WidgetListJson.class, response);

		if ( null != widgetList ) {
			return widgetList.getWidgetList();
		} else {	
			return new ArrayList<Widget>();
		}
	}


	/**
	 * @return the interactionServerUrl
	 */
	public String getInteractionServerUrl() {
		return this.urlHelper.getInteractionServerUrl();
	}


	/**
	 * @param interactionServerUrl the interactionServerUrl to set
	 */
	public void setInteractionServerUrl(String interactionServerUrl) {
		this.urlHelper.setInteractionServerUrl(interactionServerUrl);
	}

}
