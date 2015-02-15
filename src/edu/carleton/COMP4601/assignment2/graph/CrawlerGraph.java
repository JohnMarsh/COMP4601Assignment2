package edu.carleton.COMP4601.assignment2.graph;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.utility.Marshaller;

public class CrawlerGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2348692181046470369L;
	
	private String name;
	private Multigraph<CrawlerVertex, DefaultEdge> graph;
	
	private MongoClient client;
	private DB db;
	
	public Multigraph<CrawlerVertex, DefaultEdge> getGraph() {
		return graph;
	}


	public void setGraph(Multigraph<CrawlerVertex, DefaultEdge> graph) {
		this.graph = graph;
	}


	private DBCollection graphCollection;
	
	private static CrawlerGraph instance = null;
	
	public static synchronized CrawlerGraph getInstance(){
		if(instance == null){
			instance = new CrawlerGraph("crawlerGraph");
		}
		return instance;
	}
	
	
	public CrawlerGraph(String name){
		this.setName(name);
		graph = new Multigraph<CrawlerVertex, DefaultEdge>(DefaultEdge.class);
		try {
			client = new MongoClient("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = client.getDB("crawler");
		graphCollection = db.getCollection("graphs");
	}
	
	
	public void addEdge(CrawlerVertex source, CrawlerVertex dest){
		graph.addVertex(source);
		graph.addVertex(dest);
		graph.addEdge(source, dest);
	}


	@Override
	public String toString() {
		return "CrawlerGraph [graph=" + graph + "]";
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void saveToDatabase() {
		BasicDBObject object = new BasicDBObject("name", getName());
		try {
			object.append("data", Marshaller.serializeObject(graph));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graphCollection.insert(object);
	}
	
	

}
