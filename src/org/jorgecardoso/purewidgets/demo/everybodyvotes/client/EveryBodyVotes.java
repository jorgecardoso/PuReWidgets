/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.instantplaces.purewidgets.client.storage.LocalStorage;
import org.instantplaces.purewidgets.client.widgets.GuiListBox;
import org.instantplaces.purewidgets.client.widgets.GuiTextBox;
import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollService;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service.PollServiceAsync;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;



/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EveryBodyVotes implements ActionListener, PublicDisplayApplicationLoadedListener, EntryPoint{
	
	private static final String LS_CURRENT_POLL_INDEX = "currentPollIndex";
	
	private static final int POLL_DISPLAY_INTERVAL = 60000; 
	private static final int POLL_RESULT_DISPLAY_INTERVAL = 15000; 
	
	private int currentPollIndex;
	
	private LocalStorage localStorage;
	
	private List<EBVPollDao> polls;
	
	private HashMap<String, GuiListBox> widgets;
	
	private PollServiceAsync pollService;
	
	/*
	 * Indicates if we are showing the poll or the poll result due to user interaction
	 */
	private boolean showingPollResult = false;
	
	private Timer timer;
	
	private long timerRegularStart;
	private long timerResultStart;
	/*
	 * Indicates how many service responses we got. Used to trigger the gui when we have received both
	 * the closed polls and the active polls. 
	 */
	private int receivedCount = 0;
	
	@Override
	public void onApplicationLoaded() {
		Application app = PublicDisplayApplication.getApplication();
		if ( "/everybodyvotes/icon.jpg" != app.getIconBaseUrl() ) {
			app.setIconBaseUrl("/everybodyvotes/icon.jpg");
			WidgetManager.get().getServerCommunicator().setApplication(app.getPlaceId(), app.getApplicationId(), app, null);
			
		} 
		String page = Window.Location.getPath();
		if ( page.contains("admin.html") ) {
			new Admin().run();
			return;
		} 
		
		WidgetManager.get().setAutomaticInputRequests(true);
		
		this.localStorage = PublicDisplayApplication.getLocalStorage();
		
		Integer currentPoll =  this.localStorage.getInteger(LS_CURRENT_POLL_INDEX);
		
		//Window.alert(currentPoll.toString());
		
		if ( null == currentPoll ) {
			this.currentPollIndex = -1;
		} else {
			this.currentPollIndex = currentPoll.intValue();
		}
		
		pollService = GWT.create(PollService.class);
		((ServiceDefTarget)pollService).setServiceEntryPoint("/pollservice"); 
		
		
		/*
		 * This makes sure that the application is created on the server side
		 * 
		 */
		pollService.updatePolls(PublicDisplayApplication.getPlaceName(), 
				PublicDisplayApplication.getApplicationName(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Log.warn(EveryBodyVotes.class.getName(), "Could not update polls!");
					}

					@Override
					public void onSuccess(Void result) {
						Log.warn(EveryBodyVotes.class.getName(), "Polls updated successfully");
					}
		});

		
		this.askForPollList();
		
	}

	@Override
	public void onModuleLoad() {
		/*
		 * Load the Google visualization API and then the PublicDisplayApplication
		 */
		VisualizationUtils.loadVisualizationApi(new Runnable() {
			@Override
			public void run() {
				PublicDisplayApplication.load(EveryBodyVotes.this, "EveryBodyVotes", false);
			}
		} , CoreChart.PACKAGE);
		
		
	}
	
	private void startRegularTimer(int delay) {
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					EveryBodyVotes.this.onTimerElapsed();
				}
			};
		}
		timer.schedule(delay);
		this.timerRegularStart = System.currentTimeMillis();
		this.showingPollResult = false;
	}
	
	private void startRegularTimer() {
		this.startRegularTimer(POLL_DISPLAY_INTERVAL);
	}
	
	private void startResultTimer(){
		if ( null == timer ) {
			timer = new Timer() {
				@Override
				public void run() {
					EveryBodyVotes.this.onTimerElapsed();
				}
			};
		}
		timer.schedule(POLL_RESULT_DISPLAY_INTERVAL);
		this.timerResultStart = System.currentTimeMillis();
		this.showingPollResult = true;
	}

	private void onTimerElapsed() {
		if ( this.showingPollResult ) {
			this.startRegularTimer((int)(POLL_DISPLAY_INTERVAL- (this.timerResultStart-this.timerRegularStart)) );
			this.showPoll(this.polls.get(this.currentPollIndex));
		} else {
			this.advancePoll();
		}
		
	}
	
	
	private void advancePoll() {
		Log.debug(this, "Advancing poll");
		if ( null == this.polls || this.polls.size() < 1 ) {
			return;
		}
		
		this.currentPollIndex++;
		
		if ( this.currentPollIndex > this.polls.size()-1 ) {
			this.currentPollIndex = 0;
		}
		
		this.localStorage.setInt(LS_CURRENT_POLL_INDEX, this.currentPollIndex);
		
		
		this.showPoll(this.polls.get(this.currentPollIndex));
		
		this.startRegularTimer();
		
		
	}
	
	private void askForPollList() {
		
		this.receivedCount = 0;
		
		if ( null != this.polls ) {
			this.polls.clear();
		} else {
			this.polls = new ArrayList<EBVPollDao>();
		}
		
		pollService.getActivePolls(PublicDisplayApplication.getPlaceName(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(EveryBodyVotes.class.getName(), "Oops! " + caught.getMessage() );
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					//Window.alert("no polls found");
					Log.warn(EveryBodyVotes.class.getName(), "No active polls found");
				}
				Log.debug(EveryBodyVotes.class.getName(), "Received " + result.size() + " open polls");
				/*
				 * The active polls are the first in the list
				 */
				EveryBodyVotes.this.polls.addAll(0, result);
				
				EveryBodyVotes.this.widgets = new HashMap<String, GuiListBox>();
				for ( EBVPollDao poll : result ) {
					ArrayList<String> l = new ArrayList<String>();
					for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
						l.add(pollOption.getOption());
					}
					
					GuiListBox tb = new GuiListBox("poll " + poll.getPollId(), poll.getPollQuestion(), l);
					tb.setShortDescription("Vote");
					tb.setLongDescription(poll.getPollQuestion() );
					tb.addActionListener(EveryBodyVotes.this);
					EveryBodyVotes.this.widgets.put(poll.getPollId().toString(), tb);
				}
				
				
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}
		});
		
		pollService.getClosedPolls(PublicDisplayApplication.getPlaceName(), new AsyncCallback<List<EBVPollDao>> () {

			@Override
			public void onFailure(Throwable caught) {
				Log.warn(EveryBodyVotes.class.getName(), "Oops! " + caught.getMessage() );
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
			}

			@Override
			public void onSuccess(List<EBVPollDao> result) {
				if ( null == result || result.size() == 0 ) {
					//Window.alert("no polls found");
					Log.warn(EveryBodyVotes.class.getName(), "No closed polls found");
				}
				Log.debug(EveryBodyVotes.class.getName(), "Received " + result.size() + " closed polls");
				/*
				 * The closed polls are the last in the list
				 */
				EveryBodyVotes.this.polls.addAll(result);
				
				EveryBodyVotes.this.checkAdvancePoll(++EveryBodyVotes.this.receivedCount);
				
			}
		});
	}
	
	
	private void checkAdvancePoll(int count) {
		Log.debug(this, count+"");
		if ( count >= 2 ) {
			this.advancePoll();
		}
	}
	
	private void showClosedPoll(EBVPollDao poll) {
		DataTable dt = DataTable.create();
		dt.addColumn(ColumnType.STRING, "Option");
		dt.addColumn(ColumnType.NUMBER, "Votes"); 
		for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
			int i = dt.addRow();
			dt.setValue(i, 0, pollOption.getOption() + " (" + pollOption.getVotes()+")");
			dt.setValue(i, 1, pollOption.getVotes());
		}
		
		//PieChart.PieOptions options = PieChart.PieOptions.create();
		Options options = Options.create();

		options.setWidth(500);
	    options.setHeight(200);
	    //options.set3D(false);
	    options.setFontSize(23);
	    
	    options.setLegend(LegendPosition.NONE);
	    ChartArea ca = ChartArea.create();
	    ca.setTop(0);
	    ca.setLeft(0);
	    ca.setWidth("100%");
	    ca.setHeight("100%");
	    options.setChartArea(ca);
	   // options.setTitle("title");
	    options.setAxisTitlesPosition("none");
	    AxisOptions ao = AxisOptions.create();
	    ao.setTextPosition("in");
	    options.setVAxisOptions(ao);
	    
	    ao = AxisOptions.create();
	    ao.setTextPosition("none");
	    ao.setMinValue(0);
	    options.setHAxisOptions(ao);
		BarChart pie = new BarChart(dt, options);
		pie.setWidth("500px");
		
		RootPanel.get("content").clear();
		Label title = new Label("Results for \"" + poll.getPollQuestion() + "\"");
		
		RootPanel.get("content").add(title);
		RootPanel.get("content").add(pie);
		
		GuiTextBox suggest = new GuiTextBox("suggest", "Suggest a poll");
		suggest.setWidth("500px");
		RootPanel.get("content").add(suggest);
	}
	
	private void showOpenPoll(EBVPollDao poll) {
		
		GuiListBox tb = EveryBodyVotes.this.widgets.get(poll.getPollId().toString());
		RootPanel.get("content").clear();
		RootPanel.get("content").add(tb);
		
	}
	
	
	private void showPoll(EBVPollDao poll) {
		long today = new Date().getTime();
		
		if ( poll.getClosesOn() < today ) { // closed poll
			showClosedPoll(poll);
		} else {
			showOpenPoll(poll);
		}	
	}

	private void showPollResult(final String pollId) {
		Log.debug(this, "Showing poll result for poll: " + pollId);
		
		pollService.updatePolls(PublicDisplayApplication.getPlaceName(), PublicDisplayApplication.getApplicationName(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Oops. Could not update poll" + caught.getMessage());
				
			}

			@Override
			public void onSuccess(Void result) {
				
				pollService.getPoll(pollId, new AsyncCallback<EBVPollDao>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
						
					}

					@Override
					public void onSuccess(EBVPollDao result) {
						EveryBodyVotes.this.startResultTimer();
						EveryBodyVotes.this.showClosedPoll(result);
					}
					
				});
				
			}
			
		});
	}

	@Override
	public void onAction(ActionEvent<?> e) {
		
		GuiWidget widget = (GuiWidget)e.getSource();
		String pollId = widget.getWidgetId().substring(5).trim();
		Log.debug(this, "Current poll: " + this.polls.get( this.currentPollIndex ).getPollId());
		Log.debug(this, "Received poll interaction: " + pollId);
		Log.debug(this, "Equal: " + this.polls.get( this.currentPollIndex ).getPollId().toString().equals(pollId) );
		if ( this.polls.get( this.currentPollIndex ).getPollId().toString().equals(pollId) && !this.showingPollResult) {
			Log.debug(this, "Showing result");
			this.showPollResult(pollId);
		}

		
	}
}
