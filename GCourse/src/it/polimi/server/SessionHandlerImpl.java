package it.polimi.server;

import it.polimi.client.SessionHandler;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SessionHandlerImpl extends RemoteServiceServlet implements SessionHandler {

	HttpSession httpSession;
	
	
	

	@Override
	public void setSession(String email) {
		httpSession = getThreadLocalRequest().getSession();
		httpSession = this.getThreadLocalRequest().getSession();
	    httpSession.setAttribute("email", email);
	}

	@Override
	public void exitSession() {
		httpSession = this.getThreadLocalRequest().getSession();
	    httpSession.invalidate(); // kill session

	}

	@Override
	public String getSessionEmail() {
		String email;
		httpSession = getThreadLocalRequest().getSession();
		email = (String) httpSession.getAttribute("email");
		return email;		
	}

}
