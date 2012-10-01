package org.purewidgets.shared.exceptions;

import java.io.Serializable;

/**
 * Represents an HTTP exception.
 * 
 * Appengine does not support javax.xml.ws.http.HTTPException (as of Jun 2012) so 
 * I had to implement my own.
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class HttpServerException extends Exception implements Serializable { 


	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new HttpServerException without a message.
	 */
	public HttpServerException() {
		super();
	}
	
	/**
	 * Creates a new HttpServerException with the specified error message.
	 * @param message The error message associated with the exception.
	 */
	public HttpServerException(String message) {
		super(message);
	}
}
