/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets.feedback;

import org.instantplaces.purewidgets.client.widgets.GuiWidget;
import org.instantplaces.purewidgets.client.widgets.Align;
import org.instantplaces.purewidgets.client.widgets.TransparentPopupPanel;




/**
 * @author Jorge C. S. Cardoso
 *
 */
public abstract class AbstractInputFeedbackPanel extends TransparentPopupPanel implements FeedbackDisplay {

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
	
	public AbstractInputFeedbackPanel(GuiWidget widget) {
		this.panelReferencePoint = Align.CENTER;
		this.widgetReferencePoint = Align.TOP;

		this.widget = widget;
	}
	
	protected void alignPanel() {
		int widgetWidth = this.widget.getWidth();
		int widgetHeight = this.widget.getHeight();
		int widgetLeft = this.widget.getLeft();
		int widgetTop = this.widget.getTop();
		
		
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
	
}
