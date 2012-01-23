package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PollOption extends Composite  {

	interface PollOptionUiBinder extends UiBinder<Widget, PollOption> {
	}

	private static PollOptionUiBinder uiBinder = GWT.create(PollOptionUiBinder.class);

	@UiField
	Button buttonDelete;

	@UiField
	TextBox optionTextBox;
	
	@UiField
	TextBox votesTextBox;
	
	Poll poll;
	
	public PollOption() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public PollOption(Poll poll, String optionText, int votes) {
		initWidget(uiBinder.createAndBindUi(this));
		this.poll = poll;
		this.optionTextBox.setText(optionText);
		this.votesTextBox.setText(votes+"");
	}
	
	

	@UiHandler("buttonDelete")
	void onClick(ClickEvent e) {
		//Window.alert("Hello!");
		poll.onDeleteOption(this);
	}

	
	public String getText() {
		return this.optionTextBox.getText();
	}
	
	public int getVotes() {
		int votes = 0;
		try {
			votes = Integer.parseInt(this.votesTextBox.getText());
		} catch (Exception e) {
			Log.warn(this, "Could not parse votes: " + this.votesTextBox.getText());
		}
		return votes;
	}
	
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
}
