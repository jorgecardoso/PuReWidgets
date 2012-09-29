package org.purewidgets.client.feedback;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * InputEventAgeMessages provides internationalized messages about the aproximate age of the input event.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
@DefaultLocale("en")
public interface InputEventAgeMessages extends Messages {
	
	/**
	 * Gets a localized message of the age of the input event in seconds.
	 * @param value The age of the input event in seconds.
	 * @return The localized message.
	 */
	@DefaultMessage("({0,number} seconds ago)")
	@AlternateMessage({"one", "(1 second ago)"})
	String ageSecond(@PluralCount int value);	
	
	/**
	 * Gets a localized message of the age of the input event in minutes.
	 * @param value The age of the input event in minutes.
	 * @return The localized message.
	 */	
	@DefaultMessage("({0,number} minutes ago)")
	@AlternateMessage({"one", "(1 minute ago)"})
	String ageMinute(@PluralCount int value);
	
	/**
	 * Gets a localized message of the age of the input event in hours.
	 * @param value The age of the input event in hours.
	 * @return The localized message.
	 */		
	@DefaultMessage("({0,number} hours ago)")
	@AlternateMessage({"one", "(1 hour ago)"})
	String ageHour(@PluralCount int value);

	/**
	 * Gets a localized message of the age of the input event in days.
	 * @param value The age of the input event in days.
	 * @return The localized message.
	 */		
	@DefaultMessage("({0,number} days ago)")
	@AlternateMessage({"one", "(1 day)"})	
	String ageDay(@PluralCount int value);
	
	/**
	 * Gets a localized message of the age of the input event in weeks.
	 * @param value The age of the input event in weeks.
	 * @return The localized message.
	 */		
	@DefaultMessage("({0,number} weeks ago)")
	@AlternateMessage({"one", "(1 week ago)"})
	String ageWeek(@PluralCount int value);

}
	 