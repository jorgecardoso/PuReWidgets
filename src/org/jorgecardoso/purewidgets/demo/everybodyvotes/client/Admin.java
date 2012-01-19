package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.List;

import org.instantplaces.purewidgets.shared.Log;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollService;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollServiceAsync;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui.Poll;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Admin {
	
	
	private PollServiceAsync pollService;
	private Poll poll;
	
	public  void  run() {
		pollService = GWT.create(PollService.class);
		((ServiceDefTarget)pollService).setServiceEntryPoint("/pollservice"); 
		
		createAddPoll();
		//listPolls();
	}
	
	private void createAddPoll() {
		poll = new Poll();
		RootPanel.get("addpoll").add(poll);
	}
	
	
	private void listPolls() {
		pollService.getPolls("DefaultPlace", new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.debug(this, caught.getMessage());
				
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				
				VerticalPanel pollsPanel = new VerticalPanel();
				
				for ( EBVPollDao poll : result ) {
					VerticalPanel pollPanel = new VerticalPanel();
					
					HorizontalPanel questionPanel = new HorizontalPanel();
					questionPanel.add( new Label("Question:") );
					questionPanel.add( new Label(poll.getPollQuestion()) );
					
					pollPanel.add(questionPanel);
					
					int index = 1;
					for ( EBVPollOptionDao option : poll.getPollOptions() ) {
						HorizontalPanel optionPanel = new HorizontalPanel();
						optionPanel.add( new Label("Option " + index++ + ":") );
						optionPanel.add( new Label(option.getOption() ) );
						
						pollPanel.add(optionPanel);
					}
					pollsPanel.add(pollPanel);
				}
				
				
				RootPanel.get("listpolls").add(pollsPanel);
				
			}

		
			
		});
	}
	
	
}
