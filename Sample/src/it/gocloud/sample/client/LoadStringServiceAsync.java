package it.gocloud.sample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoadStringServiceAsync {

	void loadString(AsyncCallback<String> callback);
}
