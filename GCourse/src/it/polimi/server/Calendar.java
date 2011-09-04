package it.polimi.server;

import it.polimi.server.data.UserPO;
import it.polimi.server.utils.CalendarHelper;
import it.polimi.server.utils.LoadStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.allen_sauer.gwt.log.client.Log;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.util.ServiceException;

public class Calendar extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6124595083777509460L;

	/**
	  * Instantiates a CalendarService object and uses the command line arguments
	  * to authenticate. The CalendarService object is used to demonstrate
	  * interactions with the Calendar data API's calendar feeds.
	  * 
	  * @param args Must be length 2 and contain a valid username/password
	  */
	public void doGet(HttpServletRequest req, 
            HttpServletResponse resp) 
 throws IOException {
		
		CalendarHelper ch = new CalendarHelper("simone.benefico@gmail.com");
	   // Demonstrate retrieving various calendar feeds.
	   try {
	     // Create a new secondary calendar, update it, then delete it.
	     ch.createCalendar("Titolo", "Calendario");
	     ch.createEvent("Titolo", "Tennis with John April 11 3pm-3:30pm");
	     /*CalendarEntry updatedCalendar = ch.updateCalendar(newCalendar);
	     //ch.deleteCalendar(newCalendar);

	     // Subscribe to the Google Doodles calendar, update the personalization
	     // settings, then delete the subscription.
	     CalendarEntry newSubscription = ch.createSubscription(service);
	     CalendarEntry updatedSubscription = ch.updateSubscription(newSubscription);
	     *///ch.deleteSubscription(newSubscription);
	   } catch (IOException e) {
	     // Communications error
	     Log.warn("There was a problem communicating with the service.");
	     e.printStackTrace();
	   }
	 }
	 
	 
}
