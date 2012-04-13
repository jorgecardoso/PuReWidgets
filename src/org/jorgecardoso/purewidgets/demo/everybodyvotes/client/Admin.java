package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.Date;
import java.util.List;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollService;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollServiceAsync;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui.Poll;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui.PollActionListener;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Admin implements PollActionListener {
	
	
	private PollServiceAsync pollService;
	private Poll editPoll;
	
	private List<EBVPollDao> polls;
	
	private TextBox placeTextBox;
	
	public  void  run() {
		WidgetManager.get().setAutomaticInputRequests(false);
		
		pollService = GWT.create(PollService.class);
		((ServiceDefTarget)pollService).setServiceEntryPoint("/pollservice"); 
		
		String place = com.google.gwt.user.client.Window.Location.getParameter("placename");
		
		if ( null == place || place.length() == 0) {
			place = "DefaultPlace";
		}
		placeTextBox = new TextBox();
		placeTextBox.setText(place);
		Button getPollsButton = new Button("Get Polls");
		getPollsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Admin.this.askForPollList();
				
			}
			
		});
		RootPanel.get("place").add( new InlineLabel("Place:") );
		RootPanel.get("place").add(placeTextBox );
		RootPanel.get("place").add(getPollsButton);
		createAddPoll();
		askForPollList();
	}
	
	private void createAddPoll() {
		editPoll = new Poll();
		editPoll.setListener(this);
		
		RootPanel.get("addpoll").add(editPoll);
	}
	
	
	private void askForPollList() {
		if ( null != this.polls ) {
			this.polls.clear();
		}
		RootPanel.get("listpolls").clear();
		pollService.getPolls(placeTextBox.getText(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Oops! " + caught.getMessage() );
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					Window.alert("No polls found");
				}
				Admin.this.polls = result;
				Admin.this.updatePollList();
			}
		});
	}
	
	private void editPoll(EBVPollDao poll) {
		this.editPoll.setPollIdText(poll.getPollId().toString());
		this.editPoll.setQuestionText(poll.getPollQuestion());
		this.editPoll.clearOptions();
		for ( EBVPollOptionDao optionDao : poll.getPollOptions() ) {
			this.editPoll.addOption( optionDao.getOption(), optionDao.getVotes() );
		}
		
		this.editPoll.setShowAfter(new Date(poll.getShowAfter()));
		this.editPoll.setShowUntil(new Date(poll.getShowUntil()));
		this.editPoll.setClosesOn(new Date(poll.getClosesOn()));
	}
	
	private void updatePollList() {
		VerticalPanel pollsPanel = new VerticalPanel();
		
		for ( final EBVPollDao poll : this.polls ) {
			HorizontalPanel pollPanel = new HorizontalPanel();
			//pollPanel.getElement().setPropertyString("id", poll.getPollId()+"");
			
			
			pollPanel.add(new Label(poll.getPollId().toString() ) );
		
			pollPanel.add( new Label(poll.getPollQuestion()) );
			
			Button editButton = new Button("edit");
			editButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	Admin.this.editPoll(poll);
		        	
		        }
		    } );
			pollPanel.add(editButton);
			
			pollsPanel.add(pollPanel);
		}
		
		RootPanel.get("listpolls").add(pollsPanel);		
	}

	@Override
	public void onSavePoll(Poll poll) {
		EBVPollDao pollDao = PollAdapter.getEBVPollDao(poll, this.placeTextBox.getText());
		
		
		pollService.savePoll(pollDao, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Oops! " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				PopupPanel p = new PopupPanel();
				p.setTitle("Saved");
				p.setWidget( new Label("Saved") );
				p.setAutoHideEnabled(true);
				p.center();
				p.show();
				
				askForPollList();
			}
		});
		
		
	}

	@Override
	public void onDeletePoll(Poll poll) {
		
		EBVPollDao pollDao = PollAdapter.getEBVPollDao(poll, this.placeTextBox.getText());
		
		pollService.deletePoll(pollDao, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Oops! " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				PopupPanel p = new PopupPanel();
				p.setTitle("Deleted");
				p.setWidget( new Label("Deleted") );
				p.setAutoHideEnabled(true);
				p.center();
				p.show();
				
				askForPollList();
			}
		});
		
	}
	
	
}
