package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sitesservice")
public interface SitesService extends RemoteService{
	
	String createNewPage(String email, String title, String content);
	
	String listSiteContent(String email, String course);
}
