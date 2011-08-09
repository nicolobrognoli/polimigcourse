package it.polimi.mailhandler;
/*
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
*/

import java.io.IOException; 
import java.util.Properties; 

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session; 
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 
import javax.servlet.http.*; 


public class MailHandlerServlet extends HttpServlet { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp) 
            throws IOException { 
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 
        String msgBody = "Messaggio ricevuto.";
        try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String mittente=message.getFrom()[0].toString();
	        try {
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
	            System.out.println(mittente);
	            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
	            //msg.addRecipient(Message.RecipientType.TO, new InternetAddress("andrea.colombo88@gmail.com", "Mr. User"));
	            msg.setSubject("Your Example.com account has been activated");
	            msg.setText(msgBody);
	            Transport.send(msg);

	        } catch (AddressException e) {
	            // ...
	        } catch (MessagingException e) {
	            // ...
	        }
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}