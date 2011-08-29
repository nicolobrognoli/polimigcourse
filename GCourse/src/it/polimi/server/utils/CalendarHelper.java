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
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
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
 public static final String METAFEED_URL_BASE = 
     "https://www.google.com/calendar/feeds/";

 // The string to add to the user's metafeedUrl to access the allcalendars
 // feed.
 public static final String ALLCALENDARS_FEED_URL_SUFFIX = 
     "/allcalendars/full";

 // The string to add to the user's metafeedUrl to access the owncalendars
 // feed.
 public static final String OWNCALENDARS_FEED_URL_SUFFIX = 
     "/owncalendars/full";

 // The URL for the metafeed of the specified user.
 // (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
 public static URL metafeedUrl = null;

 // The URL for the allcalendars feed of the specified user.
 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/allcalendars/full)
 public static URL allcalendarsFeedUrl = null;

 // The URL for the owncalendars feed of the specified user.
 // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/owncalendars/full)
 public static URL owncalendarsFeedUrl = null;

 // The calendar ID of the public Google Doodles calendar
 public static final String DOODLES_CALENDAR_ID = 
     "c4o4i7m2lbamc4k26sc2vokh5g%40group.calendar.google.com";

 // The HEX representation of red, blue and green
 public static final String RED = "#A32929";
 public static final String BLUE = "#2952A3";
 public static final String GREEN = "#0D7813";

 /**
  * Utility classes should not have a public or default constructor.
  */
 public CalendarHelper() {
 }

 /**
  * Prints the titles of calendars in the feed specified by the given URL.
  * 
  * @param service An authenticated CalendarService object.
  * @param feedUrl The URL of a calendar feed to retrieve.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
 public void printUserCalendars(CalendarService service, URL feedUrl)
     throws IOException, ServiceException {

   // Send the request and receive the response:
   CalendarFeed resultFeed = service.getFeed(feedUrl, CalendarFeed.class);

   // Print the title of each calendar
   for (int i = 0; i < resultFeed.getEntries().size(); i++) {
     CalendarEntry entry = resultFeed.getEntries().get(i);
     Log.warn("\t" + entry.getTitle().getPlainText());
   }
 }

 /**
  * Creates a new secondary calendar using the owncalendars feed.
  * 
  * @param service An authenticated CalendarService object.
  * @return The newly created calendar entry.
  * @throws IOException If there is a problem communicating with the server.
  * @throws ServiceException If the service is unable to handle the request.
  */
public CalendarEntry createCalendar(CalendarService service)
     throws IOException, ServiceException {
   Log.warn("Creating a secondary calendar");

   // Create the calendar
   CalendarEntry calendar = new CalendarEntry();
   calendar.setTitle(new PlainTextConstruct("Little League Schedule"));
   calendar.setSummary(new PlainTextConstruct(
       "This calendar contains the practice schedule and game times."));
   calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
   calendar.setHidden(HiddenProperty.FALSE);
   calendar.setColor(new ColorProperty(BLUE));
   calendar.addLocation(new Where("", "", "Oakland"));

   // Insert the calendar
   return service.insert(owncalendarsFeedUrl, calendar);
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
   Log.warn("Updating the secondary calendar");

   calendar.setTitle(new PlainTextConstruct("New title"));
   calendar.setColor(new ColorProperty(GREEN));
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
   Log.warn("Deleting the secondary calendar");

   calendar.delete();
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

   calendar.setColor(new ColorProperty(BLUE));
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
