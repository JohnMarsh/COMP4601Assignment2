package edu.carleton.COMP4601.a1.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handles returning a 204 Http Response status for any errors not caught
 * @author devinlynch
 *
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override 
    public Response toResponse(Throwable ex) {
    	ex.printStackTrace();
    	
    	String message = ex.getMessage();
    	if(! (ex instanceof SDAException)) {
    		message = "There was an error with your request.";
    	}
    	
        return Response.status(204)
        		.entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build();    
    }
}