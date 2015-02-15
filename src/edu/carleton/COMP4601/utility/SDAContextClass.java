package edu.carleton.COMP4601.utility;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SDAContextClass implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			SearchServiceManager.getInstance().stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		SearchServiceManager.getInstance();
	}

}