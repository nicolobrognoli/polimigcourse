package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("twitter")
public interface TwitterService extends RemoteService {
	String getRequestToken() throws IllegalArgumentException;
	String getAuthUrl() throws IllegalArgumentException;
}
