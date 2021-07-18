package com.tweetapp.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException() {
	        super();
	    }

	    public ResourceAlreadyExistsException(String message) {
	    	  super(message);
	    }

	    public ResourceAlreadyExistsException(String message, Throwable cause) {
	        super(message, cause);
	    }

}
