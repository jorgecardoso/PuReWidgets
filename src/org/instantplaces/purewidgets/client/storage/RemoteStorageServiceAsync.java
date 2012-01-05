package org.instantplaces.purewidgets.client.storage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteStorageServiceAsync {
	public void get(String storageId, String name, AsyncCallback<String[]> callback);
	public void set(String storageId, String name, String value, AsyncCallback<Void> callback);
}
