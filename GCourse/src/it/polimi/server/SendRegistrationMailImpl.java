package it.polimi.server;

import it.polimi.client.SendRegistrationMail;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SendRegistrationMailImpl extends RemoteServiceServlet implements SendRegistrationMail{

	@SuppressWarnings("unchecked")
	@Override
	public String sendMail(String dest) throws IllegalArgumentException {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "Clicca sul link per confermare la registrazione al servizio:\n" +
        		"http://polimigcourse.appspot.com/confirmregistration?email=" + dest;
        User user = new User(dest, "google.com");
        boolean ok = true;
        
     // get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Message msg = new MimeMessage(session);
        try {
			//control if the email is already known
        	// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO userTemp;
			// check empty results
			if (results.isEmpty())
				ok = true;
			else 
			{
				
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(dest))					
						ok = false;
				}while(iter.hasNext());									
			}
			if(ok)
			{
				//email not registered
				msg.setFrom(new InternetAddress("register@polimigcourse.appspotmail.com", "PolimiGCourse"));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(dest, "User"));
				msg.setSubject("GCourse Registration");
				msg.setText(msgBody);
				Transport.send(msg);
				// create a new Data object
				UserPO userPO = new UserPO();
				userPO.setUser(user);
				userPO.setConfirmed(false);
				userPO.setProfessor(false);
				// store object into DataStore
				pm.makePersistent(userPO);		
				return "Mail sent...";
			}
			else
			{
				return "You have already an account.";
			}
			
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "";
	}
	
}
