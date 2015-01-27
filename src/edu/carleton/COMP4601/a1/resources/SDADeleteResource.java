package edu.carleton.COMP4601.a1.resources;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/sda/delete")
public class SDADeleteResource extends AbstractSDAResource {
	
	/**
	 * Given tags in the format "tag1:tag2:tag3...", any documents found with any of the
	 * given tags will be deleted.
	 * @param tags
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("{tags}")
	public Response deleteByTags(@QueryParam("tags") String tags,
			@Context HttpServletResponse servletResponse) throws Exception {
		
		int numDeleted = 0;
		numDeleted = getService().deleteDocumentsWithTags(Arrays.asList(tags.split(":")));
		
		if(numDeleted == 0)
			return Response.status(204).build();
		
		return Response.status(200).build();
	}
}
