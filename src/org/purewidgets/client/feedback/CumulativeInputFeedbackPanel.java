/**
 * 
 */
package org.purewidgets.client.feedback;


import java.util.ArrayList;
import java.util.List;

import org.purewidgets.client.widgets.PdWidget;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * The CumulativeInputFeedbackPanel displays input feedback to the user, accumulating various feedback
 * messages if they occur within a limited amount of time.
 * The panel can be configured with a maximum number of simultaneous feedback
 * lines to display. 
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwCumulativeInputFeedbackPanel</dt>
 * <dd>the outer element</dd>
 * <dt>.pwCumulativeInputFeedbackPanel .title</dt>
 * <dd> The Title of the panel (displayed for feedback for not visible widgets)</dd>
 * <dt>.pwCumulativeInputFeedbackPanel .accepted</dt>
 * <dd>the accepted input feedback element</dd>
 * <dt>.pwCumulativeInputFeedbackPanel .notaccepted</dt>
 * <dd>the not accepted input feedback element</dd>
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class CumulativeInputFeedbackPanel extends AbstractInputFeedbackPanel {
	/**
	 * The default style name of the outer element of this input feedback panel.
	 */
	public final String DEFAULT_STYLENAME = "pwCumulativeInputFeedbackPanel";
	

	/**
	 * The style name applied the title element.
	 */
	public final String TITLE_STYLENAME_SUFFIX = "title";
	
	/**
	 * The style name applied to accepted input feedback elements
	 */
	public final String ACCEPTED_INPUT_STYLENAME_SUFFIX = "accepted";

	
	/**
	 * The style name applied to not accepted input feedback elements.
	 */
	public final String NOTACCEPTED_INPUT_STYLENAME_SUFFIX = "notaccepted";

	
	/**
	 * The default value (5) for the maximum number of simultaneous feedback lines
	 * to display.
	 */
	public static final int DEFAULT_MAX_LINES = 5;

	
	private VerticalPanel mainPanel;
	
	/**
	 * The panel used to lay out the input feedback text lines.
	 */
	private VerticalPanel vPanel;
	
	
	private boolean showTitles = false;

	
	/**
	 * The maximum number of simultaneous feedback lines.
	 */
	private int maxLines;

	
	/**
	 * Indicates whether new items appear on the top of the panel, or on the
	 * bottom.
	 */
	private boolean newOnTop;
	
	


	/**
	 * This panel saves the previous feedback (during a pre-defined period of time)
	 * and saves it so that it is displayed with the new feedback.
	 */
	private List<InputFeedback<? extends PdWidget>> accumulatedFeedback;
	
	private Timer timer;
	
	/**
	 * Creates a new CumulativeInputFeedbackPanel for the specified PdWidget with default values for the
	 * maximum number of display lines.
	 * 
	 * @param widget The PdWidget that is the target of the input that this panel is giving feedback about.
	 */
	public CumulativeInputFeedbackPanel(PdWidget widget) {
		this(widget, DEFAULT_MAX_LINES);
	}


	/**
	 * Creates a new CumulativeInputFeedbackPanel for the specified PDWidget, with the given number
	 * of maximum lines.
	 * 
	 * @param maxLines
	 *            The maximum number of simultaneous feedback lines.
	 * @param widget 
	 * 				The PdWidget that is the target of the input that this panel is giving feedback about.
	 */
	public CumulativeInputFeedbackPanel(PdWidget widget, int maxLines) {
		
		// no automatic hiding. This input info panel has its own logic
		super(widget);
		
		/*
		 * By default, new feedback is shown on the bottom.
		 */
		this.newOnTop = false;
		
		
		//this.setDelay(delay);
		this.maxLines = maxLines;
		
		this.accumulatedFeedback = new ArrayList<InputFeedback<? extends PdWidget>>();
		
		this.mainPanel = new VerticalPanel();
		this.setWidget(mainPanel);
		
		
		// infoLabel = new Label();
		vPanel = new VerticalPanel();
		mainPanel.add(vPanel);
		
		//this.setBackground(new ImagePanel("/instantplaces/icons/bubble.gif"));
		super.setStyleName(DEFAULT_STYLENAME);
		//this.getBackground().setClassName(BACKGROUND_DEFAULT_STYLENAME);
		
		timer = new Timer() {
			@Override
			public void run() {
				CumulativeInputFeedbackPanel.this.hide(null, true);
			}
		};
	}
	

	/**
	 * Shows input feedback for the specified duration.
	 * 
	 * @param inputFeedback The information about the feedback to display.
	 * @param duration The amount of time, in milliseconds, that the feedback should be visible.
	 */
	@Override
	public void show(InputFeedback<? extends PdWidget> inputFeedback, int duration) {
		timer.schedule(duration);
		
		if ( null != inputFeedback ) {
			Log.debug(this, "Showing:" + inputFeedback.toString());
		} else {
			Log.warn(this, "Null feedback!");
			return;
		}
		
		if ( this.newOnTop ) {
			this.accumulatedFeedback.add(0, inputFeedback);
			if (this.accumulatedFeedback.size() > this.maxLines) {
				this.accumulatedFeedback.remove(this.accumulatedFeedback.size()-1);
			}
		} else {
			this.accumulatedFeedback.add(inputFeedback);
			
			if (this.accumulatedFeedback.size() > this.maxLines) {
				this.accumulatedFeedback.remove(0);
			}
		}
		
		
		
		vPanel.clear();
		
		for (InputFeedback<?> feedback : this.accumulatedFeedback) {
			
				if ( this.showTitles ) {
					Label title = new Label(feedback.getSharedFeedbackTitle() );//feed.getWidget().getShortDescription());
					title.setStyleName(TITLE_STYLENAME_SUFFIX);
					vPanel.add(title);
					
					HTML l = new HTML( feedback.getSharedFeedbackInfo()); //message.toString() );
					switch( feedback.getType() ) {
					case ACCEPTED:
						l.setStyleName(ACCEPTED_INPUT_STYLENAME_SUFFIX);
						break;
					case NOT_ACCEPTED:
						l.setStyleName(NOTACCEPTED_INPUT_STYLENAME_SUFFIX);
						break;
					}
					
					
					vPanel.add(l);					
				} else {
					
					HTML l = new HTML(feedback.getInfo() );
					
					switch( feedback.getType() ) {
					case ACCEPTED:
						l.setStyleName(ACCEPTED_INPUT_STYLENAME_SUFFIX);
						break;
					case NOT_ACCEPTED:
						l.setStyleName(NOTACCEPTED_INPUT_STYLENAME_SUFFIX);
						break;
					}
					
					
					vPanel.add(l);
				}
		
		}
		

		this.displayPanel();
	}

	/**
	 * Hides the feedback. The panel is only actually hidden if there no more feedback to 
	 * display, or if this panel only shows one line at a time.
	 *   
	 */
	@Override
	public void hide(InputFeedback<? extends PdWidget> feedback, boolean noMore) {
		if ( null != feedback ) {
			Log.debug("Stopped showing:" + feedback.toString() + ". More feedback: "+noMore);
		} else {
			Log.debug("Stopped showing feedback. More feedback: "+noMore);
		}
		
		/*
		 * In the case where the panel only displays one line at a time
		 * we want to temporarily hide the panel between lines. 
		 * 
		 * (The panel is also hidden if there is no more feedback waiting to be
		 * displayed)
		 */
		if (noMore || this.maxLines == 1) {
			if ( this.isShowing() ) {
				this.hide();
			}
			this.accumulatedFeedback.clear();
		}
	}


	
	/**
	 *  Displays the feedback panel. If the panel is already showing, this method will recalculate its position
	 *  and position it accordingly.
	 *  
	 */
	private void displayPanel() {
		if (!this.isShowing()) {
			//if ( this.widget.isDisplaying() ) {
				
				//this.title.setVisible(true);
				super.show();
				this.alignPanel();
			/*} else {
				this.title.setText( widget.getShortDescription() );
				this.title.setVisible(true);
				super.show();
				
				this.setPopupPosition(Window.getClientWidth()/2-this.getOffsetWidth()/2, Window.getClientHeight()-this.getOffsetHeight());
			}
			*/
		} else {
			/*
			 * The contents may make the popup larger, so we need to re-align it
			 */
			this.alignPanel();
		}
	}

	/**
	 * Sets new input feedback on top or on the bottom of the panel.
	 * 
	 * @param newOnTop if true, sets new feedback on top of the panel; if false, sets new feedback on the bottom of the panel.
	 */
	public void setNewOnTop(boolean newOnTop) {
		this.newOnTop = newOnTop;
	}

	/**
	 * Tests whether new feedback is displayed on top or on the bottom of the panel.
	 * @return <code>true</code> if the new feedback is displayed on top; <code>false</code> otherwise.
	 */
	public boolean isNewOnTop() {
		return newOnTop;
	}

	/**
	 * Tests if this panel shows feedback using only one line per feedback message, or if it uses a title line and
	 * a description lines for the feedback.
	 * 
	 * @return true if the panel shows titles and description lines; false if it shows only the description.
	 */
	public boolean isShowTitles() {
		return showTitles;
	}

	/**
	 * Sets whether this panel should show titles and descriptions of just descriptions for the feedback.
	 * 
	 * @param showTitles if true, this panel will show title and descriptions; if false it will show only descriptions.
	 */
	public void setShowTitles(boolean showTitles) {
		this.showTitles = showTitles;
	}
	
}
