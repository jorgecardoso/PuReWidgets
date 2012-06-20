package org.purewidgets.shared.events;

import java.util.ArrayList;

import org.purewidgets.shared.im.WidgetOption;



/**
 * Represents an input event.
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public class InputEvent {

	
	/**
	 * The identity which generated the input.
	 */
	private String persona;
	
	
	/**
	 * The WidgetOption which was selected by the user.
	 */
	private WidgetOption widgetOption;
	
	
	/**
	 * The command issued by the user.
	 */
	private ArrayList<String> parameters;
	
	
	/**
	 * The age of the input, in milliseconds.
	 */
	private int age;
	
	/**
	 * Creates a new InputEvent with the given WidgetOption and parameters.
	 * This constructor is used when there is no persona associated (i.e., the input was
	 * generated locally at the Gui).
	 * 
	 * @param persona The identity that was responsible for generating the input.
	 * @param widgetOption The WidgetOption that was selected by the user.
	 * @param parameters The parameters the user sent in the input.
	 */	
	public InputEvent( WidgetOption widgetOption, ArrayList<String> parameters) {
		this("GuiEvent", widgetOption, parameters);
	}	
	
	
	/**
	 * Creates a new InputEvent with the given Persona, WidgetOption and parameters.
	 * 
	 * @param persona The identity that was responsible for generating the input.
	 * @param widgetOption The WidgetOption that was selected by the user.
	 * @param parameters The parameters the user sent in the input.
	 */	
	public InputEvent(String persona, WidgetOption widgetOption, ArrayList<String> parameters) {
		this.persona = persona;
		this.widgetOption = widgetOption;
		this.parameters =  parameters;
	}	
	
	
	
	/**
	 * 
	 * @return The persona associated with this InputEvent.
	 */
	public String getPersona() {
		return this.persona;
	}
	
	
	public void setPersona(String persona) {
		this.persona = persona;
	}

	
	/**
	 *  
	 * @return The parameters associated with this InputEvent.
	 */
	public ArrayList<String> getParameters() {
		return this.parameters;
	}

	
	/**
	 * Sets the parameters for this input event.
	 * 
	 * @param parameters the parameters to set
	 */
	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}
	
	
	/**
	 * @param widgetOption the WidgetOption to set
	 */
	public void setWidgetOption(WidgetOption widgetOption) {
		this.widgetOption = widgetOption;
	}

	
	/**
	 * @return the optionID
	 */
	public WidgetOption getWidgetOption() {
		return this.widgetOption;
	}


	/**
	 * 
	 * @return A string representation of this object for debuggin purposes.
	 */
	public String toDebugString() {
		StringBuilder sb = new StringBuilder();
		sb.append("InputEvent:").append("\n");
		sb.append("\t Parameters: ").append(this.parameters != null ? this.parameters.toString() : "null").append("\n");
		sb.append("\t WidgetOption: ").append(this.widgetOption != null ? this.widgetOption.toDebugString() : "null").append("\n");
		
		return sb.toString();
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
	
	
	
}
