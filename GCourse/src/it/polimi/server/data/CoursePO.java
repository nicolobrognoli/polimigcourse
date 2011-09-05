package it.polimi.server.data;

import it.polimi.server.utils.LoadStore;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class CoursePO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent(mappedBy = "key")
    private UserPO professor;
    @Persistent
    private String name;
    @Persistent
    private String description;
    @Persistent
    private ArrayList<String> students;
    @Persistent
    private String calendarId;
	
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
	public ArrayList<String> getStudents() {
		return students;
	}
	/*public void setStudents(ArrayList<UserPO> students) {
		Iterator<UserPO> iter = students.iterator();				
		if (!students.isEmpty()){
			UserPO userTemp;
			AttendingPO attendingTemp;
			do{
				userTemp = (UserPO) iter.next();
				attendingTemp = new AttendingPO();
				attendingTemp.setCourse(this);
				attendingTemp.setStudent(userTemp);
				pm.makePersistent(attendingTemp);
			}while(iter.hasNext());									
		}
	}*/
	public void setStudents(ArrayList<String> students) {			
		if (!students.isEmpty()){
			Iterator<String> iter = students.iterator();			
			String userTemp;
			do{
				userTemp = (String) iter.next();
				addStudent(userTemp);
			}while(iter.hasNext());									
		}
	}
	public boolean addStudent(String student){
		UserPO user = LoadStore.loadUser(student);
		if(!user.isProfessor())
		{
			this.students.add(student);
			return true;
		}
		return false;
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
	public String toString(){
		return key.toString()+" - "+name;
	}
	
}
