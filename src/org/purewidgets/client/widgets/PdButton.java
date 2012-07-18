/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.htmlwidgets.ClickableHTMLPanel;
import org.purewidgets.client.widgets.PdDownload.PdDownloadUiBinder;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
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
 * <dt>.pwButton</dt>
 * <dd>the outer element. </dd>
 * 
 *
 * <dt>.pwButtonCaption</dt>
 * <dd>the label with application defined caption</dd>
 * 
 * <dt>.pwButtonReferencecode</dt>
 * <dd>the label with the reference code</dd>
 *
 * </dl> 
 * 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PdButton extends PdWidget {
	@UiTemplate("PdButton.ui.xml")
	interface PdButtonUiBinder extends UiBinder<Widget, PdButton> {	}
	private static PdButtonUiBinder uiBinder = GWT.create(PdButtonUiBinder.class);


	@UiField
	ClickableHTMLPanel uiVerticalPanelMain;
	
	@UiField
	Label uiHTMLCaption;
	
	@UiField
	Label uiHTMLReferenceCode;
	
	

	private org.purewidgets.shared.widgets.Button widgetButton;

	public PdButton(String widgetId, String caption) {
		this(widgetId, caption, widgetId);
	}

	public PdButton(String widgetId, String caption, String suggestedRef) {
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		widgetButton = new org.purewidgets.shared.widgets.Button(widgetId, caption);

		this.setWidget(widgetButton);
	
		this.uiHTMLCaption.setText(caption);
		
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}

	@UiHandler("uiVerticalPanelMain")
	void handleClick(ClickEvent event) {
		// Simulate an input event
		WidgetInputEvent e = new WidgetInputEvent(PdButton.this.getWidgetOptions().get(0), null);
		ArrayList<WidgetInputEvent> inputList = new ArrayList<WidgetInputEvent>();
		inputList.add(e);

		PdButton.this.widget.onInput(inputList);
		
	}
	@Override
	public InputFeedback<PdButton> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdButton> feedback = new InputFeedback<PdButton>(this, ie);
		feedback.setType(InputFeedback.Type.ACCEPTED);

		ActionEvent<PdButton> ae = new ActionEvent<PdButton>(ie, this, null);
		feedback.setActionEvent(ae);
		feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
		return feedback;
	}

	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.uiHTMLReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
		super.onReferenceCodesUpdated();
	}

	@Override
	public void setEnabled(boolean enabled) {
		Log.debug(this, "" + enabled);
		super.setEnabled(enabled);
		//this.button.setEnabled(enabled);
	}

	
}
