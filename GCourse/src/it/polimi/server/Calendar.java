package it.polimi.server;

import it.polimi.server.utils.CalendarHelper;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.allen_sauer.gwt.log.client.Log;

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
	@Override
	public void doGet(HttpServletRequest req, 
            HttpServletResponse resp) 
 throws IOException {
		
		CalendarHelper ch = new CalendarHelper("simone.benefico@gmail.com");
	   // Demonstrate retrieving various calendar feeds.
	   try {
	     String calendar = ch.createCalendar("Titolo", "Calendario");
	     ch.createEvent(calendar, "Tennis with John April 11 3pm-3:30pm");
	     ch.createSubscription(calendar);	     
	     ch.deleteSubscription(calendar);
	     ch.deleteCalendar(calendar);
	   } catch (IOException e) {
	     // Communications error
	     Log.warn("There was a problem communicating with the service.");
	     e.printStackTrace();
	   }
	 }
	 
	 
}
