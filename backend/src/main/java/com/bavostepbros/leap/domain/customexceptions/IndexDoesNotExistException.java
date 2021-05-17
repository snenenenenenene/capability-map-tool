package com.bavostepbros.leap.domain.customexceptions;

/**
*
* @author Bavo Van Meel
*
*/
public class IndexDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IndexDoesNotExistException(String message) {
		super(message);
	}

}
