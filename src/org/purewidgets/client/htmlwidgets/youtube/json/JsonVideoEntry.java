package org.purewidgets.client.htmlwidgets.youtube.json;

import java.util.ArrayList;

import org.purewidgets.client.json.GenericJson;

import com.google.gwt.core.client.JavaScriptObject;

import com.google.gwt.core.client.JsArrayString;


public class JsonVideoEntry  extends GenericJson {
	
    protected JsonVideoEntry() {
    }

    public native final String getId() /*-{
        return this.id;
    }-*/;
    
    public native final void setId(String id) /*-{
    	this.id = id;
	}-*/;
    
    public native final String getTitle() /*-{
     	return this.title;
    }-*/;
    
    public native final void setTitle(String title) /*-{
    	this.title = title;;
   	}-*/;
    
    
    public native final String getChannel() /*-{
     return this.uploader;
    }-*/;
    
    public native final void setChannel(String channel) /*-{
    	this.uploader = channel;
   	}-*/;
    
    
    public native final String getThumbnailURL() /*-{
    	return this.thumbnail.sqDefault;
    }-*/;
    
    public native final void setThumbnailURL(String thumbnailUrl) /*-{
    	if ( typeof(this.thumbnail) == 'undefined' ) {
                this.thumbnail = {};
        }
		this.thumbnail.sqDefault = thumbnailUrl;
	}-*/;    
    
    
    public native final String getDefaultPlayerUrl() /*-{
    	if ( typeof(this.player) != 'undefined' ) {
			return this.player['default']; // apparently gwt has problems with 'this.player.default'
    	} else {
    		return "";
    	}
	}-*/;

public native final void setDefaultPlayerUrl(String playerUrl) /*-{
	if ( typeof(this.player) == 'undefined' ) {
            this.player = {};
    }
	this.player['default'] = playerUrl;
}-*/;   

    
    public native final String getDescription() /*-{
    	return this.description;
    }-*/;
    
    public native final void setDescription(String description) /*-{

		this.description = description;
	}-*/;
    
    
    /**
     * 
     * @return
     */
    private native final JsArrayString getKeywordsAsJsArray() /*-{
    	if ( typeof(this.tags) == "undefined" ) {
    		return Array();
    	} else {
			return this.tags;
    	}
	}-*/;
    
    public final String[] getKeywords() {
    	JsArrayString jsArrayString = this.getKeywordsAsJsArray();
    	
    	
    	if ( jsArrayString.length() == 0 ) {
    		
    		
    		String title[] = this.getTitle().split(" ");
    		ArrayList<String> keywords = new ArrayList<String>();
    		
    		for (String s : title) {
    			s = s.trim();
    			if ( s.length() >= 4 && s.length() < 16) {
    				keywords.add(s);
    			} 
    			
    		}
    		keywords.add(this.getCategory());
    		return keywords.toArray(new String[]{});
    		
    		
    	} else {
    		String []keywords = new String[jsArrayString.length()];
    	
    		for (int i = 0; i < jsArrayString.length(); i++ ) {
    			keywords[i] = jsArrayString.get(i);
    		}
    		return keywords;
    	}
    	
    }
    
    private native final void setKeywordsAsJsArray(JsArrayString keywords) /*-{
    	
		this.tags = keywords;
	}-*/;
    
    public final void setKeywords(String []keywords) {
    	JsArrayString keywordsJsArray = JavaScriptObject.createArray().cast();
    	for (int i = 0; i < keywords.length; i++ ) {
    		keywordsJsArray.set(i, keywords[i]);
    	}
    	this.setKeywordsAsJsArray(keywordsJsArray);
    }
    
    public native final String getCategory() /*-{
		return this.category;
	}-*/;
    
    public native final void setCategory(String category) /*-{
		this.category = category;
	}-*/;
    
    
    public native final String getDuration() /*-{
		return this.duration.toString();
	}-*/;
    
    public native final void setDuration(String duration) /*-{
		this.duration = parseInt(duration);
	}-*/;
    
    
    public native final boolean existsRating() /*-{
		return this.rating != null; 
	}-*/;
    
  
    public native final double getRatingAverage() /*-{
    	if ( typeof(this.rating) == 'undefined' ) {
    		return 0.0;
    	}  	else {
			return this.rating;
    	} 
	}-*/;   
    
    public native final void setRatingAverage(double ratingAverage) /*-{
    	
		this.rating = ratingAverage; 
	}-*/;  
    
    
    public native final int getRatingNumRaters() /*-{
    	if ( typeof(this.ratingCount) == 'undefined' ) {
    		return 0.0;
    	}  	else {
			return this.ratingCount;
    	} 
	}-*/;
    
    public native final void setRatingNumRaters(int numRaters) /*-{
    	
		this.ratingCount = numRaters; 
	}-*/;
    
    public native final String getFavoriteCount() /*-{
   		if ( typeof(this.favoriteCount) == 'undefined' ) {
    		return "0";
    	}  	else {
			return  this.favoriteCount.toString();
    	}
	}-*/;  
    
    public native final void setFavoriteCount(String favouriteCount) /*-{
    	this.favoriteCount = parseInt(favouriteCount);
	}-*/; 
    
    public native final String getViewCount() /*-{
    	return this.viewCount.toString();
    	
	}-*/;
    
    public native final void setViewCount(String viewCount) /*-{
	   	this.viewCount = parseInt(viewCount);
	}-*/;    
   
}