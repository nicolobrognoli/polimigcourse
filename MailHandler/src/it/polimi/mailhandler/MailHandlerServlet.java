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
import javax.mail.Part;
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

        try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String subject,mittente=message.getFrom()[0].toString(),text="",msgBody = "Messaggio ricevuto.";
        	Object o=message.getContent();
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
            System.out.println(mittente);
            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
            msg.setSubject("Your Example.com account has been activated");
            MimeMultipart multipart=(MimeMultipart)o;
	        try {
	        	for (int i = 0; i < multipart.getCount(); i++) {
	            	  Part part =  multipart.getBodyPart(i);
	            	  System.out.println(part.getFileName());
	            	  msgBody=msgBody+"Parte "+i+": "+part.getFileName()+"Tipo: "+part.getContentType();
	            	  if(part.getFileName()!=null){
	            		  InputStream input=part.getInputStream();
	            		  int j,size;
	            		  size=part.getSize();
	            		  for(j=0;j<size;j++){
	            			 input.read();
	            		  }
	            	  }else{
	            		  if(part.isMimeType("text/plain")||part.isMimeType("text/html")){
	            			  msgBody=msgBody+(String)part.getContent();
	            			  text=text+(String)part.getContent();
	            		 }
	            		  else{
	            			  Multipart multipart2=(Multipart)part.getContent();
	            			  for(int j=0;j<multipart2.getCount();j++){
	            				  Part part2=multipart2.getBodyPart(j);
	            				  msgBody=msgBody+"Tipo: "+part2.getContentType()+"Contenuto: "+part2.getContent();
		            			  text=text+(String)part2.getContent();
	            			  }
	            		  }
	            	  }
	            }
	        	if(subject.contains("Upload")){
	        		msgBody=msgBody+"upload yes";
	        		/*Azioni per il caricamento su Google site del file mandato come allegato.*/
	        	}
	        	else{
	        		msgBody=msgBody+"upload no";
	        		if(subject.contains("Post")){
	        			msgBody=msgBody+"Post yes";
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/
	        		}else{
	        			msgBody=msgBody+"Errore";
	        			/*Errore oppure un'altra azione*/
	        		}
	        	}
	            msg.setText(msgBody);

    	        } catch (AddressException e) {
    	            // ...
    	        } catch (MessagingException e) {
    	            // ...
    	        }
            Transport.send(msg);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}