package org.purewidgets.client.widgets.youtube;

import org.purewidgets.client.json.GenericJson;

public class VideoAdapter {

	public static JsonVideoEntry fromVideo(Video video) {
		JsonVideoEntry jsonVideoEntry = GenericJson.getNew();
		
		jsonVideoEntry.setId(video.getId());
		jsonVideoEntry.setTitle(video.getTitle());
		jsonVideoEntry.setDescription(video.getDescription());
		jsonVideoEntry.setKeywords(join(video.getKeywords(), ","));
		jsonVideoEntry.setRatingMin(video.getRating().getMin());
		jsonVideoEntry.setRatingMax(video.getRating().getMax());
		jsonVideoEntry.setRatingNumRaters(video.getRating().getNumRaters());
		jsonVideoEntry.setRatingAverage(video.getRating().getAverage());
		jsonVideoEntry.setThumbnailURL(video.getThumbnail());
		jsonVideoEntry.setViewCount(video.getViewCount()+"");
		jsonVideoEntry.setChannel(video.getAuthor());
		jsonVideoEntry.setDuration(video.getDuration()+"");
		
		return jsonVideoEntry;
	}
	
	private static String join(String s[], String delimiter) {
		
	    if (s == null || s.length == 0 ) return "";
	    
	    StringBuilder builder = new StringBuilder(s[0]);
	    for (int i = 1; i < s.length; i++ ) {
	        builder.append(delimiter).append(s[i]);
	    }
	    return builder.toString();
	}
	
	public static Video fromJSONVideoEntry(JsonVideoEntry jsonVideo) {
		Video v = new Video(jsonVideo.getId(), 
				jsonVideo.getTitle(),
				jsonVideo.getChannel(),
				jsonVideo.getThumbnailURL());
		
		v.setDescription(jsonVideo.getDescription());
		
		if (jsonVideo.existsRating()) {
			v.setRating(new Rating(jsonVideo.getRatingMin(),
					jsonVideo.getRatingMax(),
					jsonVideo.getRatingAverage(),
					jsonVideo.getRatingNumRaters()));
		}
		v.setViewCount(Long.parseLong(jsonVideo.getViewCount()));
		v.setFavoriteCount(Long.parseLong(jsonVideo.getFavoriteCount()));
		v.setKeywords(parseKeywords(jsonVideo.getKeywords()));
		v.setDuration( Long.parseLong( jsonVideo.getDuration() ) );
		return v;
	}
	
	private static String[] parseKeywords(String keywords) {
		if ( null == keywords ) {
			return new String[0];
		} else {
			return keywords.split(",");
		}
	}

}
