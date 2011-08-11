package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sendregistration")
public interface LoadStoreService extends RemoteService{
	
	//search the user and modify the parameters
	String updateUser(String email, String name, boolean professor) throws IllegalArgumentException;
}
