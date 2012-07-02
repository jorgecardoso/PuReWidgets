package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PdWidget;

import com.google.gwt.user.client.ui.Widget;



/**
 * The interface for input feedback .
 * 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface FeedbackDisplay  {

	public Widget getFeedbackDisplayWidget();
	
	public PdWidget getWidget();
	
	public void hide();
	
	/**
	 * Hides the feedback.
	 * 
	 * @param feedback
	 * @param noMore Indicates if there is more feedback waiting to be displayed.
	 */
	public void hide(InputFeedback<? extends PdWidget> feedback, boolean noMore);
	public void setAlignDisplacementX(int alignDisplacementX);
	
	public void setAlignDisplacementY(int alignDisplacementY);
	
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
	 * Shows the feedback.
	 * 
	 * @param feedback
	 */
	public void show(InputFeedback<? extends PdWidget> feedback, int duration);

}
