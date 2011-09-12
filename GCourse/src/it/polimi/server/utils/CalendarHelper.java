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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;

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
	 private final String METAFEED_URL_BASE = "https://www.google.com/calendar/feeds/";
	
	 // The string to add to the user's metafeedUrl to access the allcalendars
	 // feed.
	 private final String ALLCALENDARS_FEED_URL_SUFFIX = "/allcalendars/full";
	
	 // The string to add to the user's metafeedUrl to access the owncalendars
	 // feed.
	 private final String OWNCALENDARS_FEED_URL_SUFFIX = "/owncalendars/full";
	 
	 // The string to add to the user's metafeedUrl to access the private
	 // feed.
	 private final String PRIVATE_FEED_URL_SUFFIX = "/private/full";
	
	 // The URL for the metafeed of the specified user.
	 // (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
	 private URL metafeedUrl = null;
	
	 // The URL for the allcalendars feed of the specified user.
	 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/allcalendars/full)
	 private URL allcalendarsFeedUrl = null;
	
	 // The URL for the owncalendars feed of the specified user.
	 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/owncalendars/full)
	 private  URL owncalendarsFeedUrl = null;
	
	 private String email;
	
	 private CalendarService service = new CalendarService("GCourse-Calendar-v1");
	 
	 /**
	  * Class constructor.
	  * 
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
	
	 private CalendarEntry getCalendarFromTitle(String title) throws IOException{
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
		     	return entry;
		     }
		   }
		   return null;
	 }
	 
	 /**
	  * Returns the id of the calendars with the given title.
	  * 
	  * @param title the title of the calendar which the id must retrieved of.
	  * @throws IOException If there is a problem communicating with the server.
	  */
	 private String getCalendarIdFromTitle(String title) throws IOException {
		 if (getCalendarFromTitle(title).getId() != null)
			 return getCalendarFromTitle(title).getId();
		 else
			 return null;
	 }
	 
	 private String cleanCalendarId(String calendarIdURL) {
		 return calendarIdURL.substring(calendarIdURL.lastIndexOf("/")+1);
	 }
	 
	 /**
	  * Creates a new secondary calendar using the owncalendars feed.
	  * 
	  * @param title the title of the calendar to create.
	  * @param summary the summary of the calendar to create.
	  * @return The newly created calendar entry.
	  * @throws IOException If there is a problem communicating with the server.
	  */
	 public String createCalendar(String title, String summary) throws IOException {
	   Log.info("Creating a secondary calendar");
	   // Create the calendar
	   CalendarEntry calendar = new CalendarEntry();
	   calendar.setTitle(new PlainTextConstruct(title));
	   calendar.setSummary(new PlainTextConstruct(summary));
	   calendar.setTimeZone(new TimeZoneProperty("Europe/Rome"));   
	   calendar.setHidden(HiddenProperty.FALSE);

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
	   Log.info("Calendar ID: " + retCalendar.getId());
	   return retCalendar.getId();
	 }
	
	 /**
	  * Deletes the given calendar entry.
	  * 
	  * @param calendarId The id of the calendar entry to delete.
	  * @throws IOException If there is a problem communicating with the server.
	  * @throws ServiceException If the service is unable to handle the request.
	  */
	 public void deleteCalendar(String calendarIdURL) throws IOException {
	   Log.info("Deleting the secondary calendar");	   
	   try {
		   CalendarFeed resultFeed = service.getFeed(new URL(METAFEED_URL_BASE + email + OWNCALENDARS_FEED_URL_SUFFIX), CalendarFeed.class);
		   for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			   CalendarEntry entry = resultFeed.getEntries().get(i);
			   if(cleanCalendarId(entry.getId()).equals(cleanCalendarId(calendarIdURL)))
				   entry.delete();
		   }
	   }catch (ServiceException e) {
		   service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
			try {
				CalendarFeed resultFeed = service.getFeed(new URL(METAFEED_URL_BASE + email + OWNCALENDARS_FEED_URL_SUFFIX), CalendarFeed.class);
				for (int i = 0; i < resultFeed.getEntries().size(); i++) {
					CalendarEntry entry = resultFeed.getEntries().get(i);
					if(cleanCalendarId(entry.getId()).equals(cleanCalendarId(calendarIdURL)))
						entry.delete();
				}
			} catch (ServiceException e1) {
				Log.warn("ServiceException while deleting a Calendar");
				e1.printStackTrace();
			}
			e.printStackTrace();
	   	}
	 }
	
	 public CalendarEventEntry createEvent(String calendarIdURL, String strEvent) throws IOException{
		 CalendarEventEntry event = new CalendarEventEntry();
		 URL privateFeedUrl = new URL(METAFEED_URL_BASE + cleanCalendarId(calendarIdURL) + PRIVATE_FEED_URL_SUFFIX);
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
	  * Subscribes a Google calendar using the allcalendars
	  * feed.
	  * 
	  * @param calendarId the id of the calendar to subscribe.
	  * @return The newly created calendar entry.
	  * @throws IOException If there is a problem communicating with the server.
	  */
	 public CalendarEntry createSubscription(String calendarIdURL) throws IOException{
	   Log.info("Subscribing to the Google Doodles calendar");
	   CalendarEntry calendar = new CalendarEntry();
	   calendar.setId(cleanCalendarId(calendarIdURL));
	   try {
		   return service.insert(allcalendarsFeedUrl, calendar);
	   } catch (ServiceException e) {
			service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
			try {
				return service.insert(allcalendarsFeedUrl, calendar);
			} catch (ServiceException e1) {
				Log.warn("ServiceException while creating a new Event");
				e1.printStackTrace();
			}
	   }
	   return null;
	 }
	
	 /**
	  * Deletes the given s entry.
	  * 
	  * @param calendarId The Id of the calendar entry to delete.
	  * @throws IOException If there is a problem communicating with the server.
	  */
	 public void deleteSubscription(String calendarIdURL) throws IOException {
		 Log.info("Deleting the secondary calendar");
		   try {
			   CalendarEntry calendar = service.getEntry(new URL(METAFEED_URL_BASE + email + ALLCALENDARS_FEED_URL_SUFFIX + "/" + cleanCalendarId(calendarIdURL)), CalendarEntry.class);
			   calendar.delete();
		   } catch (ServiceException e) {
			   service.setAuthSubToken(LoadStore.refreshGoogleToken(email));
				try {
					CalendarEntry calendar = service.getEntry(new URL(calendarIdURL), CalendarEntry.class);
					calendar.delete();
				} catch (ServiceException e1) {
					Log.warn("ServiceException while deleting a Subscription");
					e1.printStackTrace();
				}
				e.printStackTrace();
		   	}
	 } 
	 
}
