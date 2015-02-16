package edu.carleton.COMP4601.assignment2.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.dao.Document;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.graph.CrawlerVertex;
import edu.carleton.COMP4601.utility.Marshaller;

public class A2DocumentServiceImpl implements IA2DocumentService {
	private static MongoClient  _mongoClient;
	protected synchronized static MongoClient getMongoClient() {
		if(_mongoClient == null) {
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
	public void saveDocument(DBDocument dbDoc) {
		DBDocument existingDoc = getDBDocument(dbDoc.getUrl());
		if(existingDoc != null) {
			deleteDBDocument(existingDoc.getId());
		}
		getDocumentsCollection().insert(dbDoc);
	}

	@Override
	public DBDocument getDBDocument(String url) {
		BasicDBObject o = new BasicDBObject();
		o.put(DBDocument.URL, url);
		DBObject findOne = getDocumentsCollection().findOne(o);
		if(findOne == null)
			return null;
		return new DBDocument((BasicDBObject) findOne);

	}

	@Override
	public void deleteDBDocument(int docId) {
		BasicDBObject o = new BasicDBObject();
		o.put(DBDocument.ID, new Integer(docId));
		getDocumentsCollection().remove(o);
	}

	@Override
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

	@SuppressWarnings("unchecked")
	@Override
	public CrawlerGraph loadGraph() {
		BasicDBObject findOne = (BasicDBObject) getGraphCollection().findOne();
		if(findOne == null)
			return null;
		CrawlerGraph g = new CrawlerGraph(findOne.getString("name"));
		try {
			g.setGraph((Multigraph<CrawlerVertex, DefaultEdge>) Marshaller.deserializeObject((byte[]) findOne.get("data")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}

	@Override
	public List<DBDocument> getAllDBDocuments() throws Exception {
		List<DBDocument> docs = new ArrayList<DBDocument>();
		DBCursor find = getDocumentsCollection().find();
		while(find.hasNext()) {
			DBDocument d = (DBDocument) find.next();
			docs.add(d);
		}
		return docs;
	}

}
