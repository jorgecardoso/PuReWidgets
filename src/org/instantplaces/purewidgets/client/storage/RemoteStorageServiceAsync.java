package org.instantplaces.purewidgets.client.storage;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteStorageServiceAsync {
	public void get(String storageId, String name, AsyncCallback<String[]> callback);
	public void get(String storageId, ArrayList<String> names, AsyncCallback<String[]> callback);
	public void set(String storageId, String name, String value, AsyncCallback<Void> callback);
	public void getAll(String storageId, AsyncCallback<Map<String, String>> callback);
}
