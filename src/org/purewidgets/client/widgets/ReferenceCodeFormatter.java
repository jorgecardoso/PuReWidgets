/**
 * 
 */
package org.purewidgets.client.widgets;

/**
 * Helper class to format the reference codes on widgets. Reference codes are shown by default between parenthesis,
 * but the left and right symbols can be defined by the programmer.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class ReferenceCodeFormatter {
	public static final String DEFAULT_LEFT_BRACKET = "(";
	public static final String DEFAULT_RIGHT_BRACKET = ")";
	
	private static String leftBracket = DEFAULT_LEFT_BRACKET;
	private static String rightBracket = DEFAULT_RIGHT_BRACKET;
	
	/**
	 * Gets the symbol that is printed to the left of the reference code.
	 * @return The symbol to the left of the reference code.
	 */
	public static String getLeftBracket() {
		return leftBracket;
	}
	
	/**
	 * Sets the symbol that is printed to the left of the reference code.
	 * @param leftBracket The symbol to the left of the reference code.
	 */	
	public static void setLeftBracket(String leftBracket) {
		ReferenceCodeFormatter.leftBracket = leftBracket;
	}
	
	/**
	 * Gets the symbol that is printed to the right of the reference code.
	 * @return The symbol to the right of the reference code.
	 */	
	public static String getRightBracket() {
		return rightBracket;
	}
	
	/**
	 * Sets the symbol that is printed to the right of the reference code.
	 * @param leftBracket The symbol to the right of the reference code.
	 */		
	public static void setRightBracket(String rightBracket) {
		ReferenceCodeFormatter.rightBracket = rightBracket;
	}
	
	
	/**
	 * Formats a reference code with the left and right symbols.
	 * 
	 * @param referenceCode The reference code to format.
	 * @return The formatted reference code.
	 */
	public static String format(String referenceCode) {
		return leftBracket + (referenceCode != null ? referenceCode : "") + rightBracket;
	}
}
