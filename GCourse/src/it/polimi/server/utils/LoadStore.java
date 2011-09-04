package it.polimi.server.utils;

import it.polimi.server.data.CoursePO;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class LoadStore {
	
	protected static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	protected static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	
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
	
	public static UserPO loadUser(String sender){
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
	
	public static String getGoogleAccessToken(String email){
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
						return userTemp.getGoogleAccessToken();
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "not found";
	}
	
	public static String getGoogleRefreshToken(String email){
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
						return userTemp.getGoogleRefreshToken();
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "not found";
	}
	
	public static String refreshGoogleToken(String email){
		String accessToken = LoadStore.getGoogleAccessToken(email);
		String refreshToken = LoadStore.getGoogleRefreshToken(email);
		final HttpTransport TRANSPORT = new NetHttpTransport();
		final JsonFactory JSON_FACTORY = new JacksonFactory();
		GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(accessToken,TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,refreshToken);
		try {
			access.refreshToken();
		} catch (IOException e) {
			return "Error while refreshing Google token";
		}
		accessToken=access.getAccessToken();
		LoadStore.updateAccessToken(LoadStore.loadUser(email).getUser().getEmail(), accessToken);
		return accessToken;
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

	public static String storeNewCourse(UserPO user, String name, String description){
		UserPO professor = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(CoursePO.class);
			@SuppressWarnings("unchecked")
			List<CoursePO> results = (List<CoursePO>)query.execute();
			Iterator<CoursePO> iter = results.iterator();
			CoursePO courseTemp;
			// get POs from DataStore
			Query query2 = pm.newQuery(UserPO.class);
			@SuppressWarnings("unchecked")
			List<UserPO> results2 = (List<UserPO>)query2.execute();
			Iterator<UserPO> iter2 = results2.iterator();
			UserPO userTemp;
			do{
				userTemp = (UserPO) iter2.next();
				if(userTemp.getUser().getEmail().equals(user.getUser().getEmail()))				
					professor = userTemp;
				
			}while(iter2.hasNext());	
			do{
				if (!results.isEmpty())
				{
					courseTemp = (CoursePO) iter.next();
					if(courseTemp.getName().equals(name))
						return "Course already exists";
				}				
			
			}while(iter.hasNext());			
			CoursePO course = new CoursePO();
			course.setName(name);
			course.setProfessor(professor);
			course.setDescription(description);
			pm.makePersistent(course);
		} finally {
			// close persistence manager
			pm.close();
		}
		return "Stored";
	}

	public static String updateStudent(String student, String course){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean ok = false;
		try {
			
			// get POs from DataStore
			Query query = pm.newQuery(CoursePO.class);
			@SuppressWarnings("unchecked")
			List<CoursePO> results = (List<CoursePO>)query.execute();
			Iterator<CoursePO> iter = results.iterator();
			CoursePO courseTemp;
			
			do{
				courseTemp = (CoursePO) iter.next();
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
	
	public static String getUserSiteName(String email){
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
						return userTemp.getSiteName();
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		
		return "error";
	}
	
}

	