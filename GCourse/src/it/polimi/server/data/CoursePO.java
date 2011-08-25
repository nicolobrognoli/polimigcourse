package it.polimi.server.data;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

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
    @Persistent
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
	}
	public boolean addStudent(UserPO student){
		if(!student.isProfessor())
		{
			if(this.students == null)
				this.students = new ArrayList<UserPO>();
			this.students.add(student);
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
	
	
}
