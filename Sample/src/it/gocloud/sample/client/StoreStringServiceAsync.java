package it.gocloud.sample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StoreStringServiceAsync {

	void storeString(String data, AsyncCallback<Void> callback);
}
