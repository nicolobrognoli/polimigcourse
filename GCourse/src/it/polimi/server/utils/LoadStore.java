package it.polimi.server.utils;

import it.polimi.server.data.Course;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import twitter4j.auth.AccessToken;

import com.google.appengine.api.datastore.Query.FilterOperator;

public class LoadStore {
	public static String updateAccessToken(String email,String accessToken){
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
						userTemp.setGoogleAccessToken(accessToken);
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "updated";
	}
	
	public static String storeAccessToken(String email, String kind, String accessToken, String refreshToken){

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
						if(kind.equals("google"))
						{
							userTemp.setGoogleAccessToken(accessToken);
							userTemp.setGoogleRefreshToken(refreshToken);
						}
						else if(kind.equals("twitter"))
						{
							userTemp.setTwitterAccessToken(accessToken);
							userTemp.setTwitterSecretToken(refreshToken);
						}
						else
							return "Parameter kind error";
						
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "updated";
	}
	
	public static UserPO verifyUser(String sender){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO userTemp;
			// check empty results
			if (results.isEmpty())
				return null;
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(sender))					
					{
						return userTemp;
					}
				}while(iter.hasNext());			
				return null;
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
	}
	
	public static boolean verifyEmail(String email){
		return false;
	}
	
	public static String getTwitterAccessToken(String email){
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
				return "not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))				
						return userTemp.getTwitterAccessToken();
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "not found";
	}
	
	public static String deleteUser(String email){
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
				return "not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))				
					{
						pm.deletePersistent(userTemp);
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		
		return "deleted";
	}

	public static String storeNewCourse(UserPO professor, String name, String description){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(Course.class);
			@SuppressWarnings("unchecked")
			List<Course> results = (List<Course>)query.execute();
			Iterator<Course> iter = results.iterator();
			Course courseTemp;
			// check empty results
			
			do{
				courseTemp = (Course) iter.next();
				if(courseTemp.getName().equals(name))
					return "course already exists";
				Course course = new Course();
				course.setName(name);
				course.setProfessor(professor);
				course.setDescription(description);
				pm.makePersistent(course);
			}while(iter.hasNext());									
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "stored";
	}

	public static String updateStudent(UserPO student, String course){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean ok = false;
		try {
			
			// get POs from DataStore
			Query query = pm.newQuery(Course.class);
			@SuppressWarnings("unchecked")
			List<Course> results = (List<Course>)query.execute();
			Iterator<Course> iter = results.iterator();
			Course courseTemp;
			
			do{
				courseTemp = (Course) iter.next();
				if(courseTemp.getName().equals(course))
				{
					if(courseTemp.getStudents().contains(student))
						return "already registered";
					else
					{
						courseTemp.addStudent(student);
						ok = true;
					}					
				}					
			}while(iter.hasNext());		
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
		if(ok)
			return "updated";	
		else
			return "course not exists";
	}
}

	