package it.polimi.client;




import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("sessionhandler")
public interface SessionHandler extends RemoteService{

	String getSessionEmail(); 
	
	void exitSession();

	void setSession(String email);
}