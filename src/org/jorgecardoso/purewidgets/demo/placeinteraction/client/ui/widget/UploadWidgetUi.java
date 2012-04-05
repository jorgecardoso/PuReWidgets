package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.widget;

import org.instantplaces.purewidgets.client.widgets.ReferenceCodeFormatter;
import org.instantplaces.purewidgets.shared.Log;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.EntryClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.popup.PopupUi;
import org.jorgecardoso.purewidgets.demo.test.client.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class UploadWidgetUi extends Composite {

	
	@UiTemplate("UploadWidgetDesktop.ui.xml")
	interface UploadWidgetDesktopUiBinder extends UiBinder<Widget, UploadWidgetUi> {	}
	private static UploadWidgetDesktopUiBinder desktopUiBinder = GWT.create(UploadWidgetDesktopUiBinder.class);
	
	@UiTemplate("UploadWidgetSmartphone.ui.xml")
	interface UploadWidgetSmartphoneUiBinder extends UiBinder<Widget, UploadWidgetUi> {	}
	private static UploadWidgetSmartphoneUiBinder smartphoneUiBinder = GWT.create(UploadWidgetSmartphoneUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField Panel mainHorizontalPanel;
	@UiField Image iconImage;
	@UiField Label descriptionLabel;
	@UiField Button actionButton;
	@UiField TextBox entryTextBox;
	
	@UiField FormPanel formPanel;
	@UiField FileUpload fileUpload; 
	/*
	 * Indicates whether we should load the widget icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadWidgetIcon;
	
	private String uploadId = "myfile" + (int)(Math.random()*10000000);
	
	private org.instantplaces.purewidgets.shared.widgets.Widget pureWidget;

	public UploadWidgetUi(UiType uiType, org.instantplaces.purewidgets.shared.widgets.Widget widget) {
		this.uiType = uiType;
		this.pureWidget = widget;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		
		this.initUi();
	}
	
	private void initUi() {
		String description = this.pureWidget.getLongDescription();
		if ( null == description || description.trim().length() == 0) {
			description = this.pureWidget.getShortDescription();
		}
		this.descriptionLabel.setText(description);
		this.actionButton.setText(this.pureWidget.getShortDescription() + " " + ReferenceCodeFormatter.format(this.pureWidget.getWidgetOptions().get(0).getReferenceCode()));
		
		this.formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    this.formPanel.setMethod(FormPanel.METHOD_POST);
	    this.formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	    	@Override
		      public void onSubmitComplete(SubmitCompleteEvent event) {
		    		requestUploadUrl();
		      }
		    });
	    this.requestPostUrl();
	    
		this.fileUpload.setName(this.uploadId);
		this.fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				UploadWidgetUi.this.formPanel.submit();
			}
			
		});
		
		if ( this.loadWidgetIcon ) {
			if ( true ) { /* check icon */
				//this.mainHorizontalPanel.remove(0); // remove icon
				this.iconImage.removeFromParent();
			}
		} else {
			this.iconImage.removeFromParent();
		}
		
		this.actionButton.addClickHandler(new EntryClickHandler(this.pureWidget.getPlaceId(), this.pureWidget.getApplicationId(), 
				this.pureWidget.getWidgetId(), this.pureWidget.getWidgetOptions(), this.entryTextBox, new PopupUi(this.uiType) ));	
	}

	private UiBinder<Widget, UploadWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			this.loadWidgetIcon = true;
			return desktopUiBinder;
		case Smartphone:
			this.loadWidgetIcon = false;
			return smartphoneUiBinder;
		default:
			return desktopUiBinder;
		}
	}
	

	public void requestPostUrl() {
	
		JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);

    //Request request = 
  		 builder.requestString("http://pw-filearchive.appspot.com/serveposturl", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error(this, "Could not get post url");
				
			}

			@Override
			public void onSuccess(String result) {
				UploadWidgetUi.this.formPanel.setAction(result);
			}
  			 
  		 });
  	}
	

	public void requestUploadUrl() {
		JsonpRequestBuilder builder = new JsonpRequestBuilder();//RequestBuilder.GET, url);

	    //Request request = 
	  		 builder.requestString("http://pw-filearchive.appspot.com/serveurl?uploadId="+this.uploadId, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.error(this, "Could not get post url");
					
				}

				@Override
				public void onSuccess(String result) {
					Log.debug(result);
					UploadWidgetUi.this.entryTextBox.setText(result);
					//sendUploadInput(result);
				}
	  			 
	  		 });
		
	}

}
