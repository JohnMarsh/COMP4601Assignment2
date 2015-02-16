package edu.carleton.COMP4601.assignment2.graph;

import java.io.Serializable;

import org.jgrapht.EdgeFactory;

public class CrawlerEdgeFactory implements EdgeFactory<CrawlerVertex, CrawlerEdge>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public CrawlerEdge createEdge(CrawlerVertex arg0, CrawlerVertex arg1) {
		CrawlerEdge e = new CrawlerEdge(arg0, arg1);
		return e;
	}

}
