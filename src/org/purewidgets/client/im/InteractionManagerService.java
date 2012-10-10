/**
 * 
 */
package org.purewidgets.client.im;

import java.util.ArrayList;

import org.purewidgets.client.http.HttpService;
import org.purewidgets.client.http.HttpServiceAsync;
import org.purewidgets.client.im.json.ApplicationJson;
import org.purewidgets.client.im.json.ApplicationListJson;
import org.purewidgets.client.im.json.ChannelTokenJson;
import org.purewidgets.client.im.json.InputResponseJson;
import org.purewidgets.client.im.json.PlaceJson;
import org.purewidgets.client.im.json.PlaceListJson;
import org.purewidgets.client.im.json.WidgetInputJson;
import org.purewidgets.client.im.json.WidgetInputListJson;
import org.purewidgets.client.im.json.WidgetJson;
import org.purewidgets.client.im.json.WidgetListJson;
import org.purewidgets.client.json.GenericJson;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.im.Application;
import org.purewidgets.shared.im.ApplicationState;
import org.purewidgets.shared.im.Place;
import org.purewidgets.shared.im.UrlHelper;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The InteractionManagerService represents the services available at the interaction manager server.
 * This class is mainly for the internal use of the PuReWidgets toolkit. In general, application should
 * not need to use it directly.
 * 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class InteractionManagerService {


	/**
	 * An Http service proxy to talk to the interaction manager server.
	 */
	private final HttpServiceAsync interactionService;

	
	/**
	 * A helper class to create the various Rest style Urls for accessing the interaction manager
	 * server.
	 */
	private UrlHelper urlHelper;
	
	
	/**
	 * The application's localstorage.
	 */
	private LocalStorage localStorage;
	
	/**
	 * Creates a new InteractionManagerService that uses the interaction manager specified by the 
	 * <code>interactionServerUrl</code> and the specified application's LocalStorage.
	 * 
	 * @param interactionServerUrl The base Url of the interaction manager server.
	 * @param localStorage The application's LocalStorage.
	 */
	public InteractionManagerService(String interactionServerUrl, LocalStorage localStorage) {
		
		this.localStorage = localStorage;
		
		this.urlHelper = new UrlHelper(interactionServerUrl);
		
		interactionService = GWT.create(HttpService.class);
	}
	
	/**
	 * Deletes all widgets of the specified application. The deleleted widgets are returned in the callback.
	 * 
	 * @param placeId The place id of the application of which to delete all widgets.
	 * @param applicationId The application id of the application of to delete all widgets.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void deleteAllWidgets(String placeId, String applicationId, String callingApplicationId, 
			final AsyncCallback<ArrayList<Widget>> callback) {
		
		Log.debug(this, "Removing all widgets from " + placeId + " : " + applicationId );
		
		
		try {
			interactionService.delete(
					this.urlHelper.getWidgetsUrl(placeId, applicationId, callingApplicationId),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(InteractionManagerService.this, "Error deleting widgets from server.", caught);
							
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String result) {
							Log.debug(InteractionManagerService.this, "Got response to widget deletion: " + result);
							if ( null != callback ) {
								WidgetListJson widgetListJson = GenericJson.fromJson(result);
								ArrayList<Widget> widgetList = widgetListJson.getWidgets();
								callback.onSuccess(widgetList);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

					});
		} catch (Exception e) {
			Log.warn(InteractionManagerService.this, "Error deleting widgets from server.", e);
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback to notify.");
			}
			
		}
	}
	
	
	/**
	 * Adds a list of widgets to the specified application. The added widgets are returned back in the callback.
	 * 
	 * @param placeId The place id of the application to which the widgets will be added.
	 * @param applicationId The application id of the application to which the widgets will be added.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param widgets The widgets to add.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void addWidgetToServer(String placeId, String applicationId, String callingApplicationId, 
			ArrayList<Widget> widgets, final AsyncCallback<ArrayList<Widget>> callback) {
		
		Log.debug(this, "Adding widgets to " + placeId + " : " + applicationId);
		
		
		ArrayList<WidgetJson> widgetJsonList = new ArrayList<WidgetJson>();
		
		for ( Widget widget : widgets ) {
			WidgetJson widgetJSON = WidgetJson.create(widget);			
			widgetJsonList.add(widgetJSON);
		}
		
		
		WidgetListJson widgetListJson = GenericJson.getNew();

		widgetListJson.setWidgetsFromArrayList(widgetJsonList);
		
		
		Log.debugFinest(this, "Sending " + widgetListJson.toJsonString() + " to server");

		try {
			this.interactionService.post(widgetListJson.toJsonString(),
					this.urlHelper.getWidgetsUrl(placeId, applicationId, callingApplicationId),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(InteractionManagerService.this, "Error adding widgets to server.", caught);
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String result) {
							Log.debug(InteractionManagerService.this, "Got response to addition of widgets.");
							Log.debugFinest(InteractionManagerService.this, result);
							
							if ( null != callback ) {
								WidgetListJson widgetListJson = GenericJson.fromJson(result);
								ArrayList<Widget> widgetList = widgetListJson.getWidgets();
								callback.onSuccess(widgetList);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

					});
		} catch (Exception e) {
			Log.warn(InteractionManagerService.this, "Error adding widgets to server.", e);
			
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback to notify.");
			}
		}
	}

	
	/**
	 * Asks for input to the specified application. 
	 * 
	 * @param placeId The place id of the application from which we want to receive input.
	 * @param applicationId The application id of the application from which we want to receive input.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param from A timestamp defining the oldest allowed timestamp in the input to be received.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void getInputFromServer(String placeId, String applicationId, String callingApplicationId, String from,
			final AsyncCallback<ArrayList<WidgetInput>> callback) {
		Log.debug(this, "Asking for input for " + placeId + " : " + applicationId);
		try {
			
			
			String url = urlHelper.getApplicationInputUrl(placeId, applicationId, callingApplicationId, 
					from);
			
			interactionService.get(url, new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					Log.warn(InteractionManagerService.this, "Error getting input from server.", caught);
					
					if ( null != callback ) {
						callback.onFailure(caught);
					} else {
						Log.warn(InteractionManagerService.this, "No callback to notify.");
					}
				}

				@Override
				public void onSuccess(String result) {
					Log.debug(InteractionManagerService.this, "Got response to widget input. ");
					Log.debugFinest(InteractionManagerService.this, result);
					if ( null != callback ) {
						WidgetInputListJson widgetInputListJson = GenericJson.fromJson(result);
						ArrayList<WidgetInput> widgetInputs = widgetInputListJson.getInputs();
						callback.onSuccess(widgetInputs);
					} else {
						Log.warn(InteractionManagerService.this, "No callback to notify.");
					}
				}
			});
		} catch (Exception e) {
			Log.warn(InteractionManagerService.this, "Error getting input from server.", e);
			
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback to notify.");
			}
		}
	}
	
	/**
	 * Deletes a list of widgets from the specified application. The deleted widgets are returned back in the callback.
	 * 
	 * @param placeId The place id of the application from which the widgets will be deleted.
	 * @param applicationId The application id of the application from which the widgets will be deleted.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param widgets The list of widgets to delete.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void removeWidget(String placeId, String applicationId, String callingApplicationId,
			ArrayList<Widget> widgets, final AsyncCallback<ArrayList<Widget>> callback) {
		Log.debug(this, "Removing  widgets from " + placeId + " : " + applicationId);
		
		/*
		 * Create the URL for the DELETE method. Widget ids are passed on the 
		 * 'widget' url parameter
		 */
		StringBuilder widgetsUrlParam = new StringBuilder();
		widgetsUrlParam.append( this.urlHelper.getWidgetsUrl(placeId, applicationId, callingApplicationId) ).append("&widgets=");
		
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
		
		Log.debugFinest(this, "Sending " + widgetsUrlParam + " to server");
		
		try {
			interactionService.delete(
					widgetsUrlParam.toString(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(InteractionManagerService.this, "Error deleting widgets from server.", caught);
							
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}

						}

						@Override
						public void onSuccess(String result) {
							Log.debug(InteractionManagerService.this, "Got response to widget deletion.");
							Log.debugFinest(InteractionManagerService.this, result);
							if ( null != callback ) {
								WidgetListJson widgetListJson = GenericJson.fromJson(result);
								ArrayList<Widget> widgetList = widgetListJson.getWidgets();
								callback.onSuccess(widgetList);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

					});
		} catch (Exception e) {
			Log.warn(InteractionManagerService.this, "Error deleting widgets from server.", e);
			
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback to notify.");
			}
		}
	}
	
	
	
	/**
	 * Retrieves the list of widgets from the specified application. 
	 * 
	 * @param placeId The place id of the application from which the widgets will be retrieved.
	 * @param applicationId The application id of the application from which the widgets will be retrieved.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void getWidgetsList(String placeId, String applicationId, String callingApplicationId,
			final AsyncCallback<ArrayList<Widget>> callback) {
		Log.debug(this, "Retrieving  widgets from " + placeId + " : " + applicationId);
		
		try {
			interactionService.get( this.urlHelper.getWidgetsUrl(placeId,  applicationId, callingApplicationId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(this, "Error getting list of widgets from server.", caught);
							
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(InteractionManagerService.this, "Got response to widget list.");
							Log.debugFinest(InteractionManagerService.this, json);
							if ( null != callback ) {
								WidgetListJson widgetListJson = GenericJson.fromJson(json);
							
								ArrayList<Widget> widgetList = widgetListJson.getWidgets();
								callback.onSuccess(widgetList);
							} else {
								Log.warn(InteractionManagerService.this, "No callback to notify.");
							}
						}

					});
		} catch (Exception e) {
			Log.warn(this, "Error getting list of widgets from server.", e);
			
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback to notify.");
			}		
		}
		
	}
	
	/**
	 * Sends input to the specified application. 
	 * 
	 * @param placeId The place id of the application to which input will be sent.
	 * @param applicationId The application id of the application to which input will be sent.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param widgetInput The WidgetInput that describes the input
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void sendWidgetInput(String placeId, String applicationId, String callingApplicationId, 
			WidgetInput widgetInput, final AsyncCallback<InputResponseJson> callback) {
		
		Log.debug(this, "Sending input to " + placeId + " : " + applicationId);
		
		WidgetInputJson widgetInputJson = WidgetInputJson.create(widgetInput);
		
		Log.debugFinest(this, "Sending to server:" + widgetInputJson.toJsonString());
		
		this.interactionService.post(widgetInputJson.toJsonString(), 
				this.urlHelper.getWidgetInputUrl(placeId, applicationId, getWidgetIdUrlEscaped(widgetInput.getWidgetId()), callingApplicationId), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(this, "Error posting input. ", caught);
						
						if ( null != callback ) {
							callback.onFailure(caught);
						} else {
							Log.warn(InteractionManagerService.this, "No callback to notify.");
						}
					}

					@Override
					public void onSuccess(String result) {
						Log.debug(InteractionManagerService.this, "Got response to input sending.");
						Log.debugFinest(InteractionManagerService.this, result);
						if ( null != callback ) {
							callback.onSuccess((InputResponseJson)InputResponseJson.fromJson(result));
						} else {
							Log.warn(InteractionManagerService.this, "No callback to notify.");
						}
					}
		});
		
	}

//	
//	public void setApplication(String placeId, String applicationId, String callingApplicationId, 
//			Application application, final AsyncCallback<Application> callback) {
//		ApplicationJson applicationJson = ApplicationJson.create(application);
//		Log.debug(this, "Sending application " + placeId + " : " + applicationId);
//		Log.debugFinest(this, "Sending to server: " + applicationJson.toJsonString());
//		
//		this.interactionService.post(applicationJson.toJsonString(), 
//				this.urlHelper.getApplicationUrl(placeId, applicationId, callingApplicationId), 
//				new AsyncCallback<String>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						Log.warn(this, "Error posting application. ", caught);
//						
//						if ( null != callback ) {
//							callback.onFailure(caught);
//						} else {
//							Log.warn(InteractionManagerService.this, "No callback to notify.");
//						}
//					}
//
//					@Override
//					public void onSuccess(String result) {
//						Log.debug(InteractionManagerService.this, "Got response to sending application.");
//						Log.debugFinest(InteractionManagerService.this,  result);
//						if ( null != callback ) {
//							Application application = (Application)(ApplicationJson.fromJson(result));
//							callback.onSuccess(application);
//						} else {
//							Log.warn(InteractionManagerService.this, "No callback to notify.");
//						}
//					}
//
//		});
//	}

	/**
	 * Gets information about an application. 
	 * 
	 * @param placeId The place id of the application to get information from.
	 * @param applicationId The application id of the application  to get information from.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	public void getApplication(String placeId, String applicationId, String callingApplicationId, 
			final AsyncCallback<Application> callback) {
		
		
		Log.debug(this, "Asking for application " + placeId +" : "+applicationId);
		
		
		this.interactionService.get(
				this.urlHelper.getApplicationUrl(placeId, applicationId, callingApplicationId), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(this, "Error asking for  application. ",  caught);
						
						if ( null != callback ) {
							callback.onFailure(caught);
						} else {
							Log.warn(InteractionManagerService.this, "No callback to notify.");
						}
					}

					@Override
					public void onSuccess(String result) {
						Log.debug(InteractionManagerService.this, "Received response to application request.");
						Log.debugFinest(InteractionManagerService.this,  result);
						if ( null != callback ) {
							if ( null != result && result.length() > 0) {
								
								Application application = ((ApplicationJson)GenericJson.fromJson(result)).getApplication();
								callback.onSuccess(application);
							} else {
								callback.onFailure(new Exception("Received null application"));
							}
						} else {
							Log.warn(InteractionManagerService.this, "No callback to notify.");
						}
					}

		});
		
	}

	
	/**
	 * Gets a list of applications of a place. 
	 * 
	 * @param placeId The place id to get information from.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	public void getApplicationsList(String placeId, String callingApplicationId, final AsyncCallback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, callingApplicationId, ApplicationState.All, callback);
	}

	/**
	 * Gets a list of applications of a place, filtered by it active status.
	 * 
	 * @param placeId The place id to get information from.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param active if true, gets list of active applications; if false, get list of inactive applications.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	public void getApplicationsList(String placeId, String callingApplicationId, boolean active,
			AsyncCallback<ArrayList<Application>> callback) {
		this.getApplicationsList(placeId, callingApplicationId, active?ApplicationState.Active : ApplicationState.Inactive, callback);
		
	}
	
	/**
	 * Gets a list of applications of a place, filtered by it active status.
	 * 
	 * @param placeId The place id to get information from.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param state The state of the applications to retrieve.
	 * @param callback The callback instance to notify when the response comes back.
	 */		
	private void getApplicationsList(String placeId, String callingApplicationId, ApplicationState state,
			final AsyncCallback<ArrayList<Application>> callback) {
		Log.debug(this, "Asking for applications from " + placeId );
		
		/*
		 * Create the request url with the proper parameters based on the intended application state
		 */
		String url = this.urlHelper.getApplicationsUrl(placeId, callingApplicationId);
		switch (state) {
		case All: 
			break;
		case Active:
			url += "&active=true";
			break;
		case Inactive:
			url += "&active=false";
		}
		
		try {
			interactionService.get(url, 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(this, "Error getting list of applications from server.", caught);
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(InteractionManagerService.this, "Received applications list." );
							Log.debugFinest(InteractionManagerService.this, json );
							if ( null != callback ) {
								ApplicationListJson applicationListJson = GenericJson.fromJson(json);
					
								ArrayList<Application> applicationList = applicationListJson.getApplications();
							
								callback.onSuccess(applicationList);
							} else {
								Log.warn(this, "No callback to notify about application list");
							}
						}
					});
		} catch (Exception e) {
			Log.warn(this, "Error getting list of applications from server.", e);
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "No callback to notify.");
			}
		
		}
	}

	/**
	 * Gets information about a specific place.
	 * 
	 * @param placeId The place id to get information from.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */
	public void getPlace(String placeId, String callingApplicationId, final AsyncCallback<Place> callback) {
		Log.debug( this, "Getting place from server: " +  this.urlHelper.getPlaceUrl(placeId, callingApplicationId) );
		try {
			interactionService.get( this.urlHelper.getPlaceUrl(placeId, callingApplicationId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(this, "Error getting place from server.", caught );
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(InteractionManagerService.this, "Got response to place request.");
							Log.debugFinest(InteractionManagerService.this, json);
							/*
							 * Notify the callback 
							 */
							if ( null != callback ) {
								PlaceJson placeJson = GenericJson.fromJson(json);
							
								
							
								callback.onSuccess(placeJson.getPlace());
							} else {
								Log.warn(this, "No callback to notify about place");
							}
						}
					});
		} catch (Exception e) {
			Log.warn(this, "Error getting place from server: ", e);
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "No callback to notify.");
			}
		}				
	}
	
	/**
	 * Gets a list of available places on the interaction manager.
	 * 
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param callback The callback instance to notify when the response comes back.
	 */	
	public void getPlacesList(String callingApplicationId, final AsyncCallback<ArrayList<Place>> callback) {
		Log.debug( this, "Getting places from server: " +  this.urlHelper.getPlacesUrl(callingApplicationId) );
		try {
			interactionService.get( this.urlHelper.getPlacesUrl(callingApplicationId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(this, "Error getting list of places from server.", caught );
							if ( null != callback ) {
								callback.onFailure(caught);
							} else {
								Log.warn(this, "No callback to notify.");
							}
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(InteractionManagerService.this, "Got response to places request.");
							Log.debugFinest(InteractionManagerService.this, json);
							/*
							 * Notify the callback 
							 */
							if ( null != callback ) {
								PlaceListJson placeListJson = GenericJson.fromJson(json);
							
								ArrayList<Place> placeList = placeListJson.getPlaces();
								
							
								callback.onSuccess(placeList);
							} else {
								Log.warn(this, "No callback to notify about place list");
							}
						}
					});
		} catch (Exception e) {
			Log.warn(this, "Error getting list of places from server: ", e);
			if ( null != callback ) {
				callback.onFailure(e);
			} else {
				Log.warn(this, "No callback to notify.");
			}
		}		
	}
	
	
	/**
	 * Creates a communication channel with the interaction manager for synchronous 
	 * communication with a specific application. Currently, the channel is used only
	 * to receive WidgetInput for an application.
	 * 
	 * @param placeId The place id of the application which this channel will serve.
	 * @param applicationId The application id of the application which this channel will serve.
	 * @param callingApplicationId The application id of the application that is making the request.
	 * @param listener The listener for WidgetInput directed at the specified application.
	 */
	public void createChannel(String placeId, String applicationId, String callingApplicationId,
			final AsyncCallback<ArrayList<WidgetInput>> listener) {
		
		String token = this.localStorage.getString("ChannelToken");
		Long tokenTimestamp = this.localStorage.getLong("ChannelTokenTimestamp");
		if ( null == tokenTimestamp ) {
			tokenTimestamp = new Long(0);
		}
			
		Log.debug(this, "Channel Token asked " + (System.currentTimeMillis() - tokenTimestamp.longValue() )/(1000*60*60) + "hours ago.");
		/*
		 * If the token expire is due in more than one our we take the token and open the channel
		 * Otherwise, we ask a new token. 
		 * 
		 * This is needed because re-opening a channel after token has expired does not work:
		 * https://groups.google.com/forum/?fromgroups#!searchin/google-appengine-java/channel/google-appengine-java/kD3H6BWNYuA/NivXiDrqW7QJ
		 * 
		 * This assumes that the server sets the expiration time to the maximum: 24 hours!
		 * 
		 * Somehow, the token is expiring after 12 or so hours....
		 */
		if (  (System.currentTimeMillis() - tokenTimestamp.longValue()) >= 12*60*60*1000 ) {
			
			this.getChannelToken(placeId, applicationId, callingApplicationId, listener);
		} else {
			if ( null != token && token.length() > 0 ) { 
				this.openChannel(token, listener);
			} else {
				this.getChannelToken(placeId, applicationId, callingApplicationId, listener);
			}
		} 
	}
	
	private void openChannel(String token, final AsyncCallback<ArrayList<WidgetInput>> listener) {	
		ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
			  @Override
			  public void onChannelCreated(Channel channel) {
			    channel.open(new SocketListener() {

					@Override
					public void onOpen() {
						Log.debug(InteractionManagerService.this, "Input channel opened");
						
					}

					@Override
					public void onMessage(String message) {
						Log.debug(InteractionManagerService.this, "Got widget input.");
						Log.debugFinest(InteractionManagerService.this, message);
						if ( null != listener ) {
							WidgetInputListJson widgetInputListJson = GenericJson.fromJson(message);
							ArrayList<WidgetInput> widgetInputs = widgetInputListJson.getInputs();
							listener.onSuccess(widgetInputs);
						} else {
							Log.warn(InteractionManagerService.this, "No callback to notify.");
						}
						
					}

					@Override
					public void onError(SocketError error) {
						if ( null != listener ) {
							listener.onFailure(new Exception("Socket Error"));
						} else {
							Log.warn(InteractionManagerService.this, "No callback defined");
						}
					}

					@Override
					public void onClose() {
						if ( null != listener ) {
							listener.onFailure(new Exception("Channel closed"));
						} else {
							Log.warn(InteractionManagerService.this, "No callback defined");
						}
						
					}
			    	
			    });
			  }
			});
	}

	
	
	/**
	 * Asks the server for a channel token. 
	 * 
	 * see https://developers.google.com/appengine/docs/java/channel/
	 */
	private void getChannelToken(final String placeId, final String applicationId, final String callingApplicationId,
			final AsyncCallback<ArrayList<WidgetInput>> listener) {
		try {
			interactionService.get( urlHelper.getChannelUrl(placeId, applicationId, callingApplicationId), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Log.warn(this, "Error getting channel token from server:", caught);
							
							if ( null != listener ) {
								listener.onFailure(caught);
							} else {
								Log.warn(InteractionManagerService.this, "No callback defined");
							}
						}

						@Override
						public void onSuccess(String json) {
							Log.debug(InteractionManagerService.this, "Received channel token data." );
							
							Log.debugFinest(InteractionManagerService.this,  json);
							ChannelTokenJson channelTokenJson = GenericJson.fromJson(json);
							
							/*
							 * Store the token on local storage so that the next time, we try to reuse the 
							 * channel
							 */
							InteractionManagerService.this.localStorage.setString("ChannelToken", channelTokenJson.getToken());
							InteractionManagerService.this.localStorage.setString("ChannelTokenTimestamp", System.currentTimeMillis()+"");
							Log.debug(this, "Channel token: " + channelTokenJson.getToken());
							
							InteractionManagerService.this.openChannel(channelTokenJson.getToken(), listener);
							
						}
					});
		} catch (Exception e) {
			
			Log.warn(this, "Error getting channel token from server: ", e);
			if ( null != listener ) {
				listener.onFailure(e);
			} else {
				Log.warn(InteractionManagerService.this, "No callback defined");
			}
		}	
	}
	

	
	private String getWidgetIdUrlEscaped(Widget widget) {
		return com.google.gwt.http.client.URL.encode(widget.getWidgetId());
	}
	
	private String getWidgetIdUrlEscaped(String widgetId) {
		return com.google.gwt.http.client.URL.encode(widgetId);
	}
}
