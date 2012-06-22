package org.purewidgets.server.im.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.purewidgets.shared.logging.Log;


/**
 * Provides JSON serialization and deserialization using the Jackson JSON processor.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class GenericJson {
	
	public static <T> T fromJson(Class<T> clas, String json) {
		 ObjectMapper mapper = new ObjectMapper();
		 
		 T t = null;
		 
		 try {
			t = mapper.readValue(json, clas);
		} catch (JsonParseException e) {
			Log.error(GenericJson.class.getName(), "Error parsing JSON", e);
		} catch (JsonMappingException e) {
			Log.error(GenericJson.class.getName(), "Error in JSON mapping", e);
		} catch (IOException e) {
			Log.error(GenericJson.class.getName(), "Error", e);
		}
		
		return t;
	 }
	
	public final String toJsonString() {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonGenerationException e) {
			Log.error(GenericJson.class.getName(), "Error", e);
		} catch (JsonMappingException e) {
			Log.error(GenericJson.class.getName(), "Error", e);
		} catch (IOException e) {
			Log.error(GenericJson.class.getName(), "Error", e);
		}
		return json;
	}
}
