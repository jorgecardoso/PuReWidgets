package org.jorgecardoso.purewidgets.demo.placeinteraction.client.ui;

import java.util.ArrayList;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ApplicationListListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetManager;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Application;
import org.instantplaces.purewidgets.shared.widgets.Place;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.EntryClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.ImperativeClickHandler;
import org.jorgecardoso.purewidgets.demo.placeinteraction.client.MultipleOptionImperativeClickHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationList extends Composite implements ApplicationListListener  {

	private static ApplicationListUiBinder uiBinder = GWT.create(ApplicationListUiBinder.class);

	interface ApplicationListUiBinder extends UiBinder<Widget, ApplicationList> {
	}

	private String placeId;
	
	private Timer timerApplications;
	private Timer timerWidgets;

	
	public ApplicationList(String placeId) {
		initWidget(uiBinder.createAndBindUi(this));
		this.placeId = placeId;
		this.placeName.setText(placeId);
	}

	@UiField
	StackPanel stackPanelApplications;
	
	@UiField 
	Label placeName;


	public void start() {
		WidgetManager.get().setApplicationListListener(this);

		timerApplications = new Timer() {
			@Override
			public void run() {
				refreshApplications();
			}
		};

		timerWidgets = new Timer() {
			@Override
			public void run() {
				refreshWidgets();
			}
		};

		
		stackPanelApplications.addHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	        	if ( null!= timerWidgets ) {
	        		timerWidgets.cancel();
	        		timerWidgets.scheduleRepeating(15 * 1000);
	        	}
	    		
	    		refreshWidgets();
	            
	        }
	    }, ClickEvent.getType());
		
		
		this.timerApplications.scheduleRepeating(60 * 1000);
		this.refreshApplications();
	}
	

	protected void refreshApplications() {
		Log.debug(this, "Asking server for list of applications");
		WidgetManager.get().getApplicationsList(this.placeId);
	}

	protected void refreshWidgets() {
		if (null != this.stackPanelApplications) {
			int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
			Log.debug(this, "Selected Tab: " + selected);
			if (selected >= 0) {
				String currentApplicationId = this.stackPanelApplications.getWidget(selected).getElement().getPropertyString("id");//this.currentApplications.get(selected).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(selected);
				Log.debug(this, "Asking server for list of widgets for application: "
						+ currentApplicationId);

				WidgetManager.get().getWidgetsList(this.placeId, currentApplicationId);
			}
		}

	}

	@Override
	public void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.instantplaces.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());

		/*
		 * Get the tab that holds this application
		 */
		VerticalPanel panel = null;
		for (int i = 0; i < this.stackPanelApplications.getWidgetCount(); i++) {
			String tabName = this.stackPanelApplications.getWidget(i).getElement().getPropertyString("id");//this.currentApplications.get(i).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(i);
			if (tabName.equals(applicationId)) {
				panel = (VerticalPanel) stackPanelApplications.getWidget(i);
				break;
			}
		}
		
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
				 * Add the new widgets
				 */

				for (org.instantplaces.purewidgets.shared.widgets.Widget widget : widgetList) {
					boolean exists = false;
					for (i = 0; i < panel.getWidgetCount(); i++) {
						String existingWidgetName = panel.getWidget(i).getElement()
								.getPropertyString("id");
						if (existingWidgetName.equals(widget.getWidgetId())) {
							exists = true;
						}
					}

					if (!exists) {
						Log.debug(this, "Adding " + widget.getWidgetId() + " to panel");
						panel.add(this.getHtmlWidget(widget));
					}
				}
			}
			// this.currentWidgetsMap.put(applicationId, widgetList);
		}

		// tabPanelApplications.getWidget(index)

	}

	@Override
	public void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		Log.debug(this, "Received applications: " + applicationList);
		if (null != timerWidgets) {
			timerWidgets.cancel();
		}

//		if (null == this.currentApplications) {
//			this.currentApplications = new ArrayList<Application>();
//		}

		/*
		 * Create a temporary list just with the app names
		 */
		ArrayList<String> applicationIds = new ArrayList<String>();
		for (Application app : applicationList) {
			applicationIds.add(app.getApplicationId());
		}

		
		/*
		 * Delete applications that no longer exist
		 */
		int i = 0;
		// for ()
		while (i <  this.stackPanelApplications.getWidgetCount()) {
			String tabName = this.stackPanelApplications.getWidget(i).getElement().getPropertyString("id"); // this.currentApplications.get(i).getApplicationId(); // tabPanelApplications.getTabBar().getTabHTML(i);
			if (!applicationIds.contains(tabName)) {
				stackPanelApplications.remove(i);
				
			} else {
				// only increment if we haven't deleted anything because if we
				// did, indexed may have changed
				i++;
			}
		}

		/*
		 * Add new applications
		 */
		for (int j = 0; j < applicationList.size(); j++) {
			String appName = applicationList.get(j).getApplicationId();

			boolean exists = false;
			for (i = 0; i < this.stackPanelApplications.getWidgetCount(); i++) {
				String tabName = this.stackPanelApplications.getWidget(i).getElement().getPropertyString("id");//this.currentApplications.get(i).getApplicationId();
				if ( tabName.equals(appName) ) {
					exists = true;
					break;
				}
			}

			if ( !exists ) {
				VerticalPanel p = new VerticalPanel();
				p.getElement().setPropertyString("id", appName);
				
				Image img = new Image("placeinteractiondemo/ajax-loader.gif");
				p.add(img);
				stackPanelApplications.insert(p, j);
				stackPanelApplications.setStackText(j, appName);
			}
		}

		//this.currentApplications = applicationList;

		/*
		 * Set the selected tab
		 */
		int selected = stackPanelApplications.getSelectedIndex();// .getTabBar().getSelectedTab();
		if (selected < 0) {
			this.stackPanelApplications.showStack(0); // .getTabBar().selectTab(0);
		}

		timerWidgets.scheduleRepeating(15 * 1000);
		this.refreshWidgets();
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
		btn.addClickHandler(new EntryClickHandler(option.getReferenceCode(), textBox));
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
		
		FlowPanel flowPanel = new FlowPanel();

		ListBox listbox = new ListBox();
		listbox.setVisibleItemCount(4);
		for (WidgetOption wo : widgetOptions) {
			listbox.addItem(publicDisplayWidget.getShortDescription() + " [" + wo.getReferenceCode() + "]");
		}
		
		
		flowPanel.add(listbox);
		
		Button button = new Button("Send");
		button.addClickHandler(new MultipleOptionImperativeClickHandler(widgetOptions, listbox));
		
		flowPanel.add(button);
		
		return flowPanel;
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
		btn.addClickHandler(new ImperativeClickHandler(option.getReferenceCode()));
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
	
	public void stop() {
		if ( null != this.timerApplications ) {
			this.timerApplications.cancel();
			this.timerApplications = null;
		}
		if ( null != this.timerWidgets ) {
			this.timerWidgets.cancel();
			this.timerWidgets = null;
		}
	}


	@Override
	public void onPlaceList(ArrayList<Place> placeList) {
		// TODO Auto-generated method stub
		
	}
	

}
