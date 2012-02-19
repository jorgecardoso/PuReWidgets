package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.widget;

import java.util.ArrayList;

import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.widgetmanager.Callback;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.EntryClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ImperativeClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.MultipleOptionImperativeClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui.UiType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetListUi extends Composite  {

	
	@UiTemplate("WidgetListDesktop.ui.xml")
	interface WidgetListUiBinder extends UiBinder<Widget, WidgetListUi> {
	}
	private static WidgetListUiBinder desktopUiBinder = GWT.create(WidgetListUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField VerticalPanel panel;
	@UiField Label panelApplicationName;
	
	/*
	 * The timer to trigger requests to get the list of widgets of the application.
	 */
	private Timer timerWidgets;

	private String placeName;
	private String applicationName;
	
	public WidgetListUi( UiType uiType, String placeName, String applicationName ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		
		this.placeName = placeName;
		this.applicationName = applicationName;
		this.panelApplicationName.setText(this.applicationName);
	}
	
	private UiBinder<Widget, WidgetListUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Mobile:
			return null;
			
		default:
			return null;
		}
	}

	public void start() {

		timerWidgets = new Timer() {
			@Override
			public void run() {
				refreshWidgets();
			}
		};
		timerWidgets.scheduleRepeating(15 * 1000);
		this.refreshWidgets();
	}
	
	public void stop() {
		if ( null != this.timerWidgets ) {
			this.timerWidgets.cancel();
			this.timerWidgets = null;
		}
	}

	
	protected void refreshWidgets() {
		final String placeName = this.placeName;
		final String applicationName = this.applicationName;
		PublicDisplayApplication.getServerCommunicator().getWidgetsList(this.placeName, this.applicationName, 
				new Callback<ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget>>() {

					@Override
					public void onSuccess(
							ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
						WidgetListUi.this.onWidgetsList(placeName, applicationName, widgetList);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.warn(WidgetListUi.this, "Could not get list of widgets from server: " + exception.getMessage());
						
					}
			
		});
	}
	

	
	private void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());

		/*
		 * Sort the widgets by id
		 */
		//Collections.sort(widgetList);
		
		if ( null == widgetList || widgetList.size() == 0 ) {
			if ( null != panel ) {
				panel.clear();
				Label l = new Label("No widgets found!");
				l.setStyleName("nowidgets");
				panel.add(l);
				return;
			}
		}
			
		if (null != widgetList) {
					
			ArrayList<String> widgetIds = new ArrayList<String>();
			for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
				widgetIds.add(widget.getWidgetId());
			}
			// String applicationId = this.currentApplicationId;
			// //widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);

		

			if (null != panel) {
				Log.debug(this, panel.toString());

				/*
				 * Delete widgets that no longer exist
				 */
				int i = 0;
				while (i < panel.getWidgetCount()) {
					String widgetName = panel.getWidget(i).getElement().getPropertyString("id");

					if (!widgetIds.contains(widgetName)) {
						panel.remove(i);
						// currentWidgets.remove(i);
					} else {
						// only increment if we haven't deleted anything because
						// if we did, indexed may have changed
						i++;
					}
				}

				/*
				 * Add the new widgets. Widgets are inserted in alphabetical order
				 */
				for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
					boolean exists = false;
					
					/*
					 * The new widget will be inserted before this index (the index of the first existing widget 
					 * with name greater than the widget to be inserted)
					 */
					int indexInPanel = 0; 
					/*
					 * If we found the place for the widget to be inserted (tentatively, because the widget may 
					 * already exist)
					 */
					boolean foundPlace = false;
					
					for (i = 0; i < panel.getWidgetCount(); i++) {
						String existingWidgetName = panel.getWidget(i).getElement().getPropertyString("id");
						
						/*
						 * If the widget already exists in the panel, skip it
						 */
						if ( existingWidgetName.equals(widget.getWidgetId()) ) {
							exists = true;
							break;
						}
						
						/*
						 * If we found a widget with a name greater than the widget to be inserted mark its index
						 */
						if ( !foundPlace && existingWidgetName.compareTo(widget.getWidgetId()) > 0 ) {
							indexInPanel = i;
							foundPlace = true;
						}
					}

					if (!exists) {
						Log.debug(this, "Adding " + widget.getWidgetId() + " to panel");
						if ( foundPlace ) {
							panel.insert(this.getHtmlWidget(widget), indexInPanel);
						} else {
							panel.add(this.getHtmlWidget(widget) );
						}
					}
				}
			}
			// this.currentWidgetsMap.put(applicationId, widgetList);
		}

		// tabPanelApplications.getWidget(index)

	}
	

	Widget getHtmlWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		Widget toReturn = null;
		
		if (publicDisplayWidget.getControlType().equals(
				org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY)) {
			toReturn = getEntryWidget(publicDisplayWidget);
		} else if (publicDisplayWidget
				.getControlType()
				.equals(org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			toReturn = getImperativeWidget(publicDisplayWidget);
		} else if (publicDisplayWidget.getControlType().equals(
				org.instantplaces.purewidgets.shared.widgets.Widget.CONTROL_TYPE_DOWNLOAD)) {
			toReturn =  getDownloadWidget(publicDisplayWidget);
		}
		
		if ( null != toReturn ) {
			toReturn.setStyleName("widget");
			toReturn.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		}
		return toReturn;
	}

	Widget getEntryWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {

		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		// for ( WidgetOption wo : widgetOptions ) {
		Label label = new Label(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]");
		flowPanel.add(label);
		TextBox textBox = new TextBox();
		flowPanel.add(textBox);
		Button btn = new Button("Submit");
		flowPanel.add(btn);
		btn.addClickHandler(new EntryClickHandler(publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions(), textBox));
		// }

		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	Widget getImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		if (null != widgetOptions) {
			if (widgetOptions.size() == 1) {
				return getSingleOptionImperativeWidget(publicDisplayWidget);
			} else {
				return getMultipleOptionImperativeWidget(publicDisplayWidget);
			}
		} else {
			return null;
		}
	}

	
	Widget getMultipleOptionImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		VerticalPanel panel = new VerticalPanel();

		panel.add(new Label(publicDisplayWidget.getShortDescription()));
		
		ListBox listbox = new ListBox();
		//listbox.addStyleName(style.list());
		listbox.setVisibleItemCount(Math.min(4, widgetOptions.size()));
		for (WidgetOption wo : widgetOptions) {
			listbox.addItem(wo.getShortDescription() + " [" + wo.getReferenceCode() + "]");
		}
		
		
		panel.add(listbox);
		
		Button button = new Button("Send");
		button.addClickHandler(new MultipleOptionImperativeClickHandler(publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions(), listbox));
		
		panel.add(button);
		
		return panel;
	}

	Widget getSingleOptionImperativeWidget(
			org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		Label label = new Label(publicDisplayWidget.getLongDescription());
		flowPanel.add(label);

		Button btn = new Button(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]");
		flowPanel.add(btn);
		btn.addClickHandler(new ImperativeClickHandler( publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions()) );
		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	Widget getDownloadWidget(org.instantplaces.purewidgets.shared.widgets.Widget publicDisplayWidget) {
		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);

		FlowPanel flowPanel = new FlowPanel();
		//flowPanel.add(new Label("Download: "));
		Anchor a = new Anchor(publicDisplayWidget.getShortDescription() + " ["
				+ option.getReferenceCode() + "]", publicDisplayWidget.getContentUrl());

		flowPanel.add(a);

		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		return flowPanel;
	}

	/**
	 * @return the placeName
	 */
	public String getPlaceName() {
		return placeName;
	}

	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	
	
}
