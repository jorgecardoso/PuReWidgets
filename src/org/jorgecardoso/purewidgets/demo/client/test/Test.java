package org.jorgecardoso.purewidgets.demo.client.test;




import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.widgets.GuiButton;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test implements EntryPoint{

	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "Test", true);
		for (int i = 0; i < 10; i++) {
			GuiButton b = new GuiButton("my-button"+i, "button-----"+i);
			b.setVolatile(true);
		}
	}
}
