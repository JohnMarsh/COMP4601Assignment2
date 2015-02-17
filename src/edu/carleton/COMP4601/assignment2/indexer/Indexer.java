package edu.carleton.COMP4601.assignment2.indexer;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;

import org.apache.lucene.document.Field;

public class Indexer {
	
	private IndexWriter writer;
	private MongoClient client;
	private DB db;
	private DBCollection docCollection;
	

	public static final String INDEX_DIR = "/data/index";
	
	public static void main(String args[]){
		Indexer indexer = new Indexer();
		indexer.indexDocuments();
	}
	
	public Indexer(){
		FSDirectory dir = null;
		try {
			dir = FSDirectory.open(new File(INDEX_DIR));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		writer = null;
		try {
			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			client = new MongoClient("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("COMP4601Assignment2");
		docCollection = db.getCollection("crawlerDocuments");
		
	}

	public void indexDocuments() {
		DBCursor cursor = docCollection.find();
		while(cursor.hasNext()){
			try {
				indexADocument((DBDocument)cursor.next());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void indexDocumentsWithBoost() {
		DBCursor cursor = docCollection.find();
		while(cursor.hasNext()){
			try {
				indexADocumentWithBoost((DBDocument)cursor.next());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void indexADocumentWithBoost(DBDocument dbDoc) throws IOException {
		Document doc = new Document();
		System.out.println("Indexing document with url: "+dbDoc.getUrl());
		//.setBoost(dbDoc.getScore().floatValue())
		IntField docId = new IntField("docID", dbDoc.getId(), Field.Store.YES);
		LongField date = new LongField("Date", dbDoc.getCrawlTime(), Field.Store.YES);
		TextField url = new TextField("URL", dbDoc.getUrl(), Field.Store.YES);
		TextField contents = new TextField("contents", dbDoc.getContent(), Field.Store.YES);
		TextField type = new TextField("Content-Type", dbDoc.getMdContentType(), Field.Store.YES);
		TextField title = new TextField("Title", dbDoc.getMdTitle(), Field.Store.YES);
		
		float score = dbDoc.getScore().floatValue();
		
		docId.setBoost(score);
		date.setBoost(score);
		url.setBoost(score);
		contents.setBoost(score);
		type.setBoost(score);
		title.setBoost(score);
		
		doc.add(docId);
		doc.add(date);
		doc.add(url);
		doc.add(contents);
		doc.add(type);
		doc.add(title);
		writer.addDocument(doc);
	}

	private void indexADocument(DBDocument dbDoc) throws IOException {
		Document doc = new Document();
		System.out.println("Indexing document with url: "+dbDoc.getUrl());
		doc.add(new IntField("docID", dbDoc.getId(), Field.Store.YES));
		doc.add(new LongField("Date", dbDoc.getCrawlTime(), Field.Store.YES));
		doc.add(new TextField("URL", dbDoc.getUrl(), Field.Store.YES));
		doc.add(new TextField("contents", dbDoc.getContent(), Field.Store.YES));
		doc.add(new TextField("Content-Type", dbDoc.getMdContentType(), Field.Store.YES));
		doc.add(new TextField("Title", dbDoc.getMdTitle(), Field.Store.YES));
		writer.addDocument(doc);
	}
}
