/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

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
public class GuiButton extends PDWidget {

	/**
	 * The suffix for the caption of the textbox.
	 */
	public static final String CAPTION_STYLENAME_SUFFIX = "caption";

	public static final String DEFAULT_STYLENAME = "instantplaces-GuiButton";

	/**
	 * The suffix for the reference code.
	 */
	public static final String REFERENCECODE_STYLENAME_SUFFIX = "referencecode";

	/**
	 * The suffix for the feedback.
	 */
	// public static final String FEEDBACK_STYLENAME_SUFFIX = "-feedback";

	private com.google.gwt.user.client.ui.Button button;
	private com.google.gwt.user.client.ui.HTML lblCaption;
	private com.google.gwt.user.client.ui.HTML lblReferenceCode;
	// delete : private com.google.gwt.user.client.ui.HTML lblFeedback;

	private org.purewidgets.shared.widgets.Button widgetButton;

	public GuiButton(String widgetId, String caption) {
		this(widgetId, caption, widgetId);
	}

	public GuiButton(String widgetId, String caption, String suggestedRef) {
		super();

		widgetButton = new org.purewidgets.shared.widgets.Button(widgetId, caption);

		this.setWidget(widgetButton);
		
		this.button = new com.google.gwt.user.client.ui.Button();

		this.button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Simulate an input event
				WidgetInputEvent e = new WidgetInputEvent(GuiButton.this.getWidgetOptions().get(0), null);
				ArrayList<WidgetInputEvent> inputList = new ArrayList<WidgetInputEvent>();
				inputList.add(e);

				GuiButton.this.widget.onInput(inputList);
			}
		});
		this.lblCaption = new HTML(caption);
		this.lblReferenceCode = new HTML(ReferenceCodeFormatter.format(this.getWidgetOptions()
				.get(0).getReferenceCode()));
		// this.setLblFeedback(new HTML(""));

		this.button.getElement().appendChild(this.lblCaption.getElement());
		this.button.getElement().appendChild(this.lblReferenceCode.getElement());

		// All composites must call initWidget() in their constructors.
		initWidget(button);

		// Give the overall composite a style name.
		this.setStyleName(DEFAULT_STYLENAME);
		this.lblCaption.addStyleName(GuiButton.CAPTION_STYLENAME_SUFFIX);
		this.lblReferenceCode.addStyleName(GuiButton.REFERENCECODE_STYLENAME_SUFFIX);
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}

	@Override
	public InputFeedback<GuiButton> handleInput(WidgetInputEvent ie) {
		InputFeedback<GuiButton> feedback = new InputFeedback<GuiButton>(this, ie);
		feedback.setType(InputFeedback.Type.ACCEPTED);

		ActionEvent<GuiButton> ae = new ActionEvent<GuiButton>(ie, this, null);
		feedback.setActionEvent(ae);
		feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
		return feedback;
	}

	@Override
	public void onReferenceCodesUpdated() {
		// Log.debug(this + " Updating reference code");
		this.lblReferenceCode.setText(ReferenceCodeFormatter.format(this.getWidgetOptions().get(0)
				.getReferenceCode()));
		super.onReferenceCodesUpdated();
	}

	@Override
	public void setEnabled(boolean enabled) {
		Log.debug(this, "" + enabled);
		super.setEnabled(enabled);
		this.button.setEnabled(enabled);
	}

	@Override
	public void setSize(String w, String h) {
		super.setSize(w, h);
		button.setSize(w, h);
	}

}
