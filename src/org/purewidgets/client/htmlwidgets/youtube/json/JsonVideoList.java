package org.purewidgets.client.htmlwidgets.youtube.json;

import org.purewidgets.client.json.GenericJson;

import com.google.gwt.core.client.JsArray;


/**
 * Adapted from http://code.google.com/p/hotforcode/
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class JsonVideoList extends GenericJson {

    protected JsonVideoList() {
    }
    
    
    public native final String getVersion() /*-{
            return this.apiVersion;
    }-*/;
    
    public native final JsArray<JsonVideoEntry> getEntries() /*-{
    		if ( typeof(this.data.items) != 'undefined' ) {
            	return this.data.items;
    		} else {
    			return new Array();
    		}
    }-*/;
    
}