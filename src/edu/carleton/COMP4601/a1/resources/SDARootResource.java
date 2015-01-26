package edu.carleton.COMP4601.a1.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import edu.carleton.COMP4601.a1.dao.Document;
import edu.carleton.COMP4601.a1.service.IDocumentService;

/**
 * The root resource for the REST api
 * @author devinlynch
 *
 */
@Path("/sda")
public class SDARootResource extends AbstractSDAResource {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String index(@Context HttpServletResponse servletResponse) throws IOException {
		return "COMP4601 Searchable Document Archive";
	}
	
	/**
	 * End point for saving a new document
	 * @param multivaluedMap
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveNewDocument(MultivaluedMap<String,String> multivaluedMap,
			@Context HttpServletResponse servletResponse) {
		Document doc = new Document(multivaluedMap);
		try {
			getService().saveDocument(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(204).build();
		}

		return Response.status(200).build();
	}
	
	

}
