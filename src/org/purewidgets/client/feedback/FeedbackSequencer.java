/**
 * 
 */
package org.purewidgets.client.feedback;

import java.util.ArrayList;
import java.util.List;

import org.purewidgets.client.widgets.PdWidget;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * 
 * The FeedbackSequencer sequences the showing and hiding of the input feedback panel 
 * for the widget. When feedback for a widget is showing, the FeedbackSequencer periodically
 * checks if the widget is still visible. If it is not, the feedback panel is animated to the 
 * bottom of the screen, by default (the positioning can be changed). The bottom panel
 * is used to display feedback about all non-visible widgets, i.e., it is shared by all widgets.
 * 
 * 
 * The sequence has the following states:
 * <pre>
 *                              <-       <-        <-       <-         <-       <-
 *                           |                                                     |
 *                           \/                                                    |
 * STOPPED     ->        SHOWING(delay) -> [More feedback to display?](yes) -> INTERVAL(delay)     
 *   /\                                                (no)
 *   |                                                  | 
 *   |                                                  \/
 *     <-      <-        <-        <-       <-     <-  FINAL(delay)
 * </pre>
 * <ul>
 * <li>                                                      
 * STOPPED (not showing any input feedback and there is no feedback to show)
 * </li>
 * <li>
 * SHOWING (the panel is showing feedback)
 * </li>
 * <li>
 * INTERVAL (the panel is temporarily hidden, if there is more feedback to show)
 * </li>
 * <li> 
 * FINAL (the panel stays visible, if there is no more feedback to show)
 * </li>
 * </ul>
 *  
 * 
 * @author Jorge C. S. Cardoso
 */
public class FeedbackSequencer {

	/**
	 * The PositionAnimation class animates a popuppanel between its current position to a target position.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	class PositionAnimation extends Animation {
		
		PopupPanel panel;
		int startX, startY;
		int targetX, targetY;
		PositionAnimation(PopupPanel panel, int targetX, int targetY) {
			super();
			startX = panel.getAbsoluteLeft();
			startY = panel.getAbsoluteTop();
			this.targetX = targetX;
			this.targetY = targetY;
			this.panel = panel;
		}

		@Override
		protected void onUpdate(double progress) {
			
			panel.setPopupPosition( (int)(startX+(targetX-startX)*progress), (int)(startY+(targetY-startY)*progress) );
		}
	}

	
	/**
	 * The states of the sequencer.
	 */
	protected enum STATE {FINAL, INTERVAL, SHOWING, STOPPED}
	
	
	/**
	 * The duration of the panel animation
	 */
	public static final int ANIMATION_DURATION = 1500;
	
	/**
	 * Multiple input feedback will be shown with intervals of
	 * DEFAULT_FEEDBACK_DELAY milliseconds.
	 */
	public static final int DEFAULT_FEEDBACK_DELAY = 100;
	
	
	/**
	 * The internal input feedback will be shown for DEFAULT_FEEDBACK_DURATION
	 * milliseconds.
	 */
	public static final int DEFAULT_FEEDBACK_DURATION = 4000;
	
	/**
	 * If there is no more feedback to show, the panel will stay
	 * visible during DEFAULT_FINAL_DELAY milliseconds, be default.
	 */
	public static final int DEFAULT_FINAL_DELAY = 10000;
	
	/**
	 * The interval between checks of the visibility of the widget
	 */
	public static final int VISIBILITY_CHECK_DELAY = 300;
	
	
	/** 
	 * The panel that is used to show feedback for widgets that are not visible.
	 */
	private static FeedbackDisplay sharedFeedbackDisplay; 
	
	
	
	/**
	 * the current state.
	 */
	protected STATE state;
	
	/**
	 * The timer used to implement the sequencer.
	 */
	protected Timer timer;
	
	/**
	 * The timer used to periodically check if the widget is still visible.
	 */
	protected Timer timerCheckVisibility;
	
	/**
	 * The current input feedback being processed
	 */
	private InputFeedback<? extends PdWidget> current = null;
	
	/**
	 * The display panel for the input feedback.
	 */
	private FeedbackDisplay display;
	
	/**
	 * The duration of each feedback on the display 
	 */
	private int feedbackDuration;
	
	/**
	 * The amount of time the panel will stay visible after the last feedback is shown.
	 */
	private int feedbackFinalDelay;
	
	
	/**
	 * The interval between multiple feedbacks
	 */
	private int feedbackIntervalDelay;;
	
	/**
	 * The list of input feedback to display.
	 */
	private List<InputFeedback<? extends PdWidget>> input;
	
	
	/**
	 * The listener for events regarding the input feedback.
	 */
	private InputFeedbackListener listener;
	
	
	public  FeedbackSequencer (CumulativeInputFeedbackPanel display, InputFeedbackListener listener) {
		this(display, listener, DEFAULT_FEEDBACK_DURATION, DEFAULT_FEEDBACK_DELAY, DEFAULT_FINAL_DELAY);
	}
	
	public FeedbackSequencer (CumulativeInputFeedbackPanel display, InputFeedbackListener listener, 
			int feedbackDuration, 
			int feedbackIntervalDelay,
			int feedbackFinalDelay) {
		
		
		this.display = display;
		this.listener = listener;
		this.feedbackDuration = feedbackDuration;
		this.feedbackIntervalDelay = feedbackIntervalDelay;
		this.feedbackFinalDelay = feedbackFinalDelay;
		this.state = STATE.STOPPED;
		
		input = new ArrayList<InputFeedback<? extends PdWidget>>();
		timer = new Timer() {
			@Override
			public void run() {
				FeedbackSequencer.this.timerElapsed();				
			}
		};
		
		timerCheckVisibility = new Timer() {
			@Override
			public void run() {
				FeedbackSequencer.this.timerCheckVisibilityElapsed();				
			}
		};
		if ( null == sharedFeedbackDisplay ) {
			sharedFeedbackDisplay = new CumulativeInputFeedbackPanel(null);
			sharedFeedbackDisplay.setWidgetReferencePoint(Align.BOTTOM);
			sharedFeedbackDisplay.setAlignDisplacementY(0);
			((CumulativeInputFeedbackPanel)sharedFeedbackDisplay).setShowTitles(true);
		}
		
	}

	/**
	 * @return the sharedFeedbackDisplay
	 */
	public static FeedbackDisplay getSharedFeedbackDisplay() {
		return sharedFeedbackDisplay;
	}
	
	
	/**
	 * @param sharedFeedbackDisplay the sharedFeedbackDisplay to set
	 */
	public static void setSharedFeedbackDisplay(FeedbackDisplay sharedFeedbackDisplay) {
		FeedbackSequencer.sharedFeedbackDisplay = sharedFeedbackDisplay;
	}

	 
	/**
	 * Adds an InputFeedback to be displayed by this sequencer.
	 * 
	 * @param feedback The InputFeedback to sequence.
	 */
	public void add(InputFeedback<? extends PdWidget> feedback) {
		
		Log.debug(this,  "Added input feedback to list: " + feedback.toString());
		
		this.input.add(feedback);
		start();
	}
	
	public int getFeedbackDuration() {
		return feedbackDuration;
	}
	
	public int getFeedbackFinalDelay() {
		return feedbackFinalDelay;
	}
	
	
	public int getFeedbackIntervalDelay() {
		return feedbackIntervalDelay;
	}

	
	/**
	 * 
	 * @return True if this sequencer is currently displaying feedback, False otherwise.
	 */
	public boolean isShowing() {
		
		return state == STATE.SHOWING;
		
	}


	public void setFeedbackDuration(int feedbackDuration) {
		this.feedbackDuration = feedbackDuration;
	}


	public void setFeedbackFinalDelay(int feedbackFinalDelay) {
		this.feedbackFinalDelay = feedbackFinalDelay;
	}


	public void setFeedbackIntervalDelay(int feedbackIntervalDelay) {
		this.feedbackIntervalDelay = feedbackIntervalDelay;
	}


	/**
	 * Starts the sequencing of feedback display, if necessary.
	 * If the sequencer is in the STOPPED or FINAL states (in which case the
	 * timer will not be triggered again), this method
	 * cancels the timer and starts the sequencing again.
	 */
	private void start() {
		
		if ( STATE.STOPPED == state || STATE.FINAL == state ) {
			timer.cancel();
			state = STATE.SHOWING;
			startShowingFeedback();
			timer.schedule( this.getFeedbackDuration() );
			Log.debug(this, "Scheduling timer " + this.getFeedbackDuration());
		}
		if ( null != this.display.getWidget() && this.display.getWidget().isDisplaying() ) {
			timerCheckVisibility.schedule(300);
		}
	}


	/**
	 * This methods starts showing the current feedback by showing its display.
	 */
	private void startShowingFeedback() {
		Log.debug(this, "Starting to show feedback.");
		
		current = this.input.remove(0);
		
		/*
		 * The feedback is showed in a widget dedicated panel next to the widget itself, by default,
		 * unless the widget is not visible, in which case the feedback is displayed in a panel
		 * that is shared with other widgets.
		 */
		if ( null != this.display.getWidget() && this.display.getWidget().isDisplaying() ) {
			this.display.show(current, DEFAULT_FINAL_DELAY);
		} else {
			sharedFeedbackDisplay.show(current, DEFAULT_FINAL_DELAY);
		}
		
		/*
		 * Trigger the input feedback started event on the widget.
		 */
		if ( null != this.listener ) {
			this.listener.inputFeedbackStarted(current);
		}
		
	}
	
	/**
	 * This method stops showing the current feedback.
	 */
	private void stopShowingFeedback() {
		Log.debug(this, "Stopping feedback.");
		
		/*
		 * If there is no feedback being displayed, we don't need to check if the widget is visible...
		 */
		timerCheckVisibility.cancel();
			
		/*
		 * Trigger the input feedback event on the widget. 
		 */
		if ( null != this.listener )  {
			this.listener.inputFeedbackEnded( current, this.input.size() == 0 );
		}
		current = null;
		
	}


	/**
	 * Checks if the widget to which this FeedbackSequencer is assigned is still visible.
	 */
	private void timerCheckVisibilityElapsed() {
		Log.debug(this, "Checking visibility of widget '" + this.display.getWidget().getWidgetId() +"'");
		
		if ( null != this.display.getWidget() && this.display.getWidget().isDisplaying() ) {
			
			this.timerCheckVisibility.schedule(VISIBILITY_CHECK_DELAY);
			
		} else {
			
			Log.debug(this, "Widget '" + this.display.getWidget().getWidgetId() +"' not visible.");
			//TODO: we should allow programmers to choose where the shared panel is shown
			PositionAnimation animate = new PositionAnimation((PopupPanel)this.display.getFeedbackDisplayWidget(), 
					Window.getClientWidth()/2-this.display.getFeedbackDisplayWidget().getOffsetWidth()/2, 
					Window.getClientHeight()-this.display.getFeedbackDisplayWidget().getOffsetHeight());
			
			animate.run(ANIMATION_DURATION);
			
			/*
			 * After the animation finishes, show the shared panel and hide the individual one.
			 */
			Timer t = new Timer() {
				@Override
				public void run() {
					sharedFeedbackDisplay.show(FeedbackSequencer.this.current, DEFAULT_FINAL_DELAY+DEFAULT_FEEDBACK_DURATION);
					FeedbackSequencer.this.display.hide();
				}
			};
			t.schedule(ANIMATION_DURATION);
			
		}
		
	}

	/**
	 * The <code>timerElapsed()</code> implements the transitioning between the sequencer
	 * states. This method is called when the timer elapses.
	 *
	 */
	private void timerElapsed() {
		
		Log.debug(this, "Current state: " +state);
		
		if ( STATE.SHOWING == state ) {
			
			if ( this.input.size() > 0 ) { // there is more input, use delay between inputs
				
				state = STATE.INTERVAL;
				Log.debug(this, "New state: " +state);
				
				this.stopShowingFeedback();
				timer.schedule(this.feedbackIntervalDelay);
				Log.debug(this, "Scheduling timer: " + this.feedbackIntervalDelay);
				
			} else {
				
				state = STATE.FINAL;
				Log.debug(this, "New state: " +state);
				
				timer.schedule(this.feedbackFinalDelay);
				Log.debug(this, "Scheduling timer: " + this.feedbackFinalDelay);	
				
			}
			
		} else if ( STATE.INTERVAL == state ){
			
			state = STATE.SHOWING;
			Log.debug(this, "New state: " +state);
			
			this.startShowingFeedback();
			timer.schedule(this.feedbackDuration);
			Log.debug(this, "Scheduling timer: " + this.feedbackDuration);
			
		} else if ( STATE.FINAL == state ) {
			
			state = STATE.STOPPED;
			Log.debug(this, "New state: " +state);
			
			this.stopShowingFeedback();
			
		}
		
	}
}
