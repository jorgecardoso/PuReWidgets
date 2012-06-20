package org.purewidgets.client.widgets.youtube;

import java.util.ArrayList;
import java.util.List;

import org.purewidgets.shared.logging.Log;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class VideoFeed {
	
	private String url;
	private RequestCompleteHandler handler;
	
	private List <Video>videos;
	
	public VideoFeed(String url, RequestCompleteHandler handler) {
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
	          Log.error(this, "Couldn't retrieve JSON");
	          Log.error(this, exception.getMessage());
	        }

	        @Override
			public void onSuccess(JsonVideoList list) {
	          //if (200 == response.getStatusCode()) {
	        	  //Log.debug(this, "JSON Request finished. " + response.getText());
	            //updateTable(asArrayOfStockData(response.getText()));
	        	  //JsonVideoList list = GenericJson.fromJson(response.getText());
				  //Log.debug(this, "test");
					Log.debug(this, list.getEntryLength() + " videos found.");
					for (int i = 0; i < list.getEntryLength(); i++) {
						//Log.debug(list.getEntry(i).getId());
						//Log.debug(""+list.getEntry(i).toJsonString());
						videos.add(VideoAdapter.fromJSONVideoEntry(list.getEntry(i)));
						
						//Image img = new Image(list.getEntry(i).getThumbnailURL());
						//RootPanel.get().add(img);
					}
					handler.onRequestComplete(VideoFeed.this);
	       /*   } else {
	            Log.error (this, "Couldn't retrieve JSON (" + response.getStatusText() + ")" );
	          }*/
	        }
	      });
	    
	}
	


	public List <Video> getVideos() {
		return videos;
	}
	
	 
	
	
}
