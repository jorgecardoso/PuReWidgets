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
import org.purewidgets.shared.application.Constants;
import org.purewidgets.shared.im.Application;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The PDApplication class represents a running public display application on the client side (browser). It defines 
 * an application life-cycle for the various states of a public display application. (Currently there is only one state
 * but in the future more states will be added for a more fine-grained control of the application execution.)<p>
 * 
 * A public display application is associated with a {@link org.purewidgets.shared.im.Place}  and has a 
 * unique applicationId within that <code>Place</code>.<p>
 * 
 * A PDApplication cannot be directly instantiated. Instead, applications must call 
 * {@link #load(PDApplicationLifeCycle, String)} that will instantiate a new PDApplication object and load from the
 * InteractionManager server the existing information about the application. This is typically done in the onModuleLoad
 * of the GWT module:
 * 
 * <pre>
 * {@literal @}Override
 * public void onModuleLoad() {
 *		
 *     PDApplication.load(this, "PublicYoutubePlayer");
 * }
 * 
 * {@literal @}Override
 * public void onPDApplicationLoaded(PDApplication app) {
 *     this.pdApplication = app; // save the reference for later access if needed
 * }
 * </pre>
 * This code will load the current application and trigger an onPDApplicationLoaded event when done. 
 * "PublicYoutubePlayer" is simply a default id for this application. Typically the application id will be
 * loaded from an URL parameter (applicationid); the same happens with the place id, which is also loaded 
 * from an URL parameter (placeid).<p>
 * 
 * After being loaded, a PDApplication will have a {@link org.purewidgets.client.storage.LocalStorage} and a
 * {@link org.purewidgets.client.storage.ServerStorage} instance associated with it.<p>
 * 
 * A PDApplication provides a standard way to read and store application parameters: name/value pairs that are 
 * stored in the ServerStorage, but that can be overriden by URL parameters with the same name.<p>
 * 
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
	 * The place id.
	 */
	private String placeId;
	
	/**
	 * The application id.
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
	 * The InteractionManagerService used to communicate with the interaction manager server.
	 */
	private InteractionManagerService interactionManager;

	/**
	 * The current application data fetched from the interaction manager.
	 */
	private Application application;
	
	
	/**
	 * The current PDApplication.
	 */
	private static PDApplication current;


	/**
	 * Creates a new PDApplication with the given place id, application id, and an object that implements
	 * the PDApplicationLifeCycle (the GWT module).
	 * 
	 * @param placeId
	 * @param applicationId
	 * @param entryPoint
	 */
	private PDApplication(String placeId, String applicationId, PDApplicationLifeCycle entryPoint) {
		PDApplication.setCurrent(this);
		this.listener = entryPoint;
		this.applicationId = applicationId;
		this.placeId = placeId;
		
		this.localStorage = new LocalStorage(placeId+"-"+applicationId);

		this.remoteStorage = new ServerStorage(placeId+"-"+applicationId);
		
		
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

	/**
	 * Gets the currently running PDApplication. 
	 * 
	 * There should be only one PDApplication loaded. 
	 * 
	 * @return the current
	 */
	public static PDApplication getCurrent() {
		return current;
	}
	
	/**
	 * Loads an application from the interaction manager server and initializes the corresponding PDApplication.
	 * 
	 * @param entryPoint
	 * @param defaultApplicationId
	 */
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

		
		new PDApplication(placeId, applicationId, entryPoint);
	
	}	
	
	/**
	 * @param current the current to set
	 */
	private static void setCurrent(PDApplication current) {
		PDApplication.current = current;
	}	
	
	
	/**
	 * Gets the associated Application.
	 * 
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}		
	
	
	/**
	 * Get the application id.
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	

	/**
	 * Gets the {@link InteractionManagerService} used to communicate with the interaction manager server.
	 * @return The InteractionManagerService used to communicate with the interaction manager server.
	 */
	public InteractionManagerService getInteractionManager() {
		return interactionManager;
	}
	

	/**
	 * Gets the {@link LocalStorage} associated with this application.
	 * 
	 * @return The LocalStorage associated with this application.
	 */
	public  LocalStorage getLocalStorage() {
		return localStorage;
	}
	


	/** 
	 * Gets a parameter value interpreted as a boolean.
	 * 
	 * This method calls <code>getParameterString</code> and then tries to convert the result to a boolean. 
	 * If it cannot convert, returns the defaultValue.
	 * 
	 * @param name The parameter name to retrieve.
	 * @param defaulValue The parameter's default value to return in case the parameter's name does not exist.
	 * @return The parameter value, or <code>defaultValue</code> if the parameter name does not exist or if the value cannot
	 * be converted to a boolean.
	 * 
	 */
	public boolean getParameterBoolean(String name, boolean defaultValue) {
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
	 * Gets a parameter value interpreted as a float.
	 * 
	 * This method calls <code>getParameterString</code> and then tries to convert the result to a float. 
	 * If it cannot convert, returns the defaultValue.
	 * 
	 * @param name The parameter name to retrieve.
	 * @param defaulValue The parameter's default value to return in case the parameter's name does not exist.
	 * @return The parameter value, or <code>defaultValue</code> if the parameter name does not exist or if the value cannot
	 * be converted to a float.
	 */
	public  float getParameterFloat(String name, float defaultValue) {
		String valueString = getParameterString(name, defaultValue+"");
		float toReturn = defaultValue;
		try {
			toReturn = Float.parseFloat(valueString);
		} catch (NumberFormatException nfe) {
			Log.warn(PDApplication.class.getName(), "Could not parse '" + valueString + "' into a float.");
		}

		return toReturn;
	}

	/** 
	 * Gets a parameter value interpreted as an integer.
	 * 
	 * This method calls <code>getParameterString</code> and then tries to convert the result to an integer. 
	 * If it cannot convert, returns the defaultValue.
	 * 
	 * @param name The parameter name to retrieve.
	 * @param defaulValue The parameter's default value to return in case the parameter's name does not exist.
	 * @return The parameter value, or <code>defaultValue</code> if the parameter name does not exist or if the value cannot
	 * be converted to an integer.
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
	 * Gets an application parameter.
	 * 
	 * Application parameters are stored at the ServerStorage, but can be overridden by 
	 * URL parameters.
	 * This method first checks if a URL parameter with the given name exists, and if not,
	 * checks if a parameter with the given name exists in the ServerStorage. If no parameter is found,
	 * returns the defaultValue.
	 * 
	 * @param name The parameter name to retrieve.
	 * @param defaulValue The parameter's default value to return in case the parameter's name does not exist.
	 * @return The parameter value, or <code>defaultValue</code> if the parameter name does not exist.
	 */
	public String getParameterString(String name, String defaultValue) {

		/*
		 * Check the URL for the parameter first
		 */
		String urlValue = com.google.gwt.user.client.Window.Location.getParameter(name);

		if (null != urlValue) {
			return urlValue;
		}

		
		/*
		 * Check remote storage 
		 */
		if (null != parameters && parameters.size() > 0) {

			String remoteValue = null;
		
			remoteValue = parameters.get(name);
			Log.debug("Remote value for " + name + " is " + remoteValue);
			
	
			if (null != remoteValue) {
				return remoteValue;
			}
		} 

	
		return defaultValue;
		
	}

	/**
	 * Gets the id of the place where the application is running.
	 * 
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}

	/**
	 * Gets the {@link ServerStorage} associated with this PDApplication.
	 * 
	 * @return The ServerStorage associated with this PDApplication.
	 */
	public  ServerStorage getRemoteStorage() {
		
		return remoteStorage;
		
	}



	/**
	 * Sets a parameter on this application.
	 * 
	 * Parameters are saved to the ServerStorage.
	 * 
	 * 
	 * @param name The parameter's name.
	 * @param value The parameter's value.
	 */
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




	private void handleParametersFromServer( Map<String, String> result) {
		if (null != result) {
			this.parameters = result;
			
			for ( String key : result.keySet() ) {
				
				Log.debug(this, "Loaded from remote datastore: " + key + ": " +result.get(key) );
			}
		} else {
			
			this.parameters = new HashMap<String, String>();
		
		}
	
		
		String interactionManagerUrl = this.getParameterString(Constants.INTERACTION_MANAGER_URL_PARAMETER_NAME, Constants.INTERACTIONMANAGER_ADDRESS);
		
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
			public void onFailure(Throwable exception) {
				Log.debug(PDApplication.class.getName(), "Could not get Application");
				PDApplication.this.application = new Application(placeId, applicationId);
				PDApplication.this.listener.onPDApplicationLoaded(PDApplication.this);	
			}

			@Override
			public void onSuccess(Application application) {
				Log.debug(PDApplication.class.getName(), "Received application: " + application);
				PDApplication.this.application = application;
				PDApplication.this.listener.onPDApplicationLoaded(PDApplication.this);
				
			}
			
		});
		
		
	}

	
}
