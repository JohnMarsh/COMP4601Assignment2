package edu.carleton.COMP4601.assignment2.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.graph.CrawlerVertex;
import edu.carleton.COMP4601.assignment2.service.A2DocumentServiceImpl;
import edu.carleton.COMP4601.assignment2.service.IA2DocumentService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif" + "|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private IA2DocumentService service;

	@Override
	public void onStart() {
		this.service = new A2DocumentServiceImpl();
	}

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if(!href.contains("carleton.ca") && ! href.contains("sikaman.dyndns.org") && ! href.contains("zdirect.com")) {
			return false;
		}
		return !FILTERS.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		DBDocument doc = new DBDocument();

		doc.setId(page.getWebURL().getDocid());
		doc.setCrawlTime(System.currentTimeMillis());
		doc.setUrl(url);

		String title = "";
		String type = "";

		try {
			InputStream input = TikaInputStream.get(new URL(url));
			org.xml.sax.ContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			ParseContext context = new ParseContext();
			Parser parser = new AutoDetectParser();
			parser.parse(input, handler, metadata, context);

			title = metadata.get(Metadata.TITLE);
			type = metadata.get(Metadata.CONTENT_TYPE);

			System.out.println("Title: " + title);
			System.out.println("Content Type:+ " + type);

			doc.setMdTitle(title);
			doc.setMdContentType(type);
			doc.setContent(handler.toString());
			doc.setBinaryData(IOUtils.toByteArray(input));

			input.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (type.contains("text/html")) {
			try {
				Document jDoc = Jsoup.connect(url).get();
				Elements links = jDoc.select("a[href]");

				List<String> linkList = new ArrayList<String>();
				for (Element link : links) {
					String href = link.attr("href");
					linkList.add(href);
					doc.getLinks().add(href);
				}

				String selector = "img[src~=(?i)\\.(png|jpe?g|gif|tiff?)]";
				Elements images = jDoc.select(selector);
				for (Element image : images) {
					
				}

				CrawlerVertex currentPage = new CrawlerVertex(url);
				for (String link : linkList) {
					CrawlerVertex linkVertex = new CrawlerVertex(link);
					CrawlerGraph.getInstance().addEdge(currentPage, linkVertex);
				}
				String text = "";
				for(Element e : jDoc.select("p, h1, h2, h3, h4")) {
					text += e.text();
				}
				doc.setContent(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			getService().saveDocument(doc);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public IA2DocumentService getService() {
		return service;
	}

	public void setService(IA2DocumentService service) {
		this.service = service;
	}
}
