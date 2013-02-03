package org.purewidgets.client.htmlwidgets.interactioninstructions;


import org.purewidgets.client.application.PDApplication;
import org.purewidgets.shared.im.Place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PDInteractionInstructionsSticker extends PopupPanel {

	private static final int RESOLUTION = 50;
	
	private static PDInteractionInstructionsStickerUiBinder uiBinder = GWT
			.create(PDInteractionInstructionsStickerUiBinder.class);

	interface PDInteractionInstructionsStickerUiBinder extends
			UiBinder<Widget, PDInteractionInstructionsSticker> {
	}

	@UiField
    HorizontalPanel scrollPanel;
	
	@UiField
	HTMLPanel mainPanel;
	
	@UiField
	HorizontalPanel smsItem;
	
	@UiField
	HorizontalPanel emailItem;
	
	
	@UiField 
	Label smsTo;

	@UiField 
	Label smsText;
	
	@UiField 
	Label emailTo;
	
	@UiField 
	Label emailSubject;
	
	@UiField 
	Label webAddress;
	
	private float current;
	
	private float speed;
	
	Timer scrollTimer;

	
	public PDInteractionInstructionsSticker() {
		add(uiBinder.createAndBindUi(this));
		this.setStyleName("");
		speed = 5;
		current = mainPanel.getOffsetWidth();
		
		PDApplication.getCurrent().getInteractionManager().getPlace(PDApplication.getCurrent().getPlaceId(), 
				PDApplication.getCurrent().getApplicationId(), new AsyncCallback<Place>(){

					@Override
					public void onFailure(Throwable caught) {		
						org.purewidgets.shared.logging.Log.warn(PDInteractionInstructionsSticker.this, "Could not get place");
					}

					@Override
					public void onSuccess(Place place) {
						
						if ( null == place.getPlacePhoneNumber() || place.getPlacePhoneNumber().length() < 2 ) {
							smsItem.removeFromParent();
						} else {
							smsTo.setText("to: " + place.getPlacePhoneNumber());
							smsText.setText(place.getPlaceReferenceCode()+".(ref)");
						}
						
						if ( null == place.getPlaceEmailAddress() || place.getPlaceEmailAddress().length() < 2 ) {
							emailItem.removeFromParent();
						} else {
							emailTo.setText("to: " + place.getPlaceEmailAddress());
						}
						
						webAddress.setText(place.getPlaceInteractionUrl());
					}
			
		});	

		
	}
	


	public void start() {
		
		scrollTimer = new Timer() {

			@Override
			public void run() {
				scrollPanel.getElement().getStyle().setLeft(current, Unit.PX);
				current -= speed;
				
				if (current > 0 || current < -( scrollPanel.getOffsetWidth()-mainPanel.getOffsetWidth() ) ){
					speed = -speed;
					scrollTimer.cancel();
					new Timer() {

						@Override
						public void run() {
							if ( null != scrollTimer ) {
								scrollTimer.scheduleRepeating(RESOLUTION);
							}
							
						}
						
					}.schedule(10000);
					//current = mainPanel.getOffsetWidth();
					//speed = RESOLUTION*mainPanel.getOffsetWidth()/period;
				}
				
			}
			
		};
		scrollTimer.scheduleRepeating(RESOLUTION);
	}
	
	public void stop() {
		
		scrollTimer.cancel();
		scrollTimer = null;
	}


}
