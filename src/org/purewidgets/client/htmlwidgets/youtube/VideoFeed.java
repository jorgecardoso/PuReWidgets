package org.purewidgets.client.htmlwidgets.youtube;

import java.util.ArrayList;
import java.util.List;

import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoEntry;
import org.purewidgets.client.htmlwidgets.youtube.json.JsonVideoList;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class VideoFeed {
	
	private String url;
	private AsyncCallback<VideoFeed> handler;
	
	private List <Video>videos;
	
	public VideoFeed(String url, AsyncCallback<VideoFeed> handler) {
		videos = new ArrayList<Video>();
		this.url = url;
		this.handler = handler;
		requestJSON();
	}
	
	
	private void requestJSON() {
		Log.debug(this, "Requesting JSON from " + this.url);
		// Send request to server and catch any errors.
		JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);
		
	    
	      //Request request = 
	    builder.requestObject(this.url, new AsyncCallback<JsonVideoList>() {
	        @Override
			public void onFailure( Throwable exception) {
	        	Log.error(this,"Couldn't retrieve JSON", exception);
	        	if ( null != handler ) {
	        		handler.onFailure(exception);
	        	} else {
	        		Log.warn(VideoFeed.this, "No callback defined!"); 
	        	}
	        }

	        @Override
			public void onSuccess(JsonVideoList list) {
	        	//  Log.debug(this, "JSON Request finished. " + list.toJsonString());
	        	  Log.debug(this, "Response version: " + list.getVersion());
	        	  JsArray<JsonVideoEntry> entries = list.getEntries();
	        	  Log.debug(this, "Items: " + entries.length());
//					Log.debug(this, list.getEntries().length() + " videos found.");
					for (int i = 0; i < entries.length(); i++) {
						
					//	Log.debug(this,""+entries.get(i).toJsonString());
						//Log.debug(this, "Thumbnail: " + entries.get(i).getThumbnailURL());
						videos.add(VideoAdapter.fromJSONVideoEntry( entries.get(i) ));
					
					}
					if ( null != handler ) {
						handler.onSuccess(VideoFeed.this);
					} else {
						Log.warn(VideoFeed.this, "No callback defined!");
					}

	        }
	      });
	    
	}
	


	public List <Video> getVideos() {
		return videos;
	}
	
	 
	
	
}
