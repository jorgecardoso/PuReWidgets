package org.purewidgets.client.widgetmanager.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.interactionmanager.WidgetOption;
import org.purewidgets.shared.logging.Log;



public class WidgetOptionJson extends GenericJson  {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetOptionJson() { 
	}
	
	
	public final native void setWidgetOptionId(String id) /*-{
		this.widgetOptionId = id;
	}-*/;


	public final native String getWidgetOptionId() /*-{
		return this.widgetOptionId;
	}-*/;


	public final native void setSuggestedReferenceCode(String refCode) /*-{
		this.suggestedReferenceCode = refCode;
	}-*/;


	public final native String getSuggestedReferenceCode() /*-{
		return this.suggestedReferenceCode;
	}-*/;


	public final native void setReferenceCode(String refCode) /*-{
		this.referenceCode = refCode;
	}-*/;


	public final native String getReferenceCode() /*-{
		return this.referenceCode;
	}-*/;
	
	
	public final native void setLongDescription(String description) /*-{
	this.longDescription = description;
}-*/;


public final native String getLongDescription() /*-{
	return this.longDescription;
}-*/;

public final native void setShortDescription(String description) /*-{
this.shortDescription = description;
}-*/;


public final native String getShortDescription() /*-{
return this.shortDescription;
}-*/;

	public static WidgetOptionJson create(WidgetOption widgetOption) {
		WidgetOptionJson optionJSON = GenericJson.getNew();
		
		optionJSON.setWidgetOptionId(widgetOption.getWidgetOptionId());
		optionJSON.setSuggestedReferenceCode(widgetOption.getSuggestedReferenceCode());
		
		optionJSON.setReferenceCode(widgetOption.getReferenceCode());
		optionJSON.setLongDescription(widgetOption.getLongDescription());
		optionJSON.setShortDescription(widgetOption.getShortDescription());
		
		return optionJSON;
	}
	
	public final WidgetOption getWidgetOption() {
		WidgetOption option = new WidgetOption(this.getWidgetOptionId(), this.getSuggestedReferenceCode());
		
		option.setReferenceCode(this.getReferenceCode());
		option.setLongDescription(this.getLongDescription());
		option.setShortDescription(this.getShortDescription());
		
		return option;
	}

}
