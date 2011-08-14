package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TwitterServiceAsync {

	void getRequestToken(AsyncCallback<String> callback);
}
