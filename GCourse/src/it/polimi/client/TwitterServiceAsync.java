package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {

	void getRequestToken(AsyncCallback<String> callback);

	void getAuthUrl(AsyncCallback<String> callback);
}