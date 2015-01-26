package edu.carleton.COMP4601.a1.service;

import java.util.List;

import edu.carleton.COMP4601.a1.dao.Document;

/**
 * The service that handles persistence of documents
 * @author devinlynch
 *
 */
public interface IDocumentService {
	
	/**
	 * Saves a new document or updates an existing one if one exists with that documents Id
	 * @param document
	 * @throws Exception
	 */
	public void saveDocument(Document document) throws Exception;
	
	/**
	 * Deletes a document if it exists
	 * @param id
	 * @throws Exception
	 */
	public void deleteDocument(int id) throws Exception;
	
	/**
	 * Gets a document by its unique id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Document getDocumentById(int id) throws Exception;
	
	/**
	 * Deletes any documents who have any of the given tags
	 * @param tags
	 * @return number of documents deleted
	 */
	public int deleteDocumentsWithTags(List<String> tags);
}
