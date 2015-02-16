package edu.carleton.COMP4601.assignment2.service;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;

public interface IA2DocumentService {
	public void saveDocument(DBDocument dbDoc);
	public DBDocument getDBDocument(String url);
	public void deleteDBDocument(int docId);
	public void saveGraph(CrawlerGraph graph);
	public CrawlerGraph loadGraph();
	public void calculateAndSavePageRankScores();
}
