package com.bavostepbros.leap.domain.customexceptions;

public class ForeignKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ForeignKeyException(String message) {
		super(message);
	}

}
