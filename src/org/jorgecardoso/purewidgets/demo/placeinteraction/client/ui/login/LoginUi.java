package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.login;

import java.util.Date;

import org.jorgecardoso.purewidgets.demo.placeinteraction.client.PlaceInteractionWebpage;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.UserInfo;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.main.MainScreenUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginUi extends Composite {

	@UiTemplate("LoginUiDesktop.ui.xml")
	interface LoginUiDesktopUiBinder extends UiBinder<Widget, LoginUi> {	}
	private static LoginUiDesktopUiBinder desktopUiBinder = GWT.create(LoginUiDesktopUiBinder.class);
	
	private UiType uiType;

	@UiField InlineLabel identityName;
	@UiField Anchor signOutLink;
	@UiField HTML signInLinkWrapper;
	
	private boolean loggedIn = false;
	
	private Label labelId;
	
	public  LoginUi( UiType uiType ) {
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.uiType = uiType;
		this.checkUserIdentity();
	}
	
	private UiBinder<Widget, LoginUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		default:
			return desktopUiBinder;
		}
	}
	
	private void checkUserIdentity() {
		/*
		 * Check/set the user's identity.
		 */
		UserInfo.setUserIdentity(com.google.gwt.user.client.Window.Location.getParameter("preferredUsername"));
		if ( null == UserInfo.getUserIdentity() ) {
			UserInfo.setUserIdentity( this.getAnonymousId() );
		} else {
			loggedIn = true;
		}
		
		this.identityName.setText(UserInfo.getUserIdentity());
		
		if (loggedIn) {
			//RootPanel.get("user").add(signOutLink);
			//RootPanel.get("user").getWidget(1).setVisible(false);
			this.signInLinkWrapper.setVisible(false);
		} else {
			this.signOutLink.setVisible(false);
		}
	}

	@UiHandler("signOutLink")
	 void handleClick(ClickEvent e) {
		this.logout();
	 }
	
	private void logout(){
		this.loggedIn = false;
		UserInfo.setUserIdentity( this.getAnonymousId() );
		this.identityName.setText(UserInfo.getUserIdentity());
		this.signInLinkWrapper.setVisible(true);
		this.signOutLink.setVisible(false);
	}
	
	private String getAnonymousId() {
		/* 
		 * We generate a random identity name, starting with "Anonymous" and set
		 * a cookie with it so that we can retrieve it later if the user uses
		 * the webpage again.
		 */
		String id = Cookies.getCookie("userIdentity");
		if (null == id) {
			id = "Anonymous" + ((int) (Math.random() * 10000));
			/*
			 * The cookie is valid for one week
			 */
			Date future = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // 7
																							// days
																							// in
																							// the
																							// future
			Cookies.setCookie("userIdentity", id, future);
		}
		return id;
	}
	
}
