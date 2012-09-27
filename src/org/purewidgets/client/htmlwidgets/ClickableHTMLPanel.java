package org.purewidgets.client.htmlwidgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * An HTMLPanel that can respond to click events.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ClickableHTMLPanel extends HTMLPanel implements HasClickHandlers {

	public ClickableHTMLPanel(String html) {
        super(html);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

}
