package edu.carleton.COMP4601.assignment2.graph;

import java.io.Serializable;

public class CrawlerEdge implements Serializable {
	private static final long serialVersionUID = 1L;
	private CrawlerVertex source;
	private CrawlerVertex target;
	
	public CrawlerEdge() {
		
	}
	
	public CrawlerEdge(CrawlerVertex source, CrawlerVertex target) {
		this.source = source;
		this.target = target;
	}
	
	public CrawlerVertex getSource() {
		return source;
	}
	public void setSource(CrawlerVertex source) {
		this.source = source;
	}
	public CrawlerVertex getTarget() {
		return target;
	}
	public void setTarget(CrawlerVertex target) {
		this.target = target;
	}
	
	@Override
	public int hashCode() {
		return 31 * (source.hashCode() + target.hashCode());
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof CrawlerEdge)) {
			return false;
		}
		CrawlerEdge other = (CrawlerEdge) o;
		if(other.getSource() == null || other.getTarget() == null)
			return false;
		
		return (other.getSource().equals(target) && other.getTarget().equals(source))
				|| (other.getSource().equals(source) && other.getTarget().equals(target));
	}
}
