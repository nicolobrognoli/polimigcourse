package it.polimi.client;

import it.polimi.server.data.UserPO;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loadstoreservice")
public interface LoadStoreService extends RemoteService{
	
	//search the user and modify the parameters
	String updateUser(String email, String name, String pwd, String site, boolean professor);

	String updateUser(String email, String name, String pwd, boolean professor);
	
	boolean isProfessor(String email);

	List<String> getAttendedCourses(String email);
	
	List<String> getNotAttendedCourses(String email);

	List<String> getTaughtCourses(String email);
	
	List<String> getAllCourses();
	
	String storeNewCourse(String email, String name, String description);
	
	String deleteTwitterTokens(String email);
	
	String getGoogleAccessToken(String email);
	
	String getTwitterAccessToken(String email);
	
	String addStudentToCourse(String email, String course);
}
