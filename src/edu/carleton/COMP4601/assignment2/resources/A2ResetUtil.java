package edu.carleton.COMP4601.assignment2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.utility.DBUtil;

@Path("/sda/reset")
public class A2ResetUtil extends AbstractSDAResource {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String reset() {
		if (DBUtil.resetDB()) {
			return "<p>Reset Successful</p>";
		} else {
			return "<p>Reset Failed</p>";
		}
	}
	

}
