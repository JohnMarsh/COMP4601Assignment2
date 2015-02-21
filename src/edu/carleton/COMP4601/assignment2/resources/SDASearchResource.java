package edu.carleton.COMP4601.assignment2.resources;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.utility.SearchResult;
import edu.carleton.COMP4601.utility.SearchServiceManager;

/**
 * API paths for searching for documents
 * @author devinlynch
 *
 */
@Path("/sda/search")
public class SDASearchResource extends AbstractA2Resource {
	private SearchServiceManager manager;
	
	public SDASearchResource() {
		manager = SearchServiceManager.getInstance();
	}
	
	
	@GET
	@Path("/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public DocumentCollection searchDocumentsByTagsXML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws Exception {
		SearchResult search = manager.query(tags);
		search.await(10, TimeUnit.SECONDS);
		DocumentCollection col = new DocumentCollection();
		col.getDocuments().addAll(search.getDocs());
		return col;
	}
	
	@GET
	@Path("/{tags}")
	@Produces(MediaType.TEXT_HTML)
	public String searchDocumentsByTagsHTML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws IOException, InterruptedException {
		String noDocsResponse = "No documents found.";
		SearchResult search = manager.query(tags);
		search.await(10, TimeUnit.SECONDS);
		DocumentCollection col = new DocumentCollection();
		col.getDocuments().addAll(search.getDocs());
		if(col.getDocuments().isEmpty())
			return noDocsResponse;
		
		String html = "<html><body>";
		
		for(Document doc : col.getDocuments()) {
			html += getHtmlForSingleDocument(doc);
		}
		
		html += "</body></html>";
		return html;
	}
	
}
