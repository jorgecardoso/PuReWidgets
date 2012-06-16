/**
 * 
 */
package org.purewidgets.client.widgets.feedback;

import java.util.ArrayList;
import java.util.List;

import org.purewidgets.client.widgets.Align;
import org.purewidgets.client.widgets.GuiWidget;
import org.purewidgets.client.widgets.events.InputFeedbackListener;
import org.purewidgets.shared.Log;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * 
 * The FeedbackSequencer sequences the showing and hiding of the input feedback panel 
 * for the widget. 
 * 
 * The sequence has the following states:
 *    
 *                              <-       <-        <-       <-         <-       <-
 *                           |                                                     |
 *                           \/                                                    |
 * STOPPED     ->        SHOWING(delay) -> [More feedback to display?](yes) -> INTERVAL(delay)     
 *   /\                                                (no)
 *   |                                                  | 
 *   |                                                  \/
 *     <-      <-        <-        <-       <-     <-  FINAL(delay)
 *                                                        
 * STOPPED (not showing any input feedback and there is no feedback to show)
 * -> (when feedback is received)
 * SHOWING (the panel is showing feedback)
 * -> (after the duration period passed)
 * INTERVAL (the panel is temporarily hidden, if there is more feedback to show) | FINAL (the panel stays visible, if there is no more feedback to show)
 * 
 *  
 * 
 * @author Jorge C. S. Cardoso
 */
public class FeedbackSequencer {

	/**
	 * The internal input feedback will be shown for DEFAULT_FEEDBACK_DURATION
	 * milliseconds.
	 */
	public static final int DEFAULT_FEEDBACK_DURATION = 4000;

	
	/**
	 * Multiple input feedback will be shown with intervals of
	 * DEFAULT_FEEDBACK_DELAY milliseconds.
	 */
	public static final int DEFAULT_FEEDBACK_DELAY = 100;
	
	
	/**
	 * If there is no more feedback to show, the panel will stay
	 * visible during DEFAULT_FINAL_DELAY milliseconds, be default.
	 */
	public static final int DEFAULT_FINAL_DELAY = 10000;
	
	
	/**
	 * The listener for events regarding the input feedback.
	 */
	private InputFeedbackListener listener;
	
	/**
	 * The display panel for the input feedback.
	 */
	private CumulativeInputFeedbackPanel display;
	
	
	
	protected static CumulativeInputFeedbackPanel sharedFeedbackDisplay; 
	protected static FeedbackSequencer lastToShare = null;
	
	
	/**
	 * The duration of each feedback on the display 
	 */
	private int feedbackDuration;
	
	/**
	 * The interval between multiple feedbacks
	 */
	private int feedbackIntervalDelay;
	
	/**
	 * The amount of time the panel will stay visible after the last feedback is shown.
	 */
	private int feedbackFinalDelay;
	
	/**
	 * The timer used to implement the sequencer.
	 */
	protected Timer timer;
	
	/**
	 * The timer used to periodically check if the widget is still visible.
	 */
	protected Timer timerCheckVisibility;
	
	/**
	 * The list of input feedback to display.
	 */
	private List<InputFeedback<? extends GuiWidget>> input;
	
	/**
	 * The current input feedback being processed
	 */
	private InputFeedback<? extends GuiWidget> current = null;
	
	
	/**
	 * The states of the sequencer.
	 */
	protected enum STATE {STOPPED, SHOWING, INTERVAL, FINAL};
	
	/**
	 * the current state.
	 */
	protected STATE state;
	
	
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
		
		input = new ArrayList<InputFeedback<? extends GuiWidget>>();
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
			sharedFeedbackDisplay.setShowTitles(true);
		}
		
	}
	
	protected void timerCheckVisibilityElapsed() {
		Log.debug(this, "Checking visibility of widget '" + this.display.getWidget().getWidgetId() +"'");
		if ( null != this.display.getWidget() && this.display.getWidget().isDisplaying() ) {
			this.timerCheckVisibility.schedule(200);
		} else {
			Log.debug(this, "Widget '" + this.display.getWidget().getWidgetId() +"' not visible.");
			PositionAnimation animate = new PositionAnimation(this.display, 
					Window.getClientWidth()/2-this.display.getOffsetWidth()/2, 
					Window.getClientHeight()-this.display.getOffsetHeight());
			animate.run(1500);
			Timer t = new Timer() {
				@Override
				public void run() {
					sharedFeedbackDisplay.show(FeedbackSequencer.this.current, DEFAULT_FINAL_DELAY+DEFAULT_FEEDBACK_DURATION);
					FeedbackSequencer.this.display.hide();
					lastToShare = FeedbackSequencer.this;
				}
			};
			t.schedule(1500);
			
			
		}
		
	}


	public boolean isShowing() {
		
		return state == STATE.SHOWING;
		
	}
	
	
	
	public void add(InputFeedback<? extends GuiWidget> feedback) {
		
		Log.debug(this,  "Added input feedback to list: " + feedback.toString());
		this.input.add(feedback);
		start();
		
	}
	
	public void stop() {
		if ( null != this.timer ) {
			this.timer.cancel();
		}
	}
	
	public void clear() {
		if ( null != this.input ) {
			this.input.clear();
		}
		
	}
	
	
	/////////// Implementation of the sequencer
	 
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
	
	/**
	 * This method stops showing the current feedback by hiding its display.
	 */
	private void stopShowingFeedback() {
		timerCheckVisibility.cancel();
		
		Log.debug(this, "Stopping feedback.");
	//	this.display.hide( current, this.input.size() == 0 );
		

		
		/*
		 * Trigger the input feedback event on the widget. 
		 */
		if ( null != this.listener )  {
			this.listener.inputFeedbackEnded( current, this.input.size() == 0 );
		}
		current = null;
		
	}
	
	
	/**
	 * This methods starts showing the current feedback by showing its display.
	 */
	private void startShowingFeedback() {
		
		Log.debug(this, "Starting to show feedback.");
		current = this.input.remove(0);
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
	

	
	//////// Getters / Setters
	
	public void setFeedbackDuration(int feedbackDuration) {
		this.feedbackDuration = feedbackDuration;
	}

	
	public int getFeedbackDuration() {
		return feedbackDuration;
	}


	public void setFeedbackIntervalDelay(int feedbackIntervalDelay) {
		this.feedbackIntervalDelay = feedbackIntervalDelay;
	}


	public int getFeedbackIntervalDelay() {
		return feedbackIntervalDelay;
	}


	public void setFeedbackFinalDelay(int feedbackFinalDelay) {
		this.feedbackFinalDelay = feedbackFinalDelay;
	}


	public int getFeedbackFinalDelay() {
		return feedbackFinalDelay;
	}


	/**
	 * @return the input
	 */
	public List<InputFeedback<? extends GuiWidget>> getInput() {
		return input;
	}


	/**
	 * @param input the input to set
	 */
	public void setInput(List<InputFeedback<? extends GuiWidget>> input) {
		this.input = input;
	}
	
	class PositionAnimation extends Animation {
		
		int startX, startY;
		int targetX, targetY;
		PopupPanel panel;
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
}
