/**
 * 
 */
package org.purewidgets.shared.im;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.google.appengine.api.datastore.Key;


/**
 * 
 * A WidgetOptionID represents an item or option, within a widget, that can be 
 * independently selected by a user. These options are selected by the user
 * by means of a reference code.
 * 
 * The optionID is a string that the application uses to distinguish between
 * options of the same widget. The application can suggest to the Instant 
 * Places service which reference code should be used for the option, but 
 * there is no guarantee that that suggested reference code is accepted.
 * The actual reference code may be different from the suggested one. 
 *   
 * @author Jorge C. S. Cardoso
 *
 */

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = Visibility.ANY)
public class WidgetOption  {
	
	@SuppressWarnings("unused")

    @JsonIgnore
    private Key key;
	
	/**
	 * The application defined identifier for the option.
	 */
	protected String widgetOptionId;
	
	/**
	 * The application suggested reference code.
	 */

	protected String suggestedReferenceCode;
	
	/**
	 * The actual reference code in use.
	 */

	protected String referenceCode;
	
	

	private String longDescription;
	

	private String shortDescription;
	
	
	public WidgetOption() {
		
	}
	
	/**
	 * Creates a new WidgetOption object with the given OptionID
	 * 
	 * @param optionID
	 */
	public WidgetOption(String optionID) {
		this(optionID, null);
	}
	/**
	 * 
	 * @param optionID
	 * @param suggestedReferenceCode
	 */
	public WidgetOption(String optionID, String suggestedReferenceCode) {
		this.setWidgetOptionId(optionID);
		this.setSuggestedReferenceCode(suggestedReferenceCode);
	}

	/**
	 * @param optionID the optionID to set
	 */
	public void setWidgetOptionId(String optionID) {
		this.widgetOptionId = optionID;
	}

	/**
	 * @return the optionID
	 * 
	 */
	public String getWidgetOptionId() {
		return widgetOptionId;
	}

	/**
	 * @param suggestedReferenceCode the suggestedReferenceCode to set
	 */
	public void setSuggestedReferenceCode(String suggestedReferenceCode) {
		this.suggestedReferenceCode = suggestedReferenceCode;
	}

	/**
	 * @return the suggestedReferenceCode
	 */
	public String getSuggestedReferenceCode() {
		return suggestedReferenceCode;
	}

	/**
	 * @param referenceCode the referenceCode to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * @return the referenceCode
	 */
	public String getReferenceCode() {
		return referenceCode;
	}
	
	
   
	@Override
	public int hashCode() {
		return this.getWidgetOptionId().hashCode();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof WidgetOption) {
			return ((WidgetOption)o).getWidgetOptionId().equalsIgnoreCase(this.getWidgetOptionId());
		}
		return false;
	}
	
	
	/**
	 * Creates a string representation of this WidgetOption for debug purposes.
	 * 
	 * @return a string representation of this object.
	 */
	public String toDebugString() {
		return "WidgetOption (" + this.widgetOptionId + ", " + this.referenceCode + ")";
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

}
