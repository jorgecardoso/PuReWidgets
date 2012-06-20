/**
 * 
 */
package org.purewidgets.client.im;

/**
 * Helper class to format the reference codes on widgets.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class ReferenceCodeFormatter {
	public static final String DEFAULT_LEFT_BRACKET = "(";
	public static final String DEFAULT_RIGHT_BRACKET = ")";
	
	private static String leftBracket = DEFAULT_LEFT_BRACKET;
	private static String rightBracket = DEFAULT_RIGHT_BRACKET;
	
	public static String getLeftBracket() {
		return leftBracket;
	}
	public static void setLeftBracket(String leftBracket) {
		ReferenceCodeFormatter.leftBracket = leftBracket;
	}
	public static String getRightBracket() {
		return rightBracket;
	}
	public static void setRightBracket(String rightBracket) {
		ReferenceCodeFormatter.rightBracket = rightBracket;
	}
	
	
	
	public static String format(String referenceCode) {
		return leftBracket + referenceCode + rightBracket;
	}
}
