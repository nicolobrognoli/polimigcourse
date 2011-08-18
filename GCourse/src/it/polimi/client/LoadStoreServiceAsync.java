package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoadStoreServiceAsync {

	void updateUser(String email, String name, String pwd, boolean professor,
			AsyncCallback<String> callback);

}
