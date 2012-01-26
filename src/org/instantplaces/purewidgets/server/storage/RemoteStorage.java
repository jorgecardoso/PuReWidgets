/**
 * 
 */
package org.instantplaces.purewidgets.server.storage;

import java.util.ArrayList;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.instantplaces.purewidgets.shared.Log;
import com.google.appengine.api.datastore.Key;


/**
 * The RemoteStorage is a helper class for storing the application's 
 * widgets and name/value pairs on the serverstore.
 * 
 * @author Jorge C. S. Cardoso
 */
@PersistenceCapable
public class RemoteStorage {
	
	/**
	 * The key for the object on the datastore. The class doesn't use this
	 * but the datastore does.
	 */
	@SuppressWarnings("unused")
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	
	/**
	 * Since the datastore can't store Hashmaps, we need to implement our
	 * own hashmap like structure. 
	 * The 'keys' variable holds the names of the name/value pair.
	 */
	@Persistent
	private ArrayList<String> keys;
	
	/**
	 * Since the datastore can't store Hashmaps, we need to implement our
	 * own hashmap like structure. 
	 * The 'values' variable holds the values of the name/value pair.
	 */	
	@Persistent
	private ArrayList<String> values;
	
	
	
	
	/**
	 * Creates and empty RemoteStorage object.
	 */
	public RemoteStorage() {
		this.keys = new ArrayList<String>();
		this.values = new ArrayList<String>();
	}
	
	


	
	/**
	 * Saves a name/value pair in the DS. If the name already exists, its value will be
	 * replaced by the new one. If not, a new pair is created.
	 * 
	 * @param name The name to store.
	 * @param value The value to store.
	 */
	public void setString(String name, String value) {
		Log.debug(this, "Saving " + name + " : " + value);
		if (keys.contains(name)) {
			int index = keys.indexOf(name);
			values.set(index, value);
		} else {
			keys.add(name);
			values.add(value);
		}
		
	}

	/**
	 * Retrieves a value from the DS, given its name.
	 * 
	 * @param name The name of the value to retrieve.
	 * @return The value associated with the name, or null if the name does not exist.
	 */
	public String getString(String name) {
		if (keys.contains(name)) {
			int index = keys.indexOf(name);
			return values.get(index);
			
		} else {
			return null;
		}
		
	}
	
	public void setLong(String name, long value) {
		this.setString(name, Long.toString(value));
	}
	
	public long getLong(String name) {
		String value = this.getString(name);
		return Long.parseLong(value);
	}
	
}
