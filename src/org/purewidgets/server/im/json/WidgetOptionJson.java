
package org.purewidgets.server.im.json;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.Place;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetOption;

import com.google.appengine.api.datastore.Key;


/**
 * 
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
	
	
	private WidgetOptionJson() {
		
	}


	public static WidgetOptionJson create(WidgetOption widgetOption) {
		WidgetOptionJson widgetOptionJson = new WidgetOptionJson();
		
		widgetOptionJson.setWidgetOptionId(widgetOption.getWidgetOptionId());
		widgetOptionJson.setSuggestedReferenceCode(widgetOption.getSuggestedReferenceCode());
		widgetOptionJson.setReferenceCode(widgetOption.getReferenceCode());
		widgetOptionJson.setLongDescription(widgetOption.getLongDescription());
		widgetOptionJson.setShortDescription(widgetOption.getShortDescription());
		
		return widgetOptionJson;
		
	}
	
	public WidgetOption getWidgetOption() {
		WidgetOption widgetOption = new WidgetOption();
		
		widgetOption.setWidgetOptionId(this.widgetOptionId);
		widgetOption.setSuggestedReferenceCode(this.suggestedReferenceCode);
		widgetOption.setReferenceCode(this.referenceCode);
		widgetOption.setLongDescription(this.longDescription);
		widgetOption.setShortDescription(this.shortDescription);
		
		
		return widgetOption;
	}
	
	/**
	 * @return the widgetOptionId
	 */
	private String getWidgetOptionId() {
		return widgetOptionId;
	}


	/**
	 * @param widgetOptionId the widgetOptionId to set
	 */
	private void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}


	/**
	 * @return the suggestedReferenceCode
	 */
	private String getSuggestedReferenceCode() {
		return suggestedReferenceCode;
	}


	/**
	 * @param suggestedReferenceCode the suggestedReferenceCode to set
	 */
	private void setSuggestedReferenceCode(String suggestedReferenceCode) {
		this.suggestedReferenceCode = suggestedReferenceCode;
	}


	/**
	 * @return the referenceCode
	 */
	private String getReferenceCode() {
		return referenceCode;
	}


	/**
	 * @param referenceCode the referenceCode to set
	 */
	private void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}


	/**
	 * @return the longDescription
	 */
	private String getLongDescription() {
		return longDescription;
	}


	/**
	 * @param longDescription the longDescription to set
	 */
	private void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}


	/**
	 * @return the shortDescription
	 */
	private String getShortDescription() {
		return shortDescription;
	}


	/**
	 * @param shortDescription the shortDescription to set
	 */
	private void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
}
