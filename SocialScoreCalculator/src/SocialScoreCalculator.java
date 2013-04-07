import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException; 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import com.mongodb.MongoClient;
import com.mongodb.DB;
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
		response.getWriter().println("<h1>Hello World</h1>");
		String userId = request.getQueryString().split("=")[1];
		
		response.getWriter().println("<h2>" + userId + "</h2>");
		
		testDb(response);
	}
	
	public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new SocialScoreCalculator());
        server.start();
        server.join();
    }
	
	public static void testDb(HttpServletResponse response)
    {
    	try {
	    	MongoClient mongoClient = new MongoClient("ec2-54-245-170-121.us-west-2.compute.amazonaws.com", 27017);
	    	DB db = mongoClient.getDB( "lendrDb" );
	    	Set<String> colls = db.getCollectionNames();
	
	    	response.getWriter().println("MongoDb collections:\n");
	    			
	    	for (String s : colls) {
	    		response.getWriter().println(s + "\n");
	    	}
	    	
	    	response.getWriter().println("\n");
    	}
    	catch(Exception e) {
    		try {
				response.getWriter().println(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
    }
}