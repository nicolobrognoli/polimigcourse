package it.polimi.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import it.polimi.server.data.UserPO;
import it.polimi.client.SendRegistrationMail;
import it.polimi.server.data.PMF;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SendRegistrationMailImpl extends RemoteServiceServlet implements SendRegistrationMail{

	@Override
	public void sendMail(String dest) throws IllegalArgumentException {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "Clicca sul link per confermare la registrazione al servizio:\n" +
        		"http://polimigcourse.appspot.com/confirmregistration?email=" + dest;
        User user = new User(dest, "google.com");
        
        Message msg = new MimeMessage(session);
        try {
			msg.setFrom(new InternetAddress("register@polimigcourse.appspotmail.com", "PolimiGCourse"));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(dest, "User"));
			msg.setSubject("GCourse Registration");
			msg.setText(msgBody);
			Transport.send(msg);
			// get persistence manager
			PersistenceManager pm = PMF.get().getPersistenceManager();
			
			try {
				
				// create a new Data object
				UserPO userPO = new UserPO();
				userPO.setUser(user);
				userPO.setConfirmed(false);
				
				// store object into DataStore
				pm.makePersistent(userPO);
				
			} finally {
				
				// close persistence manager
				pm.close();
			}
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	
	}
	
}
