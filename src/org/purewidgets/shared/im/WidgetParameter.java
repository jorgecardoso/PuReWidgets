/**
 * 
 */
package org.purewidgets.shared.im;


/**
 * A WidgetParameter is a name/value pair.
 * Concrete widgets may use specific parameter names in their internal logic, but should document those names.
 * Applications may also use this for widget-instance-related data.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class WidgetParameter {
	private String name;
	
	private String value;
	
	/**
	 * Creates a new WidgetParameter with the given name and value.
	 * @param name The name for the parameter.
	 * @param value The value for the parameter.
	 */
	public WidgetParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}


	/**
	 * Gets the name of this parameter.
	 * 
	 * @return the name 
	 */
	public String getName() {
		return name;
	}


	/**
	 * Gets the value of this parameter.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * Sets the name of this parameter.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Sets the value of this parameter.
	 * 
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
