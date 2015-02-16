package edu.carleton.COMP4601.assignment2.resources;

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
}
