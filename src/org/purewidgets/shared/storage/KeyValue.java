package org.purewidgets.shared.storage;

import java.io.Serializable;

/**
 * A general KeyValue pair that can be used in RPC messages.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class KeyValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	
	/**
	 * Creates an empty KeyValue.
	 */
	public KeyValue() {
		
	}

	/**
	 * Creates a new KeyValue pair with the given key and value.
	 * @param key The key.
	 * @param value The value.
	 */
	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Gets the key of this key/value pair.
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key for this key/value pair
	 * 
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the value of this key/value pair.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of this key/value pair.
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
