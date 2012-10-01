package org.purewidgets.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.feedback.CumulativeInputFeedbackPanel;
import org.purewidgets.client.feedback.FeedbackSequencer;
import org.purewidgets.client.feedback.InputEventAgeMessages;
import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.feedback.InputFeedbackListener;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.events.WidgetInputListener;
import org.purewidgets.shared.events.ReferenceCodeListener;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Composite;

/**
 * Base class for client widgets.
 * A PdWidget represents the base class for the client side version of a
 * PuReWidgets widget. A PdWidget defines its own visual appearance, input data
 * processing and interpretation, and the high-level events that applications
 * receive when the widget is acted upon.
 * 
 * A PdWidget is associated with a {@link org.purewidgets.shared.im.Widget} (or a subclass), which holds
 * the actual data that describes the widget, and that is sent to the
 * interaction manager server.
 * 
 * The PdWidget class defines basic functionality for all widgets: 1. Listening
 * for user input 2. Triggering user input feedback on the public display 3.
 * Maintaining a cache of the reference codes assigned
 * 
 * 
 * @author Jorge C. S. Cardoso
 * @see org.purewidgets.shared.im.Widget
 */
public class PdWidget extends Composite implements WidgetInputListener, ReferenceCodeListener,
		InputFeedbackListener {

	/**
	 * The key name for storing the assigned referenced codes on local storage
	 */
	private static final String REFERENCE_CODES_STORAGE_ID = "referenceCodes";

	/**
	 * The minimum age of an input to be considered old (no feedback is
	 * triggered).
	 */
	private static final int INPUT_EVENT_OLD_AGE = 1000 * 60 * 60 * 24; // 1 day

	/**
	 * The stylename suffix for disabled widgets.
	 */
	protected static final String DEPENDENT_STYLENAME_DISABLED_WIDGET = "disabled";

	/**
	 * The default pattern to apply to generate the input feedback title for
	 * on-screen widgets. {@see org.purewidgets.client.feedback.MessagePattern}
	 */
	protected static final String DEFAULT_ONSCREEN_FEEDBACK_TITLE = MessagePattern.PATTERN_USER_NICKNAME;
	
	/**
	 * The default pattern to apply to generate the input feedback message for
	 * on-screen widgets. {@see org.purewidgets.client.feedback.MessagePattern}
	 */
	protected static final String DEFAULT_ONSCREEN_FEEDBACK_INFO = MessagePattern.PATTERN_USER_NICKNAME
			+ " " + MessagePattern.PATTERN_INPUT_AGE;

	/**
	 * The default pattern to apply to generate the input feedback message title
	 * for off-screen widgets. {@see
	 * org.purewidgets.client.feedback.MessagePattern}
	 */
	protected static final String DEFAULT_OFFSCREEN_FEEDBACK_TITLE = MessagePattern.PATTERN_USER_NICKNAME
			+ " " + MessagePattern.PATTERN_INPUT_AGE;;

	/**
	 * The default pattern to apply to generate the input feedback message
	 * description for off-screen widgets. {@see
	 * org.purewidgets.client.feedback.MessagePattern}
	 */
	protected static final String DEFAULT_OFFSCREEN_FEEDBACK_INFO = MessagePattern.PATTERN_WIDGET_SHORT_DESCRIPTION;

	/**
	 * The PuReWidgets Widget that is associated with this PdWidget.
	 */
	protected Widget widget;

	/**
	 * The action listeners that are registered to receive events from this
	 * widget
	 */
	protected List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	/**
	 * The panel used to display user input info feedback.
	 */
	protected CumulativeInputFeedbackPanel inputFeedbackDisplay;

	/**
	 * The feedback sequencer for feedback.
	 */
	private FeedbackSequencer feedbackSequencer;

	/**
	 * The pattern to apply to generate the input feedback title for on-screen
	 * widgets.
	 */
	private String onScreenFeedbackTitle = DEFAULT_ONSCREEN_FEEDBACK_TITLE;

	
	/**
	 * The pattern to apply to generate the input feedback message for on-screen
	 * widgets.
	 */
	private String onScreenFeedbackInfo = DEFAULT_ONSCREEN_FEEDBACK_INFO;

	/**
	 * The pattern to apply to generate the input feedback message title for
	 * off-screen widgets.
	 */
	private String offScreenFeedbackTitle = DEFAULT_OFFSCREEN_FEEDBACK_TITLE;

	/**
	 * The pattern to apply to generate the input feedback message description
	 * for off-screen widgets.
	 */
	private String offScreenFeedbackInfo = DEFAULT_OFFSCREEN_FEEDBACK_INFO;

	/**
	 * The visual state of the widget.
	 */
	protected boolean enabled;

	/**
	 * The input state of the widget TODO: Merge with the enabled flag
	 */
	protected boolean inputEnabled = true;

	/**
	 * Message localization for widget input age.
	 */
	InputEventAgeMessages messages;

	/**
	 * The localstorage to store the reference codes.
	 */
	private LocalStorage localStorage;

	/**
	 * The id of this widget.
	 */
	private String widgetId;

	/**
	 * Creates a new PdWidget with the specified widget id.
	 * 
	 * @param widgetId
	 *            The id of this widget (must be unique within the application).
	 */
	public PdWidget(String widgetId) {
		this.widgetId = widgetId;
		this.inputFeedbackDisplay = new CumulativeInputFeedbackPanel(this);
		this.feedbackSequencer = new FeedbackSequencer(this.inputFeedbackDisplay, this);
		this.localStorage = new LocalStorage(PDApplication.getCurrent().getApplicationId() + "-"
				+ this.widgetId);
		this.messages = GWT.create(InputEventAgeMessages.class);
	}

	/**
	 * Adds an ActionListener to this widget. ActionListeners will be notified
	 * of any actions triggered by this widget, usually in response to user
	 * input.
	 * 
	 * @param handler
	 *            The ActionListener to add.
	 */
	public void addActionListener(ActionListener handler) {
		this.actionListeners.add(handler);
	}

	/**
	 * Adds a widget option to the underlying
	 * {@link org.purewidgets.shared.im.Widget}.
	 * 
	 * @param option
	 *            The widgetOption to add.
	 * @see org.purewidgets.shared.im.Widget#addWidgetOption(WidgetOption)
	 */
	public void addWidgetOption(WidgetOption option) {
		this.widget.addWidgetOption(option);
	}

	/**
	 * Gets the feedback sequencer for this PdWidget.
	 * 
	 * @return The feedback sequencer for this PdWidget.
	 */
	public FeedbackSequencer getFeedbackSequencer() {
		return feedbackSequencer;
	}

	// /**
	// *
	// * @return The widget's bottom y coordinate
	// */
	// public int getBottom() {
	// return this.getElement().getAbsoluteBottom();
	// }

	/**
	 * Gets the local storage associated with this PdWidget.
	 * 
	 * @return The LocalStorage associated with this PdWidget.
	 */
	public LocalStorage getLocalStorage() {
		return localStorage;
	}

	// /**
	// *
	// * @return The height of the widget, in pixels.
	// */
	// public int getHeight() {
	// return this.getOffsetHeight();
	// }

	// /**
	// *
	// * @return The widget's leftmost x coordinate
	// */
	// public int getLeft() {
	// return this.getElement().getAbsoluteLeft();
	// }

	// /**
	// *
	// * @return The widget's rightmost x coordinate
	// */
	// public int getRight() {
	// return this.getElement().getAbsoluteRight();
	// }

	// /**
	// *
	// * @return The widget's top y coordinate
	// */
	// public int getTop() {
	// return this.getElement().getAbsoluteTop();
	// }

	/**
	 * Gets the long description of this PdWidget (which is the same as the underlying Widget).
	 * This method calls {@link org.purewidgets.shared.im.Widget#getLongDescription()} on the underlying Widget.
	 * 
	 * 
	 * @return The long description
	 */
	public String getLongDescription() {
		return this.widget.getLongDescription();
	}

	/**
	 * Gets the on-screen widget feedback title.
	 * 
	 * @return the on-screen widget feedback title.
	 */
	public String getOnScreenFeedbackTitle() {
		return onScreenFeedbackTitle;
	}

	// /**
	// *
	// * @return The width of the widget, in pixels.
	// */
	// public int getWidth() {
	// return this.getOffsetWidth();
	// }

	/**
	 * Gets the short description of this PdWidget (which is the same as the underlying Widget).
	 * This method calls {@link org.purewidgets.shared.im.Widget#getShortDescription()} on the underlying Widget.
	 * 
	 * 
	 * @return The short description
	 */
	public String getShortDescription() {
		return this.widget.getShortDescription();
	}

	/**
	 * Gets the feedback info for on-screen widgets.
	 * 
	 * @return The feedback info for on-screen widgets.
	 */
	public String getOnScreenFeedbackInfo() {
		return onScreenFeedbackInfo;
	}

	/**
	 * Gets the feedback info for off-screen widgets.
	 * @return The feedback info for off-screen widgets.
	 */
	public String getOffScreenFeedbackInfo() {
		return offScreenFeedbackInfo;
	}

	/**
	 * Gets the feedback title for off-screen widgets.
	 * 
	 * @return The feedback title for off-screen widgets. 
	 */
	public String getOffScreenFeedbackTitle() {
		return offScreenFeedbackTitle;
	}

	/**
	 * Gets this widget's id. There is no setId() because the id of a widget
	 * cannot be changed after it is set in the constructor.
	 * 
	 * 
	 * @return This widget's id.
	 * @see org.purewidgets.shared.im.Widget#getWidgetId()
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

	// @Override
	// public void widgetVisibilityChanged() {
	// // Log.debug(this, "Widget visibility changed, transfering feedback.");
	// // /*
	// // * Transfer the feedback to the bottom panel
	// // */
	// // this.feedbackSequencer.stop();
	// // for ( InputFeedback inputfeedback : this.feedbackSequencer.getInput()
	// ) {
	// // sharedFeedbackSequencer.add(inputfeedback);
	// // }
	// // this.feedbackSequencer.clear();
	// }

	/**
	 * Called by the FeedbackSequencer to notify that a feedback display has
	 * ended.
	 * 
	 * @param feedback
	 *            The feedback that stopped showing.
	 */
	@Override
	public final void inputFeedbackEnded(InputFeedback<? extends PdWidget> feedback, boolean noMore) {

	}

	/**
	 * Notifies that an input feedback started being displayed.
	 * 
	 * @param feedback
	 *            The feedback info that was displayed.
	 */
	@Override
	public final void inputFeedbackStarted(InputFeedback<? extends PdWidget> feedback) {
		Log.debugFinest(this, "Input feedback started: " + feedback.toString());

		/*
		 * If feedback for an accepted input has started, trigger the
		 * application event.
		 */
		if (feedback.getType() == InputFeedback.Type.ACCEPTED) {
			this.fireActionEvent(feedback.getActionEvent()); // widget specific
																// object
		}
	}

	/**
	 * Checks if this PdWidget is currently visible. We cannot simply use the
	 * isVisible method from GWT because it simply checks the visibility DOM
	 * property of the element (it does not checks the element's ascendents).
	 * 
	 * @return true if the widget is visible; false otherwise.
	 */
	public boolean isDisplaying() {
		com.google.gwt.user.client.ui.Widget current = this;
		try { // we were getting javascript exceptions (Cannot read property
				// 'display' of undefined)
				// in development mode, hence the try-catch:

			/*
			 * If the widget is not on the DOM than it is not visible.
			 */
			if (!current.isAttached()) {
				return false;
			}

			/*
			 * If any ancestor is not visible than this widget is not visible
			 * either
			 */
			while (current != null) {
				if (!current.isVisible()) {
					return false;
				}
				current = current.getParent();
			}
		} catch (Exception e) {
			Log.warn(this, "Could not determine visibility status", e);
			return false;
		}
		return true;
	}

	/**
	 * Checks if the visual state of the widget indicates that it is active.
	 * 
	 * @return The state of the widget.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Checks if this widget is input enabled, i.e., if it accepts input.
	 * @return The status of the widget regarding input.
	 */
	public boolean isInputEnabled() {
		return this.inputEnabled;
	}

	/**
	 * Event triggered by the widget manager to signal input to this widget.
	 * 
	 * @param inputList Input events targeted to this widget.
	 *            
	 */
	@Override
	public final void onInput(ArrayList<WidgetInputEvent> inputList) {
		Log.debugFinest(this, "Received input event list ");

		for (WidgetInputEvent ie : inputList) {

			/*
			 * If the input is old, just trigger the application event
			 */
			if (ie.getAge() > INPUT_EVENT_OLD_AGE) {
				/*
				 * Ask the widget to validate the input and generate an
				 * appropriate feedback
				 */
				InputFeedback<? extends PdWidget> feedback = handleInput(ie);
				this.fireActionEvent(feedback.getActionEvent());

				/*
				 * If the input is recent, generate feedback for it
				 */
			} else {

				InputFeedback<? extends PdWidget> feedback = new InputFeedback<PdWidget>(this, ie,
						null, null);
				if (this.isInputEnabled()) {

					/*
					 * If this widget is enabled than ask it to handle the
					 * input, validating it
					 */
					feedback = handleInput(ie);
				} else {
					/*
					 * If the widget is not enabled than the input is not
					 * accepted.
					 */
					feedback.setType(InputFeedback.Type.NOT_ACCEPTED);
				}

				/*
				 * Schedule the feedback to appear. Widgets may return null as
				 * an indication that no feedback should be displayed
				 */
				if (null != feedback) {

					this.feedbackSequencer.add(feedback);

				}
			}
		}

	}

	/**
	 * Triggered by the widget manager to signal an update on this widget's reference codes.
	 * The new reference codes are set automatically on the underlying Widget by the widget manager.
	 *  
	 * Concrete widgets should override this method to update their Gui with the
	 * correct reference codes.
	 */
	@Override
	public void onReferenceCodesUpdated() {

		ArrayList<String> refCodes = new ArrayList<String>();
		for (WidgetOption widgetOption : this.widget.getWidgetOptions()) {
			refCodes.add(widgetOption.getReferenceCode());
		}
		this.getLocalStorage().saveList(REFERENCE_CODES_STORAGE_ID, refCodes);
	}

	/**
	 * Removes an ActionListener from this widget.
	 * 
	 * @param handler
	 *            The ActionListener to remove.
	 */
	public void removeActionListener(ActionListener handler) {
		this.actionListeners.remove(handler);
	}

	/**
	 * Removes this widget from the widget manager (and the interaction manager server).
	 * 
	 */
	public void removeFromServer() {
		Log.debugFinest(this, "Removing widget from widgetmanager: " + this);
		WidgetManager.get().removeWidget(this.widget);

		this.localStorage.removeItem(REFERENCE_CODES_STORAGE_ID);

//		for (Widget w : this.widget.getDependentWidget()) {
//			Log.debugFinest(this, "Removing dependent widgets from widgetmanager: " + w);
//			WidgetManager.get().removeWidget(w);
//		}

	}

	/**
	 * Sends this widget to the widget manager (and the interaction manager server).
	 */
	public final void sendToServer() {
		Log.debugFinest(this, "Adding widget to widgetmanager: " + this.getWidgetId());
		WidgetManager.get().addWidget(this.widget);

//		for (Widget w : this.widget.getDependentWidget()) {
//			Log.debugFinest(this, "Adding dependent widgets to widgetmanager: " + w.getWidgetId());
//			WidgetManager.get().addWidget(w);
//		}

	}

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
			this.removeStyleDependentName(PdWidget.DEPENDENT_STYLENAME_DISABLED_WIDGET);// .setStyleName(this.getStyleName());
		} else {
			this.getWidget().addStyleDependentName(PdWidget.DEPENDENT_STYLENAME_DISABLED_WIDGET); // .setStyleName(
																									// this.getStyleName()
																									// +
																									// this.DISABLED_STYLENAME_SUFFIX);
		}
	}

	/**
	 * Sets the feedback sequencer for this widget.
	 * 
	 * @param feedbackSequencer The feedback sequencer to set.
	 */
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
	 * @param inputEnabled if true, input events are processed; if false, input events are ignored.
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

	/**
	 * Sets the local storage for this widget.
	 * 
	 * @param localStorage
	 *            the local storage to set.
	 */
	public void setLocalStorage(LocalStorage localStorage) {
		this.localStorage = localStorage;
	}

	/**
	 * Sets the long description for this widget.
	 * 
	 * @param longDescription
	 *            the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.widget.setLongDescription(longDescription);
	}

	/**
	 * Sets the off-screen widget feedback info
	 * 
	 * @param offScreenFeedbackInfo
	 *            the off-screen widget feedback info to set
	 */
	public void setOffScreenFeedbackInfo(String offScreenFeedbackInfo) {
		this.offScreenFeedbackInfo = offScreenFeedbackInfo;
	}

	/**
	 * Sets the off-screen widget feedback title.
	 * 
	 * @param offScreenFeedbackTitle
	 *            the off-screen widget feedback title to set
	 */
	public void setOffScreenFeedbackTitle(String offScreenFeedbackTitle) {
		this.offScreenFeedbackTitle = offScreenFeedbackTitle;
	}

	/**
	 * Sets the on-screen widget feedback info.
	 * @param onScreenFeedbackInfo
	 *            the  on-screen widget feedback info to set
	 */
	public void setOnScreenFeedbackInfo(String onScreenFeedbackInfo) {
		this.onScreenFeedbackInfo = onScreenFeedbackInfo;
	}

	/**
	 * Sets the on-screen widget feedback title.
	 * @param onScreenFeedbackTitle the on-screen widget feedback title to set
	 */
	public void setOnScreenFeedbackTitle(String onScreenFeedbackTitle) {
		this.onScreenFeedbackTitle = onScreenFeedbackTitle;
	}

	/**
	 * Sets the short description for this widget.
	 * 
	 * @param shortDescription
	 *            the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.widget.setShortDescription(shortDescription);
	}

	/**
	 * Sets the widget associated with this GuiWidget.
	 * 
	 * @param widget
	 */
	public void setWidget(Widget widget) {
		// Log.debug("AbstractGuiWidget " + this.toDebugString());
		this.widget = widget;
		this.widget.setInputListener(this);
		this.widget.setReferenceCodeListener(this);

		Log.debug(this, "Loading reference codes from local storage.");
		ArrayList<String> refCodes = this.getLocalStorage().loadList(REFERENCE_CODES_STORAGE_ID);
		Log.debug(this, "Reference codes: " + refCodes.toString());
		if (null != refCodes) {
			int i = 0;
			for (WidgetOption widgetOption : this.widget.getWidgetOptions()) {
				if (refCodes.size() > i) {
					widgetOption.setReferenceCode(refCodes.get(i++));
				}
			}
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
	protected void fireActionEvent(ActionEvent<? extends PdWidget> ae) {
		if (null == ae) {
			return;
		}

		// Log.debug(this, "Firing " + ae.toDebugString());
		for (ActionListener al : this.actionListeners) {
			// Log.debug(this, "   on " + al.toString());
			al.onAction(ae);
		}

	}

	protected void generateUserInputFeedbackMessage(WidgetInputEvent inputEvent,
			InputFeedback<?> inputFeedback) {

		inputFeedback.setOnScreenWidgetFeedbackTitle(replaceParameters(this.onScreenFeedbackTitle, inputEvent));
		inputFeedback.setOnScreenWidgetFeedbackInfo(replaceParameters(this.onScreenFeedbackInfo, inputEvent));
		inputFeedback.setOffScreenWidgetFeedbackTitle(replaceParameters(
				this.offScreenFeedbackTitle, inputEvent));
		inputFeedback.setOffScreenWidgetFeedbackInfo(replaceParameters(
				this.offScreenFeedbackInfo, inputEvent));
	}

	protected InputFeedback<? extends PdWidget> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdWidget> feedback = new InputFeedback<PdWidget>(this, ie, null, null);
		feedback.setType(InputFeedback.Type.ACCEPTED);

		ActionEvent<PdWidget> ae = new ActionEvent<PdWidget>(ie, this, null);
		feedback.setActionEvent(ae);
		this.generateUserInputFeedbackMessage(ie, feedback);
		return feedback;
	}

	private String getAgeString(long milli) {
		float ageSeconds = milli / 1000;

		float ageMinutes = ageSeconds / 60;
		float ageHours = ageMinutes / 60;
		float ageDays = ageHours / 24;
		float ageWeeks = ageDays / 7;

		if (ageWeeks > 1) {
			return messages.ageWeek((int) ageWeeks);
		} else if (ageDays > 1) {
			return messages.ageDay((int) ageDays);
		} else if (ageHours > 1) {
			return messages.ageHour((int) ageHours);
		} else if (ageMinutes > 1) {
			return messages.ageMinute((int) ageMinutes);
		} else {
			return messages.ageSecond((int) ageSeconds);
		}

	}

	private String noNull(String s) {
		if (null == s) {
			return "";
		}
		return s;
	}

	private String noNull(String s, int nChars) {
		s = noNull(s);
		if (s.length() > nChars) {
			s = s.substring(0, nChars) + "...";
		}
		return s;
	}

	private String replaceParameter(String inputString, String parameter, String replacement) {
		Log.debugFinest(this, "Replacing " + parameter + " in " + inputString + " with "
				+ replacement);
		String parameterPattern = parameter + "\\(([\\d]+)\\)";

		RegExp reg = RegExp.compile(parameterPattern);
		Log.debugFinest(this, "RegExp: " + reg);
		MatchResult matcher = reg.exec(inputString);
		Log.debugFinest(this, "Matcher: " + matcher);

		if (null != matcher && matcher.getGroupCount() > 1) {
			int nChars = Integer.parseInt(matcher.getGroup(1));

			inputString = reg.replace(inputString, noNull(replacement, nChars));
		} else {
			inputString = inputString.replaceAll(parameter, noNull(replacement));
		}
		return inputString;
	}

	private String replaceParameters(String inputString, WidgetInputEvent inputEvent) {
		/*
		 * %U% - user nickname %P[i]% - Input parameter i %WS% - widget short
		 * description %WL% - widget long description %WOS% - widget option
		 * short description %WOL% - widget option long description %WOR% -
		 * widget option reference code
		 */

		// nickname
		inputString = replaceParameter(inputString, MessagePattern.PATTERN_USER_NICKNAME,
				inputEvent.getNickname());

		// widget short description
		inputString = replaceParameter(inputString,
				MessagePattern.PATTERN_WIDGET_SHORT_DESCRIPTION, this.getShortDescription());

		// widget long description
		inputString = replaceParameter(inputString, MessagePattern.PATTERN_WIDGET_LONG_DESCRIPTION,
				this.getLongDescription());

		// widget option short description
		inputString = replaceParameter(inputString,
				MessagePattern.PATTERN_WIDGET_OPTION_SHORT_DESCRIPTION, inputEvent
						.getWidgetOption().getShortDescription());

		// widget option long description
		inputString = replaceParameter(inputString,
				MessagePattern.PATTERN_WIDGET_OPTION_LONG_DESCRIPTION, inputEvent.getWidgetOption()
						.getLongDescription());

		// widget option reference code
		inputString = replaceParameter(inputString,
				MessagePattern.PATTERN_WIDGET_OPTION_REFERENCE_CODE, inputEvent.getWidgetOption()
						.getReferenceCode());

		// age
		String ageString = getAgeString(inputEvent.getAge());
		if (inputEvent.getAge() < 1000 * 60 * 1) {
			ageString = "";
		}
		inputString = replaceParameter(inputString, MessagePattern.PATTERN_INPUT_AGE, ageString);

		Log.debugFinest(this, "Replacing widget parameters: " + inputString);
		if (null != inputEvent.getParameters() && inputEvent.getParameters().size() > 0) {
			for (int i = 0; i < inputEvent.getParameters().size(); i++) {

				inputString = replaceParameter(inputString,
						MessagePattern.getInputParameterPattern(i),
						inputEvent.getParameters().get(i));
			}

		}
		return inputString;
	}

}
