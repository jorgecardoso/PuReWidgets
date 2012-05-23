package org.purewidgets.client.widgets;



import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * The TransparentPopupPanel class is a variation of the PopupPanel that has a 
 * transparent background.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-TransparentPopupPanel</dt>
 * <dd>the outer element</dd> 
 * <dt>.instantplaces-TransparentPopupPanel .content</dt>
 * <dd>the inner element</dd>
 * <dt>.instantplaces-TransparentPopupPanel .background</dt>
 * <dd>the background element</dd>
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class TransparentPopupPanel extends PopupPanel {
	
	public static final String DEFAULT_STYLENAME = "instantplaces-TransparentPopupPanel";
	public static final String BACKGROUND_STYLENAME_SUFFIX = "instantplaces-TransparentPopupPanel-background";
	public static final String CONTENT_STYLENAME_SUFFIX = "content";
	
	
	private Element background;
	
	
	public TransparentPopupPanel() {
		
		super();
		
		this.background = Document.get().createDivElement();
		
		setStyleName(DEFAULT_STYLENAME);
		this.getContainerElement().setClassName(CONTENT_STYLENAME_SUFFIX);
		this.background.setClassName(BACKGROUND_STYLENAME_SUFFIX);
		
	}
	
	
	
	@Override
	public void show() {
		super.show();
		
		setBackgroundPosition();
		
		Document.get().getBody().appendChild(getBackground());
	}
	
	
	private void setBackgroundPosition() {
		int left = this.getElement().getAbsoluteLeft();
		int top = this.getElement().getAbsoluteTop();
		int width = this.getElement().getOffsetWidth();
		int height = this.getElement().getOffsetHeight();

		getBackground().getStyle().setLeft(left, Style.Unit.PX);
		getBackground().getStyle().setWidth(width, Style.Unit.PX); 
		getBackground().getStyle().setTop(top, Style.Unit.PX); 
		getBackground().getStyle().setHeight(height, Style.Unit.PX); 
	}
	
	@Override
	public void setPopupPosition(int x, int y) {
		super.setPopupPosition(x, y);
		if (this.isShowing()) {
			setBackgroundPosition();
		}
	}
	
	@Override
	public void hide() {
		super.hide();
		Document.get().getBody().removeChild(getBackground());
	}


	/**
	 * @param background the background to set
	 */
	public void setBackground(Element background) {
		this.background = background;
	}


	/**
	 * @return the background
	 */
	public Element getBackground() {
		return background;
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		//setBackgroundPosition();
	}
	
	@Override
	public void setHeight(String height) {
		super.setWidth(height);
		//setBackgroundPosition();
	}

}
