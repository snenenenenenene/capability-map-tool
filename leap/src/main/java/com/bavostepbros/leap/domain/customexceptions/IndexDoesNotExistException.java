package com.bavostepbros.leap.domain.customexceptions;

public class IndexDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IndexDoesNotExistException(String message) {
		super(message);
	}

}
