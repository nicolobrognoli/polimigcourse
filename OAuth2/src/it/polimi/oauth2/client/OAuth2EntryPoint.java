package it.polimi.oauth2.client;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

@SuppressWarnings("deprecation")
public class OAuth2EntryPoint implements EntryPoint {

	private final class CallbackImplementation implements
			Callback<String, Throwable> {
		@Override
		  public void onSuccess(String token) {
		    Window.alert("Token: "+token);
		  }

		@Override
		  public void onFailure(Throwable caught) {
		    Window.alert(caught.getMessage());			    
		  }
	}

	@Override
	public void onModuleLoad() {
		String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
		String CLIENT_ID = "267706380696.apps.googleusercontent.com"; // available from the APIs console
		String SITES_SCOPE = "https://sites.google.com/feeds/";
		String GMAIL_SCOPE = "https://mail.google.com/mail/feed/atom";
		String CONTACTS_SCOPE = "https://www.google.com/m8/feeds/";

		AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
		    .withScopes(SITES_SCOPE, GMAIL_SCOPE, CONTACTS_SCOPE); // Can specify multiple scopes here
		
		Auth.get().login(req, new CallbackImplementation());
	}

}
