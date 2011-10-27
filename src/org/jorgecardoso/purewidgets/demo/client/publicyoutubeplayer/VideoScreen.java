/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.publicyoutubeplayer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class VideoScreen extends Composite  {

	private static VideoScreenUiBinder uiBinder = GWT
			.create(VideoScreenUiBinder.class);

	interface VideoScreenUiBinder extends UiBinder<Widget, VideoScreen> {
	}

	@UiField 
	DeckPanel deckPanelTop;
	
	@UiField 
	DeckPanel deckPanelBottom;
	
	@UiField
	HTMLPanel htmlPanel;
	
	@UiField
	SimplePanel simplePanelPlayer;
	
	@UiField
	ToPlayNext toPlayNext;
	
	@UiField
	RecentlyPlayed recentlyPlayed;
	
	@UiField
	ActivityStream activityStream;
	

	public VideoScreen() {
		initWidget(uiBinder.createAndBindUi(this));

		
		deckPanelTop.showWidget(0);
	}

	public void showVideo() {
		this.deckPanelTop.showWidget(0);
	}
	
	public void showActivity() {
		this.deckPanelTop.showWidget(1);
		this.deckPanelBottom.showWidget(0);
	}
	
	public void showNext() {
		this.deckPanelTop.showWidget(1);
		this.deckPanelBottom.showWidget(1);
	}
	
	
	public void setTagCloud(Widget tagCloud) {
		this.htmlPanel.add(tagCloud, "top");
	}
	
	public void setYoutubePlayer(Widget youtubePlayer) {
		simplePanelPlayer.setWidget(youtubePlayer);
		//simplePanelPlayer.add();
	}
	
}
