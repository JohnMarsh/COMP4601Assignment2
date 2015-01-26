package edu.carleton.COMP4601.a1.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.a1.dao.Document;
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
			throw new NoIdException();
		
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
	public int deleteDocumentsWithTags(List<String> tags) {
		String tag = tags.get(0);
		
		DBObject o = new BasicDBObject("$eq", tag);
		
		DBObject fields = new BasicDBObject();
		fields.put("$elemMatch", o);
		
		DBObject query = new BasicDBObject(Document.COLUMN_TAGS, fields);
		DBCursor find = getDocumentsCollection().find(query);
		
		List<Document> docs = new ArrayList<Document>();
		while(find.hasNext()) {
			DBObject next = find.next();
			docs.add(new Document(next.toMap()));
			System.out.println(new Document(next.toMap()).getId());
		}
		
		
		return 0;
	}
	
	public static void main(String[] args) {
		DocumentServiceImpl documentServiceImpl = new DocumentServiceImpl();
		documentServiceImpl.deleteDocumentsWithTags(Arrays.asList("foo"));
	}

}
