package it.polimi.server;

import it.polimi.client.SitesService;
import it.polimi.server.data.UserPO;
import it.polimi.server.utils.CourseManager;
import it.polimi.server.utils.LoadStore;
import it.polimi.server.utils.SiteModifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SitesServiceImpl extends RemoteServiceServlet implements SitesService{

	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private final String LECTURE = "lecture";
	private final String EXERCISE = "exercise";
	private CourseManager courseManager;
	
	public String createNewPage(String email, String title, String content) {
		return createNewPage(email,title,content,null);
	}
	@Override
	public String createNewPage(String email, String title, String content,String parent) {
		
		SiteModifier siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(email), LoadStore.getUserSiteName(email));
	    String returned = "";
		try {
			returned = siteModifier.createPage(title, content, parent,null);
			if(returned.contains("expired")){
				GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(email),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(email));
				access.refreshToken();
				String newAccessToken = access.getAccessToken();
				LoadStore.updateAccessToken(email, newAccessToken);
				siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(email));
				returned = siteModifier.createPage(title, content, parent, null);
		    }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	    return returned;
	}
	public List<String> listSiteContent(String email, String course){
		String returned;
		this.courseManager = new CourseManager(course, LoadStore.loadUser(email));
		List<String> listContent = new ArrayList<String>();
		SiteModifier siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(email), LoadStore.getUserSiteName(email));
	    try {		
			GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(email),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(email));
			access.refreshToken();
			String newAccessToken = access.getAccessToken();
			LoadStore.updateAccessToken(email, newAccessToken);
			siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(email));
			return siteModifier.listSiteContent(email, course);	
	    } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	@Override
	public String pushContentToStudents(String email, String course) {
		String returned;
		this.courseManager = new CourseManager(course, LoadStore.loadUser(email));
		List<String> listContent = new ArrayList<String>();
		SiteModifier siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(email), LoadStore.getUserSiteName(email));
	    try {		
			GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(email),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(email));
			access.refreshToken();
			String newAccessToken = access.getAccessToken();
			LoadStore.updateAccessToken(email, newAccessToken);
			siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(email));
			listContent = siteModifier.listSiteContent(email, course);		
			Iterator<String> contentIter = listContent.iterator();
			if(listContent != null && !listContent.isEmpty())
			{
				do{
					String content = contentIter.next();
					String title = this.parseTitle(content);
					String body = this.parseBody(content);
					String studentEmail;
					List<UserPO> listStudents = LoadStore.getStudentsEnrolled(LoadStore.getCourseKey(course, email));
					Iterator<UserPO> iter = listStudents.iterator();
					UserPO student;
					if(listStudents != null && !listStudents.isEmpty())
					{
						do{
							
							student = iter.next();
							studentEmail = student.getUser().getEmail();
							if(title.contains("exercise"))
							{
								if(this.courseManager.checkStudentsSettings(student, this.EXERCISE))
								{
									siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(studentEmail), LoadStore.getUserSiteName(studentEmail));
									returned = siteModifier.createPage(title, body, course, null);
									if(returned.contains("expired")){
										access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(studentEmail),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(studentEmail));
										access.refreshToken();
										newAccessToken = access.getAccessToken();
										LoadStore.updateAccessToken(studentEmail, newAccessToken);
										siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(studentEmail));
										returned = siteModifier.createPage(title, body, course, null);
								    }
								}								
							}
							else
	        				{
	        					if(this.courseManager.checkStudentsSettings(student, this.LECTURE))
	        					{
	        						siteModifier = new SiteModifier(LoadStore.getGoogleAccessToken(studentEmail), LoadStore.getUserSiteName(studentEmail));
									returned = siteModifier.createPage(title, body, course, null);
									if(returned.contains("expired")){
										access = new GoogleAccessProtectedResource(LoadStore.getGoogleAccessToken(studentEmail),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET, LoadStore.getGoogleRefreshToken(studentEmail));
										access.refreshToken();
										newAccessToken = access.getAccessToken();
										LoadStore.updateAccessToken(studentEmail, newAccessToken);
										siteModifier = new SiteModifier(newAccessToken, LoadStore.getUserSiteName(studentEmail));
										returned = siteModifier.createPage(title, body, course, null);
								    }
	        					}
	        				}
							
						}while(iter.hasNext());	
					}					
				}while(contentIter.hasNext());
			}		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	    return "ok";
	}
	
	private String parseTitle(String content){
		int begin, end;
		begin = content.indexOf("<t>") + 3;
		end = content.indexOf("</t>");
		return content.substring(begin, end);
	}
	
	private String parseBody(String content){
		int begin, end;
		begin = content.indexOf("<c>") + 3;
		end = content.indexOf("</c>");
		return content.substring(begin, end);
	}
	
}
