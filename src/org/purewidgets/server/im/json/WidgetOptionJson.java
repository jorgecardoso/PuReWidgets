
package org.purewidgets.server.im.json;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.WidgetOption;

/**
 * 
 * The JSON data transfer object for WidgetOption objects.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WidgetOptionJson extends GenericJson {
	
	private String widgetOptionId;
	
	private String suggestedReferenceCode;
	
	private String referenceCode;
	
	private String longDescription;
	
	private String shortDescription;
	
	private String iconUrl;
	
	private WidgetOptionJson() {
	}


	public static WidgetOptionJson create(WidgetOption widgetOption) {
		WidgetOptionJson widgetOptionJson = new WidgetOptionJson();
		
		widgetOptionJson.setWidgetOptionId(widgetOption.getWidgetOptionId());
		widgetOptionJson.setSuggestedReferenceCode(widgetOption.getSuggestedReferenceCode());
		widgetOptionJson.setReferenceCode(widgetOption.getReferenceCode());
		widgetOptionJson.setLongDescription(widgetOption.getLongDescription());
		widgetOptionJson.setShortDescription(widgetOption.getShortDescription());
		widgetOptionJson.setIconUrl(widgetOption.getIconUrl());
		
		return widgetOptionJson;
	}
	
	public WidgetOption getWidgetOption() {
		WidgetOption widgetOption = new WidgetOption();
		
		widgetOption.setWidgetOptionId(this.widgetOptionId);
		widgetOption.setSuggestedReferenceCode(this.suggestedReferenceCode);
		widgetOption.setReferenceCode(this.referenceCode);
		widgetOption.setLongDescription(this.longDescription);
		widgetOption.setShortDescription(this.shortDescription);
		widgetOption.setIconUrl(this.iconUrl);
		return widgetOption;
	}

	/**
	 * @param widgetOptionId the widgetOptionId to set
	 */
	private void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}

	/**
	 * @param suggestedReferenceCode the suggestedReferenceCode to set
	 */
	private void setSuggestedReferenceCode(String suggestedReferenceCode) {
		this.suggestedReferenceCode = suggestedReferenceCode;
	}

	/**
	 * @param referenceCode the referenceCode to set
	 */
	private void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * @param longDescription the longDescription to set
	 */
	private void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	private void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}


	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}


	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}
