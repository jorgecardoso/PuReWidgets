package org.instantplaces.purewidgets.shared.exceptions;

import java.io.Serializable;

public class InteractionManagerException extends Exception implements Serializable { 


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InteractionManagerException() {
		super();
	}
	
	public InteractionManagerException(String message) {
		super(message);
	}
}
