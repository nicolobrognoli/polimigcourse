package it.polimi.server;

import java.io.IOException;

import it.polimi.client.CalendarService;
import it.polimi.server.utils.CalendarHelper;
import it.polimi.server.utils.LoadStore;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CalendarServiceImpl extends RemoteServiceServlet implements CalendarService{

	@Override
	public String subscribeCalendar(String userEmail, String profEmail, String courseKey) {
		String id =	LoadStore.loadCalendarId(courseKey);
		
		//inserisco studente nella lista degli acl
		CalendarHelper ch = new CalendarHelper(profEmail);
		ch.setAcl(id, userEmail, profEmail);
		
		//sottoscrizione del calendario
		ch = new CalendarHelper(userEmail);
		try {
			
			
		    ch.createSubscription(id);	    
		    return "ok";

		   } catch (IOException e) {
		     // Communications error
		     Log.warn("There was a problem communicating with the service.");
		     e.printStackTrace();
		   }
		return null;
	}

	@Override
	public String createCalendar(String email, String name, String summary) {
		String id = ""; 
		CalendarHelper ch = new CalendarHelper(email);
		try {
			id = ch.createCalendar(name, summary);
		} catch (IOException e) {
			// Communications error
		     Log.warn("There was a problem communicating with the service.");
			e.printStackTrace();
		}		
		return id;
	}
}
