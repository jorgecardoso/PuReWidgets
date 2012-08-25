package org.purewidgets.shared.events;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages.AlternateMessage;
import com.google.gwt.i18n.client.Messages.DefaultMessage;
import com.google.gwt.i18n.client.Messages.PluralCount;

@DefaultLocale("en")
public interface InputEventAgeMessages extends Messages {
	@DefaultMessage("({0,number} hours ago)")
	@AlternateMessage({"one", "(1 hour ago)"})
	String ageHour(@PluralCount int value);

	@DefaultMessage("({0,number} days ago)")
	@AlternateMessage({"one", "(1 day)"})	
	String ageDay(@PluralCount int value);
	
	@DefaultMessage("({0,number} weeks ago)")
	@AlternateMessage({"one", "(1 week ago)"})
	String ageWeek(@PluralCount int value);


	@DefaultMessage("({0,number} minutes ago)")
	@AlternateMessage({"one", "(1 minute ago)"})
	String ageMinute(@PluralCount int value);
	
	@DefaultMessage("({0,number} seconds ago)")
	@AlternateMessage({"one", "(1 second ago)"})
	String ageSecond(@PluralCount int value);	
}
	 