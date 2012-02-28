package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.widget;

import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.MultipleOptionImperativeClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ImperativeMultipleOptionWidgetUi extends Composite {

	
	@UiTemplate("ImperativeMultipleOptionWidgetDesktop.ui.xml")
	interface ImperativeMultipleOptionUiDesktopUiBinder extends UiBinder<Widget, ImperativeMultipleOptionWidgetUi> {	}
	private static ImperativeMultipleOptionUiDesktopUiBinder desktopUiBinder = GWT.create(ImperativeMultipleOptionUiDesktopUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField HorizontalPanel mainHorizontalPanel;
	@UiField Image iconImage;
	@UiField Label descriptionLabel;
	@UiField Button actionButton;
	@UiField ListBox optionsListBox;
	
	
	private org.instantplaces.purewidgets.shared.widgets.Widget pureWidget;

	public ImperativeMultipleOptionWidgetUi(UiType uiType, org.instantplaces.purewidgets.shared.widgets.Widget widget) {
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
		
		if ( true ) { /* TODO: check icon */
			this.iconImage.removeFromParent(); // remove icon
		}
		
		this.optionsListBox.setVisibleItemCount(Math.min(4, this.pureWidget.getWidgetOptions().size()));
		for (WidgetOption wo : this.pureWidget.getWidgetOptions() ) {
			this.optionsListBox.addItem(wo.getShortDescription() + " [" + wo.getReferenceCode() + "]");
		}
		
		this.actionButton.addClickHandler(new MultipleOptionImperativeClickHandler(this.pureWidget.getPlaceId(), this.pureWidget.getApplicationId(), 
				this.pureWidget.getWidgetId(), this.pureWidget.getWidgetOptions(), this.optionsListBox, new PopupUi(this.uiType)));
//		
	}

	private UiBinder<Widget, ImperativeMultipleOptionWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;

		default:
			return desktopUiBinder;
		}
	}
	

	

}
