package it.polimi.server.data;

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
    private Key userKey;

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
    @Persistent
    private String googleAccessToken;
    @Persistent
    private String googleRefreshToken;
    @Persistent
    private String twitterAccessToken;
    @Persistent
    private String twitterSecretToken;
    @Persistent
    private String siteName;
    @Persistent
    private String password;

    

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

	public String getGoogleAccessToken() {
		return googleAccessToken;
	}

	public void setGoogleAccessToken(String googleAccessToken) {
		this.googleAccessToken = googleAccessToken;
	}

	public String getGoogleRefreshToken() {
		return googleRefreshToken;
	}

	public void setGoogleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
	}

	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public void setTwitterAccessToken(String twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}

	public String getTwitterSecretToken() {
		return twitterSecretToken;
	}

	public void setTwitterSecretToken(String twitterSecretToken) {
		this.twitterSecretToken = twitterSecretToken;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Key getUserKey() {
		return userKey;
	}
}
