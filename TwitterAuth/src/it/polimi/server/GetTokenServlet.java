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
import twitter4j.auth.RequestToken;

@SuppressWarnings("serial")
public class GetTokenServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String token = null, tokenSecret = null, authorizationUrl = null;
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
		System.out.print("setted consumer");
		
		RequestToken twitterRequestToken;
		try {
			twitterRequestToken = twitter.getOAuthRequestToken();
		
			token = twitterRequestToken.getToken();
			tokenSecret = twitterRequestToken.getTokenSecret();
			authorizationUrl = twitterRequestToken.getAuthorizationURL();
			//save in the session
			HttpSession session = req.getSession();
			session.setAttribute("twitter", twitter);
			session.setAttribute("token", twitterRequestToken);
			session.setAttribute("tokenSecret", tokenSecret);
		
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title> servlet 1</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Token: " + token + " " + tokenSecret);
		out.println("<br>Link: " + authorizationUrl);
		out.println("</body>");
		out.println("</html>");
		
	}
	
}
