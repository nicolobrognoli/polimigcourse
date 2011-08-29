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
	
	CalendarHelper ch = new CalendarHelper();

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
		UserPO tempUser = LoadStore.loadUser("simone.benefico@gmail.com");
		String userName = tempUser.getUser().getEmail();
	   String accessToken = tempUser.getGoogleAccessToken();
	   String refreshToken = tempUser.getGoogleRefreshToken();
	   
	   // Create necessary URL objects
	   try {
		 CalendarHelper.metafeedUrl = new URL(CalendarHelper.METAFEED_URL_BASE + userName);
		 CalendarHelper.allcalendarsFeedUrl = new URL(CalendarHelper.METAFEED_URL_BASE + userName + 
				 CalendarHelper.ALLCALENDARS_FEED_URL_SUFFIX);
		 CalendarHelper.owncalendarsFeedUrl = new URL(CalendarHelper.METAFEED_URL_BASE + userName + 
				 CalendarHelper.OWNCALENDARS_FEED_URL_SUFFIX);
	   } catch (MalformedURLException e) {
	       // Bad URL
	       Log.warn("Uh oh - you've got an invalid URL.");
	       e.printStackTrace();
	       return;
	   }

	   // Create CalendarService and authenticate using ClientLogin
	   CalendarService service = new CalendarService("GCourse-Calendar-v1");

	   service.setAuthSubToken(accessToken);

	   // Demonstrate retrieving various calendar feeds.
	   try {
		 Log.warn("Calendars in metafeed");
	     ch.printUserCalendars(service, CalendarHelper.metafeedUrl);
	     Log.warn("Calendars in allcalendars feed");
	     ch.printUserCalendars(service, CalendarHelper.allcalendarsFeedUrl);
	     Log.warn("Calendars in owncalendars feed");
	     ch.printUserCalendars(service, CalendarHelper.owncalendarsFeedUrl);

	     // Create a new secondary calendar, update it, then delete it.
	     CalendarEntry newCalendar = ch.createCalendar(service);
	     CalendarEntry updatedCalendar = ch.updateCalendar(newCalendar);
	     //ch.deleteCalendar(newCalendar);

	     // Subscribe to the Google Doodles calendar, update the personalization
	     // settings, then delete the subscription.
	     CalendarEntry newSubscription = ch.createSubscription(service);
	     CalendarEntry updatedSubscription = ch.updateSubscription(newSubscription);
	     //ch.deleteSubscription(newSubscription);
	   } catch (IOException e) {
	     // Communications error
	     Log.warn("There was a problem communicating with the service.");
	     e.printStackTrace();
	   } catch (ServiceException e) {
	     // Server side error
		   	final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
			final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
			final HttpTransport TRANSPORT = new NetHttpTransport();
			final JsonFactory JSON_FACTORY = new JacksonFactory();
		    Log.warn("Refreshing token...");
		    e.printStackTrace();		    
			GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(accessToken,TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,refreshToken);
			access.refreshToken();
			accessToken=access.getAccessToken();
			LoadStore.updateAccessToken(tempUser.getUser().getEmail(), accessToken);
			service.setAuthSubToken(accessToken);
			Log.warn("Calendars in metafeed");
		    try {
				 ch.printUserCalendars(service, CalendarHelper.metafeedUrl);			
			     Log.warn("Calendars in allcalendars feed");
			     ch.printUserCalendars(service, CalendarHelper.allcalendarsFeedUrl);
			     Log.warn("Calendars in owncalendars feed");
			     ch.printUserCalendars(service, CalendarHelper.owncalendarsFeedUrl);
	
			     // Create a new secondary calendar, update it, then delete it.
			     CalendarEntry newCalendar = ch.createCalendar(service);
			     newCalendar = ch.updateCalendar(newCalendar);
			     //ch.deleteCalendar(newCalendar);
	
			     // Subscribe to the Google Doodles calendar, update the personalization
			     // settings, then delete the subscription.
			     CalendarEntry newSubscription = ch.createSubscription(service);
			     newSubscription = ch.updateSubscription(newSubscription);
			     //ch.deleteSubscription(newSubscription);
		    } catch (ServiceException e1) {
					Log.warn(" Service Exception e1");
					e1.printStackTrace();
			}
	   }
	 }
	 
	 
}
