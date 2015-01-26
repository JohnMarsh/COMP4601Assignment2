package edu.carleton.COMP4601.a1.dao;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.carleton.COMP4601.a1.dao.Document;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentCollection {
	@XmlElement(name="documents")
	private List<Document> documents;
	
	public DocumentCollection() {
		documents = new ArrayList<Document>();
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}