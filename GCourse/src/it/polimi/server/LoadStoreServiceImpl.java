package it.polimi.server;

import it.polimi.client.LoadStoreService;
import it.polimi.server.data.AttendingPO;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import it.polimi.server.data.CoursePO;
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
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(AttendingPO.class);
			@SuppressWarnings("unchecked")
			List<AttendingPO> results = (List<AttendingPO>)query.execute();
			Iterator<AttendingPO> iter = results.iterator();
			AttendingPO attendingTemp;
			// check empty results
			if (results.isEmpty())
				return courses;
			else 
			{
				do{
					attendingTemp = (AttendingPO) iter.next();
					if(attendingTemp.getStudent().equals(email))					
					{
						courses.add(attendingTemp.getCourseKey());
					}
				}while(iter.hasNext());									
			}
		} finally {			
			// close persistence manager
			pm.close();
		}
		return courses;
	}

	@Override
	public List<String> getTaughtCourses(String email) {
		List<String> courses = new ArrayList<String>();		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(CoursePO.class);
			List<CoursePO> results = (List<CoursePO>)query.execute();
			Iterator<CoursePO> iter = results.iterator();
			CoursePO courseTemp;
			// check empty results
			if (results.isEmpty())
				return courses;
			else 
			{
				do{
					courseTemp = (CoursePO) iter.next();
					if(courseTemp.getProfessor().getUser().getEmail().equals(email))					
					{
						courses.add(courseTemp.getName());
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return courses;
	}

	public String storeNewCourse(String email, String name, String description){
		UserPO professor = LoadStore.loadUser(email);
		String result = LoadStore.storeNewCourse(professor, name, description);
		return result;
	}

	@Override
	public String deleteTwitterTokens(String email) {
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			@SuppressWarnings("unchecked")
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
						userTemp.setTwitterAccessToken(null);
						userTemp.setTwitterSecretToken(null);
					}
				}while(iter.hasNext());									
			}
		} finally {
			// close persistence manager
			pm.close();
		}
		return "Deleted";
	}

	@Override
	public String getTwitterAccessToken(String email) {
		return LoadStore.getTwitterAccessToken(email);
	}

	@Override
	public String addStudentToCourse(String email, String courseKey) {
		boolean ok = true;
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(AttendingPO.class);
			@SuppressWarnings("unchecked")
			List<AttendingPO> results = (List<AttendingPO>)query.execute();
			Iterator<AttendingPO> iter = results.iterator();
			AttendingPO attendingTemp;
			// check empty results
			if (results.isEmpty())
				ok = true;
			else 
			{
				do{
					attendingTemp = (AttendingPO) iter.next();
					//check if is already stored
					if(attendingTemp.getCourseKey().equals(courseKey) && attendingTemp.getStudent().equals(email))
						ok= false;
				}while(iter.hasNext());									
			}
			if(ok)
			{
				AttendingPO a = new AttendingPO();
				a.setCourseKey(courseKey);
				a.setLecture(true);
				a.setExercise(true);
				a.setStudent(email);
				pm.makePersistent(a);
			}
			else
			{
				return "already registered";
			}
		} finally {			
			// close persistence manager
			pm.close();
		}
		return "completed";
	}

	@Override
	public List<String> getAllCourses() {
		List<String> courses = new ArrayList<String>();		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(CoursePO.class);
			@SuppressWarnings("unchecked")
			List<CoursePO> results = (List<CoursePO>)query.execute();
			Iterator<CoursePO> iter = results.iterator();
			CoursePO courseTemp;
			// check empty results
			if (results.isEmpty())
				return courses;
			else 
			{
				do{
					courseTemp = (CoursePO) iter.next();
					courses.add(courseTemp.getCourseKey().toString());					
				}while(iter.hasNext());									
			}
		} finally {			
			// close persistence manager
			pm.close();
		}
		return courses;
	}

	@Override
	public List<String> getNotAttendedCourses(String email) {
		List<String> all = this.getAllCourses();
		List<String> attended = this.getAttendedCourses(email);
		all.removeAll(attended);
		return all;
	}

	@Override
	public String getGoogleAccessToken(String email) {
		return LoadStore.getGoogleAccessToken(email);
	}

	@Override
	public String getCourseNameProfessor(String key) {
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(CoursePO.class);
			@SuppressWarnings("unchecked")
			List<CoursePO> results = (List<CoursePO>)query.execute();
			Iterator<CoursePO> iter = results.iterator();
			CoursePO courseTemp;
			// check empty results
			if (results.isEmpty())
				return "error";
			else 
			{
				do{
					courseTemp = (CoursePO) iter.next();
					if(courseTemp.getCourseKey().toString().equals(key))
						return courseTemp.getName() + " - " + courseTemp.getProfessor().getUser().getEmail();
				}while(iter.hasNext());									
			}
		} finally {			
			// close persistence manager
			pm.close();
		}
		return null;
	}

	
	
}
