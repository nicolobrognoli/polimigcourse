package it.polimi.server;

import javax.servlet.http.HttpSession;

import it.polimi.client.TwitterService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TwitterServiceImpl extends RemoteServiceServlet implements
		TwitterService {

	@Override
	public String getRequestToken() throws IllegalArgumentException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");

		try {
			RequestToken twitterRequestToken = twitter.getOAuthRequestToken();
			String token = twitterRequestToken.getToken();
			String tokenSecret = twitterRequestToken.getTokenSecret();
			String authorizationUrl = twitterRequestToken.getAuthorizationURL();
			
			HttpSession session = getThreadLocalRequest().getSession();
			session.setAttribute("twitter", twitter);
			session.setAttribute("token", twitterRequestToken);
			session.setAttribute("tokenSecret", tokenSecret);

			return token;			
			
		} catch (TwitterException e) {

			e.printStackTrace();
		}

		return "error";
	}
	
	@Override
	public String getAuthUrl() throws IllegalArgumentException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");

		try {
			RequestToken twitterRequestToken = twitter.getOAuthRequestToken();
			String token = twitterRequestToken.getToken();
			String tokenSecret = twitterRequestToken.getTokenSecret();
			String authorizationUrl = twitterRequestToken.getAuthorizationURL();
			
			HttpSession session = getThreadLocalRequest().getSession();
			session.setAttribute("twitter", twitter);
			session.setAttribute("token", twitterRequestToken);
			session.setAttribute("tokenSecret", tokenSecret);
			
			return authorizationUrl;
			
		}catch (TwitterException e) {

			e.printStackTrace();
		}
		return "error";
	}

	
}
