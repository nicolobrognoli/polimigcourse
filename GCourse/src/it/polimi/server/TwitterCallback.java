package it.polimi.server;

import it.polimi.server.utils.LoadStore;
import it.polimi.server.utils.TwitterManager;

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
			String email = (String) req.getSession().getAttribute("email");
		    
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			twitter.setOAuthAccessToken(accessToken);
			
			//store the tokens on the datastore
			String result = LoadStore.storeAccessToken(email, "twitter", accessToken.getToken(), accessToken.getTokenSecret());
			
			String screen = twitter.getScreenName();
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Twitter callback</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("Account: " + email);
			if(result.equals("updated"))
				out.println("<br><br>Account twitter " + screen + " registrato correttamente.");	
			else
				out.println("Errore registrazione account twitter.");
			out.println("<br><a href=home.html> Prosegui alla home page </a>");
			out.println("</body>");
			out.println("</html>");
			
			TwitterManager t = new TwitterManager(accessToken, "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
			t.sendTweet("prova...");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
