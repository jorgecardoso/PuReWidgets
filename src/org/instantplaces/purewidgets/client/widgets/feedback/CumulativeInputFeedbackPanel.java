/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets.feedback;



import java.util.ArrayList;
import java.util.List;

import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.shared.Log;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TODO: Add animation (fade in or something) to new items
 * 
 * The SequentialInputFeedbackPanel displays input feedback to the user.
 * The panel can be configured with a maximum number of simultaneous feedback
 * lines to display. The panel will display up to Maximum Lines at a time. 
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-CumulativeInputFeedbackPanel</dt>
 * <dd>the outer element</dd>
 * <dt>.instantplaces-CumulativeInputFeedbackPanel .content</dt>
 * <dd>the inner element</dd>
 * <dt>.instantplaces-CumulativeInputFeedbackPanel-background</dt>
 * <dd>the background element</dd>
 * <dt>.instantplaces-CumulativeInputFeedbackPanel .accepted</dt>
 * <dd>the accepted input feedback element</dd>
 * <dt>.instantplaces-SequentialInputFeedbackPanel .notaccepted</dt>
 * <dd>the not accepted input feedback element</dd>
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class CumulativeInputFeedbackPanel extends AbstractInputFeedbackPanel {
	/**
	 * The default style name of the outer element of this input feedback panel.
	 */
	public final String DEFAULT_STYLENAME = "instantplaces-CumulativeInputFeedbackPanel";
	
	/**
	 * The default style name of the ackground element of this input feedback panel.
	 */
	public final String BACKGROUND_DEFAULT_STYLENAME = "instantplaces-CumulativeInputFeedbackPanel-background";

	
	/**
	 * The style name for accepted input feedback lines.
	 */
	public final String ACCEPTED_INPUT_STYLENAME_SUFFIX = "accepted";

	
	/**
	 * The style name for not accepted input feedback lines.
	 */
	public final String NOTACCEPTED_INPUT_STYLENAME_SUFFIX = "notaccepted";

	
	/**
	 * The style name for system info input feedback lines.
	 */
	public final String SYSTEMINFO_INPUT_STYLENAME_SUFFIX = "systeminfo";



	
	
	/**
	 * The default value for the maximum number of simultaneous feedback lines
	 * to display.
	 */
	public static final int DEFAULT_MAX_LINES = 5;

	
	/**
	 * The panel used to lay out the input feedback text lines.
	 */
	private VerticalPanel vPanel;

	
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
	private List<InputFeedback<? extends GuiWidget>> accumulatedFeedback;
	
	
	/**
	 * Creates a new SequentialInputFeedbackPanel with default values for the
	 * maximum number of display lines and delay.
	 * 
	 */
	public CumulativeInputFeedbackPanel(GuiWidget widget) {
		this(widget, DEFAULT_MAX_LINES);
	}


	/**
	 * Creates a new SequentialInputFeedbackPanel object with the given number
	 * of maximum lines and display delay.
	 * 
	 * @param maxLines
	 *            The maximum number of simultaneous feedback lines.
	 */
	public CumulativeInputFeedbackPanel(GuiWidget widget, int maxLines) {
		
		// no automatic hiding. This input info panel has its own logic
		super(widget);
		
		/*
		 * By default, new feedback is shown on the bottom.
		 */
		this.newOnTop = false;
		
		
		//this.setDelay(delay);
		this.maxLines = maxLines;
		
		this.accumulatedFeedback = new ArrayList<InputFeedback<? extends GuiWidget>>();
		
		// infoLabel = new Label();
		vPanel = new VerticalPanel();
		this.add(vPanel);
		
		//this.setBackground(new ImagePanel("/instantplaces/icons/bubble.gif"));
		super.setStyleName(DEFAULT_STYLENAME);
		this.getBackground().setClassName(BACKGROUND_DEFAULT_STYLENAME);
	}
	

	@Override
	public void show(InputFeedback<? extends GuiWidget> feedback) {
		Log.debug(this, "Showing:" + feedback.toString());
		
		if ( this.newOnTop ) {
			this.accumulatedFeedback.add(0, feedback);
		} else {
			this.accumulatedFeedback.add(feedback);
		}
		
		if (this.accumulatedFeedback.size() > this.maxLines) {
			this.accumulatedFeedback.remove(0);
		}
		
		vPanel.clear();
		
		
		for (InputFeedback<?> feed : this.accumulatedFeedback) {
			
				StringBuilder message = new StringBuilder();
				message.append(feed.getInputEvent().getPersona());
				if (feed.getInfo() != null && feed.getInfo().length() > 0) {
					message.append(": ").append(feed.getInfo());
				}
				
				HTML l = new HTML( message.toString() );
				
				switch( feed.getType() ) {
				case ACCEPTED:
					l.setStyleName(ACCEPTED_INPUT_STYLENAME_SUFFIX);
					break;
				case NOT_ACCEPTED:
					l.setStyleName(NOTACCEPTED_INPUT_STYLENAME_SUFFIX);
					break;
				
				}
				
				vPanel.add(l);
		
		}
				
		this.displayPanel();
	}

	@Override
	public void hide(InputFeedback<? extends GuiWidget> feedback, boolean noMore) {
		Log.debug("Stopped showing:" + feedback.toString() + " more feedback: "+noMore);
		
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
	 * Fills the panel with available feedback lines and displays it. This method
	 * also schedules the timer so that the panel is hidden and a new batch of
	 * feedback lines can be shown.
	 */
	private void displayPanel() {
		if (!this.isShowing()) {
			if ( this.widget.isDisplaying() ) {
				
				super.show();
			} /*else {
				PopupPanel p = new PopupPanel();
				this.widget.removeFromParent();
				p.add(this.widget);
				p.show();
				super.show();
			}*/
		}

		this.alignPanel();

	}


	public void setNewOnTop(boolean newOnTop) {
		this.newOnTop = newOnTop;
	}


	public boolean isNewOnTop() {
		return newOnTop;
	}


	

}
