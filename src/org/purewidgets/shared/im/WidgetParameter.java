/**
 * 
 */
package org.purewidgets.shared.im;



/**
 * A general purpose name-value pair, used to store widget parameters. 
 * Concrete widgets may use specific parameter names in their internal logic, but should document those names.
 * Applications may also use this for widget-instance-related data.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class WidgetParameter {
	private String name;
	
	private String value;
	
	
	public WidgetParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
