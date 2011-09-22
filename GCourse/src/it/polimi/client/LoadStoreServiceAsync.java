package it.polimi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoadStoreServiceAsync {

	void updateUser(String email, String name, String pwd, boolean professor,
			AsyncCallback<String> callback);

	void updateUser(String email, String name, String pwd, String site,
			boolean professor, AsyncCallback<String> callback);

	void isProfessor(String email, AsyncCallback<Boolean> callback);

	void getAttendedCourses(String email, AsyncCallback<List<String>> callback);

	void getTaughtCourses(String email, AsyncCallback<List<String>> asyncCallback);

	void storeNewCourse(String email, String name, String description,
			String calendarId, AsyncCallback<String> callback);

	void deleteTwitterTokens(String email, AsyncCallback<String> callback);

	void getTwitterAccessToken(String email, AsyncCallback<String> callback);

	void addStudentToCourse(String email, String course,AsyncCallback<String> callback);

	void getAllCourses(AsyncCallback<List<String>> callback);

	void getNotAttendedCourses(String email,
			AsyncCallback<List<String>> callback);

	void getGoogleAccessToken(String email, AsyncCallback<String> callback);

	void getCourseNameProfessor(String key, AsyncCallback<String> callback);

	void getCourseSettings(String key, String email, String parameter,
			AsyncCallback<Boolean> callback);

	void storeCourseSettings(String key, String email, boolean lecture,
			boolean exercise, AsyncCallback<String> callback);

	void getCourseName(String key, AsyncCallback<String> callback);

	void getCourseDescription(String name, String userEmail,
			AsyncCallback<String> callback);

	void getCourseProfessor(String key, AsyncCallback<String> callback);

	void deleteFromCourse(String key, String email,
			AsyncCallback<String> asyncCallback);




}
