package edu.carleton.COMP4601.utility;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.graph.CrawlerVertex;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;

public class Test {

	public static void main(String[] args) {
		A2DocumentServiceImpl im = new A2DocumentServiceImpl();
		CrawlerGraph loadGraph = im.loadGraph();
		
		ArrayList<CrawlerVertex> arrayList = new ArrayList<>(loadGraph.getGraph().vertexSet());
		System.out.println(arrayList.size());
		//System.out.println(loadGraph.getGraph().containsEdge(arrayList.get(0), arrayList.get(1)));
	}

}
