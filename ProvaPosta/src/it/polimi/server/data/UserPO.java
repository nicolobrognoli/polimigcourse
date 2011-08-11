package it.polimi.server.data;

import java.util.Iterator;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class UserPO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private User user;
    @Persistent
    private String nickname;
    @Persistent
    private boolean confirmed;
    @Persistent
    private boolean professor;
    @Persistent
    private String type;

    

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Key getKey() {
		return key;
	}



	public boolean isConfirmed() {
		return confirmed;
	}



	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}



	public boolean isProfessor() {
		return professor;
	}



	public void setProfessor(boolean professor) {
		this.professor = professor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    
   
}
