package it.polimi.sites.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gsitesservice")
public interface GSitesService extends RemoteService {
	String createPage(String siteName, String pageTitle, String token,
			String pageContent);
}
