package edu.carleton.COMP4601.assignment2.service;

import java.util.List;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;

public interface IA2DocumentService {
	public void saveDocument(DBDocument dbDoc);
	public void saveDocumentAndIndex(DBDocument dbDoc);
	public DBDocument getDBDocument(String url);
	public void deleteDBDocument(int docId);
	public void saveGraph(CrawlerGraph graph);
	public CrawlerGraph loadGraph();
	public List<DBDocument> getAllDBDocuments() throws Exception;
	public void calculateAndSavePageRankScores();
	public void reIndexWithBoost();
	public void reIndexNoBoost();
	
	
	
	
	
	/** Assignment 1 **/
	
	/**
	 * Gets a document by its unique id
	 * @param id
	 * @return The document
	 * @throws Exception
	 */
	public DBDocument getDocumentById(int id) throws Exception;
	
	/**
	 * Deletes any documents who have any of the given tags
	 * @param tags
	 * @return number of documents deleted
	 */
	public int deleteDocumentsWithTags(List<String> tags) throws Exception;
	
	/**
	 * Returns any documents that contain any of the given tags
	 * @param tags
	 * @return A {@link DocumentCollection} containing all the documents
	 */
	public DocumentCollection getDocumentsWithTags(List<String> tags) throws Exception;

	public List<String> getNamesOfAllDocuments() throws Exception;
	
	public DocumentCollection getAllDocuments() throws Exception;
}
