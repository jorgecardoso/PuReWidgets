/**
 * 
 */
package org.instantplaces.purewidgets.client.storage;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
@RemoteServiceRelativePath("/storageservice")
public interface RemoteStorageService extends RemoteService {
	public String get(String storageId, String name);
	public void set(String storageId, String name, String value);
}
