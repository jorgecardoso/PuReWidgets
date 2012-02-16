package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Widget;




public interface ServerCommunicator {

	
	public void addWidget(Widget widget);
	
	public void deleteWidget(Widget widget);
	public void deleteAllWidgets(boolean volatileOnly);
	
	
	/*
	 * Places
	 */
	public void getPlacesList();
	
	/*
	 * Applications
	 */
	/**
	 * Get all applications from a place.
	 * @param placeId
	 */
	public void getApplicationsList( String placeId, Callback<ArrayList<Application>> callback);
	
	public void getApplicationsList(String placeId, boolean active, Callback<ArrayList<Application>> callback);
	
	public void getApplication(String placeId, String applicationId, Callback<Application> callback);
	
	public void setApplication(String placeId, String applicationId, Application application, Callback<Application> callback);
	
	
	public void getWidgetsList( String placeId, String applicationId );
	
	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, Callback<WidgetInput> callback);
	
	public void setServerListener(ServerListener listener);
	
	/**
	 * Enables or disables the automatic input requests. Useful for apps that don't require
	 * remote input.
	 * 
	 * @param automatic
	 */
	public void setAutomaticInputRequests(boolean automatic);
	
}
