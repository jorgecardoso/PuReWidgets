package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Poll extends Composite  {

	private static PollUiBinder uiBinder = GWT.create(PollUiBinder.class);

	interface PollUiBinder extends UiBinder<Widget, Poll> {
	}

	
	public Poll() {
		initWidget(uiBinder.createAndBindUi(this));
		options = new ArrayList<PollOption>();
		PollOption o = new PollOption();
		o.setPoll(this);
		options.add(o);
		
		updateGui();
	}
	
	@UiField
	PollQuestion question;
	
	ArrayList<PollOption> options;
	
	
	@UiField
	Button deleteButton;
	
	@UiField
	Button buttonAdd;
	
	@UiField
	VerticalPanel optionsPanel;


	@UiHandler("buttonAdd")
	void onAddClick(ClickEvent e) {
		PollOption o = new PollOption();
		o.setPoll(this);
		options.add(o);
		updateGui();
	}
	
	@UiHandler("deleteButton")
	void onDeleteClick(ClickEvent e) {
		options.add(new PollOption());
		updateGui();
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

}
