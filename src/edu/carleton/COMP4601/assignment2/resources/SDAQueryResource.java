package edu.carleton.COMP4601.assignment2.resources;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.assignment2.searcher.Searcher;

@Path("/sda/query")
public class SDAQueryResource extends AbstractA2Resource {
	private Searcher searcher;
	
	public SDAQueryResource() {
		super();
		this.searcher = new Searcher();
	}
	
	@GET
	@Path("/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public DocumentCollection searchDocumentsByTagsXML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws Exception {
		DocumentCollection collection = new DocumentCollection();
		collection.setDocuments(searcher.query(tags));
		return collection;
	}
	
	@GET
	@Path("/{tags}")
	@Produces(MediaType.TEXT_HTML)
	public String searchDocumentsByTagsHTML(@PathParam("tags") String tags, @Context HttpServletResponse servletResponse) throws IOException {
		String noDocsResponse = "No documents found.";
		ArrayList<Document> docs = searcher.query(tags);
		if(docs.isEmpty())
			return noDocsResponse;
		
		String html = "<html><body>";
		
		for(Document doc : docs) {
			html += getHtmlForSingleDocument(doc);
		}
		
		html += "</body></html>";
		return html;
	}
}
