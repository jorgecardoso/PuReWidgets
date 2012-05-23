/**
 * 
 */
package org.purewidgets.shared.events;
import org.purewidgets.client.widgets.GuiWidget;
import org.purewidgets.shared.widgetmanager.WidgetOption;
import org.purewidgets.shared.widgets.Widget;



/**
 * Represents the event that is sent to applications.
 * 
 * @author Jorge C. S. Cardoso
 */
public class ActionEvent<T> {
	private String persona;
	private T source;
	private WidgetOption optionId;
	private Object param;
	
	/**
	 * Creates an ActionEvent object without an widget specific object parameter.
	 * @see ActionEvent#ActionEvent(GuiWidgetInterface, String, Object)
	 * 
	 * @param source The object (Widget object) that fired the action event.
	 * @param optionId The option id of the widget that this event refers to.
	 */
	public ActionEvent(T source, WidgetOption optionId) {
		this(source, optionId, null);
		
	}
	
	/**
	 * Creates an ActionEvent object.
	 * 
	 * @param source The object (Widget object) that fired the action event.
	 * @param optionId The option id of the widget that this event refers to.
	 * @param param Optional widget specific object.
	 */
	public ActionEvent(T source, InputEvent ie, Object param) {
		this(
				null != ie? ie.getPersona() : null, 
				source, 
				null != ie ? ie.getWidgetOption() : null, 
						param
						);
		
	}
	
	/**
	 * Creates an ActionEvent object.
	 * 
	 * @param source The object (Widget object) that fired the action event.
	 * @param optionId The option id of the widget that this event refers to.
	 * @param param Optional widget specific object.
	 */
	public ActionEvent(T source, WidgetOption optionId, Object param) {
		this(null, source, optionId, param);
	}
	
	/**
	 * Creates an ActionEvent object.
	 * @param who The persona that generated the event (indirectly via the input)
	 * @param source The object (Widget object) that fired the action event.
	 * @param optionId The option id of the widget that this event refers to.
	 * @param param Optional widget specific object.
	 */
	public ActionEvent(String who, T source, WidgetOption optionId, Object param) {
		this.setPersona(who);
		this.source = source;
		this.optionId = optionId;
		this.setParam(param);
	}
	
	

	
	/**
	 * 
	 * @return The Widget object that fired this event.
	 */
	public T getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return The option id that this event refers to.
	 */
	public WidgetOption getSelection() {
		return optionId;
	}

	/**
	 * @param param the param to set
	 */
	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * @return the param
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * @param persona the persona that was responsible for the event
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}

	/**
	 * @return the identity
	 */
	public String getPersona() {
		return persona;
	}
	
	/**
	 * Creates a string representation of this ActionEvent for debug purposes.
	 * 
	 * @return a string representation of this object.
	 */
	public String toDebugString() {
		String m = "";
		if (this.source instanceof Widget) {
			m = ((Widget)this.source).toDebugString();
		}else if (this.source instanceof GuiWidget) {
			m = ((GuiWidget)this.source).toDebugString();
		}
		
		return "ActionEvent("  + m + ")";
	}
}
