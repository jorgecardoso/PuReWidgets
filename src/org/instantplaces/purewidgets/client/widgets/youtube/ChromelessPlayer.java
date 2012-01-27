package org.instantplaces.purewidgets.client.widgets.youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Chromeless Player API
 * 
 * Based on Uwe Maurer's ChromelessPlayer.java code:
 * http://code.google.com/p/hotforcode/
 * 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class ChromelessPlayer extends Widget {
	public static final int MIN_LOADING_INTERVAL = 1000;
	public static final int MAX_LOADING_INTERVAL = 30000;

	private static final Map<String, ChromelessPlayer> map = new HashMap<String, ChromelessPlayer>();

	private final String name;
	private YTPlayer player;
	private List<PlayerListener> listener = new ArrayList<PlayerListener>();
	private int width, height;
	private Timer loaderTimer;
	private String currentVideoID;
	private boolean listenLoading;

	public ChromelessPlayer(String name, PlayerListener listener) {
		this(name, 640, 480, false, listener);
	}

	public ChromelessPlayer(String name, boolean listenLoading,
			PlayerListener listener) {
		this(name, 640, 480, listenLoading, listener);
	}

	public ChromelessPlayer(String name, int width, int height,
			PlayerListener listener) {
		this(name, width, height, false, listener);
	}

	public ChromelessPlayer(String name, int width, int height,
			boolean listenLoading, PlayerListener listener) {
		this.listenLoading = listenLoading;
		Element swfobject = DOM.createElement("object");
		swfobject.setId(name);

		Element elem = DOM.createDiv();
		elem.appendChild(swfobject);

		setElement(elem);
		this.name = name;
		map.put(name, this);
		this.width = width;
		this.height = height;
		addPlayerListener(listener);
	}

	private void createSwf() {
		this.createSwf(this.width, this.height);
		Log.debug("Creating swf: " + this.width + " " + this.height);
	}

	public void createSwf(int width, int height) {

		player.createSwf(name, width, height);
		// setElement(DOM.getElementById("playerapi"));
		// player.setPlaybackQuality("large");
		// this.setSize(width, height);
	}

	public void addPlayerListener(PlayerListener listener) {
		if (listener == null)
			return;
		this.listener.add(listener);
	}

	public void removePlayerListener(PlayerListener listener) {
		this.listener.remove(listener);
	}

	public void preLoadVideoById(String videoId) {
		this.currentVideoID = videoId;
		this.cueVideoById(videoId);
		this.loadVideoById(videoId, 0);
		this.pause();
		if (this.listenLoading) {
			if (loaderTimer != null) {
				loaderTimer.cancel();
			}
			loaderTimer = new Timer() {
				int minuscount = 0;

				@Override
				public void run() {
					int loaded = ChromelessPlayer.this.getVideoBytesLoaded();
					int total = ChromelessPlayer.this.getVideoBytesTotal();
					if (loaded != -1) {
						int loadedPercent = (int) (100 * (loaded * 1.0 / total));
						Log.debug("Loaded: " + loadedPercent + "% " + loaded);
						if (loaded >= total) {
							//ChromelessPlayer.this.play();
						} else {
							int toWait = map(0, 100,
									ChromelessPlayer.MAX_LOADING_INTERVAL,
									ChromelessPlayer.MIN_LOADING_INTERVAL,
									loadedPercent);
							this.schedule(toWait);
							Log.debug("Waiting " + toWait + " seconds");
							for (PlayerListener pl : ChromelessPlayer.this.listener) {
								pl.onLoading(loadedPercent);
							}
						}
					} else {
						minuscount++;

						if (minuscount > 6) {
							ChromelessPlayer.this
									.preLoadVideoById(ChromelessPlayer.this.currentVideoID);
							Log
									.debug("Something went wrong loading the video... retrying.");
						} else {
							this.schedule(10000);
							Log.debug("Waiting 10 seconds");
						}
						for (PlayerListener pl : ChromelessPlayer.this.listener) {
							pl.onLoading(0);
						}

					}

				}

			};
			loaderTimer.schedule(1000);
		}

	}

	private int map(int sLow, int sHigh, int tLow, int tHigh, int sValue) {
		int sRange = sHigh - sLow;
		int tRange = tHigh - tLow;

		int x = sValue * tRange / sRange;
		return tLow + x;

	}

	/**
	 * 
	 * @@see YTPlayer.cueVideoById()
	 */
	public void cueVideoById(String videoId, int startSeconds,
			String suggestedQuality) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.cueVideoById(videoId, startSeconds, suggestedQuality);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}

	}

	/**
	 * 
	 * @@see YTPlayer.cueVideoById()
	 */
	public void cueVideoById(String videoId, int startSeconds) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.cueVideoById(videoId, startSeconds);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}

	}

	/**
	 * @@see YTPlayer.cueVideoById()
	 */
	public void cueVideoById(String videoId) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.cueVideoById(videoId);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * 
	 * @see YTPlayer.loadVideoById(String videoId, int startSeconds, String
	 *      suggestedQuality)
	 */
	public void loadVideoById(String videoId, int startSeconds,
			String suggestedQuality) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.loadVideoById(videoId, startSeconds, suggestedQuality);
			Log.debug("Loading: " + videoId);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * 
	 * @see YTPlayer.loadVideoById(String videoId, int startSeconds, String
	 *      suggestedQuality)
	 */
	public void loadVideoById(String videoId, int startSeconds) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.loadVideoById(videoId, startSeconds);
			Log.debug("Loading: " + videoId);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * 
	 * @see YTPlayer.loadVideoById(String videoId, int startSeconds, String
	 *      suggestedQuality)
	 */
	public void loadVideoById(String videoId) {
		this.currentVideoID = videoId;
		if (player != null) {
			player.loadVideoById(videoId);
			Log.debug("Loading: " + videoId);
		} else {
			Log.warn(this, "Cannot cue video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * @see YTPlayer.play()
	 */
	public void play() {
		if (player != null) {
			player.playVideo();
			Log.debug("Playing video.");
		} else {
			Log.warn(this, "Cannot play video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * @see YTPlayer.pause()
	 */
	public void pause() {
		if (player != null) {
			player.pauseVideo();
			Log.debug("Pausing video.");
		} else {
			Log.warn(this, "Cannot pause video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * @see YTPlayer.stop()
	 */
	public void stop() {
		if (player != null) {
			if (this.loaderTimer != null) this.loaderTimer.cancel();
			player.stopVideo();
			Log.debug("Stopping video.");
		} else {
			Log.warn(this, "Cannot stop video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * @see YTPlayer.seekTo()
	 */
	public void seekTo(int seconds, boolean allowSeekAhead) {
		if (player != null) {
			player.seekTo(seconds, allowSeekAhead);
			Log.debug("Seeking video to " + seconds + " seconds");
		} else {
			Log.warn(this, "Cannot seek video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * @see YTPlayer#clearVideo()
	 */
	public void clear() {
		if (player != null) {

			player.clearVideo();
			Log.debug("Clearing video.");
		} else {
			Log.warn(this, "Cannot clear video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * player.mute():Void
	 * 
	 * @see YTPlayer#mute()
	 */
	public void mute() {
		if (player != null) {

			player.mute();
			Log.debug("Muting video.");
		} else {
			Log.warn(this, "Cannot mute video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * player.unMute():Void
	 * 
	 * Unmutes the player.
	 */
	public void unMute() {
		if (player != null) {
			player.unMute();
			Log.debug("UnMuting video.");
		} else {
			Log.warn(this, "Cannot unmute video yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * player.isMuted():Boolean
	 * 
	 * @return Returns true if the player is muted, false if not.
	 */
	public boolean isMuted() {
		if (player != null) {
			return player.isMuted();
		} else {
			Log.warn(this, "Cannot get mute state yet. "
					+ "Player not ready (not instantiated).");
			return false;
		}
	}

	/**
	 * player.setVolume(volume:Number):Void
	 * 
	 * Sets the volume.
	 * 
	 * @param volume
	 *            Accepts an integer between 0 and 100.
	 */
	public void setVolume(int volume) {
		if (player != null) {
			player.setVolume(volume);
			Log.debug("Setting volume to " + volume);
		} else {
			Log.warn(this, "Cannot set volume yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	/**
	 * player.getVolume():Number
	 * 
	 * @return Returns the player's current volume, an integer between 0 and
	 *         100. Note that getVolume() will return the volume even if the
	 *         player is muted.
	 */
	public int getVolume() {
		if (player != null) {
			return player.getVolume();
		} else {
			Log.warn(this, "Cannot get volume yet. "
					+ "Player not ready (not instantiated).");
			return 0;
		}
	}

	public void setSize(int width, int height) {
		if (player != null) {
			player.setSize(width, height);
			Log.debug("Setting size: " + width + " " + height);
		} else {
			Log.warn(this, "Cannot set size yet. "
					+ "Player not ready (not instantiated).");
		}
	}

	public int getVideoBytesLoaded() {
		if (player != null) {
			return player.getVideoBytesLoaded();
		} else {
			Log.warn(this, "Cannot get bytes loaded yet. "
					+ "Player not ready (not instantiated).");
			return -1;
		}
	}

	public int getVideoBytesTotal() {
		if (player != null) {
			return player.getVideoBytesTotal();
		} else {
			Log.warn(this, "Cannot get bytes total yet. "
					+ "Player not ready (not instantiated).");
			return -1;
		}
	}

	public int getVideoStartBytes() {
		if (player != null) {
			return player.getVideoStartBytes();
		} else {
			Log.warn(this, "Cannot get start bytes yet. "
					+ "Player not ready (not instantiated).");
			return -1;
		}
	}

	public PlayerState getPlayerState() {
		if (player != null) {
			int state = player.getPlayerState();
			if (state > -1 && state < PlayerState.values().length) {
				return PlayerState.values()[state + 1];
			} else {
				return null;
			}
		} else {
			Log.warn(this, "Cannot get state yet. "
					+ "Player not ready (not instantiated).");
			return null;
		}
	}

	public int getCurrentTime() {
		if (player != null) {
			return player.getCurrentTime();
		} else {
			Log.warn(this, "Cannot get current time yet. "
					+ "Player not ready (not instantiated).");
			return -1;
		}
	}

	public String getPlaybackQuality() {
		if (player != null) {
			return player.getPlaybackQuality();
		} else {
			Log.warn(this, "Cannot get playback quality yet. "
					+ "Player not ready (not instantiated).");
			return "";
		}
	}

	public String[] getAvailableQualityLevels() {
		if (player != null) {
			return player.getAvailableQualityLevels();
		} else {
			Log.warn(this, "Cannot get available quality levels yet. "
					+ "Player not ready (not instantiated).");
			return null;
		}
	}

	public int getDuration() {
		if (player != null) {
			return player.getDuration();
		} else {
			Log.warn(this, "Cannot get duration yet. "
					+ "Player not ready (not instantiated).");
			return -1;
		}
	}

	public String getVideoUrl() {
		if (player != null) {
			return player.getVideoUrl();
		} else {
			Log.warn(this, "Cannot get video url yet. "
					+ "Player not ready (not instantiated).");
			return "";
		}
	}

	public String getVideoEmbedCode() {
		if (player != null) {
			return player.getVideoEmbedCode();
		} else {
			Log.warn(this, "Cannot get video embed code yet. "
					+ "Player not ready (not instantiated).");
			return "";
		}
	}

	public Element getPlayer() {
		return player.cast();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createSwf();
	}

	
	private static void onReady(String playerId, YTPlayer playerElement) {
		// Log.debug("OnReady: " + playerId + " " + playerElement);
		ChromelessPlayer player = map.get(playerId);
		if (player != null) {
			player.player = playerElement;
			// Log.debug("Player:" + player + " Listener:" + player.listener);
			for (PlayerListener pl : player.listener) {
				if (pl != null) {
					pl.onReady();
				} else {
					// Log.debug("Null listener");
				}
			}
		}
	}

	
	private static void onStateChange(String playerId, int state) {
		// Log.debug("OnStateChange " + playerId + " " + state);
		ChromelessPlayer player = map.get(playerId);
		if (player != null && player.listener != null && state >= -1
				&& state < PlayerState.values().length) {
			for (PlayerListener pl : player.listener) {
				/*
				 * state + 1 because the first state is (-1 Unstarted)
				 */
				pl.onStateChange(PlayerState.values()[state + 1]);
			}
		}
	}

	static {
		register();
		Log.debug("Called register");
	}

	private static native void register() /*-{
		$wnd.onYouTubePlayerReady = function(playerId) {
		      var ytplayer = $wnd.document.getElementById(playerId + "api");

		      @org.instantplaces.purewidgets.client.widgets.youtube.ChromelessPlayer::onReady(Ljava/lang/String;Lorg/instantplaces/purewidgets/client/widgets/youtube/ChromelessPlayer$YTPlayer;)(playerId, ytplayer);

		      var name = 'onPlayerStateChange'+playerId;

		      $wnd[name]  = function(state) {	      		  
		              @org.instantplaces.purewidgets.client.widgets.youtube.ChromelessPlayer::onStateChange(Ljava/lang/String;I)(playerId, state);
		      }
		      ytplayer.addEventListener("onStateChange", name);
		  }
	}-*/;

	/**
	 * 
	 * This is the implementation of the Youtube Player API.
	 * 
	 * @see <a
	 *      href="http://code.google.com/intl/pt-PT/apis/youtube/js_api_reference.html">
	 *      YouTube Player API</a>
	 * 
	 * @author Jorge C. S. Cardoso
	 * 
	 */
	public static class YTPlayer extends JavaScriptObject {
		protected YTPlayer() {
		}

		// Queueing functions
		/**
		 * 
		 * Loads the specified video's thumbnail and prepares the player to play
		 * the video. The player does not request the FLV until playVideo() or
		 * seekTo() is called. This method calls the
		 * player.cueVideoById(videoId:String, startSeconds:Number,
		 * suggestedQuality:String):Void Youtube API method.
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 * 
		 * @param startSeconds
		 *            The optional startSeconds parameter accepts a
		 *            float/integer and specifies the time from which the video
		 *            should start playing when playVideo() is called. If you
		 *            specify a startSeconds value and then call seekTo(), then
		 *            the player plays from the time specified in the seekTo()
		 *            call. When the video is cued and ready to play, the player
		 *            will broadcast a video cued event (5).
		 * 
		 * @param suggestedQuality
		 *            The optional suggestedQuality parameter specifies the
		 *            suggested playback quality for the video. Please see the
		 *            definition of the setPlaybackQuality function for more
		 *            information about playback quality.
		 */
		public native final void cueVideoById(String videoId, int startSeconds,
				String suggestedQuality) /*-{
			this.cueVideoById(videoId, startSeconds, suggestedQuality);
		}-*/;

		/**
		 * Cues the video without the optional suggestedQuality parameter.
		 * 
		 * @see YTPlayer#cueVideoById(String, int, String)
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 * @param startSeconds
		 *            The optional startSeconds parameter accepts a
		 *            float/integer and specifies the time from which the video
		 *            should start playing when playVideo() is called. If you
		 *            specify a startSeconds value and then call seekTo(), then
		 *            the player plays from the time specified in the seekTo()
		 *            call. When the video is cued and ready to play, the player
		 *            will broadcast a video cued event (5).
		 */
		public native final void cueVideoById(String videoId, int startSeconds) /*-{
			this.cueVideoById(videoId, startSeconds);
		}-*/;

		/**
		 * Cues the video without the optional parameters startSeconds and
		 * suggestedQuality.
		 * 
		 * @see YTPlayer#cueVideoById(String, int, String)
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 */
		public native final void cueVideoById(String videoId) /*-{
			this.cueVideoById(videoId);
		}-*/;

		/**
		 * 
		 * 
		 * Loads and plays the specified video. Calls the
		 * player.loadVideoById(videoId:String, startSeconds:Number,
		 * suggestedQuality:String):Void Youtube player API method.
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 * 
		 * @param startSeconds
		 *            The optional startSeconds parameter accepts a
		 *            float/integer. If it is specified, then the video will
		 *            start from the closest keyframe to the specified time.
		 * 
		 * @param suggestedQuality
		 *            The optional suggestedQuality parameter specifies the
		 *            suggested playback quality for the video. Please see the
		 *            definition of the setPlaybackQuality function for more
		 *            information about playback quality.
		 */
		public native final void loadVideoById(String videoId,
				int startSeconds, String suggestedQuality) /*-{
			this.loadVideoById(videoId, startSeconds, suggestedQuality);
		}-*/;

		/**
		 * Loads and plays the specified video without specifying the
		 * suggestedQuality parameter
		 * 
		 * 
		 * @see YTPlayer#loadVideoById(String, int, String)
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 * @param startSeconds
		 *            The optional startSeconds parameter accepts a
		 *            float/integer. If it is specified, then the video will
		 *            start from the closest keyframe to the specified time.
		 */
		public native final void loadVideoById(String videoId, int startSeconds) /*-{
			this.loadVideoById(videoId, startSeconds);
		}-*/;

		/**
		 * 
		 * Loads and plays the specified video without specifying the
		 * suggestedQuality and startSeconds parameters.
		 * 
		 * @see YTPlayer#loadVideoById(String, int, String)
		 * 
		 * @param videoId
		 *            The required videoId parameter specifies the YouTube Video
		 *            ID of the video to be played. In YouTube Data API video
		 *            feeds, the <yt:videoId> tag specifies the ID.
		 */
		public native final void loadVideoById(String videoId) /*-{
			this.loadVideoById(videoId);
		}-*/;

		// Playback controls and player settings
		// - Playing a video
		/**
		 * player.playVideo():Void
		 * 
		 * Plays the currently cued/loaded video.
		 */
		public native final void playVideo() /*-{
			this.playVideo();
		}-*/;

		/**
		 * player.pauseVideo():Void
		 * 
		 * Pauses the currently playing video.
		 */
		public native final void pauseVideo() /*-{
			this.pauseVideo();
		}-*/;

		/**
		 * player.stopVideo():Void
		 * 
		 * Stops the current video. This function also cancels the loading of
		 * the video.
		 */
		public native final void stopVideo() /*-{
			this.stopVideo();
		}-*/;

		/**
		 * player.seekTo(seconds:Number, allowSeekAhead:Boolean):Void
		 * 
		 * Seeks to the specified time of the video in seconds. The seekTo()
		 * function will look for the closest keyframe before the seconds
		 * specified. This means that sometimes the play head may seek to just
		 * before the requested time, usually no more than ~2 seconds.
		 * 
		 * @param seconds
		 *            The time to seek to.
		 * 
		 * @param allowSeekAhead
		 *            The allowSeekAhead parameter determines whether or not the
		 *            player will make a new request to the server if seconds is
		 *            beyond the currently loaded video data.
		 */
		public native final void seekTo(int seconds, boolean allowSeekAhead) /*-{
			this.seekTo(seconds, allowSeekAhead);
		}-*/;

		/**
		 * player.clearVideo():Void
		 * 
		 * Clears the video display. This function is useful if you want to
		 * clear the video remnant after calling stopVideo(). Note that this
		 * function has been deprecated in the ActionScript 3.0 Player API.
		 */
		public native final void clearVideo() /*-{
			this.clearVideo();
		}-*/;

		// - Changing the player volume

		/**
		 * player.mute():Void
		 * 
		 * Mutes the player.
		 */
		public native final void mute() /*-{
			this.mute(volume);
		}-*/;

		/**
		 * player.unMute():Void
		 * 
		 * Unmutes the player.
		 */
		public native final void unMute() /*-{
			this.unMute(volume);
		}-*/;

		/**
		 * player.isMuted():Boolean
		 * 
		 * @return Returns true if the player is muted, false if not.
		 */
		public native final boolean isMuted() /*-{
			return this.isMuted();
		}-*/;

		/**
		 * player.setVolume(volume:Number):Void
		 * 
		 * Sets the volume.
		 * 
		 * @param volume
		 *            Accepts an integer between 0 and 100.
		 */
		public native final void setVolume(int volume) /*-{
			this.setVolume(volume);
		}-*/;

		/**
		 * player.getVolume():Number
		 * 
		 * @return Returns the player's current volume, an integer between 0 and
		 *         100. Note that getVolume() will return the volume even if the
		 *         player is muted.
		 */
		public native final int getVolume() /*-{
			return this.getVolume();
		}-*/;

		// - Setting the player size

		/**
		 * player.setSize(width:Number, height:Number):Void
		 * 
		 * Sets the size in pixels of the player. You should not have to use
		 * this method in JavaScript as the player will automatically resize
		 * when the containing elements in the embed code have their height and
		 * width properties modified.
		 */
		public native final void setSize(int width, int height) /*-{
			this.setSize(width, height);
		}-*/;

		// Playback status

		/**
		 * player.getVideoBytesLoaded():Number
		 * 
		 * @return Returns the number of bytes loaded for the current video.
		 */
		public native final int getVideoBytesLoaded() /*-{
			return this.getVideoBytesLoaded();
		}-*/;

		/**
		 * player.getVideoBytesTotal():Number
		 * 
		 * @return Returns the size in bytes of the currently loaded/playing
		 *         video.
		 */
		public native final int getVideoBytesTotal() /*-{
			return this.getVideoBytesTotal();
		}-*/;

		/**
		 * player.getVideoStartBytes():Number
		 * 
		 * 
		 * @return Returns the number of bytes the video file started loading
		 *         from. Example scenario: the user seeks ahead to a point that
		 *         hasn't loaded yet, and the player makes a new request to play
		 *         a segment of the video that hasn't loaded yet.
		 */
		public native final int getVideoStartBytes() /*-{
			return this.getVideoStartBytes();
		}-*/;

		/**
		 * player.getPlayerState():Number
		 * 
		 * 
		 * @return Returns the state of the player. Possible values are
		 *         unstarted (-1), ended (0), playing (1), paused (2), buffering
		 *         (3), video cued (5).
		 */
		public native final int getPlayerState() /*-{
			return this.getPlayerState();
		}-*/;

		/**
		 * player.getCurrentTime():Number
		 * 
		 * 
		 * @return Returns the elapsed time in seconds since the video started
		 *         playing.
		 */
		public native final int getCurrentTime() /*-{
			return this.getCurrentTime();
		}-*/;

		// Playback quality
		/**
		 * player.getPlaybackQuality():String
		 * 
		 * This function retrieves the actual video quality of the current
		 * video. It returns undefined if there is no current video. Possible
		 * return values are hd720, large, medium and small.
		 * 
		 */
		public native final String getPlaybackQuality() /*-{
			return this.getPlaybackQuality();
		}-*/;

		/**
		 * player.setPlaybackQuality(suggestedQuality:String):Void
		 * 
		 * This function sets the suggested video quality for the current video.
		 * The function causes the video to reload at its current position in
		 * the new quality. If the playback quality does change, it will only
		 * change for the video being played.
		 * 
		 * Calling this function does not guarantee that the playback quality
		 * will actually change. If the playback quality does change, it will
		 * only change for the video being played. At that time, the
		 * onPlaybackQualityChange event will fire, and your code should respond
		 * to the event rather than the fact that it called the
		 * setPlaybackQuality function.
		 * 
		 * The suggestedQuality parameter value can be small, medium, large,
		 * hd720 or default. Setting the parameter value to default instructs
		 * YouTube to select the most appropriate playback quality, which will
		 * vary for different users, videos, systems and other playback
		 * conditions.
		 * 
		 * When you suggest a playback quality for a video, the suggested
		 * quality will only be in effect for that video. You should select a
		 * playback quality that corresponds to the size of your video player.
		 * For example, if your page displays a 640px by 360px video player, a
		 * medium quality video will actually look better than a large quality
		 * video. The following list shows recommended playback quality levels
		 * for different player sizes: - Quality level small: Player resolution
		 * less than 640px by 360px. - Quality level medium: Minimum player
		 * resolution of 640px by 360px. - Quality level large: Minimum player
		 * resolution of 854px by 480px. - Quality level hd720: Minimum player
		 * resolution of 1280px by 720px. - Quality level default: YouTube
		 * selects the appropriate playback quality. This setting effectively
		 * reverts the quality level to the default state and nullifies any
		 * previous efforts to set playback quality using the cueVideoById,
		 * loadVideoById or setPlaybackQuality functions.
		 * 
		 * If you call the setPlaybackQuality function with a suggestedQuality
		 * level that is not available for the video, then the quality will be
		 * set to the next lowest level that is available. For example, if you
		 * request a quality level of large, and that is unavailable, then the
		 * playback quality will be set to medium (as long as that quality level
		 * is available).
		 * 
		 * In addition, setting suggestedQuality to a value that is not a
		 * recognized quality level is equivalent to setting suggestedQuality to
		 * default.
		 * 
		 * @param quality
		 *            The quality level.
		 */
		public native final void setPlaybackQuality(String quality) /*-{
			this.setPlaybackQuality(quality);
		}-*/;

		/**
		 * player.getAvailableQualityLevels():Array
		 * 
		 * This function returns the set of quality formats in which the current
		 * video is available. You could use this function to determine whether
		 * the video is available in a higher quality than the user is viewing,
		 * and your player could display a button or other element to let the
		 * user adjust the quality.
		 * 
		 * 
		 * 
		 * Your client should not automatically switch to use the highest (or
		 * lowest) quality video or to any unknown format name. YouTube could
		 * expand the list of quality levels to include formats that may not be
		 * appropriate in your player context. Similarly, YouTube could remove
		 * quality options that would be detrimental to the user experience. By
		 * ensuring that your client only switches to known, available formats,
		 * you can ensure that your client's performance will not be affected by
		 * either the introduction of new quality levels or the removal of
		 * quality levels that are not appropriate for your player context.
		 * 
		 * @return The function returns an array of strings ordered from highest
		 *         to lowest quality. Possible array element values are hd720,
		 *         large, medium and small. This function returns an empty array
		 *         if there is no current video.
		 */
		public native final String[] getAvailableQualityLevels() /*-{
			return this.getAvailableQualityLevels();
		}-*/;

		// Retrieving video information
		/**
		 * player.getDuration():Number
		 * 
		 * @return Returns the duration in seconds of the currently playing
		 *         video. Note that getDuration() will return 0 until the
		 *         video's metadata is loaded, which normally happens just after
		 *         the video starts playing.
		 */
		public native final int getDuration() /*-{
			return this.getDuration();
		}-*/;

		/**
		 * player.getVideoUrl():String
		 * 
		 * 
		 * @return Returns the YouTube.com URL for the currently loaded/playing
		 *         video.
		 */
		public native final String getVideoUrl() /*-{
			return this.getVideoUrl();
		}-*/;

		/**
		 * player.getVideoEmbedCode():String
		 * 
		 * @return Returns the embed code for the currently loaded/playing
		 *         video.
		 */
		public native final String getVideoEmbedCode() /*-{
			return this.getVideoEmbedCode();
		}-*/;

		public native final void createSwf(String name, int width, int height) /*-{
			var params = { allowScriptAccess: "always", bgcolor: "#000000", wmode: "transparent" };

			var atts = { id: name + "api" };
			
			//$wnd.swfobject.embedSWF("http://www.youtube.com/v/u1zgFlCw8Aw?enablejsapi=1&playerapiid=" + name,
			//   name, ""+width, ""+height, "8", null, null, params, atts);
			  $wnd.swfobject.embedSWF("http://www.youtube.com/apiplayer?enablejsapi=1&version=3&playerapiid=" + name,
			   name, ""+width, ""+height, "8", null, null, params, atts);
		}-*/;
	}

}