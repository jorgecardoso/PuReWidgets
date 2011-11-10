package org.instantplaces.purewidgets.shared.widgetmanager;

import org.instantplaces.purewidgets.shared.widgets.Widget;




public interface ServerCommunicator {

	public void addWidget(Widget widget);
	
	public void deleteWidget(Widget widget);
	public void deleteAllWidgets(boolean volatileOnly);
	
	public void getPlaceApplicationsList(boolean active);
	public void getPlaceApplicationsList();
	
	public void getApplicationWidgetsList( String placeId, String applicationId );
	
	public void setServerListener(ServerListener listener);
	
	/**
	 * Enables or disables the automatic input requests. Useful for apps that don't require
	 * remote input.
	 * 
	 * @param automatic
	 */
	public void setAutomaticInputRequests(boolean automatic);
}
