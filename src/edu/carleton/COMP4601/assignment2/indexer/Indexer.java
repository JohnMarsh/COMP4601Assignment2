package edu.carleton.COMP4601.assignment2.indexer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;
import edu.carleton.COMP4601.assignment2.service.IA2DocumentService;

import org.apache.lucene.document.Field;

public class Indexer {
	private IA2DocumentService service;
	private IndexWriter writer;
	public static final String INDEX_DIR = "/data/index";
	public static final String COLUMN_DOCID = "DocID";
	public static final String COLUMN_DATE = "Date";
	public static final String COLUMN_URL = "URL";
	public static final String COLUMN_CONTENT = "Content";
	public static final String COLUMN_CONTENTTYPE = "Content-Type";
	public static final String COLUMN_TITLE = "Title";
	public static final String COLUMN_TAGS = "Tags";
	public static final String SCORE = "Title";
	
	public static void main(String args[]){
		Indexer indexer = new Indexer();
		indexer.indexDocuments();
	}
	
	public Indexer(){
		FSDirectory dir = null;
		try {
			dir = FSDirectory.open(new File(INDEX_DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		writer = null;
		try {
			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setService(new A2DocumentServiceImpl());
	}

	public void indexDocuments() {
		indexDocuments(false);
	}

	public void indexDocumentsWithBoost() {
		indexDocuments(true);
	}
	
	private void indexDocuments(boolean boost) {
		List<DBDocument> allDBDocuments = null;
		try {
			allDBDocuments = getService().getAllDBDocuments();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for(DBDocument doc : allDBDocuments){
			try {
				indexADocument(doc, boost);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void indexASingleDocument(DBDocument doc) {
		try {
			indexADocument(doc, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void indexADocument(DBDocument dbDoc, boolean boost) throws IOException {
		Document doc = new Document();
		System.out.println("Indexing document with url: "+dbDoc.getUrl());
		IntField docId = new IntField(COLUMN_DOCID, dbDoc.getId(), Field.Store.YES);
		LongField date = new LongField(COLUMN_DATE, dbDoc.getCrawlTime(), Field.Store.YES);
		TextField url = new TextField(COLUMN_URL, dbDoc.getUrl(), Field.Store.YES);
		TextField contents = new TextField(COLUMN_CONTENT, dbDoc.getContent(), Field.Store.YES);
		TextField type = new TextField(COLUMN_CONTENTTYPE, dbDoc.getMdContentType(), Field.Store.YES);
		TextField title = new TextField(COLUMN_TITLE, dbDoc.getMdTitle(), Field.Store.YES);
		
		String tagString = "";	
		if(dbDoc.getTags() != null){
			tagString = dbDoc.getTags().toString();
		}
		
		TextField tags = new TextField(COLUMN_TITLE, tagString, Field.Store.YES);
		tags.setBoost(2.0f);
		
		if( boost ) {
			float score = dbDoc.getScore().floatValue();
			docId.setBoost(score);
			date.setBoost(score);
			url.setBoost(score);
			contents.setBoost(score);
			type.setBoost(score);
			title.setBoost(score);
			tags.setBoost(score);
		}
		
		doc.add(docId);
		doc.add(date);
		doc.add(url);
		doc.add(contents);
		doc.add(type);
		doc.add(title);
		doc.add(tags);
		writer.addDocument(doc);
	}
	
	public void deleteAIndexedDocument(int id) {
		Query idQuery = null;
		idQuery = NumericRangeQuery.newIntRange(Indexer.COLUMN_DOCID, id, id, true, true);
		try {
			writer.deleteDocuments(idQuery);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IA2DocumentService getService() {
		return service;
	}

	public void setService(IA2DocumentService service) {
		this.service = service;
	}
}
