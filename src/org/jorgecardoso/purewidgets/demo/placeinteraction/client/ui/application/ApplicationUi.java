package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.application;

import org.instantplaces.purewidgets.shared.widgets.Application;
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

public class ApplicationUi extends Composite implements HasClickHandlers {

	@UiTemplate("ApplicationUiDesktop.ui.xml")
	interface ApplicationUiDesktopUiBinder extends UiBinder<Widget, ApplicationUi> {	}
	private static ApplicationUiDesktopUiBinder desktopUiBinder = GWT.create(ApplicationUiDesktopUiBinder.class);
	
	@UiTemplate("ApplicationUiSmartphone.ui.xml")
	interface ApplicationUiSmartphoneUiBinder extends UiBinder<Widget, ApplicationUi> {	}
	private static ApplicationUiSmartphoneUiBinder smartphoneUiBinder = GWT.create(ApplicationUiSmartphoneUiBinder.class);
	
	
	@UiField Image icon;
	@UiField Label name;
	
	private UiType uiType;

	

	private Application application;

	

	public ApplicationUi( UiType uiType, Application application) {
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.uiType = uiType;
		this.setApplication(application);
		
	}
	
	private UiBinder<Widget, ApplicationUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Smartphone:
			return smartphoneUiBinder;
			
		default:
			return desktopUiBinder;
		}
	}

	

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
		this.name.setText(this.application.getApplicationId());
		String url = this.application.getIconBaseUrl();
		if ( null == url ) {
			url = "http://upload.wikimedia.org/wikipedia/commons/0/0d/Icono_web_store.png";
		}
		this.icon.setUrl(url);
	}

}
