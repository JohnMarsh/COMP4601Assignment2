package edu.carleton.COMP4601.a1.resources;

import edu.carleton.COMP4601.a1.service.DocumentServiceImpl;
import edu.carleton.COMP4601.a1.service.IDocumentService;

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
}
