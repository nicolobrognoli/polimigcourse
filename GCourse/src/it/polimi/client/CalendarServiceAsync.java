package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarServiceAsync {

	void subscribeCalendar(String userEmail, String profEmail, String courseKey,
			AsyncCallback<String> callback);

	void createCalendar(String email, String name, String summary,
			AsyncCallback<String> callback);

}
