package org.instantplaces.purewidgets.client.widgets.youtube;

import org.instantplaces.purewidgets.client.json.GenericJson;


public class JsonVideoEntry  extends GenericJson {
    protected JsonVideoEntry() {
    }

    public native final String getId() /*-{
            var id=this.id['$t'];
            return id.substring(id.lastIndexOf('/') + 1);
    }-*/;
    
    public native final void setId(String id) /*-{
    	if ( typeof(this.id) == 'undefined' ) {
    		this.id = {};
    	}
    	this.id['$t'] = "/" + id;
    
	}-*/;
    
    
    
    public native final String getTitle() /*-{
     	return this.title['$t'];
    }-*/;
    
    public native final void setTitle(String title) /*-{
    	if ( typeof(this.title) == 'undefined' ) {
    		this.title = {};
    	}
    	this.title['$t'] = title;;
   	}-*/;
    
    
    public native final String getChannel() /*-{
     return this.author[0].name['$t'];
    }-*/;
    
    public native final void setChannel(String channel) /*-{
    	if ( typeof(this.author) == 'undefined' ) {
    		this.author = {};
    	}
    	if ( typeof(this.author[0]) == 'undefined') {
    		this.author[0]={};
    		this.author[0].name= {};
    	}
    	this.author[0].name['$t'] = channel;
   	}-*/;
    
    
    public native final String getThumbnailURL() /*-{
    	return this.media$group.media$thumbnail[0].url;
    }-*/;
    
    public native final void setThumbnailURL(String thumbnailUrl) /*-{
    	if ( typeof(this.media$group) == 'undefined' ) {
    		this.media$group={};
    	}
    	
    	if ( typeof(this.media$group.media$thumbnail) == 'undefined' ) {
    		this.media$group.media$thumbnail={};
    	}
    	if ( typeof(this.media$group.media$thumbnail[0]) == 'undefined' ) {
    		this.media$group.media$thumbnail[0]={};
    	}
    	    	
 		
		this.media$group.media$thumbnail[0].url = thumbnailUrl;
	}-*/;    
    
    
    public native final String getDescription() /*-{
    	return this.media$group.media$description.$t;
    }-*/;
    
    public native final void setDescription(String description) /*-{
    	if ( typeof(this.media$group) == 'undefined' ) {
    		this.media$group={};
    	}
    	
    	if ( typeof(this.media$group.media$description) == 'undefined' ) {
    		this.media$group.media$description={};
    	}
    	
		this.media$group.media$description.$t = description;
	}-*/;
    
    
    /**
     * 
     * @return
     */
    public native final String getKeywords() /*-{
		return this.media$group.media$keywords.$t;
	}-*/;
    
    public native final void setKeywords(String keywords) /*-{
    	if ( typeof(this.media$group) == 'undefined' ) {
    		this.media$group={};
    	}
    	
    	if ( typeof(this.media$group.media$keywords) == 'undefined' ) {
    		this.media$group.media$keywords={};
    	}
		this.media$group.media$keywords.$t = keywords;
	}-*/;
    
    
    
    public native final String getCategory() /*-{
		return this.media$group.media$category[0].label;
	}-*/;
    public native final void setCategory(String category) /*-{
    	if ( typeof(this.media$group) == 'undefined' ) {
    		this.media$group={};
    	}
    	
    	if ( typeof(this.media$group.media$category) == 'undefined' ) {
    		this.media$group.media$category={};
    	}
    	if ( typeof(this.media$group.media$category[0]) == 'undefined' ) {
    		this.media$group.media$category[0]={};
    	}
		this.media$group.media$category[0].label = category;
	}-*/;
    
    
    public native final String getDuration() /*-{
		return this.media$group.yt$duration.seconds;
	}-*/;
    
    public native final void setDuration(String duration) /*-{
		if ( typeof(this.media$group) == 'undefined' ) {
    		this.media$group={};
    	}
    	
    	if ( typeof(this.media$group.yt$duration) == 'undefined' ) {
    		this.media$group.yt$duration={};
    	}

		this.media$group.yt$duration.seconds = duration;
	}-*/;
    
    
    public native final boolean existsRating() /*-{
		return this.gd$rating != null; 
	}-*/;
    
    
    public native final int getRatingMin() /*-{
			return this.gd$rating.min; 
	}-*/;
    
    public native final void setRatingMin(int ratingMin) /*-{
    	if ( typeof(this.gd$rating) == 'undefined' ) {
    		this.gd$rating={};
    	}
		this.gd$rating.min = ratingMin; 
	}-*/;
    
    
 
    public native final int getRatingMax() /*-{
    	
		return this.gd$rating.max; 
	}-*/;
    
    public native final void setRatingMax(int ratingMax) /*-{
    	if ( typeof(this.gd$rating) == 'undefined' ) {
    		this.gd$rating={};
    	}
		this.gd$rating.max = ratingMax; 
	}-*/;
    
    
    public native final float getRatingAverage() /*-{
    	
		return this.gd$rating.average; 
	}-*/;   
    public native final void setRatingAverage(float ratingAverage) /*-{
    	if ( typeof(this.gd$rating) == 'undefined' ) {
    		this.gd$rating={};
    	}    	
		this.gd$rating.average = ratingAverage; 
	}-*/;  
    
    
    public native final int getRatingNumRaters() /*-{
		return this.gd$rating.numRaters; 
	}-*/;
    public native final void setRatingNumRaters(int numRaters) /*-{
    	if ( typeof(this.gd$rating) == 'undefined' ) {
    		this.gd$rating={};
    	}    	
		this.gd$rating.numRaters = numRaters; 
	}-*/;
    
    public native final String getFavoriteCount() /*-{
    	if ( typeof(this.yt$statistics) != 'undefined' && this.yt$statistics.length > 0) {
			return this.yt$statistics.favoriteCount;
    	} else {
    		return "0";
    	}
	}-*/;  
    
    public native final void setFavoriteCount(String favouriteCount) /*-{
    	if ( typeof(this.yt$statistics) == 'undefined' ) {
    		this.yt$statistics={};
    	}
	
		this.yt$statistics.favoriteCount = favouriteCount;
	}-*/; 
    
    public native final String getViewCount() /*-{
    	if ( null != this.yt$statistics ) {
			return this.yt$statistics.viewCount;
    	}  else {
    		return "0";
    	}
	}-*/;
    public native final void setViewCount(String viewCount) /*-{
	   	if ( typeof(this.yt$statistics) == 'undefined' ) {
    		this.yt$statistics={};
    	}
		this.yt$statistics.viewCount = viewCount;
	}-*/;    
   
}