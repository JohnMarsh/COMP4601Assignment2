package edu.carleton.COMP4601.assignment2.searcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.indexer.Indexer;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;
import edu.carleton.COMP4601.assignment2.service.IA2DocumentService;

public class Searcher {
	private IA2DocumentService service;
	
	public Searcher(){
		service = new A2DocumentServiceImpl();
	}
	
	public ArrayList<edu.carleton.COMP4601.assignment2.dao.Document> query(String searchString) {
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory
					.open(new File(Indexer.INDEX_DIR)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			BooleanQuery booleanQuery  = new BooleanQuery();
			
			
			Query idQuery = null;
			Query contentQuery = null;
			if(getContentQueryString(searchString) != null) {
				QueryParser parser = new QueryParser(Indexer.COLUMN_CONTENT, analyzer);
				contentQuery = parser.parse(getContentQueryString(searchString));
			}
			
			if(getIdQueryString(searchString) != null) {
				int id = Integer.parseInt(getIdQueryString(searchString));
				idQuery = NumericRangeQuery.newIntRange(Indexer.COLUMN_DOCID, id, id, true, true);
			}
			
			if(idQuery == null && contentQuery == null) {
				QueryParser parser = new QueryParser(Indexer.COLUMN_CONTENT, analyzer);
				contentQuery = parser.parse(searchString);
			}
			
			if(idQuery != null)
				booleanQuery.add(idQuery, BooleanClause.Occur.MUST);
			
			if(contentQuery != null)
				booleanQuery.add(contentQuery, BooleanClause.Occur.MUST);
			
			TopDocs results = searcher.search(booleanQuery, 1000);
			ScoreDoc[] hits = results.scoreDocs;
			ArrayList<edu.carleton.COMP4601.assignment2.dao.Document> docs = getDocs(hits, searcher);
			reader.close();
			return docs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContentQueryString(String searchString) {
		if(searchString.contains("i:")) {
			String iString = searchString.substring(searchString.indexOf("i:")+2);
			if(iString.contains(" "))
				iString = iString.substring(0, iString.indexOf(" "));
			if(!iString.isEmpty()) {
				return iString;
			}
		}
		return null;
	}
	
	public String getIdQueryString(String searchString) {
		if(searchString.contains("docId:")) {
			String iString = searchString.substring(searchString.indexOf("docId:")+6);
			if(iString.contains(" "))
				iString = iString.substring(0, iString.indexOf(" "));
			if(!iString.isEmpty()) {
				return iString;
			}
		}
		return null;
	}

	protected ArrayList<edu.carleton.COMP4601.assignment2.dao.Document> getDocs(ScoreDoc[] hits,IndexSearcher searcher) {
		ArrayList<edu.carleton.COMP4601.assignment2.dao.Document> docs = new ArrayList<edu.carleton.COMP4601.assignment2.dao.Document>();
		Set<Integer> processedIds = new HashSet<Integer>();
		for (ScoreDoc hit : hits) {
			Document indexDoc = null;
			try {
				indexDoc = searcher.doc(hit.doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String url = indexDoc.get(Indexer.COLUMN_URL);
			if (url != null) {
				DBDocument dbDocument = service.getDBDocument(url);
				if (dbDocument != null) {
					if(processedIds.contains(dbDocument.getId()))
						continue;
					processedIds.add(dbDocument.getId());
					docs.add(new edu.carleton.COMP4601.assignment2.dao.Document(dbDocument));
				}
			}
		}
		return docs;
	}

}
