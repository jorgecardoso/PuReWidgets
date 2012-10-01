/**
 * 
 */
package org.purewidgets.shared.im;

/**
 * 
 * A WidgetOption represents an item or option, within a widget, that can be 
 * independently selected by a user. 
 * 
 * A WidgetOption has an id that distinguishes it from other WidgetOptions in 
 * a Widget. 
 * 
 * A WidgetOption has a reference code assigned
 * by the interaction manager, which can be used in textual interaction mechanisms
 * to address the widget option. The application can suggest to the interaction manager which 
 * reference code should be used for the option, but 
 * there is no guarantee that that suggested reference code is accepted.
 * The actual reference code may be different from the suggested one. 
 *   
 * @author Jorge C. S. Cardoso
 *
 */
public class WidgetOption  {
	
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
	
	private String iconUrl;
	
	/**
	 * Created a default WidgetOption.
	 */
	public WidgetOption() {
		
	}
	
	/**
	 * Creates a new WidgetOption object with the given id
	 * 
	 * @param optionID The id of the WidgetOption.
	 */
	public WidgetOption(String optionID) {
		this(optionID, null);
	}
	
	/**
	 * Creates a new WidgetOption with the given id and suggested reference code.
	 * @param optionID the id for the WidgetOption
	 * @param suggestedReferenceCode The suggested reference code.
	 */
	public WidgetOption(String optionID, String suggestedReferenceCode) {
		this.setWidgetOptionId(optionID);
		this.setSuggestedReferenceCode(suggestedReferenceCode);
	}

	/**
	 * Compares this WidgetOption with another WidgetOption
	 * @return true iff this WidgetOption's id is equal to the other WidgetOption's id.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof WidgetOption) {
			return ((WidgetOption)o).getWidgetOptionId().equalsIgnoreCase(this.getWidgetOptionId());
		}
		return false;
	}

	/**
	 * Gets the url of the icon associated with this widget option.
	 * 
	 * @return the url of the icon.
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * Gets the long description associated with this widgetoption.
	 * @return the long description.
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * Gets the reference code of this widget option.
	 * @return the reference code
	 */
	public String getReferenceCode() {
		return referenceCode;
	}

	/**
	 * Gets the short description of this widget option.
	 * 
	 * @return the short description.
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Gets the suggested reference code.
	 * @return the suggested reference code
	 */
	public String getSuggestedReferenceCode() {
		return suggestedReferenceCode;
	}
	
	
   
	/**
	 * Gets the id of the widget option
	 * 
	 * @return the id of the widget option.
	 * 
	 */
	public String getWidgetOptionId() {
		return widgetOptionId;
	}
	
	@Override
	public int hashCode() {
		return this.widgetOptionId.hashCode();
	}
	
	
	/**
	 * Sets the url of the icon for this widget option.
	 * 
	 * @param iconUrl the url to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * Sets the long description for this widget option.
	 * 
	 * @param longDescription the long description to set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Sets the reference code for this widget option
	 * 
	 * @param referenceCode the reference code to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * Sets the short description for this widget option.
	 * 
	 * @param shortDescription the short description to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Sets the suggested reference code for this widget option.
	 * 
	 * @param suggestedReferenceCode the suggested reference code to set
	 */
	public void setSuggestedReferenceCode(String suggestedReferenceCode) {
		this.suggestedReferenceCode = suggestedReferenceCode;
	}

	/**
	 * Sets the id of this widget option.
	 * @param optionID the id to set
	 */
	public void setWidgetOptionId(String optionID) {
		this.widgetOptionId = optionID;
	}

//	/**
//	 * Creates a string representation of this WidgetOption for debug purposes.
//	 * 
//	 * @return a string representation of this object.
//	 */
//	public String toDebugString() {
//		return "WidgetOption (" + this.widgetOptionId + ", " + this.referenceCode + ")";
//	}

}
