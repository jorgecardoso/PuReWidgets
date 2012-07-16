/**
 * 
 */
package org.purewidgets.client.widgets;


import org.purewidgets.client.feedback.InputFeedback;
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
 * The TextBox widget shows a graphical textbox with a caption (and reference code)
 * inside.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pw-upload</dt>
 * <dd>the outer element. </dd>
 * 
 * <dt>.pw-upload-icon</dt>
 * <dd>the icon</dd>
 *
 * <dt>.pw-upload-caption</dt>
 * <dd>the label with application defined caption</dd>
 * 
 * <dt>.pw-upload-referencecode</dt>
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
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U% : %P[0]%";
	
	

	@UiField
	HTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	private org.purewidgets.shared.widgets.Upload widgetUpload;
	
	public PdUpload(String widgetID, String caption) {
		this(widgetID, caption, null);
	}
		
	public PdUpload(String widgetId, String caption, String suggestedReference) {
		super(widgetId);
		
		this.widgetUpload = new org.purewidgets.shared.widgets.Upload(widgetId, caption);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		 
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setUserInputFeedbackPattern(DEFAULT_USER_INPUT_FEEDBACK_PATTERN);
		
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
