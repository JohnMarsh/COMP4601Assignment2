package edu.carleton.COMP4601.a1.resources;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import edu.carleton.COMP4601.a1.exceptions.DocumentDoesNotExistException;


/**
 * Handles interactions with the REST API for a specific document
 * @author devinlynch
 *
 */
@Path("/sda/{id}")
public class SDADocumentResource extends AbstractSDAResource {
	@PathParam("id")
	String id;
	
	/**
	 * Returns an XML representation of a Document
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocument(@Context HttpServletResponse servletResponse) throws Exception {
		Document d = getService().getDocumentById(Integer.parseInt(id));
		if(d == null)
			throw new DocumentDoesNotExistException();
		return d;
	}
	
	/**
	 * Returns an XML representation of a Document
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getDocumentHtml(@Context HttpServletResponse servletResponse) throws Exception {
		Document document = getService().getDocumentById(Integer.parseInt(id));
		if(document == null)
			throw new DocumentDoesNotExistException();
		return "<html>"
				+"<body>"
				+getHtmlForSingleDocument(document)
				+"<body>"
				+"</html>";
	}
	
	/**
	 * End point for updating an existing document
	 * @param multivaluedMap
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	@PUT
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveNewDocumentPut(MultivaluedMap<String,String> multivaluedMap,
			@Context HttpServletResponse servletResponse) throws Exception {
		return saveNewDocument(multivaluedMap);
	}

	/**
	 * End point for updating an existing document
	 * @param multivaluedMap
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveNewDocumentPost(MultivaluedMap<String,String> multivaluedMap,
			@Context HttpServletResponse servletResponse) throws Exception {
		return saveNewDocument(multivaluedMap);
	}
	
	private Response saveNewDocument(
			MultivaluedMap<String, String> multivaluedMap) throws Exception {
		Document doc = new Document(multivaluedMap);
		doc.setId(Integer.parseInt(id));
		getService().saveDocument(doc);
		return Response.status(200).build();
	}
	
	
	/**
	 * End point for deleting an existing document
	 * @param servletResponse
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws IOException
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public Response saveNewDocument(@Context HttpServletResponse servletResponse) throws NumberFormatException, Exception {
		getService().deleteDocument(Integer.parseInt(id));
		return Response.status(200).build();
	}
}
