package org.purewidgets.client.widgets.feedback;

import org.purewidgets.client.widgets.Align;
import org.purewidgets.client.widgets.GuiWidget;



/**
 * The interface for input feedback .
 * 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface FeedbackDisplay  {

	public GuiWidget getWidget();
	
	/**
	 * Sets the reference point on the widget  that will be used to align the 
	 * the panel. 
	 * 
	 * TOP - Use the top-center point as a reference.
	 * BOTTOM - Use the bottom-center point as a reference.
	 * LEFT - Use the left-center point as a reference.
	 * RIGHT - Use the right-center point as a reference.
	 * CENTER - Use the center point as a reference.
	 * 
	 * @param widgetReferencePoint The reference point.
	 */
	public void setWidgetReferencePoint(Align widgetReferencePoint);
	
	/**
	 * Sets the reference point on the feedback panel that will be used to position
	 * the panel. 
	 * 
	 * TOP - Use the top-center point as a reference.
	 * BOTTOM - Use the bottom-center point as a reference.
	 * LEFT - Use the left-center point as a reference.
	 * RIGHT - Use the right-center point as a reference.
	 * CENTER - Use the center as a reference.
	 * 
	 * @param panelReferencePoint
	 */
	public void setPanelReferencePoint(Align panelReferencePoint);
	
	public void setAlignDisplacementX(int alignDisplacementX);
	public void setAlignDisplacementY(int alignDisplacementY);
	
	/**
	 * Shows the feedback.
	 * 
	 * @param feedback
	 */
	public void show(InputFeedback<? extends GuiWidget> feedback, int duration);
	
	/**
	 * Hides the feedback.
	 * 
	 * @param feedback
	 * @param noMore Indicates if there is more feedback waiting to be displayed.
	 */
	public void hide(InputFeedback<? extends GuiWidget> feedback, boolean noMore);

}
