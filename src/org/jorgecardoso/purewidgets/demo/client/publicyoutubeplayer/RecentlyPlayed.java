/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.publicyoutubeplayer;

import org.instantplaces.purewidgets.client.widgets.youtube.Video;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class RecentlyPlayed extends Composite {
	
	
	private static RecentlyPlayedUiBinder uiBinder = GWT
			.create(RecentlyPlayedUiBinder.class);

	interface RecentlyPlayedUiBinder extends UiBinder<Widget, RecentlyPlayed> {
	}

	
	@UiField
	VerticalPanel verticalPanel;

	private VideoActionListener videoEventListener;
	private String action;
	
	
	public  RecentlyPlayed() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}



	public void addEntry(Video v) {
		int numEntries = this.verticalPanel.getWidgetCount();
		if (  numEntries >= PublicYoutubePlayer.MAX_RECENTLY_PLAYED_VIDEOS) {
			VideoActionEntry rpe = (VideoActionEntry) this.verticalPanel.getWidget(numEntries-1);
			
			/*
			 * We have to dispose, in order to allow it to remove the guibutton from the interactionmanager
			 */
			rpe.dispose();
			this.verticalPanel.remove(numEntries-1);
		} 
		
		VideoActionEntry rpe = new VideoActionEntry(v, "Like");
		rpe.setVideoEventListener(this.videoEventListener, this.action);
		this.verticalPanel.insert(rpe, 0);
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
		for (int i = 0; i < this.verticalPanel.getWidgetCount(); i++ ) {
			VideoActionEntry rpe = (VideoActionEntry) this.verticalPanel.getWidget(i);
			rpe.setVideoEventListener(videoEventListener, action);
		}
	}

}
