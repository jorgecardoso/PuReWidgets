package org.instantplaces.purewidgets.shared.widgetmanager;

import org.instantplaces.purewidgets.shared.widgets.Widget;




public interface ServerCommunicator {

	public void addWidget(Widget widget);
	public void sendWidgetInput(String placeName, String applicationName, WidgetInput widgetInput, Callback<WidgetInput> callback);
	
	public void deleteWidget(Widget widget);
	public void deleteAllWidgets(boolean volatileOnly);
	
	public void getPlacesList();
	public void getApplicationsList(String placeId, boolean active);
	public void getApplicationsList( String placeId);
	public void getWidgetsList( String placeId, String applicationId );
	
	public void setServerListener(ServerListener listener);
	
	/**
	 * Enables or disables the automatic input requests. Useful for apps that don't require
	 * remote input.
	 * 
	 * @param automatic
	 */
	public void setAutomaticInputRequests(boolean automatic);
	
}
