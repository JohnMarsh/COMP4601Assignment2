package edu.carleton.COMP4601.assignment2.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class DBDocument extends BasicDBObject {
	public static final String ID ="ID";
	public static final String URL = "URL";
	public static final String CRAWLTIME = "CRAWLTIME";
	public static final String CONTENT = "CONTENT";
	public static final String MDTITLE = "MDTITLE";
	public static final String MDCONTENTTYPE = "MDCONTENTTYPE";
	public static final String LINKS = "LINKS";
	public static final String TAGS = "TAGS";
	public static final String BINARYDATA = "BINARYDATA";
	public static final String SCORE = "SCORE";
	private static final long serialVersionUID = -3576086765292304021L;
	
	private Integer id;
	private String url;
	private Long crawlTime;
	private String content;
	private String mdTitle;
	private String mdContentType;
	private List<String> links;
	private List<String> tags;
	private byte[] binaryData;
	private Double score;
	
	
	public DBDocument(){
		super();
		setLinks(new ArrayList<String>());
		setTags(new ArrayList<String>());
	}
	
	@SuppressWarnings("unchecked")
	public DBDocument(BasicDBObject other) {
		setId(other.getInt(ID));
		setUrl(other.getString(URL));
		setCrawlTime(other.getLong(CRAWLTIME, 0));
		setContent(other.getString(CONTENT));
		setMdTitle(other.getString(MDTITLE));
		setMdContentType(other.getString(MDCONTENTTYPE));
		setLinks((List<String>) other.get(LINKS));
		setTags((List<String>) other.get(TAGS));
		setBinaryData((byte[]) other.get(BINARYDATA));
		setScore(other.getDouble(SCORE, 0));
	}

	public DBDocument(Document document) {
		this();
		setId(document.getId());
		if(document.getScore() != null)
			setScore(document.getScore().doubleValue());
		setMdTitle(document.getName());
		setContent(document.getText());
		if(document.getTags() != null)
			setTags(new ArrayList<>(document.getTags()));
		if(document.getLinks() != null)
			setLinks(new ArrayList<>(document.getLinks()));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		put(ID, id);
	}
	public String getUrl() {
		if(url == null)
			return "";
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
		put(URL, url);
	}
	public Long getCrawlTime() {
		if(crawlTime == null)
			return (long) 1;
		return crawlTime;
	}
	public void setCrawlTime(Long crawlTime) {
		this.crawlTime = crawlTime;
		put(CRAWLTIME, crawlTime);
	}
	public String getMdTitle() {
		if(mdTitle == null)
			return "";
		return mdTitle;
	}
	public void setMdTitle(String mdTitle) {
		this.mdTitle = mdTitle;
		put(MDTITLE, mdTitle);
	}
	public String getMdContentType() {
		if(mdContentType == null)
			return "";
		return mdContentType;
	}
	public void setMdContentType(String mdContentType) {
		this.mdContentType = mdContentType;
		put(MDCONTENTTYPE, mdContentType);
	}
	public byte[] getBinaryData() {
		return binaryData;
	}
	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
		put(BINARYDATA, binaryData);
	}
	public String getContent() {
		if(content == null)
			return "";
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		put(CONTENT, content);
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
		put(LINKS, links);
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
		put(TAGS, tags);
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
		put(SCORE, score);
	}
	

}
