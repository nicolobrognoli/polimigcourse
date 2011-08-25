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
    private CoursePO course;
	@Persistent
    private UserPO student;	
	
    public CoursePO getCourse() {
		return course;
	}
	public void setCourse(CoursePO course) {
		this.course = course;
	}
	public UserPO getStudent() {
		return student;
	}
	public void setStudent(UserPO student) {
		this.student = student;
	}
	
}
