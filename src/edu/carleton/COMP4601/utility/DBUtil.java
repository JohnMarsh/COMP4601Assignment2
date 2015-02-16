package edu.carleton.COMP4601.utility;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DBUtil {
	
	public static boolean resetDB(){
		MongoClient client;
		DB db;
		try {
			client = new MongoClient("localhost");
			db = client.getDB("COMP4601Assignment2");
			db.dropDatabase();
		
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
