package edu.carleton.COMP4601.assignment2.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.graph.Multigraph;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.sleepycat.je.tree.DIN;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.assignment2.graph.CrawlerEdge;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.graph.CrawlerVertex;
import edu.carleton.COMP4601.assignment2.indexer.Indexer;
import edu.carleton.COMP4601.assignment2.searcher.Searcher;
import edu.carleton.COMP4601.utility.Marshaller;

public class A2DocumentServiceImpl implements IA2DocumentService {
	private static MongoClient _mongoClient;

	protected synchronized static MongoClient getMongoClient() {
		if (_mongoClient == null) {
			try {
				_mongoClient = new MongoClient("localhost");
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}
		return _mongoClient;
	}

	private DB getDatabase() {
		return getMongoClient().getDB("COMP4601Assignment2");
	}

	private DBCollection getDocumentsCollection() {
		return getDatabase().getCollection("crawlerDocuments");
	}

	private DBCollection getGraphCollection() {
		return getDatabase().getCollection("graphs");
	}
	
	@Override
	public void saveDocumentAndIndex(DBDocument dbDoc) {
		DBDocument existingDoc = null;
		try {
			if(dbDoc.getId() != null){
				existingDoc = getDocumentById(dbDoc.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (existingDoc != null) {
			if (dbDoc.getId() != null) {
				BasicDBObject q = new BasicDBObject();
				q.put(DBDocument.ID, new Integer(dbDoc.getId()));
				getDocumentsCollection().update(q, dbDoc);
				Indexer indexer = new Indexer();
				indexer.deleteAIndexedDocument(dbDoc.getId());
				indexer = new Indexer();
				indexer.indexASingleDocument(dbDoc);
				return;
			} else {
				deleteDBDocument(existingDoc.getId());
			}
			
		}
		Indexer indexer = new Indexer();
		indexer.indexASingleDocument(dbDoc);
		getDocumentsCollection().insert(dbDoc);
	}
	
	@Override
	public void saveDocument(DBDocument dbDoc) {
		DBDocument existingDoc = null;
		try {
			if(dbDoc.getId() != null){
				existingDoc = getDocumentById(dbDoc.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (existingDoc != null) {
			if (dbDoc.getId() != null) {
				BasicDBObject q = new BasicDBObject();
				q.put(DBDocument.ID, new Integer(dbDoc.getId()));
				getDocumentsCollection().update(q, dbDoc);
				return;
			} else {
				deleteDBDocument(existingDoc.getId());
			}
			
		}
		getDocumentsCollection().insert(dbDoc);
	}

	@Override
	public DBDocument getDBDocument(String url) {
		BasicDBObject o = new BasicDBObject();
		o.put(DBDocument.URL, url);
		DBObject findOne = getDocumentsCollection().findOne(o);
		if (findOne == null)
			return null;
		return new DBDocument((BasicDBObject) findOne);

	}

	@Override
	public void deleteDBDocument(int docId) {
		Indexer indexer = new Indexer();
		indexer.deleteAIndexedDocument(docId);

		BasicDBObject o = new BasicDBObject();
		o.put(DBDocument.ID, new Integer(docId));
		getDocumentsCollection().remove(o);
	}

	/*@Override
	public void saveGraph(CrawlerGraph graph) {
		BasicDBObject o = new BasicDBObject();
		getGraphCollection().remove(o);

		BasicDBObject object = new BasicDBObject("name", graph.getName());
		try {
			object.append("data", Marshaller.serializeObject(graph.getGraph()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getGraphCollection().insert(object);
	}
	*/
	
	@Override
	public void saveGraph(CrawlerGraph graph) {
		BasicDBObject o = new BasicDBObject();
		getGraphCollection().remove(o);

		BasicDBObject object = new BasicDBObject("name", graph.getName());
		object.append("data", graph.edgeSetToList());
		getGraphCollection().insert(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CrawlerGraph loadGraph() {
		BasicDBObject findOne = (BasicDBObject) getGraphCollection().findOne();
		if (findOne == null)
			return null;
		CrawlerGraph g = new CrawlerGraph(findOne.getString("name"));
		g.setGraphFromEdgeMap((List<Map<String, String>>) findOne.get("data"));
		return g;
	}

	@Override
	public List<DBDocument> getAllDBDocuments() throws Exception {
		List<DBDocument> docs = new ArrayList<DBDocument>();
		DBCursor find = getDocumentsCollection().find();
		while (find.hasNext()) {
			BasicDBObject o = (BasicDBObject) find.next();
			DBDocument d = new DBDocument(o);
			docs.add(d);
		}
		return docs;
	}

	public void calculateAndSavePageRankScores() {
		CrawlerGraph loadGraph = loadGraph();
		Map<CrawlerVertex, Double> pageRankScores = PageRankCalculator
				.getPageRankScores(loadGraph);
		for (Entry<CrawlerVertex, Double> entry : pageRankScores.entrySet()) {
			String url = entry.getKey().getUrl();
			if (url == null)
				continue;
			DBDocument dbDocument = getDBDocument(url);
			if (dbDocument == null)
				continue;
			dbDocument.setScore(entry.getValue());
			saveDocument(dbDocument);
		}
	}

	@Override
	public void reIndexWithBoost() {
		Indexer i = new Indexer();
		i.indexDocumentsWithBoost();
	}

	@Override
	public void reIndexNoBoost() {
		Indexer i = new Indexer();
		i.indexDocuments();
	}

	@Override
	public DBDocument getDocumentById(int id) throws Exception {
		BasicDBObject o = new BasicDBObject();
		o.put(DBDocument.ID, new Integer(id));
		DBObject findOne = getDocumentsCollection().findOne(o);
		if(findOne == null)
			return null;
		return new DBDocument((BasicDBObject) findOne);
	}

	@Override
	public int deleteDocumentsWithTags(List<String> tags) throws Exception {
		List<Document> documentsWithTags = getDocumentsWithTags(tags).getDocuments();
		
		for(Document d : documentsWithTags) {
			deleteDBDocument(d.getId());
		}
		
		return documentsWithTags.size();
	}

	@Override
	public DocumentCollection getDocumentsWithTags(List<String> tags)
			throws Exception {
		DBObject o = new BasicDBObject("$in", tags);
		
		DBObject fields = new BasicDBObject();
		fields.put("$elemMatch", o);
		
		DBObject query = new BasicDBObject(DBDocument.TAGS, fields);
		DBCursor find = getDocumentsCollection().find(query);
		
		List<Document> docs = new ArrayList<Document>();
		while(find.hasNext()) {
			DBObject next = find.next();
			docs.add(new Document(new DBDocument((BasicDBObject) next)));
		}
		
		DocumentCollection coll = new DocumentCollection();
		coll.setDocuments(docs);
		return coll;
	}

	@Override
	public List<String> getNamesOfAllDocuments() throws Exception {
		List<String> l = new ArrayList<String>();
		List<Document> allDocuments = getAllDocuments().getDocuments();
		for(Document d : allDocuments) {
			l.add(d.getName());
		}
		return l;
	}

	@Override
	public DocumentCollection getAllDocuments() throws Exception {
		DocumentCollection c = new DocumentCollection();
		List<DBDocument> allDBDocuments = getAllDBDocuments();
		for(DBDocument d : allDBDocuments) {
			c.getDocuments().add(new Document(d));
		}
		
		return c;
	}

}
