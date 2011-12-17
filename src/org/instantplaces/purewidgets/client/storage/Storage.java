/**
 * 
 */
package org.instantplaces.purewidgets.client.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class Storage {
	/**
	 * This magic number is used to determine if a string in the local storage
	 * is encoded using our encoding for string lists.
	 */
	private static String MAGIC_NUMBER = "123ListSerializer";

	private String appName;
	
	public Storage(String appName) {
		this.appName = appName;
	}
	
	public  String getString(String item) {
		String name = this.appName+"-"+item;
		String value = LocalStorage.getString(name);
		return value;
	}


public void setString(String item, String value) {
	LocalStorage.setString(this.appName+"-"+item, value);
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
		name = this.appName+"-"+name;
		
		String codedList = LocalStorage.getString(name);
		
		
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
		name = this.appName+"-"+name;
		String value = LocalStorage.getString(name);
		return decode(value);
	}

	public void saveIntList(String name, int [] list) {
		name = this.appName+"-"+name;
		if ( null == list || list.length == 0 ) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for ( int v : list ) {
			sb.append(v).append(":");
		}
		
		// delete the end ':'
		sb.deleteCharAt(sb.length()-1);
		
		LocalStorage.setString(name,  sb.toString());
	}

	/**

	 * 
	 * @param name
	 * @param values
	 */
	public void saveList(String name, ArrayList<String> values) {
		name = this.appName+"-"+name;
		LocalStorage.setString(name, encode(values));
	}
}
