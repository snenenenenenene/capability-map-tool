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
import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.EnvironmentException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StatusException;
import com.bavostepbros.leap.domain.customexceptions.StrategyException;
import com.bavostepbros.leap.domain.customexceptions.TechnologyException;

/**
*
* @author Bavo Van Meel
*
*/
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class); 
	
	@ExceptionHandler({
		InvalidInputException.class, 
		IndexDoesNotExistException.class,
		DuplicateValueException.class, 
		ForeignKeyException.class, 
		EnumException.class,
		EnvironmentException.class, 
		StatusException.class, 
		StrategyException.class,
		TechnologyException.class})
	protected ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
		logger.error(String.format("%s with message '%s' was thrown.", 
				exception.getClass().getSimpleName(), exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
