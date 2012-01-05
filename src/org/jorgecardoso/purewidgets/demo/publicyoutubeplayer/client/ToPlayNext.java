/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.widgets.youtube.JsonVideoEntry;
import org.instantplaces.purewidgets.client.widgets.youtube.Video;
import org.instantplaces.purewidgets.client.widgets.youtube.VideoAdapter;
import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ToPlayNext extends Composite {

	private static ToPlayNextUiBinder uiBinder = GWT
			.create(ToPlayNextUiBinder.class);

	interface ToPlayNextUiBinder extends UiBinder<Widget, ToPlayNext> {
	}

	
	@UiField
	VerticalPanel verticalPanelLeft;
	
	@UiField
	VerticalPanel verticalPanelRight;	

	@UiField
	VerticalPanel verticalPanelQueue;

	private VideoActionListener videoEventListener;
	
	private String action;

	private ArrayList<Video> toPlayNextVideos;
	
	private ArrayList<VideoActionEntry> toPlayNextEntries;
	
	private ArrayList<Video> videoQueue;
	
	
	public ToPlayNext() {
		initWidget(uiBinder.createAndBindUi(this));
		toPlayNextVideos = new ArrayList<Video>();
		toPlayNextEntries = new ArrayList<VideoActionEntry>();
		videoQueue = new ArrayList<Video>();
		this.loadQueueFromLocalStorage();
	}	
	
	
	public void clearSearchResults() {
		for (VideoActionEntry vae : this.toPlayNextEntries)  {
			vae.dispose();
		}
		
		this.toPlayNextEntries.clear();
		this.toPlayNextVideos.clear();
		this.createGui();
	}
	
	
	public void highlight(Video v) {
		for ( VideoActionEntry vae : this.toPlayNextEntries ) {
			if ( vae.getVideo().getId().equals(v.getId()) ) {
				vae.highlight(true);
			} else {
				vae.highlight(false);
			}
		}
	}
	
	private void createGui() {
		createGuiSearchResults();
		
		createGuiQueue();
	}

	/**
	 * 
	 */
	private void createGuiSearchResults() {
		int leftPanelSize = (int)Math.ceil(PublicYoutubePlayer.MAX_TO_PLAY_NEXT_VIDEOS/2.0);
		int rightPanelSize = PublicYoutubePlayer.MAX_TO_PLAY_NEXT_VIDEOS/2;
		
		this.verticalPanelLeft.clear();
		this.verticalPanelRight.clear();
		
		for ( VideoActionEntry vae : this.toPlayNextEntries ) {
			if ( this.verticalPanelLeft.getWidgetCount() < leftPanelSize ) {
				this.verticalPanelLeft.add(vae);
			} else if ( this.verticalPanelRight.getWidgetCount() < rightPanelSize ) {
				this.verticalPanelRight.add(vae);
			}
		}
	}

	/**
	 * 
	 */
	private void createGuiQueue() {
		this.verticalPanelQueue.clear();
		for ( Video video : this.videoQueue ) {
			this.verticalPanelQueue.add(new Label(video.getTitle()));
		}
	}
	
	
	public ArrayList<Video> getEntries() {
		return this.toPlayNextVideos;
	}
	
	public void addSearchResultEntry(Video v) {
		
		/*
		 * Create the VideoActionEntry
		 */
		VideoActionEntry rpe = new VideoActionEntry(v, "Add to queue", false);
		rpe.setVideoEventListener(this.videoEventListener, this.action);
		
		if ( this.toPlayNextVideos.size() >= PublicYoutubePlayer.MAX_TO_PLAY_NEXT_VIDEOS ) {
			VideoActionEntry toRemove = this.toPlayNextEntries.remove(0);
			toRemove.dispose();
			this.toPlayNextVideos.remove(0);
		}
		
		this.toPlayNextVideos.add(v);
		this.toPlayNextEntries.add(rpe);
		
		this.createGuiSearchResults();
	}
	
	public void addQueueEntry(Video v) {
		this.videoQueue.add(v);
		this.createGuiQueue();
		
		this.saveQueueToLocalStorage();
		//JsonVideoEntry jve = VideoAdapter.fromVideo(v);
		//Log.info(jve.toJsonString());
	}
	
	public Video getNextVideoFromQueue() {
		if ( this.videoQueue.size() == 0 ) {
			return null;
		}
		Video video = this.videoQueue.remove(0);
		this.createGuiQueue();
		this.saveQueueToLocalStorage();
		return video;
	}
	

	/**
	 * @return the videoEventListener
	 */
	public VideoActionListener getVideoEventListener() {
		return videoEventListener;
	}

	/**
	 * @param videoEventListener the videoEventListener to set
	 */
	public void setVideoEventListener(VideoActionListener videoEventListener, String action) {
		this.videoEventListener = videoEventListener;
		this.action = action;
		/*
		 * Make sure every entry has our event listener
		 */
		for (int i = 0; i < this.verticalPanelLeft.getWidgetCount(); i++ ) {
			VideoActionEntry rpe = (VideoActionEntry) this.verticalPanelLeft.getWidget(i);
			rpe.setVideoEventListener(videoEventListener, action);
		}
		
		for (int i = 0; i < this.verticalPanelRight.getWidgetCount(); i++ ) {
			VideoActionEntry rpe = (VideoActionEntry) this.verticalPanelRight.getWidget(i);
			rpe.setVideoEventListener(videoEventListener, action);
		}
	}
	
	private void saveQueueToLocalStorage() {
		ArrayList<String> videoSerialized = new ArrayList<String>();
		for ( Video video : this.videoQueue ) {
			videoSerialized.add(VideoAdapter.fromVideo(video).toJsonString());
		}
		PublicDisplayApplication.getLocalStorage().saveList("ToPlayNext-Queue", videoSerialized);
	}
	
	private void loadQueueFromLocalStorage() {
		ArrayList<String> videosSerialized = PublicDisplayApplication.getLocalStorage().loadList("ToPlayNext-Queue");
		for ( String videoSerialized : videosSerialized ) {
			JsonVideoEntry jsonVideo = JsonVideoEntry.fromJson(videoSerialized);
			this.videoQueue.add(VideoAdapter.fromJSONVideoEntry(jsonVideo));
		}
	}

}
