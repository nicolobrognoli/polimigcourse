package it.polimi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleCallbackServlet extends HttpServlet{
	private static final String SCOPE = "https://sites.google.com/feeds/";
	private static final String CALLBACK_URL = "http://polimigcourse.appspot.com/googlecallback";
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String authorizationCode = (String) req.getParameter("code");

	    // Exchange for an access and refresh token
	    GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authorizationCode, CALLBACK_URL);
	    authRequest.useBasicAuthorization = false;
	    AccessTokenResponse authResponse = authRequest.execute();
	    String accessToken = authResponse.accessToken;
	    GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(accessToken,
	        TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authResponse.refreshToken);
	    HttpRequestFactory rf = TRANSPORT.createRequestFactory(access);
	   
	    GenericUrl sitesEndpoint = new GenericUrl("https://sites.google.com/feeds/content/site/projectgcourse");
	    String requestBody = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +
	    "<category scheme=\"http://schemas.google.com/g/2005#kind\"" +
	       " term=\"http://schemas.google.com/sites/2008#webpage\" label=\"webpage\"/>" +
	    "<title>cacca</title>" +
	    "<content type=\"xhtml\">" +
	     "<div xmlns=\"http://www.w3.org/1999/xhtml\">ciao</div>" +
	    "</content>" +
	  "</entry>";
	    
	    HttpRequest request = rf.buildPostRequest(sitesEndpoint, new ByteArrayContent(requestBody));
	    request.headers.contentType = "application/atom+xml";
	     HttpResponse createPage = request.execute();
	    
	    
	    
	    
		out.println("<html>");
		out.println("<head>");
		out.println("<title> servlet 1</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Access token: " + authResponse.accessToken);
		out.println("<br>Refresh token: " + authResponse.refreshToken);
		out.println("</body>");
		out.println("</html>");
	}

}
