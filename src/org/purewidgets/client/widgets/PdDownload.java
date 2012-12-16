/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.WidgetParameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 * 
 * A file or URL that can be downloaded or opened in a browser.
 * The item to download is specified when instantiating the widget.
 * When a user downloads the item, the PdDownload triggers an ActionEvent to let the application know. 
 *  
 * By default, a PdDownload has is represented by a download icon and a caption that can be set by the programmer.
 * (The PdButton displays the reference code next to the caption).
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwDownload</dt>
 * <dd>the outer element. </dd>
 * 
 * <dt>.pwDownloadIcon</dt>
 * <dd>the icon</dd>
 *
 * <dt>.pwDownloadCaption</dt>
 * <dd>the label with application defined caption</dd>
 * 
 * <dt>.pwDownloadReferencecode</dt>
 * <dd>the label with the reference code</dd>
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PdDownload extends PdWidget {
	@UiTemplate("PdDownload.ui.xml")
	interface PdDownloadUiBinder extends UiBinder<Widget, PdDownload> {	}
	private static PdDownloadUiBinder uiBinder = GWT.create(PdDownloadUiBinder.class);


	@UiField
	HTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	private org.purewidgets.shared.widgets.Download widgetDownloadButton;

	/**
	 * Creates a new download widget with the specified id, caption, and download url.
	 * 
	 * @param widgetId The widget id.
	 * @param caption The caption associated with this download item.
	 * @param url The url of the item.
	 */
	public PdDownload(String widgetId, String caption, String url) {
		this(widgetId, caption, url, null);
	}

	/**
	 * 
	 * Creates a new download widget with the specified id, caption,  download url, and
	 * suggested reference code.
	 * 
	 * @param widgetId The widget id.
	 * @param caption The caption associated with this download item.
	 * @param url The url of the item.
	 * @param suggestedRef the suggested reference code.
	 */
	public PdDownload(String widgetId, String caption, String url, String suggestedRef) {
		this(widgetId, caption, url, suggestedRef, "", null);
	}
	
	/**
	 * 
	 * Creates a new download widget with the specified id, caption,  download url, 
	 * suggested reference code, long description, and additional widget parameters.
	 * 
	 * @param widgetId The widget id.
	 * @param caption The caption associated with this download item.
	 * @param url The url of the item.
	 * @param suggestedRef the suggested reference code.
	 * @param longDescription the long description for this widget.
	 * @param parameters The parameters for this widget.
	 */
	public PdDownload(String widgetId, String caption, String url, String suggestedRef, String longDescription, ArrayList<WidgetParameter> parameters) {
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		widgetDownloadButton = new org.purewidgets.shared.widgets.Download(widgetId, caption, url, longDescription, null, parameters);

		this.setWidget(widgetDownloadButton);
		

		//this.button = new com.google.gwt.user.client.ui.Button();

		
		this.uiHTMLCaption.setText(caption);
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions()
				.get(0).getReferenceCode()));
//		

		this.sendToServer();
		this.onReferenceCodesUpdated();
	}

	/**
	 * Sets the caption of the download button.
	 * 
	 * The caption is also the shortDescription of the widget, so this method triggers the WidgetManager to 
	 * re-send this widget's information to the Interaction Manager.
	 * 
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption) {
		this.setShortDescription(caption);
		this.uiHTMLCaption.setText(caption);
		this.sendToServer();
	}
	
	/**
	 * Sets the download url of this download button.
	 * 
	 * This method triggers an update of this widget's information to the Interaction Manager.
	 * 
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.widgetDownloadButton.setUrl(url);
		this.sendToServer();
	}
	
	@UiHandler("uiVerticalPanelMain")
	void handleClick(ClickEvent event) {
		// Simulate an input event
		WidgetInputEvent e = new WidgetInputEvent(PdDownload.this.getWidgetOptions().get(0), null);
		ArrayList<WidgetInputEvent> inputList = new ArrayList<WidgetInputEvent>();
		inputList.add(e);

		PdDownload.this.widget.onInput(inputList);
	}
	
	/**
	 * Handles input from the user, creating the ActionEvent that will be sent to the application
	 * and the InputFeedback that will be displayed on the public display.
	 * 
	 * @return InputFeedback<PdButton> the InputFeedback that will be displayed on the public display.
	 */	
	@Override
	public InputFeedback<PdDownload> handleInput(WidgetInputEvent ie) {
		ActionEvent<PdDownload> ae = new ActionEvent<PdDownload>(ie, this, null);
		InputFeedback<PdDownload> feedback = new InputFeedback<PdDownload>(this, ie, InputFeedback.Type.ACCEPTED, ae);
		
		this.generateUserInputFeedbackMessage(ie, feedback);
		
		return feedback;
	}

	/**
	 * Updates the graphical representations of the reference codes.
	 */	
	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
		super.onReferenceCodesUpdated();
	}

//	@Override
//	public void setEnabled(boolean enabled) {
//		Log.debug(this, "" + enabled);
//		super.setEnabled(enabled);
//		//this.uiButtonMain.setEnabled(enabled);
//	}

	/**
	 * Sets the graphical size of this widget.
	 * @param w The width of the widget.
	 * @param h The height of the widget.
	 */
	@Override
	public void setSize(String w, String h) {
		super.setSize(w, h);
		this.uiVerticalPanelMain.setSize(w, h);
	}

}
