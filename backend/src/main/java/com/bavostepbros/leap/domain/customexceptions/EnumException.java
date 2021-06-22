package com.bavostepbros.leap.domain.customexceptions;

public class EnumException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EnumException(String message) {
		super(message);
	}
}
