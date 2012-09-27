/**
 * 
 */
package org.purewidgets.client.feedback;

import org.purewidgets.client.widgets.PdWidget;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The AbstractInputFeedbackPanel is an abstract implementation of a panel for user input feedback display on the public 
 * display.
 * 
 * 
 * @author Jorge C. S. Cardoso
 */
public abstract class AbstractInputFeedbackPanel extends PopupPanel implements FeedbackDisplay {

	/**
	 * The widget that wishes to provide input feedback;
	 */
	protected PdWidget widget;
	

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
	
	
	/**
	 * The horizontal displacement applied to the calculated position of the panel.
	 */
	protected int alignDisplacementX;
	
	/**
	 * The vertical displacement applied to the calculated position of the panel.
	 */
	protected int alignDisplacementY;
	
	
	/**
	 * Creates a new AbstractInputFeedbackPanel for the specified PDWidget.
	 *  
	 * @param widget
	 */
	public AbstractInputFeedbackPanel(PdWidget widget) {
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

		if ( null != this.widget ) {
			/*
			 * If its relative to an existing widget, use absolute positioning
			 */
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
		} else {
			/*
			 * If it's relative to the window use relative positioning
			 */
			if (this.widgetReferencePoint != null) {
				Style s;
				switch (this.widgetReferencePoint) {
				case BOTTOM:
					s = this.getElement().getStyle();
					s.clearProperty("top");
					s.clearProperty("right");
					s.setProperty("bottom", "0%"); 
					s.setProperty("left", x-width/2, Unit.PX); 
					break;
				case TOP:
					s = this.getElement().getStyle();
					s.setProperty("top", "0%"); 
					s.clearProperty("right");
					s.setProperty("left", x-width/2, Unit.PX); 
					s.clearProperty("bottom");
					break;
				case LEFT:
					s = this.getElement().getStyle();
					s.setProperty("left", "0%"); 
					s.setProperty("top", y, Unit.PX);
					s.clearProperty("right");
					s.clearProperty("bottom");
					break;
				case RIGHT:
					s = this.getElement().getStyle();
					s.setProperty("right", "0%"); 
					s.setProperty("top", y, Unit.PX); 
					s.clearProperty("bottom");
					s.clearProperty("left");
					break;
				case CENTER:
					this.setPopupPosition(x - width / 2, y - height / 2);
					break;
				}
			}
		}
	}


	 
	/**
	 * Sets the reference point on the widget that will be used to align the 
	 * the panel. 
	 * 
	 * @param widgetReferencePoint The reference point.
	 * @see FeedbackDisplay#setWidgetReferencePoint(Align)
	 */
	@Override
	public void setWidgetReferencePoint(Align widgetReferencePoint) {
		this.widgetReferencePoint = widgetReferencePoint;
	}
	
	/**
	 * Sets the reference point of the feedback panel that will be used to position
	 * the panel. 
	 * 
	 * 
	 * @param panelReferencePoint
	 * @see FeedbackDisplay#setPanelReferencePoint(Align)
	 */
	@Override
	public void setPanelReferencePoint(Align panelReferencePoint) {
		this.panelReferencePoint = panelReferencePoint;
	}

	/**
	 * Gets the horizontal displacement applied to the panel.
	 * 
	 * @return the alignDisplacementX
	 */
	public int getAlignDisplacementX() {
		return alignDisplacementX;
	}

	/**
	 * Sets the horizontal displacement, relative to the reference point.
	 * 
	 * @param alignDisplacementX the alignDisplacementX to set
	 */
	public void setAlignDisplacementX(int alignDisplacementX) {
		this.alignDisplacementX = alignDisplacementX;
	}

	/**
	 * Gets the vertical displacement of the panel, relative to the reference point.
	 * @return the alignDisplacementY
	 */
	public int getAlignDisplacementY() {
		return alignDisplacementY;
	}

	/**
	 * Sets the vertical displacement of the panel, relative to the reference point.
	 * 
	 * @param alignDisplacementY the alignDisplacementY to set
	 */
	public void setAlignDisplacementY(int alignDisplacementY) {
		this.alignDisplacementY = alignDisplacementY;
	}
	
	/**
	 * Gets the PDWidget that this AbstractInputFeedbackPanel is serving.
	 * @return The PDWidget that this AbstractInputFeedbackPanel is serving.
	 */
	@Override
	public PdWidget getWidget() {
		return this.widget;
	}
	
	/**
	 * Gets the Panel widget used by this AbstractInputFeedbackPanel to show feedback.
	 */
	@Override
	public Widget getFeedbackDisplayWidget() {
		return this;
	}
}
