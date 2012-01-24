package org.jorgecardoso.purewidgets.demo.everybodyvotes.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.instantplaces.purewidgets.server.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.server.application.ApplicationLifeCycle;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.widgets.ListBox;
import org.instantplaces.purewidgets.shared.widgets.TextBox;
import org.instantplaces.purewidgets.shared.widgets.Widget;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao.Dao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

public class EveryBodyVotes extends HttpServlet implements ApplicationLifeCycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PublicDisplayApplication app;
	HttpServletRequest req;
	HttpServletResponse resp;
	
	List<EBVPollDao> polls = null;
	
	String message;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		
		PublicDisplayApplication.load(req, this, "EveryBodyVotes");
	}
	
	@Override
	public void loaded(PublicDisplayApplication application) {
		this.app = application;
		message = "";
	}
	
	/**
	 * Called only on the first time
	 */
	@Override
	public void setup() {
	}
	
	@Override
	public void start() {

		this.polls = Dao.getPolls("DefaultPlace");
		
		/*
		 * Create the list widgets
		 */
		for ( EBVPollDao poll : polls ) {
			ArrayList<String> listOptions = new ArrayList<String>();
			message += poll.getPollQuestion() + "<br>";
			for ( EBVPollOptionDao pollOption : poll.getPollOptions() ) {
				listOptions.add(pollOption.getOption());
				message += pollOption.getOption() + ": " + pollOption.getVotes() + "<br>";
			}
			message += "<br>";
			
			ListBox listBox = new ListBox("poll " + poll.getPollId(), poll.getPollQuestion(), listOptions);
			listBox.addActionListener(this);
		}
		
//		Dao.beginTransaction();
//		
//		EBVPollDao poll = new EBVPollDao();
//		poll.setPlaceId("DefaultPlace");
//		poll.setPollQuestion("Test question");
//		poll.getPollOptions().add(new EBVPollOptionDao("option 1"));
//		poll.vote("jorge", "option 1");
//		
//		
//		Dao.put(poll);
		
		//Dao.getPolls(placeId)
	}
	
	@Override
	public void finish() {
		Log.debug(this, "Finish");
		
		
		/*app.setLong("button_1", clicks);
		
		message += "Clicks: " + clicks + "\n";
		for (Widget w : WidgetManager.get().getWidgetList()) {
			
				message += w.toDebugString() + ";";
			
		}*/
		
		
		
		if ( null != this.resp ) {
			try {
				resp.setContentType("text/html");
				resp.getWriter().write("<html><body>" + message + "</body></html>");
			} catch (IOException e) {
				Log.error(this, "Could not write the Http response.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAction(ActionEvent<?> ae) {
		//ae = (ActionEvent<? extends Widget>)ae;
		
		
		Widget source = (Widget)ae.getSource();
		
		if ( source.getWidgetId().startsWith("poll") ) {
			String pollIdString = source.getWidgetId().substring(5);
			long pollId = Long.parseLong(pollIdString);
			for ( EBVPollDao poll : this.polls ) {
				if ( poll.getPollId() == pollId ) {
					Log.info(this, "Voting on " + poll.getPollQuestion());
					Dao.beginTransaction();
					poll.vote(ae.getPersona(), ae.getParam().toString());
					Dao.put(poll);
					Dao.commitOrRollbackTransaction();
				}
			}
		}
		
//		if ( source.getWidgetId().equals("button_1") ) {
//			this.clicks++;
//			
//			if ( this.clicks > 1 && this.clicks < 3) {
//				TextBox text = new TextBox("txt_1", "Gimme text");
//				app.addWidget(text);
//			}
//		} else if ( source.getWidgetId().equals("txt_1")) {
//			message += ae.getParam() + "\n"; 
//		}
		
		
	}
}
