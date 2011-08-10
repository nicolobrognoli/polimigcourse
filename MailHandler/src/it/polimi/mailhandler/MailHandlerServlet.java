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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties; 

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session; 
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.*; 

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;




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
            Multipart multipart=(Multipart)message.getContent();
           //OutputStream out=resp.getOutputStream();
            /*
            for (int i = 0; i < multipart.getCount(); i++) {
            	  BodyPart part =  multipart.getBodyPart(i);
            	  System.out.println(part.getFileName());
            	  msgBody=msgBody+"Parte "+i+": "+part.getFileName()+" ";
            	  if(part.getFileName()!=null){
                      Entity entity = new Entity("File "+part.getFileName(),key);
                      datastore.put(entity);
            		  InputStream input=part.getInputStream();
            		  int j,size;
            		  size=part.getSize();
            		  for(j=0;j<size;j++){
            			 input.read();
            		  }
            	  }
            }*/
	        try {

	            
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
	            System.out.println(mittente);
	            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
	            //msg.addRecipient(Message.RecipientType.TO, new InternetAddress("andrea.colombo88@gmail.com", "Mr. User"));
	            msg.setSubject("Your Example.com account has been activated");
	            for (int i = 0; i < multipart.getCount(); i++) {
	            	  BodyPart part =  multipart.getBodyPart(i);
	            	  System.out.println(part.getFileName());
	            	  msgBody=msgBody+"Parte "+i+": "+part.getFileName()+" ";
	            	  if(part.getFileName()!=null){
	            		  InputStream input=part.getInputStream();
	            		  int j,size;
	            		  size=part.getSize();
	            		  for(j=0;j<size;j++){
	            			 input.read();
	            		  }
	            	  }
	            }
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