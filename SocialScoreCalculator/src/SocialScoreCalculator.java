import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException; 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.util.JSON;

import java.util.Set;
import org.json.simple.JSONObject;
import org.eclipse.jetty.util.*;

public class SocialScoreCalculator extends AbstractHandler 
{
	public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) 
            		throws IOException, ServletException
	{
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>LENDR.CO</h1>");
		
		String queryString = request.getQueryString();
		if(queryString.length() > 0 && queryString.contains("=")) {
			String userId = request.getQueryString().split("=")[1];	
			
			int score = displayData(response, userId);
			response.getWriter().println("<h2>Social score: " + score + "</h2>");
		}
		else {
			response.getWriter().println("<h2>Error: No query string provided.</h2>");
		}
	}
	
	public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new SocialScoreCalculator());
        server.start();
        server.join();
    }
	
	public static int displayData(HttpServletResponse response, String userId)
    {
		int score = 0;
    	try {
	    	MongoClient mongoClient = new MongoClient("ec2-54-245-170-121.us-west-2.compute.amazonaws.com");
	    	DB db = mongoClient.getDB( "lendrDb" );
	    	
	    	DBCollection friends = db.getCollection("friends");
	    	DBCollection profiles = db.getCollection("profiles");
	    	
	    	BasicDBObject query = new BasicDBObject("id", userId);
	    	
	    	DBObject userProfile = (DBObject) JSON.parse((String)profiles.findOne(query).get("profile"));
	    	String userName = (String) userProfile.get("name");
	    	long friendCount = friends.getCount(query);
	    	response.getWriter().println("<h3> Number friends for " + userName + " (" + userId + "): " + friendCount + "</h3>");
			if(friendCount > 1500) {
				score = 1;
			}
    	}
    	catch(Exception e) {
    		try {
				response.getWriter().println(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
    	return score;
    }
}