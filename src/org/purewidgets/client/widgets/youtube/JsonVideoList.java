package org.purewidgets.client.widgets.youtube;

import org.purewidgets.client.json.GenericJson;


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
            return this.version;
    }-*/;
    
    public native final int getEntryLength() /*-{
            return this.feed.entry.length;
    }-*/;
    
    public native final JsonVideoEntry getEntry(int i) /*-{
            return this.feed.entry[i];
    }-*/;
    

}