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

import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;
import edu.carleton.COMP4601.utility.Marshaller;

public class CrawlerGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2348692181046470369L;
	
	private String name;
	private Multigraph<CrawlerVertex, DefaultEdge> graph;
	
	public Multigraph<CrawlerVertex, DefaultEdge> getGraph() {
		return graph;
	}

	public void setGraph(Multigraph<CrawlerVertex, DefaultEdge> graph) {
		this.graph = graph;
	}

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
		new A2DocumentServiceImpl().saveGraph(this);
	}
	
}
