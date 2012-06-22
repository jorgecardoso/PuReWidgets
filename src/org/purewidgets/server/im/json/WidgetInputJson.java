package org.purewidgets.server.im.json;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.purewidgets.shared.im.WidgetInput;

/**
 * 
 * The JSON data transfer object for WidgetInput objects.
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
		widgetInputJson.setWidgetOptionId(widgetInput.getWidgetOptionId());
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
		widgetInput.setWidgetOptionId(this.widgetOptionId);
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
	 * @param placeId the placeId to set
	 */
	private void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * @param applicationId the applicationId to set
	 */
	private void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @param referenceCode the referenceCode to set
	 */
	private void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}


	/**
	 * @param widgetId the widgetId to set
	 */
	private void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}


	/**
	 * @param widgetOptionId the widgetOptionId to set
	 */
	private void setWidgetOptionId(String widgetOptionId) {
		this.widgetOptionId = widgetOptionId;
	}



	/**
	 * @param timeStamp the timeStamp to set
	 */
	private void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	/**
	 * @param parameters the parameters to set
	 */
	private void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}



	/**
	 * @param userId the userId to set
	 */
	private void setUserId(String userId) {
		this.userId = userId;
	}



	/**
	 * @param nickname the nickname to set
	 */
	private void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/**
	 * @param inputMechanism the inputMechanism to set
	 */
	private void setInputMechanism(String inputMechanism) {
		this.inputMechanism = inputMechanism;
	}


	/**
	 * @param age the age to set
	 */
	private void setAge(int age) {
		this.age = age;
	}
}
