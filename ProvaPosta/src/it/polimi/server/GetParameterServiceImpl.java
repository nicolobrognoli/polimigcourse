package it.polimi.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import it.polimi.client.GetParameterService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GetParameterServiceImpl extends RemoteServiceServlet implements GetParameterService{

	@Override
	public void getParameter(String param) throws IllegalArgumentException {
		System.out.println("Service: param: " + param);
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "Messaggio di prova";
        
        Message msg = new MimeMessage(session);
        try {
			msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(param, "User"));
			msg.setSubject("Test GCourse");
			msg.setText(msgBody);
			Transport.send(msg);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	
	}
	
}
