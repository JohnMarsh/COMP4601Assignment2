package edu.carleton.COMP4601.assignment2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.DBObject;

import edu.carleton.COMP4601.utility.SearchServiceManager;

@Path("/sda/list")
public class A2ListUtil extends AbstractA2Resource {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String list() {
		try {
			JSONArray array = new JSONArray(SearchServiceManager.list());
			if (array.length() == 0) {
				return "<p>No Services Found</p>";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<h1>Services</h1>\n");
			sb.append("<ul>");
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				sb.append("<li>");
				sb.append("<a href=\"" + o.get("url") + "\">");
				sb.append(o.get("name"));
				sb.append("</a>");
				sb.append("</li>");
			}
			sb.append("</ul>");

			return sb.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "<p>Error Occured</p>";
		}

	}
}
