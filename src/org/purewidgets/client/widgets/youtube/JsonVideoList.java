package org.purewidgets.client.widgets.youtube;

import org.purewidgets.client.json.GenericJson;

import com.google.gwt.core.client.JavaScriptObject;
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
            return this.data.items;
    }-*/;
    
}