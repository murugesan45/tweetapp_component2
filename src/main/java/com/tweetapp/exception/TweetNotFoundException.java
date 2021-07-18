package com.tweetapp.exception;

public class TweetNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TweetNotFoundException() {
        super();
    }

    public TweetNotFoundException(String message) {
        super(message);
    }

    public TweetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
	

}
