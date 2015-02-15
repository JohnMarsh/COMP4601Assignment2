package edu.carleton.COMP4601.assignment2.exceptions;

public class DocumentDoesNotExistException extends SDAException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DocumentDoesNotExistException() {
		
	}
	
	@Override
	public String getMessage()  {
		return "Document does not exist";
	}

}
