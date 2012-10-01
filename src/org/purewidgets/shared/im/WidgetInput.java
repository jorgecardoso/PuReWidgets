package org.purewidgets.shared.im;

import java.util.ArrayList;


/**
 * The WidgetInput class represents an input from a user to an option of a widget.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class WidgetInput {
	private String placeId;
	
	private String applicationId;

	private String referenceCode;
		
	private String widgetId;
	
	private String widgetOptionId;
	
	private String timeStamp;	
	
	private ArrayList<String> parameters;
	
	private String userId;
	
	private String nickname;	
	
	private String inputMechanism;
	
//	private boolean delivered;

	private int age;
	
	/**
	 * Creates a new WidgetInput with default values.
	 */
	public WidgetInput() {
		this.placeId = "";
		this.applicationId = "";
		this.referenceCode = "";
		this.widgetId = "";
		this.widgetOptionId = "";
		this.timeStamp = "";
		this.parameters = new ArrayList<String>();
		this.userId = "";
		this.nickname = "";
		this.inputMechanism = "";
//		this.delivered = false;
		this.age = 0;
	}
	
	/**
	 * Compares this WidgetInput with another WidgetInput.
	 * @return true iff the timestamp, place id, application id, and user id are equal in the two WidgetInput.
	 */
	@Override
	public boolean equals(Object other) {
		if ( !(other instanceof WidgetInput) ) {
			return false;
		}
		
		WidgetInput otherWI = (WidgetInput)other;
		
		if ( !this.timeStamp.equals(otherWI.getTimeStamp()) || !this.placeId.equals(otherWI.getPlaceId()) 
				|| !this.applicationId.equals(otherWI.getApplicationId()) || !this.userId.equals(otherWI.getUserId())) {
			return false;
		}
		return true;
	}


	/**
	 * Gets the age of this widget input
	 * 
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	
	/**
	 * Gets the id of the application that is the target of this widget input.
	 * 
	 * @return the id of the application that is the target of this widget input.
	 */
	public String getApplicationId() {
		return applicationId;
	}


	/**
	 * Gets the input mechanism the user used to generate this input.
	 * 
	 * @return the input mechanism
	 */
	public String getInputMechanism() {
		return inputMechanism;
	}


	/**
	 * Gets the user's nickname.
	 * 
	 * @return The user's nickname.
	 */
	public String getNickname() {
		return this.nickname;
	}

	
	/**
	 * Gets the parameters of this input.
	 * 
	 * @return The parameters of the input.
	 */
	public ArrayList<String> getParameters() {
		return this.parameters;
	}


	/**
	 * Gets the id of the place to which the input is directed.
	 * 
	 * @return the id of the place.
	 */
	public String getPlaceId() {
		return placeId;
	}


	/**
	 * Gets the reference code used in the input.
	 * 
	 * @return the reference code.
	 */
	public String getReferenceCode() {
		return referenceCode;
	}


	/**
	 * Gets the timestamp of when the input was generated.
	 * 
	 * @return The timestamp of the input.
	 */
	public String getTimeStamp() {
		return this.timeStamp;
	}


	/**
	 * Gets the id of the user that generated the input.
	 * 
	 * @return the user id.
	 */
	public String getUserId() {
		return userId;
	}
	

	/**
	 * Gets the id of the widget to which the input is directed.
	 * 
	 * @return The id of the widget.
	 */
	public String getWidgetId() {
		return this.widgetId;
	}


	/**
	 * Gets the id of the widget option to which the input is directed.
	 * 
	 * @return the id of the widget option.
	 */
	public String getWidgetOptionId() {
		return this.widgetOptionId;
	}


//	/**
//	 * @return the delivered
//	 */
//	public boolean isDelivered() {
//		return delivered;
//	}


	/**
	 * Sets the age of the input
	 * 
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}


	/**
	 * Sets the id of the application.
	 * 
	 * @param applicationId the id to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


//	/**
//	 * @param delivered the delivered to set
//	 */
//	public void setDelivered(boolean delivered) {
//		this.delivered = delivered;
//	}


	/**
	 * Sets the input mechanism used in the input.
	 * 
	 * @param inputMechanism the mechanism to set
	 */
	public void setInputMechanism(String inputMechanism) {
		this.inputMechanism = inputMechanism;
	}


	/**
	 * Sets the nickname of the user
	 * 
	 * @param persona The nickname.
	 */
	public void setNickname(String persona) {
		this.nickname = persona;
	}


	/**
	 * Sets the list of parameters for this input.
	 * 
	 * @param parameters The list of parameters.
	 */
	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}


	/**
	 * Sets the id of the place.
	 * 
	 * @param placeId the id to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * Sets the reference code of the widget option to which the input is directed.
	 * 
	 * @param referenceCode the reference code to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * Sets the timestamp of the input.
	 * 
	 * @param timeStamp the timestamp of the input.
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	/**
	 * Sets the user id that generated the input.
	 * 
	 * @param userIdentifier the user id to set
	 */
	public void setUserId(String userIdentifier) {
		this.userId = userIdentifier;
	}


	/**
	 * Sets the widget id to which the input is directed.
	 * 
	 * @param widgetId the id to set.
	 */
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * sets the widget option id to which the input id directed.
	 * 
	 * @param widgetOptionId the id to set.
	 */
	public void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}
}
