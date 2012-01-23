package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class Poll extends Composite  {

	private static PollUiBinder uiBinder = GWT.create(PollUiBinder.class);

	interface PollUiBinder extends UiBinder<Widget, Poll> {
	}

	
	public Poll() {
		initWidget(uiBinder.createAndBindUi(this));
		options = new ArrayList<PollOption>();
		
		showAfterDateBox.setValue(new Date());
		updateGui();
	}
	
	@UiField
	Label pollIdLabel;
	
	@UiField
	TextBox questionTextBox;
	
	@UiField
	DateBox showAfterDateBox;
	
	
	@UiField
	DateBox showUntilDateBox;
	
	
	@UiField
	DateBox closesOnDateBox;
	
	ArrayList<PollOption> options;
	
	
	@UiField
	Button deleteButton;
	
	@UiField
	Button buttonAdd;
	
	@UiField
	Button saveButton;
	
	@UiField
	VerticalPanel optionsPanel;

	private PollActionListener listener;

	@UiHandler("buttonAdd")
	void onAddClick(ClickEvent e) {
		PollOption o = new PollOption();
		o.setPoll(this);
		options.add(o);
		updateGui();
	}
	
	@UiHandler("deleteButton")
	void onDeleteClick(ClickEvent e) {
		if ( null != this.listener ) {
			this.listener.onDeletePoll(this);
		}
	}
	
	@UiHandler("saveButton")
	void onSaveClick(ClickEvent e) {
		//options.add(new PollOption());
		//updateGui();
		if ( null != this.listener ) {
			this.listener.onSavePoll(this);
		}
	}
	
	public void onDeleteOption(PollOption optionDeleted) {
		//Window.alert("Deleting option: " + optionDeleted);
		this.options.remove(optionDeleted);
		this.updateGui();
	}
	
	void updateGui() {
		
		
		this.optionsPanel.clear();
		for ( PollOption option : this.options ) {
			this.optionsPanel.add(option);
		}
	}

	/**
	 * @return the listener
	 */
	public PollActionListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(PollActionListener listener) {
		this.listener = listener;
	}
	
	public void setPollIdText(String pollId) {
		this.pollIdLabel.setText(pollId);
	}
	public String getPollIdText() {
		return this.pollIdLabel.getText();
	}
	
	public void setQuestionText(String text) {
		this.questionTextBox.setText(text);
	}
	
	public String getQuestionText() {
		return this.questionTextBox.getText();
	}
	
	
	public void clearOptions() {
		this.options.clear();
		this.updateGui();
	}
	
	public void addOption(String option, int votes) {
		this.options.add( new PollOption(this, option, votes) );
		this.updateGui();
	}
	
	public void setShowAfter(Date d) {
		this.showAfterDateBox.setValue(d);
	}
	public Date getShowAfter() {
		return this.showAfterDateBox.getValue();
	}
	
	public void setShowUntil(Date d) {
		this.showUntilDateBox.setValue(d);
	}
	public Date getShowUntil() {
		return this.showUntilDateBox.getValue();
	}
	
	public void setClosesOn(Date d) {
		this.closesOnDateBox.setValue(d);
	}
	public Date getClosesOn() {
		return this.closesOnDateBox.getValue();
	}
	
	public ArrayList<String> getOptionsText() {
		ArrayList<String> options = new ArrayList<String>();
		for ( PollOption option : this.options ) {
			options.add( option.getText() );
		}
		return options;
	}
	
	public ArrayList<PollOption> getOptions() {
		return this.options;
	}

}
