package it.polimi.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class CoursePO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key courseKey;
	
	@Persistent(mappedBy = "userKey")
    private UserPO professor;
    @Persistent
    private String name;
    @Persistent
    private String description;
    @Persistent
    private String calendarId;
	
	public Key getCourseKey() {
		return courseKey;
	}
	public UserPO getProfessor() {
		return professor;
	}
	public boolean setProfessor(UserPO professor) {
		if(professor.isProfessor())
		{
			this.professor = professor;
			return true;
		}			
		else
			return false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}    
	public String getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}
}
