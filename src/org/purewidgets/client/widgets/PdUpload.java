/**
 * 
 */
package org.purewidgets.client.widgets;


import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.widgets.PdDownload.PdDownloadUiBinder;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.Upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Timer;


/**
 * The TextBox widget shows a graphical textbox with a caption (and reference code)
 * inside.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-GuiTextBox[-disabled]</dt>
 * <dd>the outer element. [-disabled] is applied when the textbox is disabled</dd>
 * 
 * <dt>.instantplaces-GuiTextBox[-disabled] .textbox</dt>
 * <dd>the textbox</dd>
 *
 * <dt>.instantplaces-GuiTextBox[-disabled] .captioncontainer</dt>
 * <dd>the container for the caption and reference code</dd>
 * 
 * <dt>.instantplaces-guiTextBox[-disabled] .captioncontainer .caption</dt>
 * <dd>the label with the caption</dd>
 *
 * <dt>.instantplaces-guiTextBox[-disabled] .captioncontainer .referencecode</dt>
 * <dd>the label with the reference code</dd>
 * 
 * <dt>.instantplaces-GuiTextBox[-disabled] caret</dt>
 * <dd>the label with the blinking caret</dd>
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 */
public class PdUpload extends PdWidget  {
	@UiTemplate("PdUpload.ui.xml")
	interface PdUploadUiBinder extends UiBinder<Widget, PdUpload> {	}
	private static PdUploadUiBinder uiBinder = GWT.create(PdUploadUiBinder.class);
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U% : %P[0]%";
	
	

	@UiField
	HTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	private org.purewidgets.shared.widgets.Upload widgetUpload;
	
	public PdUpload(String caption) {
		this((String)null, caption);
	}
	
	public PdUpload(String widgetID, String caption) {
		this(widgetID, caption, null);
	}
		
	public PdUpload(String widgetId, String caption, String suggestedReference) {
		super();
		
		this.widgetUpload = new org.purewidgets.shared.widgets.Upload(widgetId, caption);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		 
		
		/*
		 * Set the default user feedback pattern
		 */
		this.userInputFeedbackPattern = DEFAULT_USER_INPUT_FEEDBACK_PATTERN;
		
		this.uiHTMLCaption.setText(caption);
		this.setWidget(widgetUpload);
		//this.sendToServer();
		
	
		
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}
	
		
	
	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
		super.onReferenceCodesUpdated();
	}
	
	@Override
	public InputFeedback<PdUpload> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdUpload> feedback = new InputFeedback<PdUpload>(this, ie);
		if ( null != ie.getParameters() && ie.getParameters().size() > 0 && ie.getParameters().get(0).length() > "http".length() ) {
			feedback.setType(InputFeedback.Type.ACCEPTED);
			ActionEvent<PdUpload> ae = new ActionEvent<PdUpload>(ie, this, ie.getParameters().get(0));
			feedback.setActionEvent(ae);	
			feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
		} else {
			feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
			feedback.setType(InputFeedback.Type.NOT_ACCEPTED);
		}
		
		
		return feedback;
	}		

	
	
	@Override
	public void setInputEnabled(boolean inputEnabled) {
		super.setInputEnabled(inputEnabled);
	}
	
}