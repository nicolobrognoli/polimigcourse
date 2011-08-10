package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sendregistration")
public interface SendRegistrationMail extends RemoteService{
	
	String sendMail(String param) throws IllegalArgumentException;
}
