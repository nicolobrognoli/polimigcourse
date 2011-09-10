package it.polimi.server.utils;

import it.polimi.server.data.UserPO;

import java.util.Iterator;
import java.util.List;

public class CourseManager {
	private final String LECTURE = "lecture";
	private final String EXERCISE = "exercise";
	
	private String courseName;
	private UserPO professor;	
	
	public CourseManager(String courseName, UserPO professor) {
		super();
		this.courseName = courseName;
		this.professor = professor;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public UserPO getProfessor() {
		return professor;
	}
	
	public void setProfessor(UserPO professor) {
		this.professor = professor;
	}

	public boolean checkStudentsSettings(UserPO student, String parameter){
		String returned;
		String key = LoadStore.getCourseKey(this.courseName, this.professor.getUser().getEmail());
		boolean value = false;
		
		if(parameter.equalsIgnoreCase(this.LECTURE))
		{
			value = LoadStore.getCourseSettings(key, student.getUser().getEmail(), this.LECTURE);
		}
		else if(parameter.equalsIgnoreCase(this.EXERCISE))
		{
			value = LoadStore.getCourseSettings(key, student.getUser().getEmail(), this.EXERCISE);
		}		
		return value;
	}

}
