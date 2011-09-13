package it.polimi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarServiceAsync {

	void subscribeCalendar(String userEmail, String profEmail, String courseKey,
			AsyncCallback<String> callback);

}
