package it.polimi.server;

import it.polimi.server.utils.LoadStore;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	private static final String CALLBACK_URL = "http://polimigcourse.appspot.com/googlecallback";
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	BaseContentEntry<?> page;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String email = (String) req.getSession().getAttribute("email");
		
		String authorizationCode = req.getParameter("code");

	    // Exchange for an access and refresh token
	    GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authorizationCode, CALLBACK_URL);
	    authRequest.useBasicAuthorization = false;
	    AccessTokenResponse authResponse = authRequest.execute();

	    String result = LoadStore.storeAccessToken(email, "google", authResponse.accessToken, authResponse.refreshToken);

	    resp.sendRedirect("confirm.html");
		/*out.println("<html>");
		out.println("<head>");
		out.println("<title>Google callback</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<br>Result: " + result);
		out.println("<br>Email: " + email);
		out.println("<br>Access token: " + authResponse.accessToken);
		out.println("<br>Refresh token: " + authResponse.refreshToken);
		out.println("</body>");
		out.println("</html>");*/
	}

}
