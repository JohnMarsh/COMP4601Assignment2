package edu.carleton.COMP4601.assignment2.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class DBDocument extends BasicDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3576086765292304021L;
	
	private Integer id;
	private String url;
	private Long crawlTime;
	private String content;
	private String mdTitle;
	private String mdContentType;
	private List<String> links;
	private byte[] binaryData;
	
	
	public DBDocument(){
		super();
		setLinks(new ArrayList<String>());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getCrawlTime() {
		return crawlTime;
	}
	public void setCrawlTime(Long crawlTime) {
		this.crawlTime = crawlTime;
	}
	public String getMdTitle() {
		return mdTitle;
	}
	public void setMdTitle(String mdTitle) {
		this.mdTitle = mdTitle;
	}
	public String getMdContentType() {
		return mdContentType;
	}
	public void setMdContentType(String mdContentType) {
		this.mdContentType = mdContentType;
	}
	public byte[] getBinaryData() {
		return binaryData;
	}
	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}
	

}
