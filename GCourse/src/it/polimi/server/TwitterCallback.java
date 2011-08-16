package it.polimi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@SuppressWarnings("serial")
public class TwitterCallback extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

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
			
			String screen = twitter.getScreenName();
			//twitter.updateStatus("Prova tweet.");
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title> servlet 2</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("AccessToken: " + aToken);
			out.println("<br>AccessSecretToken: " + aSecretToken);
			out.println("<br>ScreenName: " + screen);	
			out.println("</body>");
			out.println("</html>");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
