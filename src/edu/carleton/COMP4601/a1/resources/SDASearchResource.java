package edu.carleton.COMP4601.a1.resources;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/sda/delete")
public class SDASearchResource extends AbstractSDAResource {
	
	@GET
	@Path("{tags}")
	public Response deleteByTags(@QueryParam("tags") String tags,
			@Context HttpServletResponse servletResponse) {
		
		int numDeleted = getService().deleteDocumentsWithTags(Arrays.asList(tags.split(":")));
		if(numDeleted == 0)
			return Response.status(204).build();
		
		return Response.status(200).build();
	}
}
