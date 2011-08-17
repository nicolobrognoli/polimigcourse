package it.polimi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.util.ServiceException;



@SuppressWarnings("serial")
public class GoogleCallbackServlet extends HttpServlet{
	private static final String SCOPE = "https://sites.google.com/feeds/";
	private static final String CALLBACK_URL = "http://localhost:8888/googlecallback";
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	BaseContentEntry<?> page;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String authorizationCode = req.getParameter("code");

	    // Exchange for an access and refresh token
	    GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authorizationCode, CALLBACK_URL);
	    authRequest.useBasicAuthorization = false;
	    AccessTokenResponse authResponse = authRequest.execute();
	    String accessToken = authResponse.accessToken;
	   
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse", "site", "projectgcourse");
	    sitesHelper.login(authResponse.accessToken);
	   	    
	    try {
			page = sitesHelper.createPage("webpage", "cacca3");				
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SitesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		out.println("<html>");
		out.println("<head>");
		out.println("<title> servlet 1</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Access token: " + authResponse.accessToken);
		out.println("<br>Refresh token: " + authResponse.refreshToken);
		out.println("<br>Url: " + page.toString());
		out.println("</body>");
		out.println("</html>");
	}

}
