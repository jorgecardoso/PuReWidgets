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

	public HttpServerException() {
		super();
	}
	
	public HttpServerException(String message) {
		super(message);
	}
}
