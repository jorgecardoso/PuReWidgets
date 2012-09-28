package org.purewidgets.client.im.json;

import org.purewidgets.client.json.GenericJson;
import org.purewidgets.shared.im.WidgetOption;

/**
 * 
 * WidgetOptionJson is a Json DTO for receiving and sending a widget option to the interaction manager server. 
 * 
 * @author "Jorge C. S. Cardoso"
 * @see org.purewidgets.shared.im.WidgetOption
 */
public class WidgetOptionJson extends GenericJson {

	// Overlay types always have protected, zero-arg ctors
	protected WidgetOptionJson() {
	}
	
	/**
	 *
	 * Creates a WidgetOptionJson object based on a {@link WidgetOption} object
	 * 
	 * @param widgetOption The WidgetOption object to convert.
	 * @return The converted object.
	 */
	public static WidgetOptionJson create(WidgetOption widgetOption) {
		WidgetOptionJson optionJSON = GenericJson.getNew();

		optionJSON.setWidgetOptionId(widgetOption.getWidgetOptionId());
		optionJSON.setSuggestedReferenceCode(widgetOption.getSuggestedReferenceCode());

		optionJSON.setReferenceCode(widgetOption.getReferenceCode());
		optionJSON.setLongDescription(widgetOption.getLongDescription());
		optionJSON.setShortDescription(widgetOption.getShortDescription());
		optionJSON.setIconUrl(widgetOption.getIconUrl());
		return optionJSON;
	}

	/**
	 * Gets the WidgetOption object that this WidgetOptionJson object is representing.
	 * 
	 * @return The WidgetOption object that this WidgetOptionJson object is representing.
	 */	
	public final WidgetOption getWidgetOption() {
		WidgetOption option = new WidgetOption(this.getWidgetOptionId(),
				this.getSuggestedReferenceCode());

		option.setReferenceCode(this.getReferenceCode());
		option.setLongDescription(this.getLongDescription());
		option.setShortDescription(this.getShortDescription());
		option.setIconUrl(this.getIconUrl());
		return option;
	}
	
	/**
	 * Sets the id of this widget option.
	 * 
	 * @param id The id to set.
	 */
	public final native void setWidgetOptionId(String id) /*-{
		this.widgetOptionId = id;
	}-*/;

	/**
	 * Gets the id of this widget option.
	 * 
	 * @return The id of this widget option.
	 */
	public final native String getWidgetOptionId() /*-{
		return this.widgetOptionId;
	}-*/;

	/**
	 * Sets the suggested reference code for this widget option.
	 * 
	 * @param refCode The suggested reference code.
	 */
	public final native void setSuggestedReferenceCode(String refCode) /*-{
		this.suggestedReferenceCode = refCode;
	}-*/;

	/**
	 * Gets the suggested reference code for this widget option.
	 * 
	 * @return The suggested reference code.
	 */
	public final native String getSuggestedReferenceCode() /*-{
		return this.suggestedReferenceCode;
	}-*/;

	/**
	 * Sets the reference code for this widget option.
	 * 
	 * @param refCode The reference code for this widget option.
	 */
	public final native void setReferenceCode(String refCode) /*-{
		this.referenceCode = refCode;
	}-*/;

	/**
	 * Gets the reference code for this widget option.
	 * 
	 * @return The reference code for this widget option.
	 */
	public final native String getReferenceCode() /*-{
		return this.referenceCode;
	}-*/;

	/**
	 * Sets the long description for this widget option.
	 * 
	 * @param description The long description for this widget option.
	 */
	public final native void setLongDescription(String description) /*-{
		this.longDescription = description;
	}-*/;

	/**
	 * Gets the long description for this widget option
	 * 
	 * @return The long description for this widget option.
	 * 
	 */
	public final native String getLongDescription() /*-{
		return this.longDescription;
	}-*/;

	/**
	 * Sets the short description for this widget option
	 * 
	 * @param description The short description for this widget option
	 */
	public final native void setShortDescription(String description) /*-{
		this.shortDescription = description;
	}-*/;

	/**
	 * Gets the icon url for this widget option.
	 * 
	 * @return The icon url for this widget option.
	 */
	public final native String getIconUrl() /*-{
		return this.iconUrl;
	}-*/;

	/**
	 * Sets the icon url for this widget option.
	 * 
	 * @param url The icon url for this widget option.
	 */
	public final native void setIconUrl(String url) /*-{
		this.iconUrl = url;
	}-*/;

	/**
	 * Gets the short description for this widget option.
	 * 
	 * @return The short description for this widget option.
	 */
	public final native String getShortDescription() /*-{
		return this.shortDescription;
	}-*/;

	

}
