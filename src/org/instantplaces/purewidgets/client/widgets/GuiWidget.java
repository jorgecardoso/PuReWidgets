package org.instantplaces.purewidgets.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.instantplaces.purewidgets.client.widgets.events.InputFeedbackListener;
import org.instantplaces.purewidgets.client.widgets.feedback.CumulativeInputFeedbackPanel;
import org.instantplaces.purewidgets.client.widgets.feedback.FeedbackDisplay;
import org.instantplaces.purewidgets.client.widgets.feedback.FeedbackSequencer;
import org.instantplaces.purewidgets.client.widgets.feedback.InputFeedback;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.events.InputEvent;
import org.instantplaces.purewidgets.shared.events.InputListener;
import org.instantplaces.purewidgets.shared.events.ReferenceCodeListener;
import org.instantplaces.purewidgets.shared.widgetmanager.WidgetOption;
import org.instantplaces.purewidgets.shared.widgets.Widget;

import com.google.gwt.user.client.ui.Composite;

/**
 * 

* @author Jorge C. S. Cardoso
*
 */
public class GuiWidget extends Composite implements  InputListener, ReferenceCodeListener, InputFeedbackListener {
	
	private static final int INPUT_EVENT_OLD_AGE = 1000*60*5; // 5 minutes
	protected static final String DEPENDENT_STYLENAME_DISABLED_WIDGET = "disabled";

	
	/**
	 * The widget that supports this guiwidget.
	 */
	protected Widget widget;
	
	protected List<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
/*
	private Align inputFeedbackPanelAlignment = Align.CENTER;
	private Align inputFeedbackPanelReferencePoint = Align.CENTER;
*/
	
	/**
	 * The panel used to display user input info feedback.
	 */
	protected FeedbackDisplay inputFeedbackDisplay;

	/**
	 * The feedback sequencer for internal feedback.
	 */
	private FeedbackSequencer feedbackSequencer;


	/**
	 * The visual state of the widget.
	 */
	protected boolean enabled;

	/**
	 * The input state of the widget
	 * TODO: Merge with the enabled flag
	 */
	protected boolean inputEnabled = true;
	
	/**
	 * Does nothing. Allows subclasses to take care of registering the widget in
	 * the Interaction Manager.
	 */
	/*protected AbstractGuiWidget() {
		this(null, null);
		//this.setFeedbackSequencer(new FeedbackSequencer(this));
	}*/

	/**
	 * Subclasses that implement widgets with several options should not call
	 * this constructor. Although subclasses can call super(widgetID), this
	 * should be avoided because it would mean calling the addWidget twice on
	 * the InteractionManager.
	 * 
	 * @param widgetID
	 *            The id of this widget (must be unique within the application).
	 */
	/*public AbstractGuiWidget(String widgetID) {
		this(widgetID, null);
	}*/

	/**
	 * Subclasses that implement widgets with several options should not call
	 * this constructor. Although subclasses can call super(widgetID), this
	 * should be avoided because it would mean calling the addWidget twice on
	 * the InteractionManager.
	 * 
	 * @param widgetID
	 *            The id of this widget (must be unique within the application).
	 * @param suggestedReferenceCode
	 *            The suggested reference code
	 */
	public GuiWidget() {
		this.inputFeedbackDisplay = new CumulativeInputFeedbackPanel(this);
		this.feedbackSequencer = new FeedbackSequencer(this.inputFeedbackDisplay, this);
	}

	public boolean isDisplaying() {
		com.google.gwt.user.client.ui.Widget current = this;
		/*
		 * If the widget is not on the DOM than it is not visible.
		 */
		if ( !current.isAttached() ) {
			return false;
		}
		
		/*
		 * If any ancestor is not visible than this widget is not visible either
		 */
		while ( current != null ) {
			if ( !current.isVisible() ) {
				return false;
			}
			current = current.getParent();
		}
		
		return true;
	}
	
	/**
	 * Adds an ActionListener to this widget. ActionListeners will be notified
	 * of any actions triggered by this widget, usually in response to user input.
	 * 
	 * @param handler The ActionListener to add.
	 */
	public void addActionListener(ActionListener handler) {
		
		this.actionListeners.add(handler);
	}
	

	/**
	 * Adds an optionID to this widget.
	 * 
	 * @param optionID The widgetOption to add.
	 */
	public void addWidgetOption(WidgetOption option) {
		this.widget.addWidgetOption(option);
	}
	
	/**
	 * 
	 * @return The widget's bottom y coordinate
	 */
	public int getBottom() {
		return this.getElement().getAbsoluteBottom();
	}

	public FeedbackSequencer getFeedbackSequencer() {
		return feedbackSequencer;
	}
	
	
	/**
	 * 
	 * @return The height of the widget, in pixels.
	 */
	public int getHeight() {
		return this.getOffsetHeight();
	}	

	/**
	 * 
	 * @return The widget's leftmost x coordinate
	 */
	public int getLeft() {
		return this.getElement().getAbsoluteLeft();
	}
	

	

	
	/**
	 * 
	 * @return The widget's rightmost x coordinate
	 */
	public int getRight() {
		return this.getElement().getAbsoluteRight();
	}

	/**
	 * 
	 * @return The widget's top y coordinate
	 */
	public int getTop() {
		return this.getElement().getAbsoluteTop();
	}
	
	/**
	 * Returns this widget's id. There is no setId() because the id of a
	 * widget cannot be changed after it is set in the constructor.
	 * 
	 * 
	 * @return This widget's WidgetID.
	 * @see org.instantplaces.purewidgets.shared.widgets.WidgetInterface#getId()
	 */
	public String getWidgetId() {
		return this.widget.getWidgetId();
	}

	/**
	 * Returns the list of options of this widget.
	 * 
	 * @return The ArrayList of WidgetOption.
	 */
	public ArrayList<WidgetOption> getWidgetOptions() {
		return this.widget.getWidgetOptions();
	}

	/**
	 * 
	 * @return The width of the widget, in pixels.
	 */
	public int getWidth() {
		return this.getOffsetWidth();
	}
	
	protected InputFeedback<? extends GuiWidget> handleInput(InputEvent ie) {
		InputFeedback<GuiWidget> feedback = new InputFeedback<GuiWidget>(ie);
		feedback.setType(InputFeedback.Type.ACCEPTED);
		
		ActionEvent<GuiWidget> ae = new ActionEvent<GuiWidget>(
				this, // source widget 
				ie, // input event
				null);
		feedback.setActionEvent(ae);
		return feedback;
	}
	
	/**
	 * Called by the FeedbackSequencer to notify that a feedback display has ended.
	 * 
	 * This method will always be called with the same InputFeedback that
	 * the previous start method and will always after an inputFeedbackStarted call.
	 * 
	 * @param feedback The feedback to stop showing. 
	 */
	@Override
	public final void inputFeedbackEnded(InputFeedback<? extends GuiWidget> feedback, boolean noMore) {
		
	}
	
	/**
	 * Triggers the processing of the specified InputFeedback.
	 * 
	 * @param feedback The feedback to process.
	 */
	@Override
	public final void inputFeedbackStarted(InputFeedback<? extends GuiWidget> feedback) {
		Log.debugFinest(this, "Input feedback started: " + feedback.toString());
		
		/*
		 * If feedback for an accepted input has started, trigger the
		 * application event.
		 */
		if (feedback.getType() == InputFeedback.Type.ACCEPTED) {			
				this.fireActionEvent(feedback.getActionEvent()); // widget specific object
		}
	}
	
	/**
	 * Returns the visual state of the widget.
	 * 
	 * @return The state of the widget.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean isInputEnabled() {
		return this.inputEnabled;
	}
	
	
	public boolean isVolatile() {
		return this.widget.isVolatileWidget();
	}
	
	
	
	/**
	 * Event triggered by the InteractionManager to signal
	 * input to this widget.
	 * 
	 * 
	 * @param ie An InputEvent object.
	 */
	@Override
	public final void onInput(ArrayList<InputEvent> inputList) {
		Log.debugFinest(this, "Received input event list ");
		
		for ( InputEvent ie : inputList ) {
			Log.debugFinest(this, "  Processing: " + (ie != null ? ie.toDebugString() : "null input event"));
			
			/*
			 * If the input is old, just trigger the application event
			 */
			if ( ie.getAge() > INPUT_EVENT_OLD_AGE ) {
				/*
				 * Ask the widget to validate the input and generate an appropriate feedback
				 */
				InputFeedback<? extends GuiWidget> feedback = handleInput(ie);
				this.fireActionEvent( feedback.getActionEvent() );
				
				
			/*
			 * If the input is recent, generate feedback for it
			 */
			} else {
				
				InputFeedback<? extends GuiWidget> feedback = new InputFeedback<GuiWidget>(ie);
				if( this.isInputEnabled() ) {
					
					/*
					 * If this widget is enabled than ask it to handle the input, validating it 
					 */
					feedback = handleInput(ie);
				} else {
					/*
					 * If the widget is not enabled than the input is not accepted.
					 */
					feedback.setType(InputFeedback.Type.NOT_ACCEPTED);
				}
			
				/*
				 * Schedule the feedback to appear
				 */
				this.feedbackSequencer.add(feedback);
			}
		}

	}

	
	/**
	 * Concrete widgets should override this method to update their Gui with the correct reference codes.
	 */
	@Override
	public void onReferenceCodesUpdated() {
		Log.debugFinest("AbstractGuiWidget Updating ReferenceCodes");
	}
	
	
	/**
	 * Removes an ActionListener from this widget.
	 * 
	 * @param handler The ActionListener to remove.
	 */
	public void removeActionListener(ActionListener handler) {
		this.actionListeners.remove(handler);
	}
	
	public void removeFromServer() {
		this.widget.removeFromServer();
	}

	/**
	 * Removes an optionID from this widget.
	 * @param optionID
	 */
	public void removeWidgetOption(WidgetOption option) {
		
		this.widget.removeWidgetOption(option);
	}
	

	
	
	public void sendToServer() {
		this.widget.sendToServer();
	}
		
	
	
	//
//	/**
//	 * @param alignment
//	 */
//	public void setInputFeedbackPanelAlignment(Align alignment) {
//		this.inputFeedbackPanelAlignment = alignment;
//	}
//
//	/**
//	 * @return the inputInfoPanelAlignment
//	 */
//	protected Align getInputFeedbackPanelAlignment() {
//		return inputFeedbackPanelAlignment;
//	}
//
//	/**
//	 * @param inputFeedbackPanelReferencePoint
//	 *            the inputInfoPanelReferencePoint to set
//	 */
//	protected void setInputFeedbackPanelReferencePoint(
//			Align inputFeedbackPanelReferencePoint) {
//		this.inputFeedbackPanelReferencePoint = inputFeedbackPanelReferencePoint;
//	}
//
//	/**
//	 * @return the inputInfoPanelReferencePoint
//	 */
//	protected Align getInputFeedbackPanelReferencePoint() {
//		return inputFeedbackPanelReferencePoint;
//	}
//
//	/**
//	 * @param inputFeedbackDisplay
//	 *            the inputInfoPanel to set
//	 */
//	public void setInputFeedbackDisplay(FeedbackDisplay inputFeedbackDisplay) {
//		this.inputFeedbackDisplay = inputFeedbackDisplay;
//	}
//
//	/**
//	 * @return the inputInfoPanel
//	 */
//	public FeedbackDisplay getInputFeedbackDisplay() {
//		return inputFeedbackDisplay;
//	}
//
//	/**
//	 * Sets the auto-disable feature to disable input automatically after an
//	 * input has been received. Input will be disabled during the specified
//	 * number of seconds. After that it will be enabled again.
//	 * 
//	 * @param seconds
//	 *            The number of milliseconds during which input should be
//	 *            disabled. A value of 0 (zero) disables the auto-disable
//	 *            feature.
//	 */
//	public void setAutoDisable(int milliseconds) {
//		this.autoDisable = milliseconds;
//	}
//
//	/**
//	 * Returns the duration of the auto-disable feature.
//	 * 
//	 * @return The duration of the auto-disable feature in milliseconds. 0 means
//	 *         it is not enabled.
//	 */
//	public int getAutoDisable() {
//		return this.autoDisable;
//	}
//
//	/**
//	 * @param feedbackSequencer
//	 *            the feedbackSequencer to set
//	 */
//	public void setFeedbackSequencer(FeedbackSequencer feedbackSequencer) {
//		this.feedbackSequencer = feedbackSequencer;
//	}
//
//	/**
//	 * @return the feedbackSequencer
//	 */
//	public FeedbackSequencer getFeedbackSequencer() {
//		return feedbackSequencer;
//	}
//
	/**
	 * Sets the visual feedback of the widget to enabled or disabled. This
	 * settings affects only the visual state. The widget may still receive
	 * input even if its visual state indicates otherwise.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			this.removeStyleDependentName(GuiWidget.DEPENDENT_STYLENAME_DISABLED_WIDGET);// .setStyleName(this.getStyleName());
		} else {
			this.getWidget().addStyleDependentName(GuiWidget.DEPENDENT_STYLENAME_DISABLED_WIDGET); //.setStyleName(					this.getStyleName() + this.DISABLED_STYLENAME_SUFFIX);
		}
	}
	
	
	public void setFeedbackSequencer(FeedbackSequencer feedbackSequencer) {
		this.feedbackSequencer = feedbackSequencer;
	}
	
	/**
	 * Enables or disables this Widget's processing of InputEvents. If disabled,
	 * the Widget will no longer fire ActionEvents.
	 * 
	 * Subclasses should obey this rule and call fireActionEvent (which does
	 * this checking) to fire ActionEvents to their listeners.
	 * 
	 * @param inputEnabled
	 */
	public void setInputEnabled(boolean inputEnabled) {

		this.inputEnabled = inputEnabled;

		/*
		 * If we're showing feedback don't mess with it...
		 */
		if (!this.feedbackSequencer.isShowing()) {
			this.setEnabled(inputEnabled);
		}
		// this.hideInfoPanel();
	}
	
	


	
	public void setVolatile(boolean isVolatile) {
		this.widget.setVolatileWidget(isVolatile);
	}

	
	/*
	 * Getters / Setters
	 */

	/**
	 * Sets the widget associated with this GuiWidget.
	 * 
	 * @param widget
	 */
	public void setWidget(Widget widget) {
		//Log.debug("AbstractGuiWidget " + this.toDebugString());
		this.widget = widget; 
		this.widget.setInputListener(this);
		this.widget.setReferenceCodeListener(this);
	}


	
	/**
	 * Sets this widget's options to the specified ArrayList.
	 * The existing options are discarded.
	 * 
	 * @param options
	 */
	public void setWidgetOptions(ArrayList<WidgetOption> options) {
		this.widget.setWidgetOptions(options);
	}
	
	/**
	 * Creates a string representation of this widget for debug purposes.
	 * 
	 * @return a string representation of this object.
	 */
	public String toDebugString() {
		if (this.widget != null) {
			return "GuiWidget(" + this.widget.toDebugString() + ")";
		} else {
			return "GuiWidget()";
		}
	}
	
	/**
	 * Fires an ActionEvent to all registered listeners of this Widget.
	 * 
	 * 
	 * @param ae
	 *            The ActionEvent to fire. If null, a default ActionEvent will
	 *            be created and sent.
	 */
	protected void fireActionEvent(ActionEvent<? extends GuiWidget> ae) {
		if ( null == ae ) {
			return;
		}
		
		//Log.debug(this, "Firing " + ae.toDebugString());
		for ( ActionListener al : this.actionListeners ) {
			//Log.debug(this, "   on " + al.toString());
			al.onAction(ae);
		}
		
	}
	
	
	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return this.widget.getShortDescription();
	}


	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.widget.setShortDescription(shortDescription);
	}


	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return this.widget.getLongDescription();
	}


	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.widget.setLongDescription(longDescription);
	}
}
