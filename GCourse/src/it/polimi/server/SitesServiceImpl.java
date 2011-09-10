package it.polimi.server;

import it.polimi.client.SitesService;
import it.polimi.server.utils.LoadStore;
import it.polimi.server.utils.SiteModifier;

import java.io.IOException;
import java.net.MalformedURLException;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SitesServiceImpl extends RemoteServiceServlet implements SitesService{

	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	@Override
	public String createNewPage(String email, String title, String content) {
		
		SiteModifier siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(email), LoadStore.getUserSiteName(email));
	    String returned = "";
		try {
			returned = siteModifier.createPage(title, content, null,null);
			if(returned.contains("expired")){
				GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(email),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(email));
				access.refreshToken();
				String newAccessToken = access.getAccessToken();
				LoadStore.updateAccessToken(email, newAccessToken);
				siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(email));
				returned = siteModifier.createPage(title, content, null, null);
		    }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	    return returned;
	}

}
