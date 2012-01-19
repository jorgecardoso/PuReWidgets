package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service;

import java.util.ArrayList;
import java.util.List;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PollServiceAsync {
	public void getPolls(String placeId, AsyncCallback<List<EBVPollDao>> callback);
	
}
