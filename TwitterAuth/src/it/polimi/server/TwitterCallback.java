package it.polimi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@SuppressWarnings("serial")
public class TwitterCallback extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		HttpSession session = req.getSession();
		
		//Twitter twitter = new TwitterFactory().getInstance();
		//twitter.setOAuthConsumer("GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");

		AccessToken accessToken;
		try {
			
			Twitter twitter = (Twitter) req.getSession().getAttribute("twitter");
		    RequestToken requestToken = (RequestToken) req.getSession().getAttribute("token");
		    String verifier = req.getParameter("oauth_verifier");
			
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			twitter.setOAuthAccessToken(accessToken);
			
			//User user = twitter.verifyCredentials();
			
			String aToken = accessToken.getToken();
			String aSecretToken = accessToken.getTokenSecret();
/*
			int id = (int) accessToken.getUserId();
			User user = twitter.showUser(id+"");
			String screenName = user.getScreenName();*/

			
			out.println("<html>");
			out.println("<head>");
			out.println("<title> servlet 2</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("AccessToken: " + aToken);
			out.println("<br>AccessSecretToken: " + aSecretToken);
			out.println("<br>Oauth verifier: " + verifier);	
			//out.println("<br>ScreenName: " + screenName);	
			out.println("</body>");
			out.println("</html>");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
