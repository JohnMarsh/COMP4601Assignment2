package edu.carleton.COMP4601.assignment2.crawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;

public class Controller {

	public static void main(String[] args) throws Exception{
		 int numberOfCrawlers = 10;
		 String crawlStorageFolder = "/data/crawl/root";

         CrawlConfig config = new CrawlConfig();
         config.setCrawlStorageFolder(crawlStorageFolder);
         config.setMaxPagesToFetch(100);
         config.setPolitenessDelay(100);
         config.setIncludeBinaryContentInCrawling(true);

         /*
          * Instantiate the controller for this crawl.
          */
         PageFetcher pageFetcher = new PageFetcher(config);
         RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
         RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
         CrawlController controller =  new CrawlController(config, pageFetcher, robotstxtServer);
		
         /*
          * For each crawl, you need to add some seed urls. These are the first
          * URLs that are fetched and then the crawler starts following links
          * which are found in these pages
          */
         controller.addSeed("http://sikaman.dyndns.org:8888/courses/4601/resources/N-0.html");
         controller.addSeed("http://www.carleton.ca");

         /*
          * Start the crawl. This is a blocking operation, meaning that your code
          * will reach the line after this only when crawling is finished.
          */
         controller.start(MyCrawler.class, numberOfCrawlers); 
         
         CrawlerGraph.getInstance().saveToDatabase();
         
         
	}
}
