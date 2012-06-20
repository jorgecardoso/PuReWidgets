/**
 * 
 */
package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.Align;
import org.purewidgets.client.widgets.GuiWidget;
import org.purewidgets.client.widgets.TransparentPopupPanel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;




/**
 * @author Jorge C. S. Cardoso
 *
 */
public abstract class AbstractInputFeedbackPanel extends PopupPanel implements FeedbackDisplay {

	/**
	 * The widget that wishes to provide input feedback;
	 */
	protected GuiWidget widget;
	

	/**
	 * The reference point on the feedback panel that will be positioned in the
	 * (x, y) coordinates.
	 */
	protected Align panelReferencePoint;

	/**
	 * The reference point on the widget that will serve to align the feedback 
	 * panel.
	 */
	protected Align widgetReferencePoint;
	
	
	protected int alignDisplacementX;
	protected int alignDisplacementY;
	
	public AbstractInputFeedbackPanel(GuiWidget widget) {
		this.panelReferencePoint = Align.BOTTOM;
		this.widgetReferencePoint = Align.TOP;

		this.widget = widget;
		
		alignDisplacementX = 0;
		alignDisplacementY = 25;
	}
	
	protected void alignPanel() {
		
		int widgetWidth; 
		int widgetHeight;
		int widgetLeft; 
		int widgetTop;
		
		if ( null != this.widget ) {
			widgetWidth = this.widget.getWidth();
			widgetHeight = this.widget.getHeight();
			widgetLeft = this.widget.getLeft();
			widgetTop = this.widget.getTop();
		} else {
			widgetWidth = Window.getClientWidth();
			widgetHeight = Window.getClientHeight();
			widgetLeft = 0;
			widgetTop = 0;
		}
		
		/* Calculate the (x,y) coordinates of the screen where the
		 * panel will appear.
		 */
		int x = 0, y = 0;
		
		switch (this.widgetReferencePoint) {
		case BOTTOM:
			x = widgetLeft+widgetWidth/2;
			y = widgetTop+widgetHeight;
			
			break;
		case TOP:
			x = widgetLeft+widgetWidth/2;
			y = widgetTop;
			break;
		case LEFT:
			x = widgetLeft;
			y = widgetTop+widgetHeight/2;
			break;
		case RIGHT:
			x = widgetLeft+widgetWidth;
			y = widgetTop+widgetHeight/2;
			break;
		case CENTER:
			x = widgetLeft+widgetWidth/2;
			y = widgetTop+widgetHeight/2;
			break;
		}
		
		x += alignDisplacementX;
		y += alignDisplacementY;
		
		/*
		 * Position the popup panel, aligned according to the panel alignment.
		 */
		int height = this.getOffsetHeight();
		int width = this.getOffsetWidth();

		if (this.panelReferencePoint != null) {
			switch (this.panelReferencePoint) {
			case BOTTOM:
				this.setPopupPosition(x - width / 2, y - height);
				break;
			case TOP:
				this.setPopupPosition(x - width / 2, y);
				break;
			case LEFT:
				this.setPopupPosition(x, y - height / 2);
				break;
			case RIGHT:
				this.setPopupPosition(x - width, y - height / 2);
				break;
			case CENTER:
				this.setPopupPosition(x - width / 2, y - height / 2);
				break;
			}
		}
	}

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
	@Override
	public void setWidgetReferencePoint(Align widgetReferencePoint) {
		this.widgetReferencePoint = widgetReferencePoint;
	}
	
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
	@Override
	public void setPanelReferencePoint(Align panelReferencePoint) {
		this.panelReferencePoint = panelReferencePoint;
	}

	/**
	 * @return the alignDisplacementX
	 */
	public int getAlignDisplacementX() {
		return alignDisplacementX;
	}

	/**
	 * @param alignDisplacementX the alignDisplacementX to set
	 */
	public void setAlignDisplacementX(int alignDisplacementX) {
		this.alignDisplacementX = alignDisplacementX;
	}

	/**
	 * @return the alignDisplacementY
	 */
	public int getAlignDisplacementY() {
		return alignDisplacementY;
	}

	/**
	 * @param alignDisplacementY the alignDisplacementY to set
	 */
	public void setAlignDisplacementY(int alignDisplacementY) {
		this.alignDisplacementY = alignDisplacementY;
	}
	
	public GuiWidget getWidget() {
		return this.widget;
	}
	
	public Widget getFeedbackDisplayWidget() {
		return this;
	}
}
