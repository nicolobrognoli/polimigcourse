package it.polimi.server.utils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterManager {
	
	private AccessToken accessToken;
	private String client_key, client_secret;

	
	
	
	public TwitterManager(AccessToken accessToken, String client_key,
			String client_secret) {
		super();
		this.accessToken = accessToken;
		this.client_key = client_key;
		this.client_secret = client_secret;
	}




	public boolean sendTweet(String tweet){
		if(this.accessToken == null)
			return false;
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(this.client_key, this.client_secret);
		twitter.setOAuthAccessToken(accessToken);
		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}
}
