/**
 * 
 */
package org.instantplaces.purewidgets.client.storage;
import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
@RemoteServiceRelativePath("/storageservice")
public interface RemoteStorageService extends RemoteService {
	public String[] get(String storageId, String name);
	public String[] get(String storageId, ArrayList<String> names);
	public void set(String storageId, String name, String value);
	
	public Map<String, String> getAll(String storageId);
}
