package it.polimi.server.utils;

/* Copyright (c) 2008 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import com.allen_sauer.gwt.log.client.Log;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.SelectedProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
* Demonstrates interactions with the Calendar data API's calendar feeds using
* the Java client library:
* 
* <ul>
* <li>Retrieving the metafeed list of all the user's calendars</li>
* <li>Retrieving the allcalendars list of calendars</li>
* <li>Retrieving the owncalendars list of calendars</li>
* <li>Creating a new calendar</li>
* <li>Updating an existing calendar</li>
* <li>Deleting a calendar</li>
* <li>Subscribing to an existing calendar</li>
* <li>Updating a subscription</li>
* <li>Deleting a subscription</li>
* </ul>
*/
@SuppressWarnings("unused")
public class CalendarHelper {

 // The base URL for a user's calendar metafeed (needs a username appended).
 private final String METAFEED_URL_BASE = 
     "https://www.google.com/calendar/feeds/";

 // The string to add to the user's metafeedUrl to access the allcalendars
 // feed.
 private final String ALLCALENDARS_FEED_URL_SUFFIX = 
     "/allcalendars/full";

 // The string to add to the user's metafeedUrl to access the owncalendars
 // feed.
 private final String OWNCALENDARS_FEED_URL_SUFFIX = 
     "/owncalendars/full";
 
 // The string to add to the user's metafeedUrl to access the private
 // feed.
 private final String PRIVATE_FEED_URL_SUFFIX = 
     "/private/full";

 // The URL for the metafeed of the specified user.
 // (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
 private URL metafeedUrl = null;

 // The URL for the allcalendars feed of the specified user.
 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/allcalendars/full)
 private URL allcalendarsFeedUrl = null;

 // The URL for the owncalendars feed of the specified user.
 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/owncalendars/full)
 private  URL owncalendarsFeedUrl = null;

 // The calendar ID of the public Google Doodles calendar
 public static final String DOODLES_CALENDAR_ID = 
     "c4o4i7m2lbamc4k26sc2vokh5g%40group.calendar.google.com";

 private String email;

 private CalendarService service = new CalendarService("GCourse-Calendar-v1");
 /**
  * Utility classes should not have a public or default constructor.
 * @param accessToken 
 * @throws MalformedURLException 
  */
 public CalendarHelper(String email) {
	 try {
		metafeedUrl = new URL(METAFEED_URL_BASE + email);
		allcalendarsFeedUrl = new URL(METAFEED_URL_BASE + email + ALLCALENDARS_FEED_URL_SUFFIX);
		owncalendarsFeedUrl = new URL(METAFEED_URL_BASE + email + OWNCALENDARS_FEED_URL_SUFFIX);		
		this.email = email;
	 } catch (MalformedURLException e) {
		Log.warn("Error while setting FeedUrls");
		e.printStackTrace();
	}
	service.setAuthSubToken(LoadStore.getGoogleAccessToken(email));	 
 }

 /**
  * Prints the titles of calendars in the feed specified by the given URL.
  * 
  * @param service An authenticated CalendarService object.
  * @param feedUrl The URL of a calendar feed to retrieve.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public String getCalendarIdFromTitle(String title) throws IOException {

   // Send the request and receive the response:
   CalendarFeed resultFeed = null;
	try {
		resultFeed = service.getFeed(owncalendarsFeedUrl, CalendarFeed.class);
	} catch (ServiceException e) {
		service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
		try {
			resultFeed = service.getFeed(owncalendarsFeedUrl, CalendarFeed.class);
		} catch (ServiceException e1) {
			Log.warn("ServiceException while retrieving Calendar Id");
			e1.printStackTrace();
		}
		e.printStackTrace();
	}
   CalendarEntry entry;
   for (int i = 0; i < resultFeed.getEntries().size(); i++) {
     entry = resultFeed.getEntries().get(i);
     if (entry.getTitle().getPlainText().equalsIgnoreCase(title)){
    	 String id = entry.getId();
    	 Log.warn("Calendar Id: " + id.substring(id.lastIndexOf("/")+1));    	 
     	return id.substring(id.lastIndexOf("/")+1);
     }
   }
   Log.warn("Calendar Not Found");
   return "Calendar not fount";
 }

 /**
  * Creates a new secondary calendar using the owncalendars feed.
  * 
  * @param service An authenticated CalendarService object.
  * @return The newly created calendar entry.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
public CalendarEntry createCalendar(String title, String summary)
     throws IOException {
   Log.info("Creating a secondary calendar");

   // Create the calendar
   CalendarEntry calendar = new CalendarEntry();
   calendar.setTitle(new PlainTextConstruct(title));
   calendar.setSummary(new PlainTextConstruct(summary));
   //calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
   //calendar.setHidden(HiddenProperty.FALSE);
   //calendar.addLocation(new Where("", "", "Oakland"));
   
   // Insert the calendar
   CalendarEntry retCalendar = null;
   try {
	retCalendar = service.insert(owncalendarsFeedUrl, calendar);
} catch (ServiceException e) {
	service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
	try {
		retCalendar = service.insert(owncalendarsFeedUrl, calendar);
	} catch (ServiceException e1) {
		Log.warn("ServiceException while creating a new Calendar");
		e1.printStackTrace();
	}
	e.printStackTrace();
}
   return retCalendar;
 }

 /**
  * Updates the title, color, and selected properties of the given calendar
  * entry using the owncalendars feed. Note that the title can only be updated
  * with the owncalendars feed.
  * 
  * @param calendar The calendar entry to update.
  * @return The newly updated calendar entry.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public CalendarEntry updateCalendar(CalendarEntry calendar)
     throws IOException, ServiceException {
   Log.info("Updating the secondary calendar");
   calendar.setTitle(new PlainTextConstruct("New title"));
   calendar.setSelected(SelectedProperty.TRUE);
   return calendar.update();
 }

 /**
  * Deletes the given calendar entry.
  * 
  * @param calendar The calendar entry to delete.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public void deleteCalendar(CalendarEntry calendar)
     throws IOException, ServiceException {
   Log.info("Deleting the secondary calendar");
   calendar.delete();
 }

 public CalendarEventEntry createEvent(String calendar, String strEvent) throws IOException{
	 CalendarEventEntry event = new CalendarEventEntry();
	 URL privateFeedUrl = new URL(METAFEED_URL_BASE + this.getCalendarIdFromTitle(calendar) + PRIVATE_FEED_URL_SUFFIX);
	 event.setContent(new PlainTextConstruct(strEvent));
	 event.setQuickAdd(true);
	 CalendarEventEntry newEvent = null;
	 // Send the request and receive the response:
	 try {
		newEvent = service.insert(privateFeedUrl, event);	
	} catch (ServiceException e) {
		service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
		try {
			newEvent = service.insert(privateFeedUrl, event);
		} catch (ServiceException e1) {
			Log.warn("ServiceException while creating a new Event");
			e1.printStackTrace();
		}
	}
	 return newEvent;
 }
 
 /**
  * Subscribes to the public Google Doodles calendar using the allcalendars
  * feed.
  * 
  * @param service An authenticated CalendarService object.
  * @return The newly created calendar entry.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public CalendarEntry createSubscription(CalendarService service)
     throws IOException, ServiceException {
   Log.warn("Subscribing to the Google Doodles calendar");

   CalendarEntry calendar = new CalendarEntry();
   calendar.setId(DOODLES_CALENDAR_ID);
   return service.insert(allcalendarsFeedUrl, calendar);
 }

 /**
  * Updated the color property of the given calendar entry.
  * 
  * @param calendar The calendar entry to update.
  * @return The newly updated calendar entry.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public CalendarEntry updateSubscription(CalendarEntry calendar)
     throws IOException, ServiceException {
   Log.warn("Updating the display color of the Doodles calendar");

   return calendar.update();
 }

 /**
  * Deletes the given calendar entry.
  * 
  * @param calendar The calendar entry to delete
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public void deleteSubscription(CalendarEntry calendar)
     throws IOException, ServiceException {
   Log.warn("Deleting the subscription to the Doodles calendar");

   calendar.delete();
 } 
 
 /**
  * Prints the command line usage of this sample application.
  */
 public void usage() {
   Log.warn("Syntax: CalendarFeedDemo <username> <password>");
   Log.warn("\nThe username and password are used for "
       + "authentication.  The sample application will modify the specified "
       + "user's calendars so you may want to use a test account.");
 }  
}
