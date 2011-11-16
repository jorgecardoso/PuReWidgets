package org.jorgecardoso.purewidgets.demo.client.test;




import org.instantplaces.purewidgets.client.application.PublicDisplayApplication;
import org.instantplaces.purewidgets.client.widgets.GuiDownloadButton;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test implements EntryPoint{
	
	
	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "Test", true);
		
		GuiDownloadButton gdb = new GuiDownloadButton("downloadid", "Download test video", "http://jorgecardoso.eu");
	}
}
