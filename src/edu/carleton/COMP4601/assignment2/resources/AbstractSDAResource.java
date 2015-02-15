package edu.carleton.COMP4601.assignment2.resources;

import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.service.DocumentServiceImpl;
import edu.carleton.COMP4601.assignment2.service.IDocumentService;

public class AbstractSDAResource {
	private IDocumentService service;

	public AbstractSDAResource() {
		service = new DocumentServiceImpl();
	}
	
	public IDocumentService getService() {
		return service;
	}

	public void setService(IDocumentService service) {
		this.service = service;
	}
	
	public String getHtmlForSingleDocument(Document document) {
		return "<b>ID:</b> "+document.getId()+"<br>"
				+"<b>Text:</b> "+document.getText()+"<br>"
				+"<b>Name:</b> "+document.getName()+"<br>"
				+"<b>Links:</b> "+document.getLinks()+"<br>"
				+"<b>Tags:</b> "+document.getTags()+"<br>"
				+"--------------------------<br>";
	}
}
