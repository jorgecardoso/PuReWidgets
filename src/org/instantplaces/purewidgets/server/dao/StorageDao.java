package org.instantplaces.purewidgets.server.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import org.instantplaces.purewidgets.shared.Log;
import com.googlecode.objectify.annotation.NotSaved;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class StorageDao {

	/**
	 * This magic number is used to determine if a string in the local storage
	 * is encoded using our encoding for string lists.
	 */
	@NotSaved
	private static String MAGIC_NUMBER = "123ListSerializer";

	@Id
	private String storageId;
	
	/**
	 * Since the datastore can't store Hashmaps, we need to implement our
	 * own hashmap like structure. 
	 * The 'keys' variable holds the names of the name/value pair.
	 */
	private ArrayList<String> keys;
	
	/**
	 * Since the datastore can't store Hashmaps, we need to implement our
	 * own hashmap like structure. 
	 * The 'values' variable holds the values of the name/value pair.
	 */	
	private ArrayList<String> values;
	
	
	@SuppressWarnings("unused")
	private StorageDao() {
	}
	
	public StorageDao(String storageId) {
		this.storageId = storageId;
		this.keys = new ArrayList<String>();
		this.values = new ArrayList<String>();
	}
	
	
	public  String getString(String item) {
		//String name = this.storageId+"-"+item;
		String value = this.getStringFromStorage(item);
		return value;
	}


	public void setString(String item, String value) {
		this.setStringInStorage(item, value);
	}

/**
 * Saves a name/value pair in the DS. If the name already exists, its value will be
 * replaced by the new one. If not, a new pair is created.
 * 
 * @param name The name to store.
 * @param value The value to store.
 */
private void setStringInStorage(String name, String value) {
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
private String getStringFromStorage(String name) {
	if (keys.contains(name)) {
		int index = keys.indexOf(name);
		return values.get(index);
		
	} else {
		return null;
	}
	
}

public void setLongInStorage(String name, long value) {
	this.setString(name, Long.toString(value));
}

public long getLongFromStorage(String name) {
	String value = this.getString(name);
	return Long.parseLong(value);
}

	
	/**
	 * Decodes a string encoded in a list of length:string format.
	 *
	 * @return An ArrayList with the decoded Strings.
	 */
	public static ArrayList<String> decode(String value) {
		if (null == value || !value.startsWith(MAGIC_NUMBER)) {
			return new ArrayList<String>();
		}

		ArrayList<String> list = new ArrayList<String>();

		int lenEnd = -1;
		int start = MAGIC_NUMBER.length();
		do {

			lenEnd = value.indexOf(":", start);
			if (-1 != lenEnd) {

				String lenString = value.substring(start, lenEnd);
				int len = Integer.parseInt(lenString);
				String item = null;
				if (len > 0) {
					item = value.substring(lenEnd + 1, lenEnd + 1 + len);

				}
				start = lenEnd + len + 1;

				list.add(item);
			}
		} while (lenEnd != -1);
		return list;
	}

	/**
	 * Encodes a list of strings in a single string, using a length:string
	 * encoding
	 * 
	 * @param values
	 * @return
	 */
	public static String encode(List<String> values) {
		/*
		 * And encoded null value or empty list is just an empty string. (on decoding empty strings
		 * are return as empty lists)
		 */
		if (null == values || values.size() == 0) {
			return "";
		}
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(MAGIC_NUMBER);
		
		
		for (String s : values) {
			
			/*
			 * If an item is null or zero lenght we just store a zero lenght indication.
			 * On decoding, these items will be converted to null items on the returned list.
			 */
			if (null == s || s.length() == 0) {
				sBuilder.append("0:");
			} else {
				sBuilder.append(s.length()).append(":").append(s);
			}
		}
		return sBuilder.toString();
	}
	

	
	public int [] loadIntList(String name) {
		//name = this.storageId+"-"+name;
		
		String codedList = this.getStringFromStorage(name);
		
		
		if ( null == codedList || codedList.length() == 0) {
			return new int[0];
		}
		
		String stringValues[] = codedList.split(":");
		if ( null == stringValues || stringValues.length < 1 ) {
			return new int[0];
		}
		
		int [] values = new int[stringValues.length];
		
		for ( int i = 0; i < stringValues.length; i++ ) {
			values[i] = Integer.parseInt( stringValues[i] );
		}
		
		return values;
		
	}
	
	public  ArrayList<String> loadList(String name) {
		//name = this.storageId+"-"+name;
		String value = this.getStringFromStorage(name);
		return decode(value);
	}

	public void saveIntList(String name, int [] list) {
		//name = this.storageId+"-"+name;
		if ( null == list || list.length == 0 ) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for ( int v : list ) {
			sb.append(v).append(":");
		}
		
		// delete the end ':'
		sb.deleteCharAt(sb.length()-1);
		
		this.setStringInStorage(name,  sb.toString());
	}

	/**

	 * 
	 * @param name
	 * @param values
	 */
	public void saveList(String name, ArrayList<String> values) {
		//name = this.storageId+"-"+name;
		this.setStringInStorage(name, encode(values));
	}

	/**
	 * @return the keys
	 */
	public ArrayList<String> getKeys() {
		//ArrayList<String> keys = new ArrayList<String>();
		//for ( String completeKey : this.keys ) {
		//	keys.add(completeKey.substring(this.storageId.length()+1));
		//}
		return this.keys;
	}

	
}
