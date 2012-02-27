package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.widget;

import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EntryWidgetUi extends Composite {

	
	@UiTemplate("EntryWidgetDesktop.ui.xml")
	interface EntryWidgetDesktopUiBinder extends UiBinder<Widget, EntryWidgetUi> {	}
	private static EntryWidgetDesktopUiBinder desktopUiBinder = GWT.create(EntryWidgetDesktopUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField HorizontalPanel mainHorizontalPanel;
	@UiField Image iconImage;
	@UiField Label descriptionLabel;
	@UiField Button actionButton;
	@UiField TextBox entryTextBox;
	
	private org.instantplaces.purewidgets.shared.widgets.Widget pureWidget;

	public EntryWidgetUi(UiType uiType, org.instantplaces.purewidgets.shared.widgets.Widget widget) {
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
		this.actionButton.setText(this.pureWidget.getShortDescription());
		
		if ( true ) { /* check icon */
			this.mainHorizontalPanel.remove(0); // remove icon
		}
	}

	private UiBinder<Widget, EntryWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}
	

	

}
