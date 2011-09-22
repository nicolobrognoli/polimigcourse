package it.polimi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calendarservice")
public interface CalendarService extends RemoteService {
	
	String subscribeCalendar(String userEmail, String profEmail, String courseKey);

	String createCalendar(String email, String name, String summary);
}
