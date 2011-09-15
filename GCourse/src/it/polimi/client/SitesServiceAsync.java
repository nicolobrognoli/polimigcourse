package it.polimi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SitesServiceAsync {

	void createNewPage(String email, String title, String content,
			AsyncCallback<String> callback);
	void createNewPage(String email, String title, String content,String parent,
			AsyncCallback<String> callback);

	void pushContentToStudents(String email, String course,
			AsyncCallback<String> callback);
	
	void listSiteContent(String email, String course,AsyncCallback<List<String>> callback);
}
