/**
 * 
 */
package org.purewidgets.client.widgets;


import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * Allows users to upload files to the application.
 * When a user uploads the item, the PdDownload triggers an ActionEvent with the url of the uploaded
 * file. 
 *  
 * By default, a PdUpload has is represented by a download icon and a caption that can be set by the programmer.
 * (The PdUpload displays the reference code next to the caption).
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwUpload</dt>
 * <dd>the outer element. </dd>
 * 
 * <dt>.pwUploadIcon</dt>
 * <dd>the icon</dd>
 *
 * <dt>.pwUploadCaption</dt>
 * <dd>the label with application defined caption</dd>
 * 
 * <dt>.pwUploadReferencecode</dt>
 * <dd>the label with the reference code</dd>
 *
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 */
public class PdUpload extends PdWidget  {
	
	@UiTemplate("PdUpload.ui.xml")
	interface PdUploadUiBinder extends UiBinder<Widget, PdUpload> {	}
	private static PdUploadUiBinder uiBinder = GWT.create(PdUploadUiBinder.class);

	private final String USER_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME + ": " + MessagePattern.getInputParameterPattern(0)+"(10) " + MessagePattern.PATTERN_INPUT_AGE;
	private final String USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME + " " + MessagePattern.PATTERN_INPUT_AGE;
	private final String USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN = MessagePattern.getInputParameterPattern(0)+"(10)";
	
	

	@UiField
	HTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	private org.purewidgets.shared.widgets.Upload widgetUpload;
	
	/**
	 * Creates a new PdUpload with the specified widget id and caption.
	 * 
	 * @param widgetID The widget id.
	 * @param caption The caption.
	 */
	public PdUpload(String widgetID, String caption) {
		this(widgetID, caption, null);
	}
		
	/**
	 * 
	 * Creates a new PdUpload with the specified widget id, caption, and suggested reference code.
	 * 
	 * @param widgetID The widget id.
	 * @param caption The caption.
	 * @param suggestedReference The suggested reference code.
	 */
	public PdUpload(String widgetId, String caption, String suggestedReference) {
		super(widgetId);
		
		this.widgetUpload = new org.purewidgets.shared.widgets.Upload(widgetId, caption);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		 
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setOnScreenFeedbackInfo(USER_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackInfo(USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackTitle(USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN);
		
		this.uiHTMLCaption.setText(caption);
		this.setWidget(widgetUpload);
		//this.sendToServer();
		
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}
	
	/**
	 * Sets the caption of the upload widget.
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
	 * Updates the graphical representations of the reference codes.
	 */		
	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
		super.onReferenceCodesUpdated();
	}
	
	/**
	 * Handles input from the user, creating the ActionEvent that will be sent to the application
	 * and the InputFeedback that will be displayed on the public display.
	 * 
	 * @return InputFeedback<PdButton> the InputFeedback that will be displayed on the public display.
	 */		
	@Override
	public InputFeedback<PdUpload> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdUpload> feedback = new InputFeedback<PdUpload>(this, ie, null, null);
		if ( null != ie.getParameters() && ie.getParameters().size() > 0 && ie.getParameters().get(0).length() > "http".length() ) {
			feedback.setType(InputFeedback.Type.ACCEPTED);
			ActionEvent<PdUpload> ae = new ActionEvent<PdUpload>(ie, this, ie.getParameters().get(0));
			feedback.setActionEvent(ae);	
			this.generateUserInputFeedbackMessage(ie, feedback);
		} else {
			this.generateUserInputFeedbackMessage(ie, feedback);
			feedback.setType(InputFeedback.Type.NOT_ACCEPTED);
		}
		
		return feedback;
	}		


	
}
