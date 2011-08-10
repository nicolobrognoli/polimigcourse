package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SendRegistrationMailAsync {

	void sendMail(String param, AsyncCallback<Void> callback);

}
