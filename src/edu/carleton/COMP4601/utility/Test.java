package edu.carleton.COMP4601.utility;

import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;

public class Test {

	public static void main(String[] args) {
		CrawlerGraph loadGraph = new A2DocumentServiceImpl().loadGraph();
		System.out.println(loadGraph.getGraph().edgeSet().size());
	}

}
