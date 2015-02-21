package edu.carleton.COMP4601.utility;

import java.util.logging.Level;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import edu.carleton.COMP4601.assignment2.dao.DocumentCollection;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;

public class Test {

	public static void main(String[] args) {
		SearchResult sr = new SearchResult(100);
		String url = "http://192.168.0.12:8080/COMP4601SDA/";
		WebResource service;
		Client client = Client.create(new DefaultClientConfig());
		if (url.endsWith("/"))
			service = client.resource(url);
		else
			service = client.resource(url + "/");

		ClientResponse r = service.path(SDAConstants.REST)
				.path(SDAConstants.SDA).path("/query/bob")
				.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);
		// Check to make sure that we got a reasonable response
		if (r.getStatus() < 204) {
			sr.addAll(r.getEntity(DocumentCollection.class)
					.getDocuments());
		} else {
			System.out.println(r.getStatus());
		}
		
		System.out.println(sr.getDocs().size());
	}

}
