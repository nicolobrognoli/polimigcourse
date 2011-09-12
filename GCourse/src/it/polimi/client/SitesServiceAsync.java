package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SitesServiceAsync {

	void createNewPage(String email, String title, String content,
			AsyncCallback<String> callback);

	void listSiteContent(String email, String course,
			AsyncCallback<String> callback);

}
