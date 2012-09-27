package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PdWidget;

import com.google.gwt.user.client.ui.Widget;



/**
 * The FeedbackDisplay interface defines the general contract for providing feedback about user input to a specific PdWidget.
 * 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface FeedbackDisplay  {

	/**
	 * Gets the GWT widget that shows the feedback on the public display.
	 * @return The GWT widget that shows the feedback on the public display.
	 */
	public Widget getFeedbackDisplayWidget();
	
	
	/**
	 * Gets the PdWidget that this FeedbackDisplay is associated with.
	 * @return The PdWidget that this FeedbackDisplay is associated with.
	 */
	public PdWidget getWidget();
	
	/**
	 * Hides the input feedback widget.
	 */
	public void hide();
	
	/**
	 * Hides the specified feedback, and indicates whether there is more feedback to
	 * display next.
	 * 
	 * @param feedback The InputFeedback to hide.
	 * @param noMore Indicates if there is more feedback waiting to be displayed.
	 */
	public void hide(InputFeedback<? extends PdWidget> feedback, boolean noMore);
	
	
	/**
	 * Sets the horizontal displacement, relative to the reference point.
	 * 
	 * @param alignDisplacementX the alignDisplacementX to set
	 */	
	public void setAlignDisplacementX(int alignDisplacementX);
	
	/**
	 * Gets the vertical displacement of the panel, relative to the reference point.
	 * @return the alignDisplacementY
	 */	
	public void setAlignDisplacementY(int alignDisplacementY);
	
	/**
	 * Sets the reference point of the feedback panel that will be used to position
	 * the panel. 
	 * 
	 * TOP - Use the top-center point of the panel as a reference.<br>
	 * BOTTOM - Use the bottom-center point of the panel as a reference.<br>
	 * LEFT - Use the left-center point of the panel as a reference.<br>
	 * RIGHT - Use the right-center point of the panel as a reference.<br>
	 * CENTER - Use the center point of the panel as a reference.<br>
	 * 
	 * @param panelReferencePoint
	 */
	public void setPanelReferencePoint(Align panelReferencePoint);
	
	/**
	 * Sets the reference point on the PdWidget that will be used to align the 
	 * the feedback GWT widget. 
	 * 
	 * 
	 * TOP - Use the top-center point of the widget as a reference.<br>
	 * BOTTOM - Use the bottom-center point of the widget as a reference.<br>
	 * LEFT - Use the left-center point of the widget as a reference.<br>
	 * RIGHT - Use the right-center point of the widget as a reference.<br>
	 * CENTER - Use the center point of the widget as a reference.<br>
	 * 
	 * @param widgetReferencePoint The reference point.
	 */
	public void setWidgetReferencePoint(Align widgetReferencePoint);
	
	/**
	 * Shows input feedback for the specified duration.
	 * 
	 * @param inputFeedback The information about the feedback to display.
	 * @param duration The amount of time, in milliseconds, that the feedback should be visible.
	 */
	public void show(InputFeedback<? extends PdWidget> feedback, int duration);

}
