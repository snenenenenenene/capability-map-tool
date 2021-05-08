package com.bavostepbros.leap.domain.customexceptions;

/**
*
* @author Bavo Van Meel
*
*/
public class InvalidInputException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(String message) {
		super(message);
	}
}
