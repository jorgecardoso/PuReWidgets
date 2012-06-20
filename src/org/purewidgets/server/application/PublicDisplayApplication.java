/**
 * 
 */
package org.purewidgets.server.application;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServletRequest;

import org.purewidgets.server.PMF;
import org.purewidgets.server.storage.RemoteStorage;
import org.purewidgets.server.widgetmanager.ServerServerCommunicator;
import org.purewidgets.shared.Log;
import org.purewidgets.shared.widgets.Widget;

import com.google.appengine.api.datastore.Key;

/**
 * @author Jorge C. S. Cardoso
 *
 */
@PersistenceCapable
public class PublicDisplayApplication {

	/**
	 * The URL query string parameter name that holds the application id
	 */
	public static final String APP_NAME_PARAMETER = "appname";
	
	/**
	 * The URL query string parameter name that holds the place id
	 */
	public static final String PLACE_NAME_PARAMETER = "placename";	
	
	/**
	 * If the URL does not contain the application id, this application id will be used as default
	 */
	private static final String DEFAULT_APP_NAME = "DefaultApplication";

	/**
	 * If the URL does not contain the application id, this application id will be used as default
	 */
	private static final String DEFAULT_PLACE_NAME = "DefaultPlace";
	
	
	@SuppressWarnings("unused")
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * The application id.
	 */
	@Persistent
	private String appId;
	
	/**
	 * The place id.
	 */
	@Persistent
	private String placeId;
	
	@NotPersistent
	private RemoteStorage remoteStorage;
	
	/**
	 * The PersistenceManager used to access the datastore.
	 */
	@NotPersistent
	private PersistenceManager persistenceManager;
	
	/**
	 * The ApplicationLifeCycle object.
	 */
	@NotPersistent
	private ApplicationLifeCycle applicationLifeCycle;
	
	/**
	 * The ServerCommunicator object used to communicate with the InteractionManager.
	 */
	@NotPersistent
	private ServerServerCommunicator serverCommunicator;
	
	
	/**
	 * The widgets used by the app
	 */
	@NotPersistent
	private ArrayList<Widget> widgets;
	
	private boolean firstTime;
	
	protected PublicDisplayApplication (String placeId, String appId, PersistenceManager pm, ApplicationLifeCycle acl) {
		this.placeId = placeId;
		this.appId = appId;
		this.persistenceManager = pm;
		this.applicationLifeCycle = acl;
	}
	
	
	private void init() {
		Log.info(this, "Initing application " + this.appId);
		this.widgets = new ArrayList<Widget>();
		this.remoteStorage = RemoteStorage.get(placeId, appId);
		//WidgetManager.get().setWidgetList(remoteStorage.loadWidgets(this.applicationLifeCycle, persistenceManager));
		
		String interactionManager = this.remoteStorage.getString("imurl", "http://pw-interactionmanager.appspot.com");
		Log.info(this, "Using interaction manager: " + interactionManager);
		serverCommunicator = new ServerServerCommunicator(persistenceManager, this.remoteStorage, this.placeId, this.appId);
		serverCommunicator.setInteractionServerUrl(interactionManager);
//		WidgetManager.get().setServerCommunication(serverCommunicator);
//		
//		Log.debug("Clearing widget manager");
//		WidgetManager.get().getWidgetList().clear();
	}
	
	public void run() {
		Log.info(this, "Running application " + this.appId);
	
    	/*
    	 * Setup is called once for each application only.
    	 */
		if (this.firstTime) {
			Log.debug(this, "Running application for the first time");
			this.applicationLifeCycle.setup();
		}
    	
		Log.debug(this, "Triggering 'start' event");
		this.applicationLifeCycle.start();
		
		Log.debug(this, "Asking for input");
		serverCommunicator.askForInputFromServer();
		
		Log.debug(this, "Triggering 'finish' event");
		this.applicationLifeCycle.finish();
		//remoteStorage.saveWidgets(WidgetManager.get().getWidgetList(), persistenceManager);
		persistenceManager.makePersistent(this);
		
		persistenceManager.close();
	}

	public static void load(HttpServletRequest req, ApplicationLifeCycle acl, String defaultAppName) {
		String appId = req.getParameter(APP_NAME_PARAMETER);
		
		if (null == appId) {
			appId = defaultAppName;
			if (null == appId) {
				appId = DEFAULT_APP_NAME;
				Log.warn(PublicDisplayApplication.class.getCanonicalName(), "Could not read '"+APP_NAME_PARAMETER+"' parameter from query string. Using default appname.");
			}
		}
		Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Using application name: " + appId);
		
		String placeId = req.getParameter(PLACE_NAME_PARAMETER);
		if (null == placeId) {
			placeId = DEFAULT_PLACE_NAME;
			Log.warn(PublicDisplayApplication.class.getCanonicalName(), "Could not read '"+PLACE_NAME_PARAMETER+"' parameter from query string. Using default placename.");
		}
		Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Using place name: " + placeId);
		
		load(placeId, appId, acl);
	}
	
	public static void load(String placeId, String appId, ApplicationLifeCycle acl) {
		Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Launching application '" + appId + "'");
		PersistenceManager pm = PMF.get().getPersistenceManager();

				
		
		// Load application
		Query query = pm.newQuery(PublicDisplayApplication.class);
		query.setFilter("appId == appIdParam && placeId == placeIdParam");
		query.declareParameters("String appIdParam, String placeIdParam");
		
	    PublicDisplayApplication application = null;
	    try {
	        
			@SuppressWarnings("unchecked")
			List<PublicDisplayApplication> results = (List<PublicDisplayApplication>) query.execute(appId, placeId);
	        
	        if (!results.isEmpty()) {
	        	application = results.get(0);
	        	Log.info(PublicDisplayApplication.class.getCanonicalName(), "Retrieved application " + application.appId);

	        	if (results.size() > 1) {
	        		Log.warn(PublicDisplayApplication.class.getCanonicalName(), "But found more matching applications in the DS");
	        		for (PublicDisplayApplication app : results) {
	        			Log.warn(PublicDisplayApplication.class.getCanonicalName(), "Application: " + app.appId + " was also in the DS");
	        		}
	        	}
	        	application.firstTime = false;
	        	application.setPersistenceManager(pm);
	        	application.setApplicationLifeCycle(acl);
	        	application.init();
	        	
	        } else {
	        	Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Application not found. Creating new.");
	        	application = new PublicDisplayApplication(placeId, appId, pm, acl);
	        	application.firstTime = true;
	        	application.setPersistenceManager(pm);
	        	application.setApplicationLifeCycle(acl);
	        	application.init();
	        	

	        }
	    } catch (Exception e) {
	    	Log.error(PublicDisplayApplication.class.getCanonicalName(), "Could not access data store.");
	    	e.printStackTrace();
	    }  finally {
	        query.closeAll();
	    }	    
	    Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Triggering 'loaded' event");
	    acl.loaded(application);
	    
	    Log.debug(PublicDisplayApplication.class.getCanonicalName(), "Running application");
	    application.run();
		//return application;
	}
	
	
	
	/**
	 * Saves a name/value pair in the DS. If the name already exists, its value will be
	 * replaced by the new one. If not, a new pair is created.
	 * 
	 * @param name The name to store.
	 * @param value The value to store.
	 */
	public void setString(String name, String value) {
		this.remoteStorage.setString(name, value);
	}

	/**
	 * Retrieves a value from the DS, given its name.
	 * 
	 * @param name The name of the value to retrieve.
	 * @return The value associated with the name, or null if the name does not exist.
	 */
	public String getString(String name) {
		return this.remoteStorage.getString(name);
	}
	
	public void setLong(String name, long value) {
		this.remoteStorage.setLong(name, value);
	}
	
	public long getLong(String name) {
		return this.remoteStorage.getLong(name);
	}
	
	public void addWidget(Widget w, boolean propagateToServer) {
		Log.debug(this, "Adding widget '" + w.getWidgetId() + "' to application.");
		if ( !this.widgets.contains(w) ) {
			this.widgets.add(w);
		}
		
		if ( propagateToServer ) {
			Log.debug(this, "Sending widget '" + w.getWidgetId() + "' to server.");
			this.serverCommunicator.addWidget(w);
		}
	}
	
	public void removeWidget(Widget w, boolean propagateToServer) {
		Log.debug(this, "Removing widget '" + w.getWidgetId() + "' from application.");
		this.widgets.remove(w);
		
		if ( propagateToServer ) {
			Log.debug(this, "Removing widget '" + w.getWidgetId() + "' from server.");
			this.serverCommunicator.deleteWidget(w);
		}
	}

	private void setPersistenceManager(PersistenceManager pm) {
		this.persistenceManager = pm;
	}

	@SuppressWarnings("unused")
	private PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	private void setApplicationLifeCycle(ApplicationLifeCycle applicationLifeCycle) {
		this.applicationLifeCycle = applicationLifeCycle;
	}

	@SuppressWarnings("unused")
	private ApplicationLifeCycle getApplicationLifeCycle() {
		return applicationLifeCycle;
	}


	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}


	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
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
	 * @return the widgets
	 */
	public ArrayList<Widget> getWidgets() {
		return widgets;
	}


	/**
	 * @param widgets the widgets to set
	 */
	public void setWidgets(ArrayList<Widget> widgets) {
		this.widgets = widgets;
	}


	/**
	 * @return the serverCommunicator
	 */
	public ServerServerCommunicator getServerCommunicator() {
		return serverCommunicator;
	}


	/**
	 * @param serverCommunicator the serverCommunicator to set
	 */
	public void setServerCommunicator(ServerServerCommunicator serverCommunicator) {
		this.serverCommunicator = serverCommunicator;
	}

}
