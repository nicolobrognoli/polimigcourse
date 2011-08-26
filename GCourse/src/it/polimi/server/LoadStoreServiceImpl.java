package it.polimi.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import it.polimi.client.LoadStoreService;
import it.polimi.server.data.CoursePO;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;
import it.polimi.server.utils.LoadStore;

@SuppressWarnings("serial")
public class LoadStoreServiceImpl extends RemoteServiceServlet implements LoadStoreService {

	@SuppressWarnings("unchecked")
	public String updateUser(String email, String name, String  pwd, boolean professor) throws IllegalArgumentException {
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO userTemp;
			// check empty results
			if (results.isEmpty())
				return "email not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))					
					{
						userTemp.setNickname(name);
						userTemp.setPassword(pwd);
						userTemp.setProfessor(professor);
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "updated";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateUser(String email, String name, String pwd, String site, boolean professor) {
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO userTemp;
			// check empty results
			if (results.isEmpty())
				return "email not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))					
					{
						userTemp.setNickname(name);
						userTemp.setPassword(pwd);
						userTemp.setProfessor(professor);
						userTemp.setSiteName(site);
					}
				}while(iter.hasNext());									
			}
		} finally {
			// close persistence manager
			pm.close();
		}
		return "updated";
	}

	@Override
	public boolean isProfessor(String email) {
		return LoadStore.loadUser(email).isProfessor();
	}
	
	@Override
	public List<String> getAttendedCourses(String email) {
		List<String> courses = new ArrayList<String>();		
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// The Query interface assembles a query
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("AttendingPO");
		q.addFilter("student", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, LoadStore.loadUser(email));
		// PreparedQuery contains the methods for fetching query results from the datastore
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			CoursePO course = (CoursePO)result.getProperty("course");
			courses.add(course.toString());
		}		
		return courses;
	}

	@Override
	public List<String> getTaughtCourses(String email) {
		List<String> courses = new ArrayList<String>();		
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// The Query interface assembles a query
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("CoursePO");
		q.addFilter("professor", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, LoadStore.loadUser(email));
		// PreparedQuery contains the methods for fetching query results from the datastore
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			String course = (String)result.getKey().toString() + " - " + (String)result.getProperty("Name");
			courses.add(course.toString());
		}		
		return courses;
	}
}
