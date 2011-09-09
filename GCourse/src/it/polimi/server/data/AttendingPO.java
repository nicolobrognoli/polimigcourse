package it.polimi.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class AttendingPO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
    private String courseKey;
	@Persistent
    private String student;	
	@Persistent
	private boolean lecture;
	@Persistent
	private boolean exercise;
	
  
	public String getCourseKey() {
		return courseKey;
	}
	public void setCourseKey(String courseKey) {
		this.courseKey = courseKey;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;		
	}
	public boolean isLecture() {
		return lecture;
	}
	public void setLecture(boolean lecture) {
		this.lecture = lecture;
	}
	public boolean isExercise() {
		return exercise;
	}
	public void setExercise(boolean exercise) {
		this.exercise = exercise;
	}
	
}
