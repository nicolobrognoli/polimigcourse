package it.polimi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;



@SuppressWarnings("serial")
public class Authorization  extends HttpServlet {

	private static final String SCOPE = "https://sites.google.com/feeds/";
	private static final String CALLBACK_URL = "http://polimigcourse.appspot.com/googlecallback";

	
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String authorizeUrl = new GoogleAuthorizationRequestUrl(CLIENT_ID, CALLBACK_URL, SCOPE).build();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title> servlet 1</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<a href=\"" + authorizeUrl + "\">Link google</a>");
		out.println("</body>");
		out.println("</html>");
	}
	
	
	
}
