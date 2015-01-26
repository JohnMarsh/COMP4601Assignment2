package edu.carleton.COMP4601.a1.resources;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.a1.dao.Document;
import edu.carleton.COMP4601.a1.dao.DocumentCollection;

/**
 * API paths for searching for documents
 * @author devinlynch
 *
 */
@Path("/sda/search")
public class SDASearchResource extends AbstractSDAResource {
	/**
	 * Searches for documents containing any of the given tags in the URL in the format tag1:tag2:tag3 etc. and returns back XML
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public DocumentCollection searchDocumentsByTagsXML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws IOException {
		try {
			return getService().getDocumentsWithTags(Arrays.asList(tags.split(":")));
		} catch (Exception e) {
			e.printStackTrace();
			servletResponse.sendError(204);
			return null;
		}
	}
	
	/**
	 * Searches for documents containing any of the given tags in the URL in the format tag1:tag2:tag3 etc. and returns back HTML
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/{tags}")
	@Produces(MediaType.TEXT_HTML)
	public String searchDocumentsByTagsHTML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws IOException {
		String noDocsResponse = "No documents found.";
		DocumentCollection collection;
		try {
			collection = getService().getDocumentsWithTags(Arrays.asList(tags.split(":")));
		} catch (Exception e) {
			e.printStackTrace();
			servletResponse.sendError(204);
			return noDocsResponse;
		}
		
		if(collection.getDocuments().isEmpty())
			return noDocsResponse;
		
		String html = "<html><body>";
		
		for(Document doc : collection.getDocuments()) {
			html += getHtmlForSingleDocument(doc);
		}
		
		html += "</body></html>";
		return html;
	}
	
}
