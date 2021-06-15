package com.bavostepbros.leap.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.EnvironmentException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.RelationshipException;
import com.bavostepbros.leap.domain.customexceptions.StatusException;
import com.bavostepbros.leap.domain.customexceptions.StrategyException;
import com.bavostepbros.leap.domain.customexceptions.TechnologyException;
import com.bavostepbros.leap.domain.customexceptions.ValidationErrorResponse;
import com.bavostepbros.leap.domain.customexceptions.Violation;

/**
 *
 * @author Bavo Van Meel
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ErrorHandlingController {
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class);

	
	/** 
	 * @param exception
	 * @return ResponseEntity<String>
	 */
	@ExceptionHandler({ InvalidInputException.class, IndexDoesNotExistException.class, DuplicateValueException.class,
			ForeignKeyException.class, EnumException.class, EnvironmentException.class, StatusException.class,
			StrategyException.class, TechnologyException.class, RelationshipException.class })
	protected ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
		logger.error(String.format("%s with message '%s' was thrown.", exception.getClass().getSimpleName(),
				exception.getMessage()));
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	
	/** 
	 * @param e
	 * @return ResponseEntity<ValidationErrorResponse>
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ResponseEntity<ValidationErrorResponse> onConstraintValidationException(ConstraintViolationException e) {
		ValidationErrorResponse error = new ValidationErrorResponse();
		for (ConstraintViolation violation : e.getConstraintViolations()) {
			error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
		}
		return new ResponseEntity<ValidationErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	
	/** 
	 * @param e
	 * @return ResponseEntity<ValidationErrorResponse>
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ValidationErrorResponse error = new ValidationErrorResponse();
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
		}
		return new ResponseEntity<ValidationErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
