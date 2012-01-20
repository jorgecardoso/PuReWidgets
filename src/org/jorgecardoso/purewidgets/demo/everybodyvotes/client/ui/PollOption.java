package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

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
	
	Poll poll;
	
	public PollOption() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public PollOption(Poll poll, String optionText) {
		initWidget(uiBinder.createAndBindUi(this));
		this.poll = poll;
		this.optionTextBox.setText(optionText);
	}
	
	

	@UiHandler("buttonDelete")
	void onClick(ClickEvent e) {
		//Window.alert("Hello!");
		poll.onDeleteOption(this);
	}

	
	public String getText() {
		return this.optionTextBox.getText();
	}
	
	
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
}
