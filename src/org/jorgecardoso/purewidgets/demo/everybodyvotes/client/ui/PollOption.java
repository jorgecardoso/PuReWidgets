package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PollOption extends Composite  {

	private static PollOptionUiBinder uiBinder = GWT.create(PollOptionUiBinder.class);

	interface PollOptionUiBinder extends UiBinder<Widget, PollOption> {
	}

	public PollOption() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	Poll poll;
	
	@UiField
	TextBox optionTextBox;
	
	@UiField
	Button buttonDelete;
	

	@UiHandler("buttonDelete")
	void onClick(ClickEvent e) {
		//Window.alert("Hello!");
		poll.onDeleteOption(this);
	}

	
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	
}
