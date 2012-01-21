/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.ArrayList;
import java.util.List;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.widgets.GuiListBox;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollService;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollServiceAsync;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EveryBodyVotes implements PublicDisplayApplicationLoadedListener, EntryPoint{
	
	private List<EBVPollDao> polls;
	private PollServiceAsync pollService;
	
	@Override
	public void onModuleLoad() {
		
		PublicDisplayApplication.load(this, "EveryBodyVotes", false);
		WidgetManager.get().setAutomaticInputRequests(true);
		
	}

	@Override
	public void onApplicationLoaded() {
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			new Admin().run();
			return;
		} 
		
		pollService = GWT.create(PollService.class);
		((ServiceDefTarget)pollService).setServiceEntryPoint("/pollservice"); 
		
		this.askForPollList();
		
	}
	
	
	

	private void askForPollList() {
		if ( null != this.polls ) {
			this.polls.clear();
		}
		
		pollService.getPolls(PublicDisplayApplication.getPlaceName(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(EveryBodyVotes.class.getName(), "Oops! " + caught.getMessage() );
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					Log.warn(EveryBodyVotes.class.getName(), "No polls found");
				}
				EveryBodyVotes.this.polls = result;
				EveryBodyVotes.this.showPoll(result.get(0));
			}
		});
	}
	
	private void showPoll(EBVPollDao poll) {
		
		ArrayList<String> l = new ArrayList<String>();
		for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
			l.add(pollOption.getOption());
		}
		
		GuiListBox tb = new GuiListBox("poll " + poll.getPollId(), poll.getPollQuestion(), l);
		RootPanel.get("content").add(tb);
		
	}
	
}
