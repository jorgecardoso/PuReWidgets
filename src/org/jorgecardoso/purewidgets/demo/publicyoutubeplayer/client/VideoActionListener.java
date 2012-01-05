package org.jorgecardoso.purewidgets.demo.publicyoutubeplayer.client;

import org.instantplaces.purewidgets.client.widgets.youtube.Video;
import org.instantplaces.purewidgets.shared.events.ActionEvent;

public interface VideoActionListener {
	public void onVideoAction(ActionEvent<?> event, Video video, String action);
}
