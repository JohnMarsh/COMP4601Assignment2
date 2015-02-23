package edu.carleton.COMP4601.assignment2.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.Multigraph;

import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;

public class CrawlerGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2348692181046470369L;
	
	private String name;
	private Multigraph<CrawlerVertex, CrawlerEdge> graph;
	
	public Multigraph<CrawlerVertex, CrawlerEdge> getGraph() {
		return graph;
	}

	public void setGraph(Multigraph<CrawlerVertex, CrawlerEdge> graph) {
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
		graph = new Multigraph<CrawlerVertex, CrawlerEdge>(new CrawlerEdgeFactory());
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
	
	public List<Map<String, String>> edgeSetToList() {
		ArrayList<Map<String, String>> l = new ArrayList<Map<String,String>>();
		
		for(CrawlerEdge e: getGraph().edgeSet()) {
			Map<String, String> m = new HashMap<>();
			if(e.getSource() == null || e.getTarget() == null || e.getSource().getUrl() == null || e.getTarget().getUrl() == null)
				continue;
			m.put("source", e.getSource().getUrl());
			m.put("target", e.getTarget().getUrl());
			l.add(m);
		}
		
		return l;
	}
	
	public void setGraphFromEdgeMap(List<Map<String, String>> edges) {
		setGraph(new Multigraph<CrawlerVertex, CrawlerEdge>(new CrawlerEdgeFactory()));
		
		for(Map<String, String> m : edges) {
			CrawlerVertex v1 = new CrawlerVertex(m.get("source"));
			CrawlerVertex v2 = new CrawlerVertex(m.get("target"));
			addEdge(v1, v2);
		}
	}
}
