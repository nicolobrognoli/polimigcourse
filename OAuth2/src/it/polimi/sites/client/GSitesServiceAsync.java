package it.polimi.sites.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GSitesServiceAsync {

	void createPage(AsyncCallback<String> callback);

}
