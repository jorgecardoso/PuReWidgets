/**
 * 
 */
package org.purewidgets.client.widgets;


import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.htmlwidgets.ClickableHTMLPanel;
import org.purewidgets.client.widgets.PdButton.PdButtonUiBinder;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.widgets.TextBox;

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
 * <dt>.pwTextBox</dt>
 * <dd>the outer element. </dd>
 * 
 * <dt>.pwTextBoxTextbox</dt>
 * <dd>the textbox element </dd>
 *
 * <dt>.pwTextBoxCaptioncontainer</dt>
 * <dd>the container for the caption and reference code</dd>
 * 
 * <dt>.pwTextBoxCaptioncontainerCaption</dt>
 * <dd>the label with the caption</dd>
 *
 * <dt>.pwTextBoxCaptioncontainerReferencecode</dt>
 * <dd>the label with the reference code</dd>
 * 
 * <dt>.pwTextBoxCaret</dt>
 * <dd>the label with the blinking caret</dd>
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 */
public class PdTextBox extends PdWidget implements KeyPressHandler, FocusHandler {
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U%: %P[0]%";
	
	
	@UiTemplate("PdTextBox.ui.xml")
	interface PdTextBoxUiBinder extends UiBinder<Widget, PdTextBox> {	}
	private static PdTextBoxUiBinder uiBinder = GWT.create(PdTextBoxUiBinder.class);
	
	@UiField
	ClickableHTMLPanel uiPanelMain;
	
	/**
	 * The graphical textbox.
	 */
	@UiField
	com.google.gwt.user.client.ui.TextBox uiTextbox;
	
	@UiField
	Label uiLabelCaret;
	

	
	@UiField
	Label uiLabelCaption;
	
	@UiField
	Label uiLabelReferencecode;
	
	
	Timer caretTimer;
	boolean isCaretOn;
	String caretOn = "|";
	String caretOff = "";
	
	private org.purewidgets.shared.widgets.TextBox widgetTextBox;
	
	/**
	 *  The input text
	 */
	private String text = "";
	

		
	public PdTextBox(String widgetId, String caption, String suggestedReference) {
		
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setUserInputFeedbackPattern(DEFAULT_USER_INPUT_FEEDBACK_PATTERN);
		
		this.widgetTextBox = new org.purewidgets.shared.widgets.TextBox(widgetId, caption);
		
		this.setWidget(widgetTextBox);
		//this.sendToServer();
		
		
	    /*
	     * Gui stuff
	     */
		
		uiTextbox.addKeyPressHandler(this);
		uiTextbox.addFocusHandler(this);
		//textBox.setText(caption);
		
		uiLabelCaption.setText( this.widgetTextBox.getShortDescription() );
		
		
		
		uiLabelReferencecode.setText( ReferenceCodeFormatter.format( this.getWidgetOptions().get(0).getReferenceCode() ) );
		
		uiLabelCaret.setText("I");
		
		caretTimer = new Timer() {

			@Override
			public void run() {
				flashCaret();
				isCaretOn = !isCaretOn;
			}
			
		};
		caretTimer.scheduleRepeating(1000);
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}
	
	private void flashCaret() {
		if (isCaretOn) {
			PdTextBox.this.uiLabelCaret.setText(caretOff);
			
		} else {
			PdTextBox.this.uiLabelCaret.setText(caretOn);
		}
		
	}
	
		
	

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == 13) {
			Log.debug(this, "Enter detected");
			
			ArrayList<String> params = new ArrayList<String>();
			params.add(this.uiTextbox.getText());
			
			WidgetInputEvent e = new WidgetInputEvent(this.getWidgetOptions().get(0), params);
			uiTextbox.setText("");
			
			// remove the focus so that the internal caret disappears.
			uiTextbox.setFocus(false);
			
			ArrayList<WidgetInputEvent> inputList = new ArrayList<WidgetInputEvent>();
			inputList.add(e);
			this.widget.onInput(inputList);
			//this.onInput(inputList);	
		}
		
	}
	
	@Override
	public void onReferenceCodesUpdated() {
		if (!(this.uiLabelReferencecode  == null)) {
			this.uiLabelReferencecode.setText( ReferenceCodeFormatter.format( this.getWidgetOptions().get(0).getReferenceCode() ) );
		}
		super.onReferenceCodesUpdated();
	}
	
	@Override
	public InputFeedback<PdTextBox> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdTextBox> feedback = new InputFeedback<PdTextBox>(this, ie);
		if ( null != ie.getParameters() && ie.getParameters().size() > 0 ) {
			feedback.setType(InputFeedback.Type.ACCEPTED);
			ActionEvent<PdTextBox> ae = new ActionEvent<PdTextBox>(ie, this, ie.getParameters().get(0));
			feedback.setActionEvent(ae);	
			feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
		} else {
			feedback.setType(InputFeedback.Type.NOT_ACCEPTED);
		}
		
		
		return feedback;
	}		


	@Override
	public void onFocus(FocusEvent event) {
		//this.textBox.setFocus(false);
		
	}
	
	
	public void setText(String text) {
		this.uiTextbox.setText(text);
		this.text = text;
	}
	
	public void setCaret(boolean on) {
		if (on) {
			caretOn = "|";
		} else {
			caretOn = "";
		}
		this.flashCaret();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		Log.debug("" + inputEnabled);
		uiTextbox.setEnabled(enabled);
		//setCaret(enabled);
		super.setEnabled(enabled);
	}
	
	@Override
	public void setInputEnabled(boolean inputEnabled) {
		super.setInputEnabled(inputEnabled);
		this.setCaret(inputEnabled);
	}
	/*
	@Override
	public void onInput(InputEvent ie) {
		
		InputFeedback f = new InputFeedback(ie);
	
		this.getFeedbackSequencer().add(f);
		
	}
	
	@Override
	public void start(InputFeedback inputFeedback) {
		Log.debug("");
		
		// Disable the caret when showing feedback
		this.setCaret(false);
		
		InputFeedback.Type type;
		InputEvent ie = inputFeedback.getInputEvent();
		String text = "";

		if (ie.getCommand() != null && ie.getCommand().getParameter(0) != null) {
			type = this.isInputEnabled()?InputFeedback.Type.ACCEPTED:InputFeedback.Type.NOT_ACCEPTED;
			text = ie.getCommand().getParameter(0);
			if (type == InputFeedback.Type.ACCEPTED) {
				inputFeedback.setInfo(ie.getIdentity().getName()+": "+text);
			} else {
				inputFeedback.setInfo(ie.getIdentity().getName());
			}
		} else {
			type = InputFeedback.Type.NOT_ACCEPTED;
			if (this.isInputEnabled()) { // not accepted because of parameter error
				inputFeedback.setInfo(ie.getIdentity().getName() + ": " + constants.textboxEmptyTextError());
			} else {
				inputFeedback.setInfo(ie.getIdentity().getName());
			}
		}
		inputFeedback.setType(type);
		
		this.text = text;
	
		this.getInputFeedbackDisplay().show(inputFeedback);

		// Only fire application event if the input was accepted
		if (inputFeedback.getType() == InputFeedback.Type.ACCEPTED) {
			ActionEvent ae = new ActionEvent(ie.getIdentity(), this, ie.getWidgetOptionID(), text);
			this.fireActionEvent(ae);
		}
	}
	
@Override
public void stop(InputFeedback feedback, boolean noMore) {
	super.stop(feedback, noMore);
	
	// if there's no more feedback waiting, turn the caret on
	if (noMore) {
		this.setCaret(this.isInputEnabled());
	}
}
*/

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}



}
