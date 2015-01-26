package edu.carleton.COMP4601.a1.service;

import edu.carleton.COMP4601.a1.dao.Document;

public interface IDocumentService {
	public void saveDocument(Document document) throws Exception;
	public void deleteDocument(int id) throws Exception;
}
