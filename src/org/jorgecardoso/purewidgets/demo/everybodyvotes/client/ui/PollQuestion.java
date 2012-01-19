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

public class PollQuestion extends Composite  {

	private static PollQuestionUiBinder uiBinder = GWT.create(PollQuestionUiBinder.class);

	interface PollQuestionUiBinder extends UiBinder<Widget, PollQuestion> {
	}

	
	
	
	public PollQuestion() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	
	
	@UiField
	TextBox questionTextBox;


	

}
