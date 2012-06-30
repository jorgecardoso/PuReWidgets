/**
 * 
 */
package org.purewidgets.client.application;

import java.util.HashMap;
import java.util.Map;

import org.purewidgets.client.im.InteractionManagerService;
import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.client.storage.ServerStorage;
import org.purewidgets.shared.im.Application;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The PublicDisplayApplication class represents the client part of the
 * public display application. 
 * 
 * A PublicDisplayApplication provides a standard way to read and store application parameters that
 * can be set on the URL query, but which are also stored at the server.
 * 
 * It also provides direct access to a LocalStorage and a ServerStorage.
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PDApplication  {
	/**
	 * The URL query string parameter name that holds the place id
	 */
	private static final String PLACE_ID_PARAMETER = "placeid";
	
	/**
	 * The URL query string parameter name that holds the application id
	 */
	private static final String APPLICATION_ID_PARAMETER = "applicationid";

	/**
	 * If the URL does not contain the application id, this application id will
	 * be used as default
	 */
	private static final String DEFAULT_APPLICATION_ID = "DefaultApplication";

	/**
	 * If the URL does not contain the application id, this application id will
	 * be used as default
	 */
	private static final String DEFAULT_PLACE_ID = "DefaultPlace";

	/**
	 * The place name
	 */
	private String placeId;
	
	/**
	 * The application name
	 */
	private String applicationId;
	
	
	private PDApplicationLifeCycle listener;


	/**
	 * The LocalStorage for this application
	 */
	private LocalStorage localStorage;

	/**
	 * The parameter/values  for the application-defined options that are stored in the 
	 * remote storage/passed in the URL
	 */
	private Map<String, String> parameters;


	/**
	 * The RemoteStorage for this application
	 */
	private ServerStorage remoteStorage;

	/**
	 * The ServerCommunicator used to communicate with the interaction manager
	 */
	private InteractionManagerService interactionManager;

	private Application application;

	/**
	 * @return the applicationName
	 */
	public  String getApplicationName() {
		return applicationId;
	}

	public  LocalStorage getLocalStorage() {
	
		return localStorage;
		
	}

	/** 
	 * This function returns a boolean application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public  boolean getParameterBoolean(String name, boolean defaultValue) {
		String valueString = getParameterString(name, defaultValue+"");
		boolean toReturn = defaultValue;
		try {
			toReturn = Boolean.parseBoolean(valueString);
		} catch (NumberFormatException nfe) {
			Log.warn(PDApplication.class.getName(), "Could not parse '" + valueString + "' into a boolean (true/false).");
		}

		return toReturn;
	}
	
	/** 
	 * This function returns an int application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public  int getParameterInt(String name, int defaultValue) {
		String valueString = getParameterString(name, defaultValue+"");
		int toReturn = defaultValue;
		try {
			toReturn = Integer.parseInt(valueString);
		} catch (NumberFormatException nfe) {
			Log.warn(PDApplication.class.getName(), "Could not parse '" + valueString + "' into an integer.");
		}

		return toReturn;
	}	
	
	/** 
	 * This function returns a string application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public  String getParameterString(String name, String defaultValue) {

		/*
		 * Check remote storage first
		 */
		if (null != parameters && parameters.size() > 0) {

			String remoteValue = null;
		
			remoteValue = parameters.get(name);
			Log.debug("Remote value for " + name + " is " + remoteValue);
			
	
			if (null != remoteValue) {
				return remoteValue;
			}
		} 

		/*
		 * Check the URL for the parameter
		 */
		String urlValue = com.google.gwt.user.client.Window.Location.getParameter(name);

		if (null != urlValue) {
			return urlValue;
		}

		return defaultValue;
		
	}		
	
	
	public  ServerStorage getRemoteStorage() {
		
		return remoteStorage;
		
	}

	private PDApplication(String placeId, String applicationId, PDApplicationLifeCycle entryPoint) {

		this.listener = entryPoint;
		this.applicationId = applicationId;
		this.placeId = placeId;
		
		this.localStorage = new LocalStorage(applicationId);

		this.remoteStorage = new ServerStorage(placeId, applicationId);
		
		
		this.remoteStorage.getAll(new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					PDApplication.this.handleParametersFromServer(null);
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					PDApplication.this.handleParametersFromServer(result);
				}
			});
		
		org.purewidgets.client.Resources.INSTANCE.css().ensureInjected();
	}
	

	public static void load(PDApplicationLifeCycle entryPoint,
			String defaultApplicationId) {
		
				
		String placeId = com.google.gwt.user.client.Window.Location.getParameter(PLACE_ID_PARAMETER);

		if (null == placeId) {
			placeId = DEFAULT_PLACE_ID;
		}
		Log.debug(PDApplication.class.getName(), "Using place name: " + placeId);
		
		String applicationId = com.google.gwt.user.client.Window.Location
				.getParameter(APPLICATION_ID_PARAMETER);

		if (null == applicationId) {
			if (null == defaultApplicationId) {
				applicationId = DEFAULT_APPLICATION_ID;
			} else {
				applicationId = defaultApplicationId;
			}
		}
		Log.debug(PDApplication.class.getName(), "Using application name: "
				+ applicationId);

		
		PDApplication pdApplication = new PDApplication(placeId, applicationId, entryPoint);
	
	}
	


	protected void handleParametersFromServer( Map<String, String> result) {
		if (null != result) {
			this.parameters = result;
			
			for ( String key : result.keySet() ) {
				
				Log.debug(this, "Loaded from remote datastore: " + key + ": " +result.get(key) );
			}
		} else {
			
			this.parameters = new HashMap<String, String>();
		
		}
	
		
		String interactionManagerUrl = this.getParameterString("imurl", "http://pw-interactionmanager.appspot.com");
		
		Log.info(this, "Using interaction manager: " + interactionManagerUrl);
		
		InteractionManagerService serverCommunicator = new InteractionManagerService(interactionManagerUrl, this.localStorage);
		this.interactionManager = serverCommunicator;
		
		WidgetManager.create(this.placeId, this.applicationId, this.localStorage, serverCommunicator); 
		
		//WidgetManager.get().setServerCommunication( serverCommunicator );

		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
				
			}
		});		
		
		
		serverCommunicator.getApplication(this.placeId, this.applicationId, this.applicationId, new AsyncCallback<Application>() {

			@Override
			public void onSuccess(Application application) {
				Log.debug(PDApplication.class.getName(), "Received application: " + application);
				PDApplication.this.application = application;
				PDApplication.this.listener.onPDApplicationLoaded(PDApplication.this);
				
			}

			@Override
			public void onFailure(Throwable exception) {
				Log.debug(PDApplication.class.getName(), "Could not get Application");
				PDApplication.this.application = new Application(placeId, applicationId);
				PDApplication.this.listener.onPDApplicationLoaded(PDApplication.this);	
			}
			
		});
		
		
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationId = applicationName;
	}

	public void setParameterString(String name, String value) {
		parameters.put(name, value);
		remoteStorage.setString(name, value, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(PDApplication.this, "Could not save to remote datastore" );
			}

			@Override
			public void onSuccess(Void result) {
				Log.debug(PDApplication.this, "Saved to remote datastore" );
			}
			
		});
	}


	/**
	 * @return the serverCommunicator
	 */
	public InteractionManagerService getServerCommunicator() {
		return interactionManager;
	}

	/**
	 * @param serverCommunicator the serverCommunicator to set
	 */
	public void setServerCommunicator(InteractionManagerService serverCommunicator) {
		this.interactionManager = serverCommunicator;
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	private void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}

	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @param localStorage the localStorage to set
	 */
	public void setLocalStorage(LocalStorage localStorage) {
		this.localStorage = localStorage;
	}

	/**
	 * @param remoteStorage the remoteStorage to set
	 */
	public void setRemoteStorage(ServerStorage remoteStorage) {
		this.remoteStorage = remoteStorage;
	}

	/**
	 * @return the interactionManager
	 */
	public InteractionManagerService getInteractionManager() {
		return interactionManager;
	}

	/**
	 * @param interactionManager the interactionManager to set
	 */
	public void setInteractionManager(InteractionManagerService interactionManager) {
		this.interactionManager = interactionManager;
	}


	
}
