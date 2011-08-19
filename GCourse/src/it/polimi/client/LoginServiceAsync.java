package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void checkUser(String nickname, String pwd, AsyncCallback<Boolean> callback);

}
