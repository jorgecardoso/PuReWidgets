/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.place;

import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class PlaceUi extends Composite implements HasText, HasClickHandlers {

	
	@UiTemplate("PlaceDesktop.ui.xml")
	interface PlaceDesktopUiBinder extends UiBinder<Widget, PlaceUi> {}
	public static PlaceDesktopUiBinder desktopUiBinder = GWT.create(PlaceDesktopUiBinder.class);
	
	/*
	 * The type of ui we will generate
	 */
	private UiType uiType;
	
	public PlaceUi( UiType uiType ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
	}
	
	private UiBinder<Widget, PlaceUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}
	

	@UiField
	Button button;


	public void setText(String text) {
		button.setText(text);
	}

	
	public String getText() {
		return button.getText();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

}
