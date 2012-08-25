package org.purewidgets.client.feedback;

public class MessagePattern {
	/* %U% - user nickname
	 * %P[i]% - Input parameter i
	 * %WS% - widget short description
	 * %WL% - widget long description
	 * %WOS% - widget option short description
	 * %WOL% - widget option long description
	 * %WOR% - widget option reference code
	 */
	public static final String PATTERN_USER_NICKNAME = "%U%";
	public static final String PATTERN_WIDGET_SHORT_DESCRIPTION = "%WS%";
	public static final String PATTERN_WIDGET_LONG_DESCRIPTION = "%WL%";
	public static final String PATTERN_WIDGET_OPTION_SHORT_DESCRIPTION = "%WOS%";
	public static final String PATTERN_WIDGET_OPTION_LONG_DESCRIPTION = "%WOL%";
	public static final String PATTERN_WIDGET_OPTION_REFERENCE_CODE = "%WOR%";
	public static final String PATTERN_INPUT_AGE = "%AGE%";
	public static final String getInputParameterPattern(int index) {
		return "%P["+index+"]";
	}
}
