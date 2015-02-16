package edu.carleton.COMP4601.assignment2.searcher;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.assignment2.indexer.Indexer;

public class Searcher {
	
	
	private MongoClient client;
	private DB db;
	private DBCollection docCollection;
	
	public static void main(String args[]){
		Searcher searcher = new Searcher();
		System.out.println(searcher.query("computer"));
	}
	
	

	/**
	 *  DUN USE IT LIKE IT IS NOW
	 */
	@Deprecated()
	public Searcher(){
		try {
			client = new MongoClient("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("crawler");
		docCollection = db.getCollection("documents");
	}

	public ArrayList<DBObject> query(String searchString) {
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory
					.open(new File(Indexer.INDEX_DIR)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser("CONTENTS", analyzer);
			Query q = parser.parse(searchString);
			TopDocs results = searcher.search(q, 1000);
			ScoreDoc[] hits = results.scoreDocs;
			ArrayList<DBObject> docs =  getDocs(hits, searcher);
			reader.close();
			return docs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<DBObject> getDocs(ScoreDoc[] hits,IndexSearcher searcher) {
		ArrayList<DBObject> docs = new ArrayList<DBObject>();
		for (ScoreDoc hit : hits) {
			Document indexDoc = null;
			try {
				indexDoc = searcher.doc(hit.doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String id = indexDoc.get("DOC_ID");
			if (id != null) {
				BasicDBObject o = new BasicDBObject();
				o.put("id", new Integer(id));
				DBObject d = docCollection.findOne(o);
				if (d != null)
					docs.add(d);
			}
		}
		return docs;
	}

}
