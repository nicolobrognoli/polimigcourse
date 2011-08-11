package it.polimi.client;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionHandlerAsync {

	void setSession(String email, AsyncCallback<Void> callback);

	void exitSession(AsyncCallback<Void> callback);

	void getSessionEmail(AsyncCallback<String> callback);

}
