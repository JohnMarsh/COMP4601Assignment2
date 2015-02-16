package edu.carleton.COMP4601.assignment2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sda/list")
public class A2ListUtil extends AbstractA2Resource {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String list() {
		return "";
	}
}
