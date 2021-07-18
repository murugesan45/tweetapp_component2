package com.tweetapp.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mongodb.MongoTimeoutException;

@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	//error 401 and 403 are handled in security package 
	
	@ResponseStatus(HttpStatus.NOT_FOUND) // 404
	@ExceptionHandler(TweetNotFoundException.class)
	public void handleNotFound(TweetNotFoundException ex) {
		LOGGER.error("Error!! Requested tweet not found " + ex);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND) // 404
	@ExceptionHandler(ResourceNotFoundException.class)
	public void handleNotFound(ResourceNotFoundException ex) {
		LOGGER.error("Error!! Requested resource not found " + ex);
	}
	

	@ResponseStatus(HttpStatus.CONFLICT) // 409
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public void handleAlreadyExists(ResourceAlreadyExistsException ex) {
		LOGGER.error("Error!! Requested resourse already Exists " + ex);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void handleBadRequestError(MethodArgumentNotValidException ex) {
		LOGGER.error("Error!! The validation failed " + ex);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(Exception.class)
	public void handleGeneralError(Exception ex) {
		LOGGER.error("Error!! An error occurred processing request " + ex);
	}
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
	@ExceptionHandler(MongoTimeoutException.class)
	public void handleMongoDbConnection(Exception ex) {
		LOGGER.error("Error!! Mongodb is not connected " + ex);
	}
}
