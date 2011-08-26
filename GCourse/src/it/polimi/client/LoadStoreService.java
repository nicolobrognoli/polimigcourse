package it.polimi.client;

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

	List<String> getTaughtCourses(String email);
}
