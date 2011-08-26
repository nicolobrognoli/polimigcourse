package it.polimi.server.data;

import it.polimi.server.utils.LoadStore;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
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
	
	@Persistent
    private UserPO professor;
    @Persistent
    private String name;
    @Persistent
    private String description;
    @Persistent(mappedBy = "student")
    private ArrayList<UserPO> students;
	
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
	public ArrayList<UserPO> getStudents() {
		return students;
	}
	public void setStudents(ArrayList<UserPO> students) {
		this.students = students;
		PersistenceManager pm = PMF.get().getPersistenceManager();
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
	}
	public boolean addStudent(String student){
		UserPO user = LoadStore.loadUser(student);
		if(!user.isProfessor())
		{
			if(this.students == null)
				this.students = new ArrayList<UserPO>();
			this.students.add(user);
			AttendingPO attendingTemp = new AttendingPO();
			attendingTemp.setCourse(this);
			attendingTemp.setStudent(user);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.makePersistent(attendingTemp);
			return true;
		}			
		else
			return false;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}    
	public String toString(){
		return key.toString()+" - "+name;
	}
	
}
