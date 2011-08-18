package it.polimi.server;
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

import it.polimi.server.utils.LoadStore;

import java.io.IOException; 
import java.io.InputStream;
import java.util.Properties; 

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session; 
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.*; 

import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.util.ServiceException;




public class MailHandlerServlet extends HttpServlet { 
    /**
	 * 
	 */
	private String getRealUser(String sender){
		String realUser;
		int i,start,end;
		for(end=start=i=0;i<sender.length();i++){
			if(sender.charAt(i)=='<'){
				start=i;
			}
			else if(sender.charAt(i)=='@'){
				end=i;
				i=sender.length();
			}
			
		}
		realUser=sender.substring(start+1,end);
		return realUser;
	}
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp) 
            throws IOException { 
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 

        try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String subject,sender=message.getFrom()[0].toString(),text="",msgBody = "Messaggio ricevuto.",realSender;
        	Object o=message.getContent();
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            realSender=this.getRealUser(sender);
            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
            System.out.println(realSender);
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
	        		String returned=LoadStore.verifyUser(realSender);
	        		if(returned.contains("errore")){
	        			msgBody+=returned;
	        		}
	        		else{
	        			BaseContentEntry<?> page;
	        		    SitesHelper sitesHelper;
	        		    sitesHelper = new SitesHelper("polimigcourse", "site", "projectgcourse");
	        		    sitesHelper.login(returned);
	        		    try {
	        				page = sitesHelper.createPage("webpage", "cacca");
	        			
	        			} catch (ServiceException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			} catch (SitesException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	        		}
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