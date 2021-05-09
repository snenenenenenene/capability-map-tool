package com.bavostepbros.leap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnvironmentException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StatusException;
import com.bavostepbros.leap.domain.customexceptions.StrategyException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class); 
	
	@ExceptionHandler(InvalidInputException.class)
	protected ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
		logger.error(String.format("InvalidInputException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IndexDoesNotExistException.class)
	protected ResponseEntity<String> handleIndexDoesNotExistException(IndexDoesNotExistException exception) {
		logger.error(String.format("IndexDoesNotExistException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DuplicateValueException.class)
	protected ResponseEntity<String> handleDuplicateValueException(DuplicateValueException exception) {
		logger.error(String.format("DuplicateValueException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ForeignKeyException.class)
	protected ResponseEntity<String> handleForeignKeyException(ForeignKeyException exception) {
		logger.error(String.format("ForeignKeyException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EnvironmentException.class)
	protected ResponseEntity<String> handleEnvironmentException(EnvironmentException exception) {
		logger.error(String.format("EnvironmentException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(StatusException.class)
	protected ResponseEntity<String> handleStatusException(StatusException exception) {
		logger.error(String.format("EnvironmentException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(StrategyException.class)
	protected ResponseEntity<String> handleStrategyException(StrategyException exception) {
		logger.error(String.format("StrategyException with message '%s' was thrown.", exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
