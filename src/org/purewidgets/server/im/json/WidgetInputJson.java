package org.purewidgets.server.im.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.Widget;
import org.purewidgets.shared.im.WidgetInput;


/**
 * 
 * @author Jorge C. S. Cardoso
 *
 */

@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WidgetInputJson extends GenericJson {
	

	private String placeId;
	
	private String applicationId;
		
	private String widgetId;
	
	private String widgetOptionId;
	
	private String referenceCode;
	
	
	private ArrayList<String> parameters;
	
	private String userId;
	
	private String nickname;	
	
	private String inputMechanism;
	
	private String timeStamp;

	private int age;
	
	private WidgetInputJson() {
		
	}
	
	public static WidgetInputJson create(WidgetInput widgetInput) {
		WidgetInputJson widgetInputJson = new WidgetInputJson();
		
		widgetInputJson.setPlaceId(widgetInput.getPlaceId());
		widgetInputJson.setApplicationId(widgetInput.getApplicationId());
		widgetInputJson.setWidgetId(widgetInput.getWidgetId());
		widgetInputJson.setReferenceCode(widgetInput.getReferenceCode());
		widgetInputJson.setParameters(widgetInput.getParameters());
		widgetInputJson.setUserId(widgetInput.getUserId());
		widgetInputJson.setNickname(widgetInput.getNickname());
		widgetInputJson.setInputMechanism(widgetInput.getInputMechanism());
		widgetInputJson.setTimeStamp(widgetInput.getTimeStamp());
		widgetInputJson.setAge(widgetInput.getAge());
		
		return widgetInputJson;
	}
	
	public WidgetInput getWidgetInput() {
		WidgetInput widgetInput = new WidgetInput();
		
		widgetInput.setPlaceId(this.placeId);
		widgetInput.setApplicationId(this.applicationId);
		widgetInput.setWidgetId(this.widgetId);
		widgetInput.setReferenceCode(this.referenceCode);
		widgetInput.setParameters(this.parameters);
		widgetInput.setUserId(this.userId);
		widgetInput.setNickname(this.nickname);
		widgetInput.setInputMechanism(this.inputMechanism);
		widgetInput.setTimeStamp(this.timeStamp);
		widgetInput.setAge(this.age);
		
		return widgetInput;
	}

	/**
	 * @return the placeId
	 */
	private String getPlaceId() {
		return placeId;
	}

	/**
	 * @param placeId the placeId to set
	 */
	private void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	/**
	 * @return the applicationId
	 */
	private String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the referenceCode
	 */
	private String getReferenceCode() {
		return referenceCode;
	}

	/**
	 * @param referenceCode the referenceCode to set
	 */
	private void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * @return the widgetId
	 */
	private String getWidgetId() {
		return widgetId;
	}

	/**
	 * @param widgetId the widgetId to set
	 */
	private void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * @return the widgetOptionId
	 */
	private String getWidgetOptionId() {
		return widgetOptionId;
	}

	/**
	 * @param widgetOptionId the widgetOptionId to set
	 */
	private void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}

	/**
	 * @return the timeStamp
	 */
	private String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	private void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the parameters
	 */
	private ArrayList<String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	private void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the userId
	 */
	private String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	private void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the nickname
	 */
	private String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	private void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the inputMechanism
	 */
	private String getInputMechanism() {
		return inputMechanism;
	}

	/**
	 * @param inputMechanism the inputMechanism to set
	 */
	private void setInputMechanism(String inputMechanism) {
		this.inputMechanism = inputMechanism;
	}

	/**
	 * @return the age
	 */
	private int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	private void setAge(int age) {
		this.age = age;
	}
}
