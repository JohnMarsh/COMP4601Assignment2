package edu.carleton.COMP4601.assignment2.resources;

import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;
import edu.carleton.COMP4601.assignment2.service.IA2DocumentService;

public class AbstractA2Resource {
	private IA2DocumentService service;
	
	public AbstractA2Resource(){
		this.service = new A2DocumentServiceImpl();
	}
	
	public IA2DocumentService getService(){
		return this.service;
	}
	
	public String getHtmlForSingleDocument(Document document) {
		return "<b>ID:</b> "+document.getId()+"<br>"
				+"<b>Text:</b> "+document.getText()+"<br>"
				+"<b>Name:</b> "+document.getName()+"<br>"
				+"<b>Links:</b> "+document.getLinks()+"<br>"
				+"<b>Tags:</b> "+document.getTags()+"<br>"
				+"<b>Score:</b> "+document.getScore()+"<br>"
				+"--------------------------<br>";
	}
}
