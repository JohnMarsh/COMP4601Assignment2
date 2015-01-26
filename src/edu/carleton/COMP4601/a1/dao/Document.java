package edu.carleton.COMP4601.a1.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@XmlRootElement
public class Document {
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_SCORE = "score";
	public static final String COLUMN_TAGS = "tags";
	public static final String COLUMN_LINKS = "links";
	
	
	private Integer id;
	private Integer score;
	private String name;
	private String text;
	private ArrayList<String> tags;
	private ArrayList<String> links;

	public Document() {
		tags = new ArrayList<String>();
		links = new ArrayList<String>();
	}

	public Document(Integer id) {
		this();
		this.id = id;
	}
	
	public Document(MultivaluedMap<String, String> map) {
		this();
		if(map.getFirst("id") != null)
			this.id = Integer.parseInt(map.getFirst("id"));
		if(map.getFirst("score") != null)
			this.score = Integer.parseInt(map.getFirst("score"));
		this.name = map.getFirst("name");
		this.text = map.getFirst("text");
		if(map.getFirst("tags") != null) {
			this.tags = new ArrayList<>(Arrays.asList(map.getFirst("tags").split(",")));
		}
		if(map.getFirst("links") != null) {
			this.links = new ArrayList<>(Arrays.asList(map.getFirst("links").split(",")));
		}
	}
	
	@SuppressWarnings("unchecked")
	public Document(Map<?, ?> map) {
		this();
		this.id = (Integer) map.get(COLUMN_ID);
		this.score = (Integer) map.get(COLUMN_SCORE);
		this.name = (String) map.get(COLUMN_NAME);
		this.text = (String) map.get(COLUMN_TEXT);
		this.tags = (ArrayList<String>) map.get(COLUMN_TAGS);
		this.links = (ArrayList<String>) map.get(COLUMN_LINKS);
	}
	
	public Integer getId() {
		return id;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getScore() {
		return score;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public void removeTag(String tag) {
		tags.remove(tag);
	}

	public void addLink(String link) {
		links.add(link);
	}

	public void removeLink(String link) {
		links.remove(link);
	}
	
	public DBObject toDBObject() {
		BasicDBObject o = new BasicDBObject();
		o.put(COLUMN_ID, getId());
		o.put(COLUMN_LINKS, getLinks());
		o.put(COLUMN_NAME, getName());
		o.put(COLUMN_SCORE, getScore());
		o.put(COLUMN_TAGS, getTags());
		o.put(COLUMN_TEXT, getText());
		return o;
	}
	
}