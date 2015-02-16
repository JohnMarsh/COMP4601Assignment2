package edu.carleton.COMP4601.assignment2.service;

import java.util.List;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;

public interface IA2DocumentService {
	public void saveDocument(DBDocument dbDoc);
	public DBDocument getDBDocument(String url);
	public void deleteDBDocument(int docId);
	public void saveGraph(CrawlerGraph graph);
	public CrawlerGraph loadGraph();
	public List<DBDocument> getAllDBDocuments() throws Exception;
	public void calculateAndSavePageRankScores();
}
