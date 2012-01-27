package org.jorgecardoso.purewidgets.demo.everybodyvotes.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.instantplaces.purewidgets.client.widgets.GuiTextBox;
import org.instantplaces.purewidgets.server.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.server.application.ApplicationLifeCycle;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.ListBox;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.instantplaces.purewidgets.shared.widgets.TextBox;
import org.instantplaces.purewidgets.shared.widgets.Widget;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao.Dao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;
import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollOptionDao;

public class EveryBodyVotes extends HttpServlet implements ApplicationLifeCycle, ApplicationListListener {

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

		this.polls = Dao.getActivePolls("DefaultPlace");
		
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
		
		TextBox suggest = new TextBox("suggest", "Suggest a poll");
		suggest.addActionListener(this);
		
		WidgetManager.get().setApplicationListListener(this);
		WidgetManager.get().getWidgetsList(this.app.getPlaceId(), this.app.getAppId());
		
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
		} else if ( source.getWidgetId().equals("suggest") ) {
			this.sendMail(ae.getParam().toString());
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

	private void sendMail(String suggestion) {
		Log.warn(this, "Sending email: " + suggestion);
		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = suggestion;

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("jorgediablu@gmail.com", "Jorge Cardoso"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("jorgecardoso@ieee.org", "Mr. User"));
            msg.setSubject("Poll suggestion");
            msg.setText(msgBody);
            Transport.send(msg);
    
        } catch (AddressException e) {
            Log.warn(this, e.getMessage());
        } catch (MessagingException e) {
            Log.warn(this, e.getMessage());
        } catch (UnsupportedEncodingException e) {
			Log.warn(this, e.getMessage());
			e.printStackTrace();
		}
		
	}

	@Override
	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWidgetsList(String placeId, String applicationId, ArrayList<Widget> widgetList) {
		/*
		 * Go through our widgets and delete the ones that refer to closed polls
		 */
		for ( Widget widget : widgetList ) {
			if (widget.getWidgetId().equals("suggest")) continue;
			
			boolean exists = false;
			for ( EBVPollDao poll : this.polls ) {
				if ( widget.getWidgetId().substring(5).equals( String.valueOf(poll.getPollId()) ) ) {
					exists = true;
				}
			}
			if (!exists) {
				WidgetManager.get().removeWidget(widget);
			}
		}
		
	}

	@Override
	public void onPlaceList(ArrayList<Place> placeList) {
		// TODO Auto-generated method stub
		
	}
}
