package org.instantplaces.purewidgets.client.widgets.youtube;

import org.instantplaces.purewidgets.client.json.GenericJson;


public class JsonVideoEntry  extends GenericJson {
    protected JsonVideoEntry() {
    }

    public native final String getId() /*-{
            var id=this.id['$t'];
            return id.substring(id.lastIndexOf('/') + 1);
    }-*/;
    
    public native final String getTitle() /*-{
     return this.title['$t'];
    }-*/;
    
    public native final String getChannel() /*-{
     return this.author[0].name['$t'];
    }-*/;
    
    public native final String getThumbnailURL() /*-{
    	return this.media$group.media$thumbnail[0].url;
    }-*/;
    
    public native final String getDescription() /*-{
    	return this.media$group.media$description.$t;
    }-*/;
    
    /**
     * 
     * @return
     */
    public native final String getKeywords() /*-{
		return this.media$group.media$keywords.$t;
	}-*/;
    
    public native final String getCategory() /*-{
		return this.media$group.media$category[0].label;
	}-*/;
    
    public native final String getDuration() /*-{
		return this.media$group.yt$duration.seconds;
	}-*/;
    
    public native final boolean existsRating() /*-{
		return this.gd$rating != null; 
	}-*/;
    
    public native final int getRatingMin() /*-{
			return this.gd$rating.min; 
	}-*/;
    
    public native final int getRatingMax() /*-{
		return this.gd$rating.max; 
	}-*/;
    
    public native final float getRatingAverage() /*-{
		return this.gd$rating.average; 
	}-*/;   
    
    public native final int getRatingNumRaters() /*-{
		return this.gd$rating.numRaters; 
	}-*/;
    
    public native final String getFavoriteCount() /*-{
    	if ( null != this.yt$statistics ) {
			return this.yt$statistics.favoriteCount;
    	} else {
    		return "0";
    	}
	}-*/;  
    
    public native final String getViewCount() /*-{
    	if ( null != this.yt$statistics ) {
			return this.yt$statistics.viewCount;
    	}  else {
    		return "0";
    	}
	}-*/;
    
   
}