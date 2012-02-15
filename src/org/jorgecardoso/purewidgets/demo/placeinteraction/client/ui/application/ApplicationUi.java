package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.application;

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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationUi extends Composite implements HasText, HasClickHandlers {

	@UiTemplate("ApplicationUiDesktop.ui.xml")
	interface ApplicationUiDesktopUiBinder extends UiBinder<Widget, ApplicationUi> {	}
	private static ApplicationUiDesktopUiBinder desktopUiBinder = GWT.create(ApplicationUiDesktopUiBinder.class);
	
	@UiField Image icon;
	@UiField Label name;
	
	private UiType uiType;

	

	private String applicationName;

	

	public ApplicationUi( UiType uiType, String applicationName) {
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.uiType = uiType;
		this.applicationName = applicationName;
		this.name.setText(this.applicationName);
		this.icon.setUrl("http://upload.wikimedia.org/wikipedia/commons/0/0d/Icono_web_store.png");
	}
	
	private UiBinder<Widget, ApplicationUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}

	
	public void setText(String text) {
		this.applicationName = text;
		this.name.setText(text);
	}

	public String getText() {
		return this.name.getText();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

}
