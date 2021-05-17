package com.bavostepbros.leap.domain.customexceptions;

/**
*
* @author Bavo Van MeeL
*
*/
public class DuplicateValueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateValueException(String message) {
		super(message);
	}

}
