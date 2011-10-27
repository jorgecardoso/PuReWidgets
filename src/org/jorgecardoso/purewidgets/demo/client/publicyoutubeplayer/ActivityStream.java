/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.publicyoutubeplayer;

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
public class ActivityStream extends Composite{

	private static ActivityStreamUiBinder uiBinder = GWT
			.create(ActivityStreamUiBinder.class);

	interface ActivityStreamUiBinder extends UiBinder<Widget, ActivityStream> {
	}

	@UiField
	VerticalPanel verticalPanel;
	
	
	public ActivityStream() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	

	public void addEntry(String message) {
		if ( this.verticalPanel.getWidgetCount() >= PublicYoutubePlayer.MAX_STREAM_SIZE ) {
			this.verticalPanel.remove(this.verticalPanel.getWidgetCount()-1);
		}
		this.verticalPanel.insert(this.createStreamEntryPanel(message+"-teste"), 0);
	}
	
	


	private Widget createStreamEntryPanel(String s) {
		Label l  = new Label(s);
		return l;
	}
	

}
