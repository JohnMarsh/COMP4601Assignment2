package edu.carleton.COMP4601.a1.resources;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import edu.carleton.COMP4601.a1.dao.Document;


/**
 * Handles interactions with the REST api for a specific document
 * @author devinlynch
 *
 */
@Path("/sda/{id}")
public class SDADocumentResource extends AbstractSDAResource {
	@PathParam("id")
	String id;
	
	/**
	 * End point for updating an existing document
	 * @param multivaluedMap
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@POST
	@PUT
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveNewDocument(MultivaluedMap<String,String> multivaluedMap,
			@Context HttpServletResponse servletResponse) {
		Document doc = new Document(multivaluedMap);
		doc.setId(Integer.parseInt(id));
		
		try {
			getService().saveDocument(doc);
		} catch (Exception e) {
			return Response.status(204).build();
		}

		return Response.status(200).build();
	}
	
	/**
	 * End point for deleting an existing document
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public Response saveNewDocument(@Context HttpServletResponse servletResponse) {
		try {
			getService().deleteDocument(Integer.parseInt(id));
		} catch (Exception e) {
			return Response.status(204).build();
		}

		return Response.status(200).build();
	}
}
