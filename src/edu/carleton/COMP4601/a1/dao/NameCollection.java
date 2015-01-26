package edu.carleton.COMP4601.a1.dao;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="names")
public class NameCollection {
	private List<String> name;
	
	public NameCollection() {
		this.name = new ArrayList<String>();
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> names) {
		this.name = names;
	}
	
	
}
