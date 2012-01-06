/**
 * 
 */
package org.instantplaces.purewidgets.client.application;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.storage.LocalStorage;
import org.instantplaces.purewidgets.client.storage.RemoteStorage;
import org.instantplaces.purewidgets.client.widgetmanager.ClientServerCommunicator;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The PublicDisplayApplication class represents the graphical part of the
 * public display application. Its main purpose is to extract the application id
 * from the URL query string and initialize the WidgetManager.
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PublicDisplayApplication {
	/**
	 * The URL query string parameter name that holds the application id
	 */
	private static final String APP_NAME_PARAMETER = "appname";

	/**
	 * The application name
	 */
	private static String applicationName;

	/**
	 * Indicates if this application should automatically delete volatile
	 * widgets on startup and finish
	 */
	private static boolean autoDeleteVolatile;

	/**
	 * If the URL does not contain the application id, this application id will
	 * be used as default
	 */
	private static final String DEFAULT_APP_NAME = "DefaultApplication";

	/**
	 * If the URL does not contain the application id, this application id will
	 * be used as default
	 */
	private static final String DEFAULT_PLACE_NAME = "DefaultPlace";

	private static PublicDisplayApplicationLoadedListener listener;

	/**
	 * Indicates if the application has been loaded
	 */
	private static boolean loaded = false;

	/**
	 * The LocalStorage for this application
	 */
	private static LocalStorage localStorage;

	/**
	 * The parameter names for the application-defined options that are stored in the 
	 * remote storage/passed in the URL
	 */
	private static ArrayList<String> parameterNames;
	
	/**
	 * The values for the application-defined options that are stored in the 
	 * remote storage/passed in the URL
	 */
	private static ArrayList<String> parameterValues;

	/**
	 * The URL query string parameter name that holds the place id
	 */
	private static final String PLACE_NAME_PARAMETER = "placename";
	/**
	 * The place name
	 */
	private static String placeName;

	/**
	 * The RemoteStorage for this application
	 */
	private static RemoteStorage remoteStorage;

	/**
	 * @return the applicationName
	 */
	public static String getApplicationName() {
		return applicationName;
	}

	public static LocalStorage getLocalStorage() {
		if (!loaded) {
			Log.error("org.instantplaces.purewidgets.client.aplication.PublicDisplayApplication",
					"Error getting Local Storage: application not loaded yet. Call load() first");
			return null;
		} else {
			return localStorage;
		}
	}

	/** 
	 * This function returns a string application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public static String getParameterString(String name, String defaultValue) {
		if (null == parameterNames || parameterNames.size() == 0)
			return defaultValue;
		String remoteValue = null;
		for (int i = 0; i < parameterNames.size(); i++) {
			if (parameterNames.get(i).equals(name)) {
				if (null != parameterValues && parameterValues.size() > 0) {
					remoteValue = parameterValues.get(i);
					break;
				}
			}
		}

		if (null != remoteValue) {
			return remoteValue;
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
	
	/** 
	 * This function returns an int application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public static int getParameterInt(String name, int defaultValue) {
		String valueString = getParameterString(name, defaultValue+"");
		int toReturn = defaultValue;
		try {
			toReturn = Integer.parseInt(valueString);
		} catch (NumberFormatException nfe) {
			Log.warn(PublicDisplayApplication.class.getName(), "Could not parse '" + valueString + "' into an integer.");
		}

		return toReturn;
	}	
	
	/** 
	 * This function returns a boolean application parameter value by first checking if
	 * it is set in the remote storage, then on the URL. Remote storage parameters have
	 * precedence over URL parameters.
	 */
	public static boolean getParameterBoolean(String name, boolean defaultValue) {
		String valueString = getParameterString(name, defaultValue+"");
		boolean toReturn = defaultValue;
		try {
			toReturn = Boolean.parseBoolean(valueString);
		} catch (NumberFormatException nfe) {
			Log.warn(PublicDisplayApplication.class.getName(), "Could not parse '" + valueString + "' into a boolean (true/false).");
		}

		return toReturn;
	}		
	

	/**
	 * @return the placeName
	 */
	public static String getPlaceName() {
		return placeName;
	}

	public static RemoteStorage getRemoteStorage() {
		if (!loaded) {
			Log.error("org.instantplaces.purewidgets.client.aplication.PublicDisplayApplication",
					"Error getting Remote Storage: application not loaded yet. Call load() first");
			return null;
		} else {
			return remoteStorage;
		}
	}

	public static void load(PublicDisplayApplicationLoadedListener entryPoint,
			String defaultAppName, boolean autoDeleteVolatile) {
		load(entryPoint, defaultAppName, autoDeleteVolatile, null);
	}

	public static void load(PublicDisplayApplicationLoadedListener entryPoint,
			String defaultAppName, boolean autoDeleteVolatile, ArrayList<String> parameters) {
		listener = entryPoint;

		placeName = com.google.gwt.user.client.Window.Location.getParameter(PLACE_NAME_PARAMETER);

		if (null == placeName) {
			placeName = DEFAULT_PLACE_NAME;
		}
		Log.debug(PublicDisplayApplication.class.getName(), "Using place name: " + placeName);

		applicationName = com.google.gwt.user.client.Window.Location
				.getParameter(APP_NAME_PARAMETER);

		if (null == applicationName) {
			if (null == defaultAppName) {
				applicationName = DEFAULT_APP_NAME;
			} else {
				applicationName = defaultAppName;
			}
		}
		Log.debug(PublicDisplayApplication.class.getName(), "Using application name: "
				+ applicationName);

		WidgetManager.get().setServerCommunication(
				new ClientServerCommunicator(placeName, applicationName));

		/*
		 * Delete all volatile widgets that may have left on the server before
		 */
		PublicDisplayApplication.autoDeleteVolatile = autoDeleteVolatile;
		if (autoDeleteVolatile) {
			Log.debug(PublicDisplayApplication.class.getName(), "Removing volatile widgets");
			WidgetManager.get().removeAllWidgets(true);
		}

		PublicDisplayApplication.loaded = true;

		localStorage = new LocalStorage(applicationName);

		remoteStorage = new RemoteStorage(placeName, applicationName);

		Window.addCloseHandler(new CloseHandler<Window>() {

			@Override
			public void onClose(CloseEvent<Window> event) {
				if (PublicDisplayApplication.autoDeleteVolatile) {
					Log.debug(PublicDisplayApplication.class.getName(), "Removing volatile widgets");
					WidgetManager.get().removeAllWidgets(true);
				}
			}
		});

		parameterNames = parameters;
		parameterValues = new ArrayList<String>();

		if (null == parameters) {
			listener.onApplicationLoaded();
			return;
		} else {
			remoteStorage.getStrings(parameterNames, new AsyncCallback<String[]>() {

				@Override
				public void onFailure(Throwable caught) {
					listener.onApplicationLoaded();

				}

				@Override
				public void onSuccess(String[] result) {
					if (null != result) {
						for (String value : result) {
							parameterValues.add(value);
						}
					}
					listener.onApplicationLoaded();

				}

			});
		}

	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public static void setApplicationName(String applicationName) {
		PublicDisplayApplication.applicationName = applicationName;
	}

	/**
	 * @param placeName
	 *            the placeName to set
	 */
	public static void setPlaceName(String placeName) {
		PublicDisplayApplication.placeName = placeName;
	}
}
