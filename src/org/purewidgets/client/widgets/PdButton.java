/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.htmlwidgets.ClickableHTMLPanel;
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
 * An action button widget that can by "clicked". When "clicked" it triggers an ActionEvent. 
 * By default, a PdButton has a button like graphical appearance with a caption that can be set by the programmer.
 * (The PdButton displays the reference code next to the caption).<p>
 * 
 * Here's an example of creating a button and receiving input:
 * <pre>
 * // Create a button widget giving it an id and a label
 * PdButton pdButton = new PdButton("myButtonId", "Activate me");	
 *
 * // Register the action listener for the list 
 * pdButton.addActionListener(new ActionListener() {
 *     {@literal @}Override
 *     public void onAction(ActionEvent&lt;?&gt; e) {
 *	
 *         // Get the widget that triggered the event.		 
 *         PdWidget source = (PdWidget) e.getSource();
 *
 *         // If the button was activated, do something...
 *         if ( source.getWidgetId().equals("myButtonId") ) {
 *             RootPanel.get().add(new Label("Button activated"));
 *         }
 *     }
 * });
 *		
 * // Add the graphical representation of the button to the browser
 * // window.
 * RootPanel.get().add(pdButton);
 * </pre>
 * 
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwButton</dt>
 * <dd>the outer element. </dd>
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

	/**
	 * Creates a button with the specified id and caption.
	 * 
	 * @param widgetId The id of the widget.
	 * @param caption The caption of the button.
	 */
	public PdButton(String widgetId, String caption) {
		this(widgetId, caption, widgetId);
	}
	
	/**
	 * Created a button with the specified id, caption, and suggested reference code.
	 * 
	 * @param widgetId The id of the widget.
	 * @param caption The caption of the button.
	 * @param suggestedRef The suggested reference code.
	 */
	public PdButton(String widgetId, String caption, String suggestedRef) {
		this(widgetId, caption, suggestedRef, "", null);
	}
	
	/**
	 * Created a button with the specified id, caption, suggested reference code, long description, and parameters.
	 * 
	 * @param widgetId The id of the widget.
	 * @param caption The caption of the button.
	 * @param suggestedRef The suggested reference code.
	 * @param longDescription The long description for the button.
	 * @param parameters Parameters for the button.
	 */
	public PdButton(String widgetId, String caption, String suggestedRef, String longDescription, ArrayList<WidgetParameter> parameters) {
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		widgetButton = new org.purewidgets.shared.widgets.Button(widgetId, caption, longDescription, null, parameters);

		this.setWidget(widgetButton);
	
		this.uiHTMLCaption.setText(caption);
		
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}

	/**
	 * Sets the caption of the button.
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
	
	@UiHandler("uiVerticalPanelMain")
	void handleClick(ClickEvent event) {
		// Simulate an input event
		WidgetInputEvent e = new WidgetInputEvent(PdButton.this.getWidgetOptions().get(0), null);
		ArrayList<WidgetInputEvent> inputList = new ArrayList<WidgetInputEvent>();
		inputList.add(e);

		PdButton.this.widget.onInput(inputList);
		
	}
	
	/**
	 * Handles input from the user, creating the ActionEvent that will be sent to the application
	 * and the InputFeedback that will be displayed on the public display.
	 * 
	 * @return InputFeedback<PdButton> the InputFeedback that will be displayed on the public display.
	 */
	@Override
	public InputFeedback<PdButton> handleInput(WidgetInputEvent ie) {
		ActionEvent<PdButton> ae = new ActionEvent<PdButton>(ie, this, null);
		InputFeedback<PdButton> feedback = new InputFeedback<PdButton>(this, ie, InputFeedback.Type.ACCEPTED, ae);
		
		this.generateUserInputFeedbackMessage(ie, feedback);
		
		return feedback;
	}

	/**
	 * Updates the graphical representations of the reference codes.
	 * 
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
//		//this.button.setEnabled(enabled);
//	}

	
}
