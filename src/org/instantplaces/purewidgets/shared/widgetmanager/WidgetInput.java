package org.instantplaces.purewidgets.shared.widgetmanager;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * The WidgetInput class represents an input from a user to an option of a widget.
 * An input is issued by a persona and consists of a list  of zero or more parameters.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
public class WidgetInput {
	private String placeId;
	
	private String applicationId;

	private String referenceCode;
		

	private String widgetId;
	
	private String widgetOptionId;
	
	private String timeStamp;	
	
	private ArrayList<String> parameters;
	
	private String userIdentifier;
	
	private String persona;	
	
	private String inputMechanism;
	
	private boolean delivered;

	private int age;
	
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}


	public String getWidgetId() {
		return this.widgetId;
	}

	
	public void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}


	public String getWidgetOptionId() {
		return this.widgetOptionId;
	}


	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	public String getTimeStamp() {
		return this.timeStamp;
	}


	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}


	public ArrayList<String> getParameters() {
		return this.parameters;
	}


	public void setPersona(String persona) {
		this.persona = persona;
	}


	public String getPersona() {
		return this.persona;
	}
	
	public final String toDebugString() {
		return "WidgetInput(" + "persona: " + this.persona + "; parameters " + this.parameters.toString() + ")";
	}


	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}


	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}


	/**
	 * @return the inputMechanism
	 */
	public String getInputMechanism() {
		return inputMechanism;
	}


	/**
	 * @param inputMechanism the inputMechanism to set
	 */
	public void setInputMechanism(String inputMechanism) {
		this.inputMechanism = inputMechanism;
	}


	/**
	 * @return the delivered
	 */
	public boolean isDelivered() {
		return delivered;
	}


	/**
	 * @param delivered the delivered to set
	 */
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}


	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}


	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}


	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	/**
	 * @return the referenceCode
	 */
	public String getReferenceCode() {
		return referenceCode;
	}


	/**
	 * @param referenceCode the referenceCode to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}


	/**
	 * @return the userIdentifier
	 */
	public String getUserIdentifier() {
		return userIdentifier;
	}


	/**
	 * @param userIdentifier the userIdentifier to set
	 */
	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

}
