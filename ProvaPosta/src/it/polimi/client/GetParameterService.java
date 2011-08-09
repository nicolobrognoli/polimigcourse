package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("getparameter")
public interface GetParameterService extends RemoteService{
	
	void getParameter(String param) throws IllegalArgumentException;
}
