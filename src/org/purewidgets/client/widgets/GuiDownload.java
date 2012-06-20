/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.widgets.feedback.InputFeedback;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.logging.Log;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-GuiButton</dt>
 * <dd>the &lt;button&gt; element</dd>
 * <dt>.instantplaces-GuiButton-disabled</dt>
 * <dd>the disabled &lt;button&gt; element</dd>
 * <dt>.instantplaces-GuiButton .caption</dt>
 * <dd>the caption of the button</dd>
 * <dt>.instantplaces-GuiButton .referencecode</dt>
 * <dd>the reference code element</dd>
 * <dt>.instantplaces-GuiButton-disabled .caption</dt>
 * <dd>the disabled caption element</dd>
 * <dt>.instantplaces-GuiButton-disabled .referencecode</dt>
 * <dd>the disabled reference code element</dd>
 * </dl>
 * 
 * The component elements (.caption and .referencecode) also keep the GWT style
 * name (gwt-Html).
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class GuiDownload extends GuiWidget {
	@UiTemplate("GuiDownload.ui.xml")
	interface GuiDownloadUiBinder extends UiBinder<Widget, GuiDownload> {	}
	private static GuiDownloadUiBinder uiBinder = GWT.create(GuiDownloadUiBinder.class);


	@UiField
	HTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	private org.purewidgets.shared.widgets.Download widgetDownloadButton;

	public GuiDownload(String widgetId, String caption, String url) {
		this(widgetId, caption, url, null);
	}

	public GuiDownload(String widgetId, String caption, String url, String suggestedRef) {
		initWidget(uiBinder.createAndBindUi(this));
		
		widgetDownloadButton = new org.purewidgets.shared.widgets.Download(widgetId, caption, url);

		this.setWidget(widgetDownloadButton);
		

		//this.button = new com.google.gwt.user.client.ui.Button();

		
		this.uiHTMLCaption.setText(caption);
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions()
				.get(0).getReferenceCode()));
//		
//		// Give the overall composite a style name.
//		this.setStyleName(DEFAULT_STYLENAME);
//		this.lblCaption.addStyleName(GuiButton.CAPTION_STYLENAME_SUFFIX);
//		this.lblReferenceCode.addStyleName(GuiButton.REFERENCECODE_STYLENAME_SUFFIX);
		this.sendToServer();
	}

	
	void handleClick(ClickEvent event) {
		// Simulate an input event
		InputEvent e = new InputEvent(GuiDownload.this.getWidgetOptions().get(0), null);
		ArrayList<InputEvent> inputList = new ArrayList<InputEvent>();
		inputList.add(e);

		GuiDownload.this.widget.onInput(inputList);
	}
	
	@Override
	public InputFeedback<GuiDownload> handleInput(InputEvent ie) {
		return null;
	}

	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
	}

	@Override
	public void setEnabled(boolean enabled) {
		Log.debug(this, "" + enabled);
		super.setEnabled(enabled);
		//this.uiButtonMain.setEnabled(enabled);
	}

	@Override
	public void setSize(String w, String h) {
		super.setSize(w, h);
		this.uiVerticalPanelMain.setSize(w, h);
	}

}
