package edu.carleton.COMP4601.a1.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.a1.dao.Document;
import edu.carleton.COMP4601.a1.dao.DocumentCollection;
import edu.carleton.COMP4601.a1.exceptions.NoIdException;

/**
 * MongoDB implementation of the document service that persists documents to MongoDB
 * @author devinlynch
 *
 */
public class DocumentServiceImpl implements IDocumentService {
	private static MongoClient  _mongoClient;
	private static final String DOCUMENTS = "DOCUMENTS";
	
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
		return getMongoClient().getDB("COMP4601Assignment1");
	}
	
	private DBCollection getDocumentsCollection() {
		return getDatabase().getCollection(DOCUMENTS);
	}
	
	@Override
	public void saveDocument(Document document) throws Exception {
		if(document.getId() == null)
			document.setId(createNewDocumentId());
		
		Document existingDoc = getDocumentById(document.getId());
		if(existingDoc != null) {
			BasicDBObject o = new BasicDBObject();
			o.put(Document.COLUMN_ID, new Integer(existingDoc.getId()));
			getDocumentsCollection().update(o, document.toDBObject());
		} else {
			getDocumentsCollection().insert(document.toDBObject());
		}
	}

	@Override
	public void deleteDocument(int id) throws Exception {
		BasicDBObject o = new BasicDBObject();
		o.put(Document.COLUMN_ID, new Integer(id));
		getDocumentsCollection().remove(o);
	}

	@Override
	public Document getDocumentById(int id) throws Exception {
		BasicDBObject o = new BasicDBObject();
		o.put(Document.COLUMN_ID, new Integer(id));
		DBObject findOne = getDocumentsCollection().findOne(o);
		if(findOne == null)
			return null;
		return new Document(findOne.toMap());
	}

	@Override
	public int deleteDocumentsWithTags(List<String> tags) throws Exception {
		List<Document> documentsWithTags = getDocumentsWithTags(tags).getDocuments();
		
		for(Document d : documentsWithTags) {
			deleteDocument(d.getId());
		}
		
		return documentsWithTags.size();
	}

	@Override
	public DocumentCollection getDocumentsWithTags(List<String> tags) throws Exception {
		DBObject o = new BasicDBObject("$in", tags);
		
		DBObject fields = new BasicDBObject();
		fields.put("$elemMatch", o);
		
		DBObject query = new BasicDBObject(Document.COLUMN_TAGS, fields);
		DBCursor find = getDocumentsCollection().find(query);
		
		List<Document> docs = new ArrayList<Document>();
		while(find.hasNext()) {
			DBObject next = find.next();
			docs.add(new Document(next.toMap()));
		}
		
		DocumentCollection coll = new DocumentCollection();
		coll.setDocuments(docs);
		return coll;
	}

	@Override
	public List<String> getNamesOfAllDocuments() throws Exception {
		List<String> l = new ArrayList<String>();
		
		DBCursor find = getDocumentsCollection().find();
		while(find.hasNext()) {
			Document d = new Document(find.next().toMap());
			l.add(d.getName());
		}
		
		return l;
	}
	
	public int createNewDocumentId() {
		DBCursor limit = getDocumentsCollection().find().sort(new BasicDBObject(Document.COLUMN_ID, -1)).limit(1);
		DBObject o = null;
		if(limit.hasNext())
			o = limit.next();
		if(o == null)
			return 1;
		
		Document d = new Document(o.toMap());
		
		return d.getId() + 1;
	}
	
	public static void main(String[] args) {
		System.out.println(new DocumentServiceImpl().createNewDocumentId());
	}
}
