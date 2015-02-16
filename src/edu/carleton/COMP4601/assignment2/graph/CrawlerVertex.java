package edu.carleton.COMP4601.assignment2.graph;

import java.io.Serializable;

public class CrawlerVertex implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -146287377390030452L;
	private String url;
	
	public CrawlerVertex(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrawlerVertex other = (CrawlerVertex) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CrawlerVertex [url=" + url + "]";
	}
	
	
	

}
