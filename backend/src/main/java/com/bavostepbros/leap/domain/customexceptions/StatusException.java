package com.bavostepbros.leap.domain.customexceptions;

/**
*
* @author Bavo Van Meel
*
*/
public class StatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StatusException(String message) {
		super(message);
	}
}
