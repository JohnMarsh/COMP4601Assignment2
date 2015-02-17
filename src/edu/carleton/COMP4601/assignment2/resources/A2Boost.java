package edu.carleton.COMP4601.assignment2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/sda/boost")
public class A2Boost extends AbstractA2Resource {
	
	
	@GET
	public void boost(){
		getService().reIndexWithBoost();
	}

}
