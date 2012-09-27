package org.purewidgets.client.feedback;

/**
 * MessagePattern defines string patterns that can be used on the input feedback panels to refer to various
 * user input variables.
 * 
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class MessagePattern {
	/* %U% - user nickname
	 * %P[i]% - Input parameter i
	 * %WS% - widget short description
	 * %WL% - widget long description
	 * %WOS% - widget option short description
	 * %WOL% - widget option long description
	 * %WOR% - widget option reference code
	 */
	
	/**
	 * The user's nickname.
	 */
	public static final String PATTERN_USER_NICKNAME = "%U%";
	
	/**
	 * The widget's short description.
	 */
	public static final String PATTERN_WIDGET_SHORT_DESCRIPTION = "%WS%";
	
	/**
	 * The widget's long description.
	 */
	public static final String PATTERN_WIDGET_LONG_DESCRIPTION = "%WL%";
	
	/**
	 * The widget option's short description.
	 */
	public static final String PATTERN_WIDGET_OPTION_SHORT_DESCRIPTION = "%WOS%";
	
	/**
	 * The widget option's long description.
	 */
	public static final String PATTERN_WIDGET_OPTION_LONG_DESCRIPTION = "%WOL%";
	
	/**
	 * The widget option's reference code.
	 */
	public static final String PATTERN_WIDGET_OPTION_REFERENCE_CODE = "%WOR%";
	
	/**
	 * The input's age
	 */
	public static final String PATTERN_INPUT_AGE = "%AGE%";
	
	/**
	 * An input parameter.
	 * @param index The index of the parameter
	 * @return A pattern that represents that parameter's index.
	 */
	public static final String getInputParameterPattern(int index) {
		return "%P"+index+"%";
	}
}
