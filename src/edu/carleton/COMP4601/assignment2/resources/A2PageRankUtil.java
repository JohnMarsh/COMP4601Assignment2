package edu.carleton.COMP4601.assignment2.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.carleton.COMP4601.assignment2.dao.DBDocument;

@Path("/sda/pagerank")
public class A2PageRankUtil extends AbstractA2Resource {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String pageRank() {
		try {
			List<DBDocument> docs = getService().getAllDBDocuments();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			for (DBDocument doc : docs) {
				sb.append("<tr>");

				sb.append("<td>");
				sb.append(doc.getMdTitle());
				sb.append("</td>");

				sb.append("<td>");
				sb.append(doc.getScore());
				sb.append("</td>");

				sb.append("</tr>");
			}
			sb.append("</table>");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "<p>Error Occured</p>";
		}
	}

}
