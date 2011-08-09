package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetParameterServiceAsync {

	void getParameter(String param, AsyncCallback<Void> callback);

}
