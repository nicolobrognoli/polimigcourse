package it.polimi.sites.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GSitesServiceAsync {

	void createPage(String siteName, String pageTitle,
			String token, String pageContent, AsyncCallback<String> callback);

}
