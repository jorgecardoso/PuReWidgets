package org.jorgecardoso.purewidgets.demo.client.publicyoutubeplayer;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.instantplaces.purewidgets.client.storage.Storage;
import org.instantplaces.purewidgets.client.widgets.youtube.EmbeddedPlayer;
import org.instantplaces.purewidgets.client.widgets.youtube.PlayerError;
import org.instantplaces.purewidgets.client.widgets.youtube.PlayerListener;
import org.instantplaces.purewidgets.client.widgets.youtube.PlayerState;
import org.instantplaces.purewidgets.client.widgets.youtube.RequestCompleteHandler;
import org.instantplaces.purewidgets.client.widgets.youtube.VideoFeed;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.widgets.GuiTagCloud;

import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.client.widgets.ReferenceCodeFormatter;
import org.instantplaces.purewidgets.client.widgets.youtube.Video;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.widgets.TagCloud;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PublicYoutubePlayer implements EntryPoint, VideoActionListener, ActionListener, RequestCompleteHandler, PlayerListener{
	
	
	private class MyLinkedHashMap extends LinkedHashMap<String, Video> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private static final int MAX_ENTRIES = 50;

		
	    @Override
		protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
	        return size() > MAX_ENTRIES;
	    }
	}
	
	/**
	 * The Url parameter name for setting the maximum video duration
	 */
	private static final String URL_PARAMETER_MAX_VIDEO_DURATION = "maxvideotime";
	
	/**
	 * The default maximum video duration allowed.
	 */
	private static final int DEFAULT_MAX_VIDEO_DURATION = 15*60; // seconds
	
	/**
	 * The Url parameter name for setting the duration of the 'Activity' screen
	 */
	private static final String URL_PARAMETER_ACTIVITY_SCREEN_DURATION = "activityscreentime";
	
	/**
	 * The default value for the duration of the 'Activity' screen
	 */
	private static int DEFAULT_ACTIVITY_SCREEN_DURATION	=	60; // seconds
	
	/**
	 * The Url parameter name for setting the duration of the 'To play next' screen
	 */
	private static final String URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION = "toplaynextscreentime";
	
	/**
	 * The default value for the duration of the 'To play next' screen
	 */
	private static int DEFAULT_TOPLAYNEXT_SCREEN_DURATION = 60; // seconds
	
	
	/**
	 * The Url parameter name for setting the duration of the 'To play next' screen
	 */
	private static final String URL_PARAMETER_TOPLAYNEXT_CONFIRMATION_DURATION = "toplaynextconfirmationtime";
	
	/**
	 * The default value for the duration of the 'To play next' screen
	 */
	private static int DEFAULT_TOPLAYNEXT_CONFIRMATION_DURATION = 15; // seconds
	
	/**
	 * The duration of the 'Activity' screen
	 */
	private int activityScreenDuration;
	
	/**
	 * The duration of the 'To play next' screen
	 */
	private int toPlayNextScreenDuration;
	
	/**
	 * The maximum duration of a video
	 */
	private int maxVideoDuration;
	
	/**
	 * The duration of the confirmation screen before starting to play the video
	 */
	private int toPlayNextConfirmationDuration;
	
	/**
	 * If a video stalls for some reason we wait this period of time before searching another one.
	 */
	private static final int STALLED_WAIT_PERIOD = 20*1000; //miliseconds

	
	/**
	 * Size of the panel with the last played videos;
	 */
	public static final int MAX_RECENTLY_PLAYED_VIDEOS = 3;
	
	
	/**
	 * The size of the To Play Next list
	 */
	public static final int MAX_TO_PLAY_NEXT_VIDEOS = 6;
	
	
	public static final int MAX_STREAM_SIZE = 5;


	private static final int MAX_TAG_CLOUD_SIZE = 25;
	
	
	MyLinkedHashMap allPlayedVideos;
	
	
	ArrayList<String> recentlyPlayedVideos;
	
	
	ArrayList<String> stream;
	
	EmbeddedPlayer youtube;
	
	ArrayList<String> placeTags;
	
	GuiTagCloud gtc;
	
	VideoScreen screen;
	
	Video toPlay;

	Timer stalledTimer;
	
	
	/*
	 * The panel with the recently played videos
	 */
	
	//RecentlyPlayed recentlyPlayed;
	
	/**
	 * The last search tags used.
	 */
	private String lastSearchTags;


	Timer stateTimer;
	
	int currentState;
	
	@Override
	public void onAction(ActionEvent<?> e) {
		GuiWidget source = (GuiWidget)e.getSource();
		
		Log.debug(this, "Received application event");
		Log.debug(this,  e.toDebugString());
		
		 if ( source.getWidgetId().equals("tagcloud") ) {
			this.addToStream(e.getPersona() + " suggested " + (String)e.getParam() );
		}
		
	}
	
	@Override
	public void onError(PlayerError state) {
		Log.debug(this, "Player Error: " + state.name());
		//this.searchVideos();
	}
	
	@Override
	public void onLoading(int percent) {
	}

	
	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "PublicYoutubePlayer");
		
		ReferenceCodeFormatter.setLeftBracket("[");
		ReferenceCodeFormatter.setRightBracket("]");
		
		/*
		 * Parse URL parameters
		 * TODO: centralize the other url parameters here
		 */
		this.maxVideoDuration = this.parseUrlParameterInt(URL_PARAMETER_MAX_VIDEO_DURATION, DEFAULT_MAX_VIDEO_DURATION);
		
		this.activityScreenDuration = this.parseUrlParameterInt(URL_PARAMETER_ACTIVITY_SCREEN_DURATION, DEFAULT_ACTIVITY_SCREEN_DURATION);
		
		this.toPlayNextScreenDuration = this.parseUrlParameterInt(URL_PARAMETER_TOPLAYNEXT_SCREEN_DURATION, DEFAULT_TOPLAYNEXT_SCREEN_DURATION);
		
		this.toPlayNextConfirmationDuration = this.parseUrlParameterInt(URL_PARAMETER_TOPLAYNEXT_CONFIRMATION_DURATION, DEFAULT_TOPLAYNEXT_CONFIRMATION_DURATION);
		
		stalledTimer = new Timer() {
			@Override
			public void run() {
				timerStalledElapsed();
			}
		};
		
		stateTimer = new Timer() {
			@Override
			public void run() {
				timerStateElapsed();
			}
		};
		/*
		 * Load data from the LocalStorage
		 */
		this.loadLocalData();
		

		
		this.initGui();
		
		screen.showVideo();
		this.gotoState(1);
		this.gtc.updateGui();
	}
	
	
	@Override
	public void onReady() {
		Log.debug(this, "Youtube player ready");
		
		if ( this.toPlay != null ) {
			this.youtube.loadVideoById(this.toPlay.getId());
		}
		
	}


	@Override
	public void onRequestComplete(VideoFeed feed) {
		Log.debug(this, "Video search complete");
		
		List<Video> videos = feed.getVideos();
		
		/*
		 * Fill in the originating tags property 
		 */
		for ( Video video : videos ) {
			video.setOriginatingTags(this.lastSearchTags);
		}
		
		/*
		 * Filter videos according to length
		 */
		ArrayList<Video> filteredByLength = new ArrayList<Video>();
		for ( Video video : videos ) {
			if (video.getDuration() <= this.maxVideoDuration ) {
				filteredByLength.add(video);
			}
		}
		
		/*
		 * Filter the duration filtered videos according to whether they have been played or not
		 */
		ArrayList<Video> filteredByLengthAndPlayed = new ArrayList<Video>();
		for (Video video : filteredByLength) {
			if ( !this.allPlayedVideos.keySet().contains( video.getId() ) ) {
				filteredByLengthAndPlayed.add(video);
			}
		}
		
		
		/*
		 * If the duration and played filtered videos are enough to fill the ToPlay panel use them
		 */
		if ( filteredByLengthAndPlayed.size() >= MAX_TO_PLAY_NEXT_VIDEOS ) {
			this.screen.toPlayNext.clear();
			
			for (int i = 0; i < MAX_TO_PLAY_NEXT_VIDEOS;  i++ ) {
				this.screen.toPlayNext.addEntry(filteredByLengthAndPlayed.get(i));
			}
		/*
		 * If not, just add the ones we have
		 */
		} else {
			Log.info(this, "Could not find more than " + filteredByLengthAndPlayed.size() + " videos.");
			for ( Video video : filteredByLengthAndPlayed ) {
				this.screen.toPlayNext.addEntry(video);
			}
		}
		
		
		ArrayList<Video> toPlayList = this.screen.toPlayNext.getEntries();
		
		int rand = (int)(Math.random()*toPlayList.size());
		
		toPlay = toPlayList.get(rand);
		
		//youtube.preLoadVideoById(toPlay.getId());
		//youtube.loadVideoById(toPlay.getId());
		//screen.showActivity();
	}

	@Override
	public void onStateChange(PlayerState state) {
		Log.debug(this, "Player State: " +  state.name());
		
		if ( state == PlayerState.PAUSED || state == PlayerState.UNSTARTED || state == PlayerState.BUFFERING) {
			stalledTimer.schedule(STALLED_WAIT_PERIOD);
			Log.debug(this, "Scheduling stalled timer.");
		} else {
			stalledTimer.cancel();
			Log.debug(this, "Canceling stalled timer.");
		}
		
		if ( state == PlayerState.ENDED ) {
			this.videoEnded();
			
		} 
	}


	
	@Override
	public void onVideoAction(ActionEvent<?> e, Video video, String action) {

		Log.debug("User Clicked youtube" + video);
			//label.setText(label.getText() + e.toDebugString());
			
			//String videoId = source.getWidgetId().substring("btn_like_".length());
			
			//Video video = this.allPlayedVideos.get(videoId);
			
			if ( null != video ) {
				if ( action.equalsIgnoreCase("like") ) {
					Log.debug(this, e.getPersona() + " liked video " + video.getId() );
					this.addToStream(e.getPersona() + " liked video " + video.getTitle());
				
					this.updateTagCloud( video );
				} else if ( action.equalsIgnoreCase("play") ) {
					Log.debug(this, e.getPersona() + "wants to play " + video.getId());
					this.addToStream(e.getPersona() + " wants to play " + video.getTitle());
					this.toPlay = video;
					this.gotoState(3);
				}
			}
			
		
	}


	/**
	 * @param toPlay
	 */
	private void addToAllPlayedVideos(Video toPlay) {
	
		/*
		 * we remove first because the allPlayedVideos is a LinkedHashMap with insertion ordering 
		 * that automatically removes the oldest entries if they exceed the limit. However, put does
		 * not affect the insertion order if the entry already exists. We don't want this behaviour.
		 */
		if ( this.allPlayedVideos.containsKey(toPlay) ) {
			this.allPlayedVideos.remove( toPlay ); 
		}
		
		this.allPlayedVideos.put( toPlay.getId(), toPlay );
		
		this.savePlayedVideos();
	}


	private void addToRecentlyPlayedList(Video video) {

		/*
		 * 
		 */
		this.recentlyPlayedVideos.add( video.getId() );
		
		
		/*
		 * Remove the first 
		 */
		if ( this.recentlyPlayedVideos.size() > MAX_RECENTLY_PLAYED_VIDEOS ) {
			this.recentlyPlayedVideos.remove(0);
		}
		
		
		
		this.screen.recentlyPlayed.addEntry(video);
		
		
		saveRecentlyPlayedVideos();
		
	}


	private void addToStream(String s) {
		this.screen.activityStream.addEntry(s);
		this.stream.add(s);
		
		if ( this.stream.size() > MAX_STREAM_SIZE ) {
			this.stream.remove(0);
		}
		
		
		this.saveStream();
	}

	/**
	 * 
	 */
	private void decreaseTagCloudFrequency() {
		Log.warn(this, "Decreasing tag frequencies");
		
		ArrayList<TagCloud.Tag> tags = gtc.getTagList();
		
		
		for ( TagCloud.Tag tag : tags ) {
			
			int freq = tag.getFrequency();
			
			freq = (int)(freq*0.9);
			if ( freq < 1 ) {
				freq = 1;
			}
			
			tag.setFrequency(freq);
			
		}
		
		gtc.setTagList(tags);
		this.gtc.updateGui();
		this.saveTagCloud();
	}

	
	


	/**
	 * Randomly chooses a tag from the tag cloud, giving more weight to more frequent tags.
	 * @return
	 */
	private String getSearchTag() {
		
		ArrayList<TagCloud.Tag> tags = gtc.getTagListSortedByFrequency();
		
		/*
		 * sum the normalized frequencies
		 */
		float frequencySum = 0;
		for ( TagCloud.Tag tag : tags ) {
			frequencySum += tag.getNormalizedFrequency();
			//Log.debug(this, tag.getWord() + "  " + tag.getNormalizedFrequency());
		}
		
		
		double sum = 0;
		double r = Math.random();
		for ( TagCloud.Tag tag : tags ) {
			
			/*
			 * The normalized frequency is between 1 and 0, but we want the sum of all frequencies to add up to 1
			 * so we now divide the normalized tag frequency by the sum of all tag frequencies.
			 */
			sum += tag.getNormalizedFrequency()/frequencySum;
			
			if ( r < sum ) {
				return tag.getWord();
			}
		}
		/*
		 * We must have something. If the admin didn't set a place tags list just use the word 'youtube'
		 */
		if ( tags.size() > 0 ) {
			return tags.get(tags.size()-1).getWord();
		} else {
			return "youtube";
		}
	}


	

	private void gotoState( int state ) {
		if ( 0 == state ) { // video
			this.currentState = 0;
			this.screen.showVideo();
			this.initVideoPlayer();
			//this.youtube.play();
			
		} else if ( 1 == state ) { // recently played
			this.currentState = 1;
			this.searchVideos();
			this.screen.showActivity();
			stateTimer.schedule(this.activityScreenDuration*1000);
			
			
		} else if ( 2 == state ) { // to play next
			this.currentState = 2;
			this.screen.showNext();
			this.screen.toPlayNext.highlight(this.toPlay);
			stateTimer.schedule(this.toPlayNextScreenDuration*1000);
			
		} else if ( 3 == state ) { //show what is going to play
			this.currentState = 3;
			this.screen.toPlayNext.highlight(this.toPlay);
			stateTimer.schedule(this.toPlayNextConfirmationDuration*1000);
		}
	}
	
	
	private void initGui() {
		Log.warn(this, "initGui");
		screen = new VideoScreen();
		
		RootPanel.get().add(screen);
		
		this.initTagCloud();
		
		this.initStream();
		
		this.initRecentlyPlayedVideos();
		
		this.initToPlayNext();
		
		this.initVideoPlayer();
		this.gtc.updateGui();
	}
	
	

	private void initRecentlyPlayedVideos() {
	
		
		//this.screen.recentlyPlayed = new RecentlyPlayed(MAX_RECENTLY_PLAYED_VIDEOS);
		this.screen.recentlyPlayed.setVideoEventListener(this, "like");
		for (String recentlyPlayedId : this.recentlyPlayedVideos ) {
			this.screen.recentlyPlayed.addEntry(this.allPlayedVideos.get(recentlyPlayedId));
		}
		//this.recentlyPlayed.setEntries(this.re)
		//screen.setRecentlyPlayed(this.screen.recentlyPlayed);
	}


	private void initStream() {		
		for ( String s : this.stream ) {
			this.screen.activityStream.addEntry(s);
		}
	}

	private void initTagCloud() {
		//RootPanel.get("tagcloud").add(gtc);
		screen.setTagCloud(gtc);
		
	}
	
	



	private void initToPlayNext() {
		this.screen.toPlayNext.setVideoEventListener(this, "play");
		
	}
	
	private void initVideoPlayer() {
		youtube = new EmbeddedPlayer("youtubeplayer", "100%", "100%", false, this);
		//RootPanel.get("youtube").add(youtube);
		screen.setYoutubePlayer(youtube);
	}

	private void loadAllPlayedVideos() {
		ArrayList<String> videoIds = PublicDisplayApplication.getStorage().loadList("PlayedVideoIds");
		
		ArrayList<String> videoTitles = PublicDisplayApplication.getStorage().loadList("PlayedVideoTitles");

		ArrayList<String> videoThumbnails = PublicDisplayApplication.getStorage().loadList("PlayedVideoThumbnails");
		
		ArrayList<String> videoKeywords = PublicDisplayApplication.getStorage().loadList("PlayedVideoKeywords");
		
		ArrayList<String> videoOriginatingKeywords = PublicDisplayApplication.getStorage().loadList("PlayedVideoOriginatingKeywords");
		
		
		this.allPlayedVideos = new MyLinkedHashMap();
		
		/*
		 * If anything is null return an empty hashmap
		 */
		
		if ( null == videoIds || 
				null == videoTitles ||
				null == videoThumbnails ||
				null == videoKeywords ||
				null == videoOriginatingKeywords ) {
			return;
		}
		
		/*
		 * If there is discrepancy in the lengths of the lists, return empty
		 */
		if ( videoIds.size() != videoTitles.size() ||  
				videoIds.size() != videoThumbnails.size() ||
				videoIds.size() != videoKeywords.size() ||
				videoIds.size() != videoOriginatingKeywords.size() ) {
			return;
		}
		
		
		for ( int i = 0; i < videoIds.size(); i++ ) {
			Video v = new Video(videoIds.get(i), videoTitles.get(i), "", videoThumbnails.get(i));
			
			v.setKeywords( Storage.decode( videoKeywords.get(i) ).toArray(new String[]{}) );
			v.setOriginatingTags(videoOriginatingKeywords.get(i));
			
			this.allPlayedVideos.put(videoIds.get(i), v);
		}
	
		
		
	}


	/**
	 * Loads the data from the localstorage.
	 */
	private void loadLocalData() {

		this.loadAllPlayedVideos();
		
		this.loadRecentlyPlayedVideos();
		
		this.loadStream();
		
		this.loadTagCloud();
		
	}
	
	private void loadRecentlyPlayedVideos() {
		this.recentlyPlayedVideos = PublicDisplayApplication.getStorage().loadList("LastPlayedVideos");
		/*
		 * Trim down to the proper size (we may have changed the size limit)
		 */
		while ( this.recentlyPlayedVideos.size() > MAX_RECENTLY_PLAYED_VIDEOS ) {
			this.recentlyPlayedVideos.remove(0);
		}
	}



	private void loadStream() {
		this.stream = PublicDisplayApplication.getStorage().loadList("Stream");
		while ( this.stream.size() > MAX_STREAM_SIZE ) {
			this.stream.remove(0);
		}
	}


	private void loadTagCloud() {
		ArrayList<String> keywords = PublicDisplayApplication.getStorage().loadList("TagCloudKeywords");
		
		String userTags =  com.google.gwt.user.client.Window.Location.getParameter("allowusertags");
		boolean allowUserTags = false;
		
		
		if ( userTags != null && userTags.equalsIgnoreCase("true") ) {
			allowUserTags = true;
		}
		
		int [] frequencies = PublicDisplayApplication.getStorage().loadIntList("TagCloudFrequencies");
		
		if ( keywords.size() == 0 || frequencies.length == 0 ) {
			gtc = new GuiTagCloud("tagcloud", allowUserTags);
			
		} else {
			gtc = new GuiTagCloud("tagcloud", keywords.toArray( new String[]{}), frequencies, allowUserTags);
		}
		
		
		this.gtc.addActionListener(this);
		
		/*
		 * Read the place tags from the URL
		 */
		String urlPlaceTags = com.google.gwt.user.client.Window.Location.getParameter("placetags");
		Log.debug("Place Tags: " + urlPlaceTags);
		this.placeTags = new ArrayList<String>();
		
		/*
		 * Make sure the tagcloud has the placetags
		 */
		if ( null != urlPlaceTags && urlPlaceTags.length() > 0 ) {
			String tags[] = urlPlaceTags.split(",");
			
			for ( String tag : tags ) {
				this.placeTags.add(tag);
				if ( !gtc.contains(tag) ) {
					gtc.addTag(tag, 1);
				}
			}
		}
		
		
	}
	
	/**
	 * 
	 */
	private int parseUrlParameterInt(String urlParameterName, int defaultValue) {
		String parameterValueString =  com.google.gwt.user.client.Window.Location.getParameter(urlParameterName);
		int value = defaultValue;
		try {
			value = Integer.parseInt(parameterValueString);
		} catch (NumberFormatException nfe) {
			Log.warn(this, "Could not parse '"+urlParameterName+"' URL parameter value: " + parameterValueString +". Using default value: " + defaultValue);
		}
		return value;
	}
	
	private void savePlayedVideos() {
		ArrayList<String> videoIds = new ArrayList<String>(); 
		
		ArrayList<String> videoTitles = new ArrayList<String>(); 

		ArrayList<String> videoThumbnails = new ArrayList<String>(); 
		
		ArrayList<String> videoKeywords = new ArrayList<String>(); 
		
		ArrayList<String> videoOriginatingKeywords = new ArrayList<String>(); 
		
		for ( String key : this.allPlayedVideos.keySet() ) {
			videoIds.add(key);
			videoTitles.add(allPlayedVideos.get(key).getTitle());
			videoThumbnails.add(allPlayedVideos.get(key).getThumbnail());
			
			
			videoKeywords.add(Storage.encode( Arrays.asList( allPlayedVideos.get(key).getKeywords() ) ));
			
			videoOriginatingKeywords.add( allPlayedVideos.get(key).getOriginatingTags() );
		}
		
		PublicDisplayApplication.getStorage().saveList("PlayedVideoIds", videoIds);
		PublicDisplayApplication.getStorage().saveList("PlayedVideoTitles", videoTitles);
		PublicDisplayApplication.getStorage().saveList("PlayedVideoThumbnails", videoThumbnails);
		PublicDisplayApplication.getStorage().saveList("PlayedVideoKeywords", videoKeywords);
		PublicDisplayApplication.getStorage().saveList("PlayedVideoOriginatingKeywords", videoOriginatingKeywords);
		
		
	}
	
	
	private void saveRecentlyPlayedVideos() {
		PublicDisplayApplication.getStorage().saveList("LastPlayedVideos", this.recentlyPlayedVideos);
	}
	
	private void saveStream() {
		PublicDisplayApplication.getStorage().saveList("Stream", this.stream);
		
	}
	private void saveTagCloud() {
		ArrayList<TagCloud.Tag> tags = gtc.getTagList();
		
		ArrayList<String> keywords = new ArrayList<String>();
		int[] frequencies = new int[tags.size()];
		
		int i = 0;
		for ( TagCloud.Tag t : tags ) {
			keywords.add(t.getWord());
			frequencies[i++] = t.getFrequency();
		}
		
		PublicDisplayApplication.getStorage().saveList("TagCloudKeywords", keywords);
		PublicDisplayApplication.getStorage().saveIntList("TagCloudFrequencies", frequencies);
	}
	
	private void searchVideos() {
		this.lastSearchTags = getSearchTag();
		Log.debug(this, "Searching for: " + this.lastSearchTags);
		new VideoFeed(
				"http://gdata.youtube.com/feeds/api/videos?max-results=50&orderby=rating&alt=json&q=" + lastSearchTags +"&key=AI39si4VBKUt9aEEwH9balCm6H5Dqf8iAIUemNuFQRmjpWaP8c2XV3tCZLcidL6oW6cdl-84IP5nk2Lsofp1H9Jhj7zgKfAZqg",
				//"http://gdata.youtube.com/feeds/api/videos?alt=json&author=eartes",
				//"http://gdata.youtube.com/feeds/api/standardfeeds/top_rated?alt=json",
				this);		
	}
	
	private void timerStalledElapsed() {
		this.youtube.stop();
		this.videoEnded();
	}
	
	private void timerStateElapsed() {
		if ( 1 == this.currentState ) {
			this.gotoState(2);
		} else if ( 2 == this.currentState ) {
			this.gotoState(3);
		} else if ( 3 == this.currentState ) {
			this.gotoState(0);
		}
	}
	
	


	private void updateTagCloud( Video video ) {
		
		TagCloud.Tag originTag = this.gtc.getTag( video.getOriginatingTags() );
		Log.warn(this, "Updating tag cloud with tag '" +originTag + "' from video '" + video.getId() + "'");
		if ( null == originTag ) {
			return;
		}
		int f = originTag.getFrequency();
		f = f + 3;
		if ( f > 50 ) {
			f = 50;
		}
		originTag.setFrequency(f);
		Log.warn(this, "Updating tag cloud 1 ");
		//
		
		/*
		 * Add two keywords of the video to the tag cloud, that aren't already on the tag cloud
		 */
		String[] keywords = video.getKeywords();
		
		
		
		int count = 0;
		for ( String keyword: keywords ) {
			if ( !this.gtc.contains(keyword) ) {
				this.gtc.addTag(keyword, 6);
				count ++;
				if ( count >= 2 ) {
					break;
				}
			}
		}
		Log.warn(this, "Updating tag cloud 2");
		
		/*
		 * Limit tag cloud size.
		 * 
		 * TODO: make this more efficient
		 */
		while ( this.placeTags.size() <= MAX_TAG_CLOUD_SIZE && this.gtc.getKeywords().size() > MAX_TAG_CLOUD_SIZE ) {
			ArrayList<TagCloud.Tag> tags = this.gtc.getTagListSortedByFrequency(false);
			Iterator<TagCloud.Tag> it = tags.iterator();
			
			while( it.hasNext() ) {
				
				TagCloud.Tag tag = it.next();
				
				if ( !this.placeTags.contains( tag.getWord() ) ) {
					//remove
					it.remove();
					break;
				}
			}
			
			this.gtc.setTagList(tags);
			
			Log.warn(this, "Updating tag cloud 3");
		}
		this.gtc.updateGui();
		this.saveTagCloud();
	}

	private void videoEnded() {

		Log.debug(this, "Finished playing " + this.toPlay.getTitle());
		this.addToAllPlayedVideos( this.toPlay );
		this.addToRecentlyPlayedList( this.toPlay );
		
		this.gotoState(1);
		decreaseTagCloudFrequency();
	}
	
	
}
