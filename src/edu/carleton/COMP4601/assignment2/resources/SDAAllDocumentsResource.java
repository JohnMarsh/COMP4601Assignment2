package edu.carleton.COMP4601.assignment2.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;

@Path("/sda/documents")
public class SDAAllDocumentsResource extends AbstractA2Resource {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String documentsHtml(@Context HttpServletResponse servletResponse) throws IOException {
		String noDocumentsHtml = "No documents found.";
		List<String> namesOfAllDocuments;
		try {
			namesOfAllDocuments = getService().getNamesOfAllDocuments();
		} catch (Exception e) {
			e.printStackTrace();
			return noDocumentsHtml;
		}
		if(namesOfAllDocuments.size() == 0)
			return noDocumentsHtml;
		
		String html = "<html><body>";
		for(String s : namesOfAllDocuments) {
			html += s + "<br>";
		}
		html+= "</body></html>";
		return html;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public DocumentCollection documentXML(@Context HttpServletResponse servletResponse) throws IOException {
		try {
			return getService().getAllDocuments();
		} catch (Exception e) {
			e.printStackTrace();
			return new DocumentCollection();
		}
	}
}
